package com.keycloakmigrator.config;

/**
 * Configuration for connecting to a Keycloak server.
 *
 * <p>This class holds the necessary credentials and connection information
 * to authenticate with a Keycloak server using client credentials flow.</p>
 *
 * <p>Configuration can be provided via:</p>
 * <ul>
 *   <li>Constructor parameters</li>
 *   <li>Environment variables (see {@link #fromEnvironment()})</li>
 *   <li>A combination of both using {@link #merge(KeycloakConfig)}</li>
 * </ul>
 */
public final class KeycloakConfig {

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    /**
     * Creates an empty configuration.
     */
    public KeycloakConfig() {
        this(null, null, null, null);
    }

    /**
     * Creates a configuration with the specified values.
     *
     * @param serverUrl    the Keycloak server URL (e.g., "http://localhost:8080")
     * @param realm        the realm for authentication (usually "master")
     * @param clientId     the client ID for authentication
     * @param clientSecret the client secret for authentication
     */
    public KeycloakConfig(final String serverUrl, final String realm,
                          final String clientId, final String clientSecret) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Creates a configuration from environment variables.
     *
     * <p>Environment variables used:</p>
     * <ul>
     *   <li>{@code KEYCLOAK_URL} - The Keycloak server URL</li>
     *   <li>{@code KEYCLOAK_REALM} - The realm for authentication (default: "master")</li>
     *   <li>{@code KEYCLOAK_CLIENT_ID} - The client ID for authentication</li>
     *   <li>{@code KEYCLOAK_CLIENT_SECRET} - The client secret for authentication</li>
     * </ul>
     *
     * @return a new configuration populated from environment variables
     */
    public static KeycloakConfig fromEnvironment() {
        return new KeycloakConfig(
            System.getenv("KEYCLOAK_URL"),
            getEnvOrDefault("KEYCLOAK_REALM", "master"),
            System.getenv("KEYCLOAK_CLIENT_ID"),
            System.getenv("KEYCLOAK_CLIENT_SECRET")
        );
    }

    private static String getEnvOrDefault(final String name, final String defaultValue) {
        final String value = System.getenv(name);
        return value != null && !value.isBlank() ? value : defaultValue;
    }

    /**
     * Merges this configuration with another, preferring non-null values from the other.
     *
     * <p>This is useful for combining environment-based configuration with
     * CLI argument overrides.</p>
     *
     * @param other the configuration to merge with (can be null)
     * @return a new merged configuration
     */
    public KeycloakConfig merge(final KeycloakConfig other) {
        if (other == null) {
            return this;
        }
        return new KeycloakConfig(
            other.serverUrl != null ? other.serverUrl : this.serverUrl,
            other.realm != null ? other.realm : this.realm,
            other.clientId != null ? other.clientId : this.clientId,
            other.clientSecret != null ? other.clientSecret : this.clientSecret
        );
    }

    /**
     * Validates that all required configuration is present.
     *
     * @throws IllegalStateException if configuration is incomplete
     */
    public void validate() {
        if (serverUrl == null || serverUrl.isBlank()) {
            throw new IllegalStateException("Keycloak server URL is required. " +
                "Set via --keycloak-url or KEYCLOAK_URL environment variable.");
        }
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("Keycloak client ID is required. " +
                "Set via --client-id or KEYCLOAK_CLIENT_ID environment variable.");
        }
        if (clientSecret == null || clientSecret.isBlank()) {
            throw new IllegalStateException("Keycloak client secret is required. " +
                "Set via --client-secret or KEYCLOAK_CLIENT_SECRET environment variable.");
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getRealm() {
        return realm;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public String toString() {
        return "KeycloakConfig{" +
            "serverUrl='" + serverUrl + '\'' +
            ", realm='" + realm + '\'' +
            ", clientId='" + clientId + '\'' +
            ", clientSecret='[REDACTED]'" +
            '}';
    }
}
