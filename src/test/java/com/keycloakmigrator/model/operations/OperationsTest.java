package com.keycloakmigrator.model.operations;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for various Operation implementations.
 */
class OperationsTest {

    // ==================== CreateRealmOperation Tests ====================

    @Test
    void createRealmOperation_getDescription_shouldReturnMeaningfulDescription() {
        // Given
        final CreateRealmOperation op = new CreateRealmOperation();
        op.setName("my-realm");

        // When
        final String description = op.getDescription();

        // Then
        assertEquals("Create realm: my-realm", description);
    }

    @Test
    void createRealmOperation_getTargetRealm_shouldReturnRealmName() {
        // Given
        final CreateRealmOperation op = new CreateRealmOperation();
        op.setName("test-realm");

        // When/Then
        assertEquals("test-realm", op.getTargetRealm());
    }

    @Test
    void createRealmOperation_enabled_shouldDefaultToTrue() {
        // Given
        final CreateRealmOperation op = new CreateRealmOperation();

        // Then
        assertTrue(op.getEnabled());
    }

    // ==================== DeleteRealmOperation Tests ====================

    @Test
    void deleteRealmOperation_getDescription_shouldReturnMeaningfulDescription() {
        // Given
        final DeleteRealmOperation op = new DeleteRealmOperation();
        op.setName("old-realm");

        // When
        final String description = op.getDescription();

        // Then
        assertEquals("Delete realm: old-realm", description);
    }

    // ==================== CreateClientOperation Tests ====================

