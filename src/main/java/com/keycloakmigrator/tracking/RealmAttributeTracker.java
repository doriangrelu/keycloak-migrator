package com.keycloakmigrator.tracking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keycloakmigrator.client.KeycloakAdminClient;
import com.keycloakmigrator.model.Changeset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * Tracks migration history using realm attributes in Keycloak.
 *
 * Attributes used:
 * - migrator.lastVersion: The last applied migration version
 * - migrator.history: JSON array of applied migrations
 */
public class RealmAttributeTracker {

    private static final Logger log = LoggerFactory.getLogger(RealmAttributeTracker.class);

    private static final String ATTR_LAST_VERSION = "migrator.lastVersion";
    private static final String ATTR_HISTORY = "migrator.history";

    private final KeycloakAdminClient client;
    private final ObjectMapper objectMapper;

    public RealmAttributeTracker(KeycloakAdminClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Get the last applied migration version for a realm.
     *
     * @param realmName the realm name
     * @return the last version, or 0 if no migrations have been applied
     */
    public int getLastAppliedVersion(String realmName) {
        if (!client.realmExists(realmName)) {
            return 0;
        }

        Map<String, String> attributes = client.getRealmAttributes(realmName);
        String lastVersion = attributes.get(ATTR_LAST_VERSION);

        if (lastVersion == null || lastVersion.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(lastVersion);
        } catch (NumberFormatException e) {
            log.warn("Invalid last version value: {}", lastVersion);
            return 0;
        }
    }

    /**
     * Get the full migration history for a realm.
     *
     * @param realmName the realm name
     * @return list of migration records
     */
    public List<MigrationRecord> getMigrationHistory(String realmName) {
        if (!client.realmExists(realmName)) {
            return Collections.emptyList();
        }

        Map<String, String> attributes = client.getRealmAttributes(realmName);
        String historyJson = attributes.get(ATTR_HISTORY);

        if (historyJson == null || historyJson.isBlank()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(historyJson, new TypeReference<List<MigrationRecord>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse migration history: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Record that a changeset has been applied.
     *
     * @param realmName the realm name
     * @param changeset the changeset that was applied
     */
    public void recordMigration(String realmName, Changeset changeset) {
        log.debug("Recording migration {} for realm {}", changeset.getVersion(), realmName);

        // Update last version
        client.setRealmAttribute(realmName, ATTR_LAST_VERSION, String.valueOf(changeset.getVersion()));

        // Update history
        List<MigrationRecord> history = getMigrationHistory(realmName);
        history.add(new MigrationRecord(
            changeset.getVersion(),
            changeset.getAuthor(),
            Instant.now(),
            changeset.getComment()
        ));

        try {
            String historyJson = objectMapper.writeValueAsString(history);
            client.setRealmAttribute(realmName, ATTR_HISTORY, historyJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize migration history", e);
        }
    }

    /**
     * Check if a specific version has been applied.
     *
     * @param realmName the realm name
     * @param version the version to check
     * @return true if the version has been applied
     */
    public boolean isVersionApplied(String realmName, int version) {
        List<MigrationRecord> history = getMigrationHistory(realmName);
        return history.stream().anyMatch(r -> r.version() == version);
    }

    /**
     * Get pending changesets that haven't been applied yet.
     *
     * @param realmName the realm name
     * @param allChangesets all available changesets
     * @return list of changesets that need to be applied
     */
    public List<Changeset> getPendingChangesets(String realmName, List<Changeset> allChangesets) {
        int lastVersion = getLastAppliedVersion(realmName);

        return allChangesets.stream()
            .filter(c -> c.getVersion() > lastVersion)
            .sorted(Comparator.comparingInt(Changeset::getVersion))
            .toList();
    }

    /**
     * Record of a single migration.
     */
    public record MigrationRecord(
        int version,
        String author,
        Instant appliedAt,
        String comment
    ) {}

    /**
     * Status of migrations for a realm.
     */
    public record MigrationStatus(
        String realmName,
        int lastAppliedVersion,
        int availableVersion,
        int pendingCount,
        List<MigrationRecord> history
    ) {
        public boolean isUpToDate() {
            return pendingCount == 0;
        }
    }

    /**
     * Get the migration status for a realm.
     *
     * @param realmName the realm name
     * @param allChangesets all available changesets
     * @return the migration status
     */
    public MigrationStatus getStatus(String realmName, List<Changeset> allChangesets) {
        int lastApplied = getLastAppliedVersion(realmName);
        int maxAvailable = allChangesets.stream()
            .mapToInt(Changeset::getVersion)
            .max()
            .orElse(0);
        int pendingCount = (int) allChangesets.stream()
            .filter(c -> c.getVersion() > lastApplied)
            .count();
        List<MigrationRecord> history = getMigrationHistory(realmName);

        return new MigrationStatus(realmName, lastApplied, maxAvailable, pendingCount, history);
    }
}
