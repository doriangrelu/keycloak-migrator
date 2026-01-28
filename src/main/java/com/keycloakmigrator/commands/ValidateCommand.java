package com.keycloakmigrator.commands;

import com.keycloakmigrator.parser.XmlChangesetParser;
import com.keycloakmigrator.parser.XmlChangesetParser.ValidationResult;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to validate changelog XML files.
 */
@Command(
    name = "validate",
    description = "Validate changelog XML files"
)
public class ValidateCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Directory or file to validate")
    private Path path;

    @Override
    public Integer call() {
        try {
            System.out.println("Keycloak Migrator - Validate");
            System.out.println("============================");
            System.out.println("Path: " + path);
            System.out.println();

            XmlChangesetParser parser = new XmlChangesetParser(true);

            if (path.toFile().isFile()) {
                // Validate single file
                ValidationResult result = parser.validate(path);
                printResult(result);
                return result.valid() ? 0 : 1;
            }

            // Validate directory
            List<ValidationResult> results = parser.validateDirectory(path);

            if (results.isEmpty()) {
                System.out.println("No XML files found in directory.");
                return 0;
            }

            int validCount = 0;
            int invalidCount = 0;

            System.out.println("Validation Results:");
            System.out.println("-------------------");

            for (ValidationResult result : results) {
                printResult(result);
                if (result.valid()) {
                    validCount++;
                } else {
                    invalidCount++;
                }
            }

            System.out.println();
            System.out.println("Summary:");
            System.out.println("  Valid: " + validCount);
            System.out.println("  Invalid: " + invalidCount);
            System.out.println("  Total: " + results.size());

            // Also try to parse and check for duplicate versions
            try {
                parser.parseDirectory(path);
                System.out.println("  Version check: OK (no duplicates)");
            } catch (IllegalStateException e) {
                System.out.println("  Version check: FAILED - " + e.getMessage());
                invalidCount++;
            }

            return invalidCount > 0 ? 1 : 0;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private void printResult(ValidationResult result) {
        if (result.valid()) {
            System.out.println("  [OK] " + result.file().getFileName());
        } else {
            System.out.println("  [FAIL] " + result.file().getFileName());
            System.out.println("         Error: " + result.error());
        }
    }
}
