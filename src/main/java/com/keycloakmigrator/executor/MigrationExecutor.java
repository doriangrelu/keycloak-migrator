package com.keycloakmigrator.executor;

import com.keycloakmigrator.client.KeycloakAdminClient;
import com.keycloakmigrator.model.Changeset;
import com.keycloakmigrator.model.operations.Operation;
import com.keycloakmigrator.tracking.RealmAttributeTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Executes migrations against a Keycloak server.
 */
public class MigrationExecutor {

    private static final Logger log = LoggerFactory.getLogger(MigrationExecutor.class);

    private final KeycloakAdminClient client;
    private final RealmAttributeTracker tracker;

    public MigrationExecutor(KeycloakAdminClient client) {
        this.client = client;
        this.tracker = new RealmAttributeTracker(client);
    }

    /**
     * Execute all pending migrations for a given realm context.
     *
     * @param changesets all changesets to potentially apply
     * @param targetRealm the realm to track migrations against (usually the first realm created)
     * @return the result of the migration execution
     */
    public MigrationResult executeMigrations(List<Changeset> changesets, String targetRealm) {
        log.info("Starting migration execution for realm context: {}", targetRealm);

        // Determine which realm to use for tracking
        String trackingRealm = determineTrackingRealm(changesets, targetRealm);

        // Get pending changesets
        List<Changeset> pending = tracker.getPendingChangesets(trackingRealm, changesets);

        if (pending.isEmpty()) {
            log.info("No pending migrations to apply");
            return new MigrationResult(0, 0, Collections.emptyList(), Collections.emptyList());
        }

        log.info("Found {} pending migrations to apply", pending.size());

        List<Changeset> applied = new ArrayList<>();
        List<MigrationError> errors = new ArrayList<>();

        for (Changeset changeset : pending) {
            log.info("Applying changeset version {} by {}", changeset.getVersion(), changeset.getAuthor());

            if (changeset.getComment() != null) {
                log.info("Comment: {}", changeset.getComment());
            }

            try {
                executeChangeset(changeset);

                // Record the migration after successful execution
                // Use the tracking realm (first realm affected or specified target)
                String realmForTracking = determineRealmForChangeset(changeset, trackingRealm);
                if (client.realmExists(realmForTracking)) {
                    tracker.recordMigration(realmForTracking, changeset);
                }

                applied.add(changeset);
                log.info("Successfully applied changeset version {}", changeset.getVersion());

            } catch (Exception e) {
                log.error("Failed to apply changeset version {}: {}", changeset.getVersion(), e.getMessage());
                errors.add(new MigrationError(changeset, e));

                if (changeset.getFailOnError()) {
                    log.error("Stopping migration due to failure (failOnError=true)");
                    break;
                } else {
                    log.warn("Continuing despite error (failOnError=false)");
                }
            }
        }

        return new MigrationResult(pending.size(), applied.size(), applied, errors);
    }

    /**
     * Execute a single changeset.
     *
     * @param changeset the changeset to execute
     */
    public void executeChangeset(Changeset changeset) throws Exception {
        List<Operation> operations = changeset.getOperations();

        log.debug("Executing {} operations in changeset version {}", operations.size(), changeset.getVersion());

        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i);
            log.info("  [{}/{}] {}", i + 1, operations.size(), operation.getDescription());
            operation.execute(client);
        }
    }

    /**
     * Preview what migrations would be applied without actually applying them.
     *
     * @param changesets all changesets
     * @param targetRealm the target realm
     * @return list of changesets that would be applied
     */
    public List<Changeset> previewMigrations(List<Changeset> changesets, String targetRealm) {
        String trackingRealm = determineTrackingRealm(changesets, targetRealm);
        return tracker.getPendingChangesets(trackingRealm, changesets);
    }

    /**
     * Get the migration tracker.
     */
    public RealmAttributeTracker getTracker() {
        return tracker;
    }

    private String determineTrackingRealm(List<Changeset> changesets, String targetRealm) {
        // If a target realm is specified and exists, use it
        if (targetRealm != null && !targetRealm.isBlank() && client.realmExists(targetRealm)) {
            return targetRealm;
        }

        // Otherwise, find the first realm that will be created or already exists
        for (Changeset changeset : changesets) {
            for (Operation op : changeset.getOperations()) {
                String realm = op.getTargetRealm();
                if (realm != null && client.realmExists(realm)) {
                    return realm;
                }
            }
        }

        // If no existing realm found, return the target realm (might be created)
        return targetRealm != null ? targetRealm : "master";
    }

    private String determineRealmForChangeset(Changeset changeset, String defaultRealm) {
        // Find the first realm affected by this changeset
        for (Operation op : changeset.getOperations()) {
            String realm = op.getTargetRealm();
            if (realm != null && client.realmExists(realm)) {
                return realm;
            }
        }
        return defaultRealm;
    }

    /**
     * Result of a migration execution.
     */
    public record MigrationResult(
        int totalPending,
        int totalApplied,
        List<Changeset> applied,
        List<MigrationError> errors
    ) {
        public boolean isSuccess() {
            return errors.isEmpty();
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }

    /**
     * Error that occurred during migration.
     */
    public record MigrationError(Changeset changeset, Exception exception) {
        public String getMessage() {
            return "Changeset " + changeset.getVersion() + ": " + exception.getMessage();
        }
    }
}
