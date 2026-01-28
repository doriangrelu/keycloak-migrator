package com.keycloakmigrator.parser;

import com.keycloakmigrator.model.ChangeLog;
import com.keycloakmigrator.model.Changeset;
import com.keycloakmigrator.model.operations.CreateRealmOperation;
import com.keycloakmigrator.model.operations.Operation;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link XmlChangesetParser}.
 */
class XmlChangesetParserTest {

    private XmlChangesetParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new XmlChangesetParser(false); // Disable schema validation for tests
    }

    @Test
    void parseFile_withValidChangelog_shouldParseSuccessfully() throws IOException, JAXBException {
        // Given
        final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="test">
                    <comment>Test changeset</comment>
                    <createRealm name="test-realm">
                        <displayName>Test Realm</displayName>
                        <enabled>true</enabled>
                    </createRealm>
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("test-changelog.xml");
        Files.writeString(file, xml);

        // When
        final ChangeLog changeLog = parser.parseFile(file);

        // Then
        assertNotNull(changeLog);
        assertEquals(1, changeLog.getChangesets().size());

        final Changeset changeset = changeLog.getChangesets().get(0);
        assertEquals(1, changeset.getVersion());
        assertEquals("test", changeset.getAuthor());
        assertEquals("Test changeset", changeset.getComment());
        assertEquals(1, changeset.getOperations().size());

        final Operation operation = changeset.getOperations().get(0);
        assertInstanceOf(CreateRealmOperation.class, operation);

        final CreateRealmOperation createRealm = (CreateRealmOperation) operation;
        assertEquals("test-realm", createRealm.getName());
        assertEquals("Test Realm", createRealm.getDisplayName());
        assertTrue(createRealm.getEnabled());
    }

    @Test
    void parseFile_withMultipleChangesets_shouldParseAll() throws IOException, JAXBException {
        // Given
        final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin">
                    <createRealm name="realm1">
                        <enabled>true</enabled>
                    </createRealm>
                </changeset>
                <changeset version="2" author="admin">
                    <createRealm name="realm2">
                        <enabled>true</enabled>
                    </createRealm>
                </changeset>
                <changeset version="3" author="dev">
                    <createRealm name="realm3">
                        <enabled>false</enabled>
                    </createRealm>
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("multi-changelog.xml");
        Files.writeString(file, xml);

        // When
        final ChangeLog changeLog = parser.parseFile(file);

        // Then
        assertEquals(3, changeLog.getChangesets().size());
        assertEquals(1, changeLog.getChangesets().get(0).getVersion());
        assertEquals(2, changeLog.getChangesets().get(1).getVersion());
        assertEquals(3, changeLog.getChangesets().get(2).getVersion());
    }

    @Test
    void parseDirectory_shouldParseAllXmlFiles() throws IOException {
        // Given
        final String xml1 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin">
                    <createRealm name="realm1"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final String xml2 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="2" author="admin">
                    <createRealm name="realm2"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;

        Files.writeString(tempDir.resolve("01-first.xml"), xml1);
        Files.writeString(tempDir.resolve("02-second.xml"), xml2);

        // When
        final List<Changeset> changesets = parser.parseDirectory(tempDir);

        // Then
        assertEquals(2, changesets.size());
        assertEquals(1, changesets.get(0).getVersion());
        assertEquals(2, changesets.get(1).getVersion());
    }

    @Test
    void parseDirectory_shouldSortByVersion() throws IOException {
        // Given - files in wrong order
        final String xml1 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="3" author="admin">
                    <createRealm name="realm3"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final String xml2 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin">
                    <createRealm name="realm1"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;

        Files.writeString(tempDir.resolve("z-last.xml"), xml1);
        Files.writeString(tempDir.resolve("a-first.xml"), xml2);

        // When
        final List<Changeset> changesets = parser.parseDirectory(tempDir);

        // Then - should be sorted by version, not filename
        assertEquals(2, changesets.size());
        assertEquals(1, changesets.get(0).getVersion());
        assertEquals(3, changesets.get(1).getVersion());
    }

    @Test
    void parseDirectory_withDuplicateVersions_shouldThrowException() throws IOException {
        // Given
        final String xml1 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin">
                    <createRealm name="realm1"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final String xml2 = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="other">
                    <createRealm name="realm2"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;

        Files.writeString(tempDir.resolve("first.xml"), xml1);
        Files.writeString(tempDir.resolve("second.xml"), xml2);

        // When/Then
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> parser.parseDirectory(tempDir)
        );
        assertTrue(exception.getMessage().contains("Duplicate changeset version"));
    }

    @Test
    void parseDirectory_withNonDirectory_shouldThrowException() {
        // Given
        final Path file = tempDir.resolve("not-a-directory.txt");

        // When/Then
        assertThrows(
            IllegalArgumentException.class,
            () -> parser.parseDirectory(file)
        );
    }

    @Test
    void validate_withValidFile_shouldReturnValid() throws IOException {
        // Given
        final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin">
                    <createRealm name="test"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("valid.xml");
        Files.writeString(file, xml);

        // When
        final XmlChangesetParser.ValidationResult result = parser.validate(file);

        // Then
        assertTrue(result.valid());
        assertNull(result.error());
    }

    @Test
    void validate_withInvalidXml_shouldReturnInvalid() throws IOException {
        // Given
        final String invalidXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="not-a-number" author="admin">
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("invalid.xml");
        Files.writeString(file, invalidXml);

        // When
        final XmlChangesetParser.ValidationResult result = parser.validate(file);

        // Then
        assertFalse(result.valid());
        assertNotNull(result.error());
    }

    @Test
    void validate_withMalformedXml_shouldReturnInvalid() throws IOException {
        // Given
        final String malformedXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog>
                <unclosed-tag>
            </changelog>
            """;
        final Path file = tempDir.resolve("malformed.xml");
        Files.writeString(file, malformedXml);

        // When
        final XmlChangesetParser.ValidationResult result = parser.validate(file);

        // Then
        assertFalse(result.valid());
        assertNotNull(result.error());
    }

    @Test
    void parseFile_withFailOnErrorAttribute_shouldParse() throws IOException, JAXBException {
        // Given
        final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin" failOnError="false">
                    <createRealm name="test"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("fail-on-error.xml");
        Files.writeString(file, xml);

        // When
        final ChangeLog changeLog = parser.parseFile(file);

        // Then
        assertFalse(changeLog.getChangesets().get(0).getFailOnError());
    }

    @Test
    void parseFile_withContextAttribute_shouldParse() throws IOException, JAXBException {
        // Given
        final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <changelog xmlns="http://keycloak-migrator.com/changelog">
                <changeset version="1" author="admin" context="dev">
                    <createRealm name="test"><enabled>true</enabled></createRealm>
                </changeset>
            </changelog>
            """;
        final Path file = tempDir.resolve("context.xml");
        Files.writeString(file, xml);

        // When
        final ChangeLog changeLog = parser.parseFile(file);

        // Then
        assertEquals("dev", changeLog.getChangesets().get(0).getContext());
    }
}
