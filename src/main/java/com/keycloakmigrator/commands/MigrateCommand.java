package com.keycloakmigrator.commands;

import com.keycloakmigrator.client.KeycloakAdminClient;
import com.keycloakmigrator.config.KeycloakConfig;
import com.keycloakmigrator.executor.MigrationExecutor;
import com.keycloakmigrator.model.Changeset;
import com.keycloakmigrator.parser.XmlChangesetParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to apply pending migrations.
 */
@Command(
    name = "migrate",
    description = "Apply pending migrations to Keycloak"
)
public class MigrateCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Directory containing changelog XML files")
    private Path changelogDir;

    @Option(names = {"--keycloak-url", "-u"}, description = "Keycloak server URL")
    private String keycloakUrl;

    @Option(names = {"--client-id", "-c"}, description = "Client ID for authentication")
    private String clientId;

    @Option(names = {"--client-secret", "-s"}, description = "Client secret for authentication")
    private String clientSecret;

    @Option(names = {"--realm", "-r"}, description = "Authentication realm (default: master)")
    private String realm;

    @Option(names = {"--target-realm", "-t"}, description = "Target realm for migration tracking")
    private String targetRealm;

    @Option(names = {"--dry-run"}, description = "Preview changes without applying them")
    private boolean dryRun;

    @Option(names = {"--skip-validation"}, description = "Skip XML schema validation")
    private boolean skipValidation;

    @Override
    public Integer call() {
        try {
            // Build configuration from CLI args + environment variables
            KeycloakConfig cliConfig = new KeycloakConfig(keycloakUrl, realm, clientId, clientSecret);
            KeycloakConfig envConfig = KeycloakConfig.fromEnvironment();
            KeycloakConfig config = envConfig.merge(cliConfig);

            config.validate();

            System.out.println("Keycloak Migrator - Migrate");
            System.out.println("===========================");
            System.out.println("Server: " + config.getServerUrl());
            System.out.println("Auth Realm: " + config.getRealm());
            System.out.println("Changelog Directory: " + changelogDir);
            System.out.println();

            // Parse changesets
            XmlChangesetParser parser = new XmlChangesetParser(!skipValidation);
            List<Changeset> changesets = parser.parseDirectory(changelogDir);

            System.out.println("Found " + changesets.size() + " changeset(s)");
            System.out.println();

            if (changesets.isEmpty()) {
                System.out.println("No changesets found. Nothing to do.");
                return 0;
            }

            try (KeycloakAdminClient client = new KeycloakAdminClient(config)) {
                MigrationExecutor executor = new MigrationExecutor(client);

                if (dryRun) {
                    // Preview mode
                    System.out.println("DRY RUN MODE - No changes will be applied");
                    System.out.println();

                    List<Changeset> pending = executor.previewMigrations(changesets, targetRealm);

                    if (pending.isEmpty()) {
                        System.out.println("No pending migrations.");
                    } else {
                        System.out.println("Pending migrations (" + pending.size() + "):");
                        for (Changeset cs : pending) {
                            System.out.println("  - Version " + cs.getVersion() + " by " + cs.getAuthor());
                            if (cs.getComment() != null) {
                                System.out.println("    " + cs.getComment());
                            }
                            System.out.println("    Operations: " + cs.getOperations().size());
                        }
                    }

                    return 0;
                }

                // Execute migrations
                MigrationExecutor.MigrationResult result = executor.executeMigrations(changesets, targetRealm);

                System.out.println();
                System.out.println("Migration Summary");
                System.out.println("-----------------");
                System.out.println("Total pending: " + result.totalPending());
                System.out.println("Applied: " + result.totalApplied());
                System.out.println("Errors: " + result.errors().size());

                if (result.hasErrors()) {
                    System.out.println();
                    System.out.println("Errors:");
                    for (MigrationExecutor.MigrationError error : result.errors()) {
                        System.out.println("  - " + error.getMessage());
                    }
                    return 1;
                }

                if (result.isSuccess()) {
                    System.out.println();
                    System.out.println("All migrations applied successfully!");
                }

                return 0;
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
