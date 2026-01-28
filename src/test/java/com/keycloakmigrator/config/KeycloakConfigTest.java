package com.keycloakmigrator.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link KeycloakConfig}.
 */
class KeycloakConfigTest {

    @Test
    void constructor_withAllParameters_shouldSetAllFields() {
        // When
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            "secret"
        );

        // Then
        assertEquals("http://localhost:8080", config.getServerUrl());
        assertEquals("master", config.getRealm());
        assertEquals("admin-cli", config.getClientId());
        assertEquals("secret", config.getClientSecret());
    }

    @Test
    void defaultConstructor_shouldCreateEmptyConfig() {
        // When
        final KeycloakConfig config = new KeycloakConfig();

        // Then
        assertNull(config.getServerUrl());
        assertNull(config.getRealm());
        assertNull(config.getClientId());
        assertNull(config.getClientSecret());
    }

    @Test
    void merge_withNullOther_shouldReturnThis() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            "secret"
        );

        // When
        final KeycloakConfig merged = config.merge(null);

        // Then
        assertSame(config, merged);
    }

    @Test
    void merge_shouldPreferOtherNonNullValues() {
        // Given
        final KeycloakConfig base = new KeycloakConfig(
            "http://base:8080",
            "base-realm",
            "base-client",
            "base-secret"
        );
        final KeycloakConfig other = new KeycloakConfig(
            "http://other:8080",
            null,  // Should keep base value
            "other-client",
            null   // Should keep base value
        );

        // When
        final KeycloakConfig merged = base.merge(other);

        // Then
        assertEquals("http://other:8080", merged.getServerUrl());
        assertEquals("base-realm", merged.getRealm());
        assertEquals("other-client", merged.getClientId());
        assertEquals("base-secret", merged.getClientSecret());
    }

    @Test
    void merge_withEmptyBase_shouldUseOtherValues() {
        // Given
        final KeycloakConfig base = new KeycloakConfig();
        final KeycloakConfig other = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            "secret"
        );

        // When
        final KeycloakConfig merged = base.merge(other);

        // Then
        assertEquals("http://localhost:8080", merged.getServerUrl());
        assertEquals("master", merged.getRealm());
        assertEquals("admin-cli", merged.getClientId());
        assertEquals("secret", merged.getClientSecret());
    }

    @Test
    void validate_withCompleteConfig_shouldNotThrow() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            "secret"
        );

        // When/Then - should not throw
        assertDoesNotThrow(config::validate);
    }

    @Test
    void validate_withMissingServerUrl_shouldThrow() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            null,
            "master",
            "admin-cli",
            "secret"
        );

        // When/Then
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            config::validate
        );
        assertTrue(exception.getMessage().contains("server URL"));
    }

    @Test
    void validate_withBlankServerUrl_shouldThrow() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "   ",
            "master",
            "admin-cli",
            "secret"
        );

        // When/Then
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            config::validate
        );
        assertTrue(exception.getMessage().contains("server URL"));
    }

    @Test
    void validate_withMissingClientId_shouldThrow() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            null,
            "secret"
        );

        // When/Then
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            config::validate
        );
        assertTrue(exception.getMessage().contains("client ID"));
    }

    @Test
    void validate_withMissingClientSecret_shouldThrow() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            null
        );

        // When/Then
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            config::validate
        );
        assertTrue(exception.getMessage().contains("client secret"));
    }

    @Test
    void toString_shouldNotExposeSecret() {
        // Given
        final KeycloakConfig config = new KeycloakConfig(
            "http://localhost:8080",
            "master",
            "admin-cli",
            "super-secret-value"
        );

        // When
        final String result = config.toString();

        // Then
        assertFalse(result.contains("super-secret-value"));
        assertTrue(result.contains("[REDACTED]"));
        assertTrue(result.contains("http://localhost:8080"));
        assertTrue(result.contains("admin-cli"));
    }
}