    @Test
    void createClientOperation_getDescription_shouldIncludeClientIdAndRealm() {
        // Given
        final CreateClientOperation op = new CreateClientOperation();
        op.setRealm("my-realm");
        op.setClientId("my-client");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("my-client"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createClientOperation_defaults_shouldBeSetCorrectly() {
        // Given
        final CreateClientOperation op = new CreateClientOperation();

        // Then
        assertTrue(op.getEnabled());
        assertEquals("openid-connect", op.getProtocol());
        assertFalse(op.getPublicClient());
        assertFalse(op.getBearerOnly());
        assertTrue(op.getStandardFlowEnabled());
        assertFalse(op.getImplicitFlowEnabled());
        assertTrue(op.getDirectAccessGrantsEnabled());
        assertFalse(op.getServiceAccountsEnabled());
    }

    @Test
    void createClientOperation_getTargetRealm_shouldReturnRealm() {
        // Given
        final CreateClientOperation op = new CreateClientOperation();
        op.setRealm("target");

        // Then
        assertEquals("target", op.getTargetRealm());
    }

    // ==================== CreateUserOperation Tests ====================

    @Test
    void createUserOperation_getDescription_shouldIncludeUsernameAndRealm() {
        // Given
        final CreateUserOperation op = new CreateUserOperation();
        op.setRealm("my-realm");
        op.setUsername("john.doe");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("john.doe"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createUserOperation_temporaryPassword_shouldDefaultToFalse() {
        // Given
        final CreateUserOperation op = new CreateUserOperation();

        // Then
        assertFalse(op.getTemporaryPassword());
    }

    // ==================== CreateRealmRoleOperation Tests ====================

    @Test
    void createRealmRoleOperation_getDescription_shouldIncludeRoleNameAndRealm() {
        // Given
        final CreateRealmRoleOperation op = new CreateRealmRoleOperation();
        op.setRealm("my-realm");
        op.setName("admin");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("admin"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createRealmRoleOperation_composite_shouldDefaultToFalse() {
        // Given
        final CreateRealmRoleOperation op = new CreateRealmRoleOperation();

        // Then
        assertFalse(op.getComposite());
    }

    // ==================== CreateClientRoleOperation Tests ====================

    @Test
    void createClientRoleOperation_getDescription_shouldIncludeRoleClientAndRealm() {
        // Given
        final CreateClientRoleOperation op = new CreateClientRoleOperation();
        op.setRealm("my-realm");
        op.setClientId("my-client");
        op.setName("editor");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("editor"));
        assertTrue(description.contains("my-client"));
        assertTrue(description.contains("my-realm"));
    }

    // ==================== DeleteRoleOperation Tests ====================

    @Test
    void deleteRoleOperation_getDescription_forRealmRole_shouldNotIncludeClient() {
        // Given
        final DeleteRoleOperation op = new DeleteRoleOperation();
        op.setRealm("my-realm");
        op.setName("old-role");
        // clientId is null = realm role

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("realm role"));
        assertTrue(description.contains("old-role"));
        assertFalse(description.contains("client"));
    }

    @Test
    void deleteRoleOperation_getDescription_forClientRole_shouldIncludeClient() {
        // Given
        final DeleteRoleOperation op = new DeleteRoleOperation();
        op.setRealm("my-realm");
        op.setName("old-role");
        op.setClientId("my-client");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("client role"));
        assertTrue(description.contains("my-client"));
    }

    // ==================== CreateGroupOperation Tests ====================

    @Test
    void createGroupOperation_getDescription_shouldIncludeGroupNameAndRealm() {
        // Given
        final CreateGroupOperation op = new CreateGroupOperation();
        op.setRealm("my-realm");
        op.setName("Administrators");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("Administrators"));
        assertTrue(description.contains("my-realm"));
    }

    // ==================== CreateClientScopeOperation Tests ====================

    @Test
    void createClientScopeOperation_getDescription_shouldIncludeScopeNameAndRealm() {
        // Given
        final CreateClientScopeOperation op = new CreateClientScopeOperation();
        op.setRealm("my-realm");
        op.setName("custom-scope");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("custom-scope"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createClientScopeOperation_protocol_shouldDefaultToOpenidConnect() {
        // Given
        final CreateClientScopeOperation op = new CreateClientScopeOperation();

        // Then
        assertEquals("openid-connect", op.getProtocol());
    }

    // ==================== CreateIdentityProviderOperation Tests ====================

    @Test
    void createIdentityProviderOperation_getDescription_shouldIncludeAliasAndRealm() {
        // Given
        final CreateIdentityProviderOperation op = new CreateIdentityProviderOperation();
        op.setRealm("my-realm");
        op.setAlias("google");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("google"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createIdentityProviderOperation_enabled_shouldDefaultToTrue() {
        // Given
        final CreateIdentityProviderOperation op = new CreateIdentityProviderOperation();

        // Then
        assertTrue(op.getEnabled());
    }

    // ==================== CreateProtocolMapperOperation Tests ====================

    @Test
    void createProtocolMapperOperation_getDescription_forClient_shouldIncludeClientId() {
        // Given
        final CreateProtocolMapperOperation op = new CreateProtocolMapperOperation();
        op.setRealm("my-realm");
        op.setClientId("my-client");
        op.setName("custom-mapper");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("custom-mapper"));
        assertTrue(description.contains("my-client"));
    }

    @Test
    void createProtocolMapperOperation_getDescription_forClientScope_shouldIncludeClientScopeName() {
        // Given
        final CreateProtocolMapperOperation op = new CreateProtocolMapperOperation();
        op.setRealm("my-realm");
        op.setClientScopeName("custom-scope");
        op.setName("custom-mapper");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("custom-mapper"));
        assertTrue(description.contains("custom-scope"));
    }

    // ==================== CreateAuthenticationFlowOperation Tests ====================

    @Test
    void createAuthenticationFlowOperation_getDescription_shouldIncludeAliasAndRealm() {
        // Given
        final CreateAuthenticationFlowOperation op = new CreateAuthenticationFlowOperation();
        op.setRealm("my-realm");
        op.setAlias("custom-flow");

        // When
        final String description = op.getDescription();

        // Then
        assertTrue(description.contains("custom-flow"));
        assertTrue(description.contains("my-realm"));
    }

    @Test
    void createAuthenticationFlowOperation_defaults_shouldBeSetCorrectly() {
        // Given
        final CreateAuthenticationFlowOperation op = new CreateAuthenticationFlowOperation();

        // Then
        assertEquals("basic-flow", op.getProviderId());
        assertTrue(op.getTopLevel());
        assertFalse(op.getBuiltIn());
    }
}
