package com.keycloakmigrator.commands;

import com.keycloakmigrator.client.KeycloakAdminClient;
import com.keycloakmigrator.config.KeycloakConfig;
import com.keycloakmigrator.executor.MigrationExecutor;
import com.keycloakmigrator.model.Changeset;
import com.keycloakmigrator.parser.XmlChangesetParser;
import com.keycloakmigrator.tracking.RealmAttributeTracker;
import com.keycloakmigrator.tracking.RealmAttributeTracker.MigrationRecord;
import com.keycloakmigrator.tracking.RealmAttributeTracker.MigrationStatus;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to show migration status.
 */
@Command(
    name = "status",
    description = "Show migration status for a realm"
)
public class StatusCommand implements Callable<Integer> {

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

    @Option(names = {"--target-realm", "-t"}, required = true, description = "Target realm to check status")
    private String targetRealm;

    @Option(names = {"--show-history"}, description = "Show full migration history")
    private boolean showHistory;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault());

    @Override
    public Integer call() {
        try {
            // Build configuration
            KeycloakConfig cliConfig = new KeycloakConfig(keycloakUrl, realm, clientId, clientSecret);
            KeycloakConfig envConfig = KeycloakConfig.fromEnvironment();
            KeycloakConfig config = envConfig.merge(cliConfig);

            config.validate();

            System.out.println("Keycloak Migrator - Status");
            System.out.println("==========================");
            System.out.println("Server: " + config.getServerUrl());
            System.out.println("Target Realm: " + targetRealm);
            System.out.println();

            // Parse changesets
            XmlChangesetParser parser = new XmlChangesetParser(false);
            List<Changeset> changesets = parser.parseDirectory(changelogDir);

            try (KeycloakAdminClient client = new KeycloakAdminClient(config)) {
                // Check if realm exists
                if (!client.realmExists(targetRealm)) {
                    System.out.println("Realm '" + targetRealm + "' does not exist.");
                    System.out.println();
                    System.out.println("Available changesets: " + changesets.size());
                    System.out.println("All changesets are pending (realm will be created).");
                    return 0;
                }

                MigrationExecutor executor = new MigrationExecutor(client);
                RealmAttributeTracker tracker = executor.getTracker();

                MigrationStatus status = tracker.getStatus(targetRealm, changesets);

                System.out.println("Migration Status");
                System.out.println("----------------");
                System.out.println("Last applied version: " + (status.lastAppliedVersion() == 0 ? "None" : status.lastAppliedVersion()));
                System.out.println("Latest available version: " + (status.availableVersion() == 0 ? "None" : status.availableVersion()));
                System.out.println("Pending migrations: " + status.pendingCount());
                System.out.println("Status: " + (status.isUpToDate() ? "UP TO DATE" : "PENDING MIGRATIONS"));
                System.out.println();

                // Show pending changesets
                if (status.pendingCount() > 0) {
                    List<Changeset> pending = tracker.getPendingChangesets(targetRealm, changesets);
                    System.out.println("Pending Changesets:");
                    for (Changeset cs : pending) {
                        System.out.println("  - Version " + cs.getVersion() + " by " + cs.getAuthor());
                        if (cs.getComment() != null) {
                            System.out.println("    " + cs.getComment());
                        }
                    }
                    System.out.println();
                }

                // Show history if requested
                if (showHistory && !status.history().isEmpty()) {
                    System.out.println("Migration History:");
                    System.out.println("------------------");
                    for (MigrationRecord record : status.history()) {
                        System.out.println("  Version " + record.version() + ":");
                        System.out.println("    Author: " + record.author());
                        System.out.println("    Applied: " + DATE_FORMAT.format(record.appliedAt()));
                        if (record.comment() != null) {
                            System.out.println("    Comment: " + record.comment());
                        }
                    }
                }
            }

            return 0;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
