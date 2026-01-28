package com.keycloakmigrator.parser;

import com.keycloakmigrator.model.ChangeLog;
import com.keycloakmigrator.model.Changeset;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Parser for XML changelog files.
 *
 * <p>This parser reads Keycloak migration changelog files in XML format and
 * converts them into {@link ChangeLog} and {@link Changeset} objects.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>XSD schema validation (optional)</li>
 *   <li>Support for include directives</li>
 *   <li>Recursive directory scanning</li>
 *   <li>Duplicate version detection</li>
 * </ul>
 *
 * @see ChangeLog
 * @see Changeset
 */
public final class XmlChangesetParser {

    private static final Logger LOG = LoggerFactory.getLogger(XmlChangesetParser.class);
    private static final String SCHEMA_RESOURCE = "/schema/changeset.xsd";

    private final JAXBContext jaxbContext;
    private final Schema schema;
    private final boolean validateSchema;

    /**
     * Creates a parser with schema validation enabled.
     */
    public XmlChangesetParser() {
        this(true);
    }

    /**
     * Creates a parser with configurable schema validation.
     *
     * @param validateSchema whether to validate XML against the XSD schema
     */
    public XmlChangesetParser(final boolean validateSchema) {
        this.validateSchema = validateSchema;
        try {
            this.jaxbContext = JAXBContext.newInstance(ChangeLog.class);

            if (validateSchema) {
                final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                try (final InputStream schemaStream = getClass().getResourceAsStream(SCHEMA_RESOURCE)) {
                    if (schemaStream != null) {
                        this.schema = sf.newSchema(new StreamSource(schemaStream));
                    } else {
                        LOG.warn("Schema file not found, validation disabled");
                        this.schema = null;
                    }
                }
            } else {
                this.schema = null;
            }
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to initialize XML parser", e);
        }
    }

    /**
     * Parses a single changelog file.
     *
     * @param file the changelog file to parse
     * @return the parsed changelog
     * @throws JAXBException if parsing fails
     */
    public ChangeLog parseFile(final Path file) throws JAXBException {
        LOG.debug("Parsing changelog file: {}", file);

        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        if (schema != null && validateSchema) {
            unmarshaller.setSchema(schema);
        }

        final ChangeLog changeLog = (ChangeLog) unmarshaller.unmarshal(file.toFile());

        // Process includes
        processIncludes(changeLog, file.getParent());

        LOG.debug("Parsed {} changesets from {}", changeLog.getChangesets().size(), file);
        return changeLog;
    }

    /**
     * Parses all changelog files in a directory recursively.
     *
     * <p>Files are processed in alphabetical order. All changesets are
     * collected and sorted by version number.</p>
     *
     * @param directory the directory containing changelog files
     * @return a combined list of all changesets, sorted by version
     * @throws IOException              if reading the directory fails
     * @throws IllegalArgumentException if the path is not a directory
     * @throws IllegalStateException    if duplicate versions are found
     */
    public List<Changeset> parseDirectory(final Path directory) throws IOException {
        LOG.info("Scanning directory for changelog files: {}", directory);

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        final List<Changeset> allChangesets = new ArrayList<>();

        // Find all XML files in the directory and subdirectories
        try (final Stream<Path> files = Files.walk(directory)) {
            final List<Path> xmlFiles = files
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xml"))
                .sorted()
                .toList();

            LOG.info("Found {} XML files", xmlFiles.size());

            for (final Path xmlFile : xmlFiles) {
                try {
                    final ChangeLog changeLog = parseFile(xmlFile);
                    allChangesets.addAll(changeLog.getChangesets());
                } catch (final JAXBException e) {
                    throw new IllegalStateException("Failed to parse file: " + xmlFile, e);
                }
            }
        }

        // Sort by version number
        allChangesets.sort(Comparator.comparingInt(Changeset::getVersion));

        // Validate no duplicate versions
        validateUniqueVersions(allChangesets);

        LOG.info("Total changesets found: {}", allChangesets.size());
        return allChangesets;
    }

    /**
     * Validates a single changelog file.
     *
     * @param file the file to validate
     * @return the validation result
     */
    public ValidationResult validate(final Path file) {
        try {
            parseFile(file);
            return new ValidationResult(true, file, null);
        } catch (final Exception e) {
            return new ValidationResult(false, file, e.getMessage());
        }
    }

    /**
     * Validates all changelog files in a directory.
     *
     * @param directory the directory to validate
     * @return list of validation results for each file
     * @throws IOException if reading the directory fails
     */
    public List<ValidationResult> validateDirectory(final Path directory) throws IOException {
        final List<ValidationResult> results = new ArrayList<>();

        try (final Stream<Path> files = Files.walk(directory)) {
            final List<Path> xmlFiles = files
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xml"))
                .toList();

            for (final Path xmlFile : xmlFiles) {
                results.add(validate(xmlFile));
            }
        }

        return results;
    }

    private void processIncludes(final ChangeLog changeLog, final Path baseDir) throws JAXBException {
        if (changeLog.getIncludes() == null || changeLog.getIncludes().isEmpty()) {
            return;
        }

        for (final ChangeLog.Include include : changeLog.getIncludes()) {
            final Path includePath;
            if (include.getRelativeToChangelogFile()) {
                includePath = baseDir.resolve(include.getFile());
            } else {
                includePath = Path.of(include.getFile());
            }

            LOG.debug("Processing include: {}", includePath);
            final ChangeLog includedChangeLog = parseFile(includePath);
            changeLog.getChangesets().addAll(includedChangeLog.getChangesets());
        }
    }

    private void validateUniqueVersions(final List<Changeset> changesets) {
        final Set<Integer> seenVersions = new HashSet<>();
        for (final Changeset changeset : changesets) {
            if (!seenVersions.add(changeset.getVersion())) {
                throw new IllegalStateException(
                    "Duplicate changeset version found: " + changeset.getVersion() +
                    " (author: " + changeset.getAuthor() + ")");
            }
        }
    }

    /**
     * Result of validating a changelog file.
     *
     * @param valid whether the file is valid
     * @param file  the validated file path
     * @param error the error message if invalid, null otherwise
     */
    public record ValidationResult(boolean valid, Path file, String error) {
        @Override
        public String toString() {
            if (valid) {
                return "VALID: " + file;
            }
            return "INVALID: " + file + " - " + error;
        }
    }
}
