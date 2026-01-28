package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;

/**
 * Base interface for all migration operations.
 */
public interface Operation {

    /**
     * Execute this operation against the Keycloak server.
     *
     * @param client the Keycloak admin client
     * @throws Exception if the operation fails
     */
    void execute(KeycloakAdminClient client) throws Exception;

    /**
     * Get a human-readable description of this operation.
     *
     * @return the operation description
     */
    String getDescription();

    /**
     * Get the target realm for this operation, if applicable.
     *
     * @return the realm name, or null if not applicable
     */
    default String getTargetRealm() {
        return null;
    }
}
