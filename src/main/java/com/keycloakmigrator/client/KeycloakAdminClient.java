package com.keycloakmigrator.client;

import com.keycloakmigrator.config.KeycloakConfig;
import com.keycloakmigrator.model.operations.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wrapper around Keycloak Admin Client for executing migration operations.
 */
public class KeycloakAdminClient implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminClient.class);

    private final Keycloak keycloak;
    private final KeycloakConfig config;

    public KeycloakAdminClient(KeycloakConfig config) {
        this.config = config;
        this.keycloak = KeycloakBuilder.builder()
            .serverUrl(config.getServerUrl())
            .realm(config.getRealm())
            .clientId(config.getClientId())
            .clientSecret(config.getClientSecret())
            .grantType("client_credentials")
            .build();
    }

    @Override
    public void close() {
        if (keycloak != null) {
            keycloak.close();
        }
    }

    // ==================== Realm Operations ====================

    public void createRealm(CreateRealmOperation op) {
        log.info("Creating realm: {}", op.getName());

        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(op.getName());
        realm.setEnabled(op.getEnabled());

        if (op.getDisplayName() != null) realm.setDisplayName(op.getDisplayName());
        if (op.getDisplayNameHtml() != null) realm.setDisplayNameHtml(op.getDisplayNameHtml());
        if (op.getRegistrationAllowed() != null) realm.setRegistrationAllowed(op.getRegistrationAllowed());
        if (op.getRegistrationEmailAsUsername() != null) realm.setRegistrationEmailAsUsername(op.getRegistrationEmailAsUsername());
        if (op.getVerifyEmail() != null) realm.setVerifyEmail(op.getVerifyEmail());
        if (op.getResetPasswordAllowed() != null) realm.setResetPasswordAllowed(op.getResetPasswordAllowed());
        if (op.getLoginWithEmailAllowed() != null) realm.setLoginWithEmailAllowed(op.getLoginWithEmailAllowed());
        if (op.getDuplicateEmailsAllowed() != null) realm.setDuplicateEmailsAllowed(op.getDuplicateEmailsAllowed());
        if (op.getRememberMe() != null) realm.setRememberMe(op.getRememberMe());
        if (op.getBruteForceProtected() != null) realm.setBruteForceProtected(op.getBruteForceProtected());
        if (op.getSslRequired() != null) realm.setSslRequired(op.getSslRequired());
        if (op.getAccessTokenLifespan() != null) realm.setAccessTokenLifespan(op.getAccessTokenLifespan());
        if (op.getSsoSessionIdleTimeout() != null) realm.setSsoSessionIdleTimeout(op.getSsoSessionIdleTimeout());
        if (op.getSsoSessionMaxLifespan() != null) realm.setSsoSessionMaxLifespan(op.getSsoSessionMaxLifespan());
        if (op.getPasswordPolicy() != null) realm.setPasswordPolicy(op.getPasswordPolicy());
        if (op.getLoginTheme() != null) realm.setLoginTheme(op.getLoginTheme());
        if (op.getAccountTheme() != null) realm.setAccountTheme(op.getAccountTheme());
        if (op.getAdminTheme() != null) realm.setAdminTheme(op.getAdminTheme());
        if (op.getEmailTheme() != null) realm.setEmailTheme(op.getEmailTheme());
        if (op.getInternationalizationEnabled() != null) realm.setInternationalizationEnabled(op.getInternationalizationEnabled());
        if (op.getSupportedLocales() != null) realm.setSupportedLocales(new HashSet<>(op.getSupportedLocales()));
        if (op.getDefaultLocale() != null) realm.setDefaultLocale(op.getDefaultLocale());

        if (op.getSmtpServer() != null) {
            Map<String, String> smtp = new HashMap<>();
            var smtpConfig = op.getSmtpServer();
            if (smtpConfig.getHost() != null) smtp.put("host", smtpConfig.getHost());
            if (smtpConfig.getPort() != null) smtp.put("port", smtpConfig.getPort());
            if (smtpConfig.getFrom() != null) smtp.put("from", smtpConfig.getFrom());
            if (smtpConfig.getFromDisplayName() != null) smtp.put("fromDisplayName", smtpConfig.getFromDisplayName());
            if (smtpConfig.getSsl() != null) smtp.put("ssl", smtpConfig.getSsl().toString());
            if (smtpConfig.getStarttls() != null) smtp.put("starttls", smtpConfig.getStarttls().toString());
            if (smtpConfig.getAuth() != null) smtp.put("auth", smtpConfig.getAuth().toString());
            if (smtpConfig.getUser() != null) smtp.put("user", smtpConfig.getUser());
            if (smtpConfig.getPassword() != null) smtp.put("password", smtpConfig.getPassword());
            realm.setSmtpServer(smtp);
        }

        keycloak.realms().create(realm);
        log.info("Realm '{}' created successfully", op.getName());
    }

    public void updateRealm(UpdateRealmOperation op) {
        log.info("Updating realm: {}", op.getName());

        RealmResource realmResource = keycloak.realm(op.getName());
        RealmRepresentation realm = realmResource.toRepresentation();

        if (op.getDisplayName() != null) realm.setDisplayName(op.getDisplayName());
        if (op.getDisplayNameHtml() != null) realm.setDisplayNameHtml(op.getDisplayNameHtml());
        if (op.getEnabled() != null) realm.setEnabled(op.getEnabled());
        if (op.getRegistrationAllowed() != null) realm.setRegistrationAllowed(op.getRegistrationAllowed());
        if (op.getRegistrationEmailAsUsername() != null) realm.setRegistrationEmailAsUsername(op.getRegistrationEmailAsUsername());
        if (op.getVerifyEmail() != null) realm.setVerifyEmail(op.getVerifyEmail());
        if (op.getResetPasswordAllowed() != null) realm.setResetPasswordAllowed(op.getResetPasswordAllowed());
        if (op.getLoginWithEmailAllowed() != null) realm.setLoginWithEmailAllowed(op.getLoginWithEmailAllowed());
        if (op.getDuplicateEmailsAllowed() != null) realm.setDuplicateEmailsAllowed(op.getDuplicateEmailsAllowed());
        if (op.getRememberMe() != null) realm.setRememberMe(op.getRememberMe());
        if (op.getBruteForceProtected() != null) realm.setBruteForceProtected(op.getBruteForceProtected());
        if (op.getSslRequired() != null) realm.setSslRequired(op.getSslRequired());
        if (op.getAccessTokenLifespan() != null) realm.setAccessTokenLifespan(op.getAccessTokenLifespan());
        if (op.getSsoSessionIdleTimeout() != null) realm.setSsoSessionIdleTimeout(op.getSsoSessionIdleTimeout());
        if (op.getSsoSessionMaxLifespan() != null) realm.setSsoSessionMaxLifespan(op.getSsoSessionMaxLifespan());
        if (op.getPasswordPolicy() != null) realm.setPasswordPolicy(op.getPasswordPolicy());
        if (op.getLoginTheme() != null) realm.setLoginTheme(op.getLoginTheme());
        if (op.getAccountTheme() != null) realm.setAccountTheme(op.getAccountTheme());
        if (op.getAdminTheme() != null) realm.setAdminTheme(op.getAdminTheme());
        if (op.getEmailTheme() != null) realm.setEmailTheme(op.getEmailTheme());
        if (op.getInternationalizationEnabled() != null) realm.setInternationalizationEnabled(op.getInternationalizationEnabled());
        if (op.getSupportedLocales() != null) realm.setSupportedLocales(new HashSet<>(op.getSupportedLocales()));
        if (op.getDefaultLocale() != null) realm.setDefaultLocale(op.getDefaultLocale());

        realmResource.update(realm);
        log.info("Realm '{}' updated successfully", op.getName());
    }

    public void deleteRealm(String realmName) {
        log.info("Deleting realm: {}", realmName);
        keycloak.realm(realmName).remove();
        log.info("Realm '{}' deleted successfully", realmName);
    }

    // ==================== Client Operations ====================

    public void createClient(CreateClientOperation op) {
        log.info("Creating client '{}' in realm '{}'", op.getClientId(), op.getRealm());

        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(op.getClientId());
        client.setEnabled(op.getEnabled());
        client.setProtocol(op.getProtocol());

        if (op.getName() != null) client.setName(op.getName());
        if (op.getClientDescription() != null) client.setDescription(op.getClientDescription());
        if (op.getPublicClient() != null) client.setPublicClient(op.getPublicClient());
        if (op.getBearerOnly() != null) client.setBearerOnly(op.getBearerOnly());
        if (op.getConsentRequired() != null) client.setConsentRequired(op.getConsentRequired());
        if (op.getStandardFlowEnabled() != null) client.setStandardFlowEnabled(op.getStandardFlowEnabled());
        if (op.getImplicitFlowEnabled() != null) client.setImplicitFlowEnabled(op.getImplicitFlowEnabled());
        if (op.getDirectAccessGrantsEnabled() != null) client.setDirectAccessGrantsEnabled(op.getDirectAccessGrantsEnabled());
        if (op.getServiceAccountsEnabled() != null) client.setServiceAccountsEnabled(op.getServiceAccountsEnabled());
        if (op.getAuthorizationServicesEnabled() != null) client.setAuthorizationServicesEnabled(op.getAuthorizationServicesEnabled());
        if (op.getRootUrl() != null) client.setRootUrl(op.getRootUrl());
        if (op.getBaseUrl() != null) client.setBaseUrl(op.getBaseUrl());
        if (op.getAdminUrl() != null) client.setAdminUrl(op.getAdminUrl());
        if (op.getRedirectUris() != null) client.setRedirectUris(op.getRedirectUris());
        if (op.getWebOrigins() != null) client.setWebOrigins(op.getWebOrigins());
        if (op.getSecret() != null) client.setSecret(op.getSecret());
        if (op.getClientAuthenticatorType() != null) client.setClientAuthenticatorType(op.getClientAuthenticatorType());
        if (op.getDefaultClientScopes() != null) client.setDefaultClientScopes(op.getDefaultClientScopes());
        if (op.getOptionalClientScopes() != null) client.setOptionalClientScopes(op.getOptionalClientScopes());
        if (op.getFrontchannelLogout() != null) client.setFrontchannelLogout(op.getFrontchannelLogout());
        if (op.getFullScopeAllowed() != null) client.setFullScopeAllowed(op.getFullScopeAllowed());

        if (op.getAttributes() != null) {
            Map<String, String> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    CreateClientOperation.AttributeEntry::getValue
                ));
            client.setAttributes(attrs);
        }

        Response response = keycloak.realm(op.getRealm()).clients().create(client);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create client: " + response.getStatusInfo().getReasonPhrase());
        }
        response.close();
        log.info("Client '{}' created successfully", op.getClientId());
    }

    public void updateClient(UpdateClientOperation op) {
        log.info("Updating client '{}' in realm '{}'", op.getClientId(), op.getRealm());

        ClientResource clientResource = getClientByClientId(op.getRealm(), op.getClientId());
        ClientRepresentation client = clientResource.toRepresentation();

        if (op.getName() != null) client.setName(op.getName());
        if (op.getClientDescription() != null) client.setDescription(op.getClientDescription());
        if (op.getEnabled() != null) client.setEnabled(op.getEnabled());
        if (op.getPublicClient() != null) client.setPublicClient(op.getPublicClient());
        if (op.getBearerOnly() != null) client.setBearerOnly(op.getBearerOnly());
        if (op.getConsentRequired() != null) client.setConsentRequired(op.getConsentRequired());
        if (op.getStandardFlowEnabled() != null) client.setStandardFlowEnabled(op.getStandardFlowEnabled());
        if (op.getImplicitFlowEnabled() != null) client.setImplicitFlowEnabled(op.getImplicitFlowEnabled());
        if (op.getDirectAccessGrantsEnabled() != null) client.setDirectAccessGrantsEnabled(op.getDirectAccessGrantsEnabled());
        if (op.getServiceAccountsEnabled() != null) client.setServiceAccountsEnabled(op.getServiceAccountsEnabled());
        if (op.getRootUrl() != null) client.setRootUrl(op.getRootUrl());
        if (op.getBaseUrl() != null) client.setBaseUrl(op.getBaseUrl());
        if (op.getAdminUrl() != null) client.setAdminUrl(op.getAdminUrl());
        if (op.getRedirectUris() != null) client.setRedirectUris(op.getRedirectUris());
        if (op.getWebOrigins() != null) client.setWebOrigins(op.getWebOrigins());
        if (op.getSecret() != null) client.setSecret(op.getSecret());
        if (op.getFrontchannelLogout() != null) client.setFrontchannelLogout(op.getFrontchannelLogout());
        if (op.getFullScopeAllowed() != null) client.setFullScopeAllowed(op.getFullScopeAllowed());

        clientResource.update(client);
        log.info("Client '{}' updated successfully", op.getClientId());
    }

    public void deleteClient(String realm, String clientId) {
        log.info("Deleting client '{}' from realm '{}'", clientId, realm);
        ClientResource clientResource = getClientByClientId(realm, clientId);
        clientResource.remove();
        log.info("Client '{}' deleted successfully", clientId);
    }

    // ==================== User Operations ====================

    public void createUser(CreateUserOperation op) {
        log.info("Creating user '{}' in realm '{}'", op.getUsername(), op.getRealm());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(op.getUsername());
        user.setEnabled(op.getEnabled());

        if (op.getEmail() != null) user.setEmail(op.getEmail());
        if (op.getFirstName() != null) user.setFirstName(op.getFirstName());
        if (op.getLastName() != null) user.setLastName(op.getLastName());
        if (op.getEmailVerified() != null) user.setEmailVerified(op.getEmailVerified());
        if (op.getGroups() != null) user.setGroups(op.getGroups());
        if (op.getRealmRoles() != null) user.setRealmRoles(op.getRealmRoles());
        if (op.getRequiredActions() != null) user.setRequiredActions(op.getRequiredActions());

        if (op.getAttributes() != null) {
            Map<String, List<String>> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateUserOperation.UserAttribute::getKey,
                    CreateUserOperation.UserAttribute::getValues
                ));
            user.setAttributes(attrs);
        }

        Response response = keycloak.realm(op.getRealm()).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }
        String userId = extractIdFromLocation(response);
        response.close();

        // Set password if provided
        if (op.getPassword() != null) {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(op.getPassword());
            credential.setTemporary(op.getTemporaryPassword());
            keycloak.realm(op.getRealm()).users().get(userId).resetPassword(credential);
        }

        // Assign client roles if provided
        if (op.getClientRoles() != null) {
            UserResource userResource = keycloak.realm(op.getRealm()).users().get(userId);
            for (var clientRoleMapping : op.getClientRoles()) {
                ClientResource clientResource = getClientByClientId(op.getRealm(), clientRoleMapping.getClientId());
                String clientUuid = clientResource.toRepresentation().getId();
                List<RoleRepresentation> roles = clientRoleMapping.getRoles().stream()
                    .map(roleName -> clientResource.roles().get(roleName).toRepresentation())
                    .toList();
                userResource.roles().clientLevel(clientUuid).add(roles);
            }
        }

        log.info("User '{}' created successfully", op.getUsername());
    }

    public void updateUser(UpdateUserOperation op) {
        log.info("Updating user '{}' in realm '{}'", op.getUsername(), op.getRealm());

        UserResource userResource = getUserByUsername(op.getRealm(), op.getUsername());
        UserRepresentation user = userResource.toRepresentation();

        if (op.getEmail() != null) user.setEmail(op.getEmail());
        if (op.getFirstName() != null) user.setFirstName(op.getFirstName());
        if (op.getLastName() != null) user.setLastName(op.getLastName());
        if (op.getEnabled() != null) user.setEnabled(op.getEnabled());
        if (op.getEmailVerified() != null) user.setEmailVerified(op.getEmailVerified());
        if (op.getRequiredActions() != null) user.setRequiredActions(op.getRequiredActions());

        if (op.getAttributes() != null) {
            Map<String, List<String>> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateUserOperation.UserAttribute::getKey,
                    CreateUserOperation.UserAttribute::getValues
                ));
            user.setAttributes(attrs);
        }

        userResource.update(user);

        // Handle realm role changes
        if (op.getAddRealmRoles() != null) {
            List<RoleRepresentation> rolesToAdd = op.getAddRealmRoles().stream()
                .map(name -> keycloak.realm(op.getRealm()).roles().get(name).toRepresentation())
                .toList();
            userResource.roles().realmLevel().add(rolesToAdd);
        }
        if (op.getRemoveRealmRoles() != null) {
            List<RoleRepresentation> rolesToRemove = op.getRemoveRealmRoles().stream()
                .map(name -> keycloak.realm(op.getRealm()).roles().get(name).toRepresentation())
                .toList();
            userResource.roles().realmLevel().remove(rolesToRemove);
        }

        // Handle group changes
        if (op.getAddGroups() != null) {
            for (String groupName : op.getAddGroups()) {
                GroupRepresentation group = findGroupByName(op.getRealm(), groupName);
                userResource.joinGroup(group.getId());
            }
        }
        if (op.getRemoveGroups() != null) {
            for (String groupName : op.getRemoveGroups()) {
                GroupRepresentation group = findGroupByName(op.getRealm(), groupName);
                userResource.leaveGroup(group.getId());
            }
        }

        log.info("User '{}' updated successfully", op.getUsername());
    }

    public void deleteUser(String realm, String username) {
        log.info("Deleting user '{}' from realm '{}'", username, realm);
        UserResource userResource = getUserByUsername(realm, username);
        userResource.remove();
        log.info("User '{}' deleted successfully", username);
    }

    // ==================== Role Operations ====================

    public void createRealmRole(CreateRealmRoleOperation op) {
        log.info("Creating realm role '{}' in realm '{}'", op.getName(), op.getRealm());

        RoleRepresentation role = new RoleRepresentation();
        role.setName(op.getName());
        role.setComposite(op.getComposite());

        if (op.getRoleDescription() != null) role.setDescription(op.getRoleDescription());

        if (op.getAttributes() != null) {
            Map<String, List<String>> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    e -> List.of(e.getValue())
                ));
            role.setAttributes(attrs);
        }

        keycloak.realm(op.getRealm()).roles().create(role);

        // Add composite roles if specified
        if (op.getCompositeRoles() != null && !op.getCompositeRoles().isEmpty()) {
            List<RoleRepresentation> composites = op.getCompositeRoles().stream()
                .map(name -> keycloak.realm(op.getRealm()).roles().get(name).toRepresentation())
                .toList();
            keycloak.realm(op.getRealm()).roles().get(op.getName()).addComposites(composites);
        }

        log.info("Realm role '{}' created successfully", op.getName());
    }

    public void createClientRole(CreateClientRoleOperation op) {
        log.info("Creating client role '{}' for client '{}' in realm '{}'", op.getName(), op.getClientId(), op.getRealm());

        RoleRepresentation role = new RoleRepresentation();
        role.setName(op.getName());
        role.setComposite(op.getComposite());

        if (op.getRoleDescription() != null) role.setDescription(op.getRoleDescription());

        if (op.getAttributes() != null) {
            Map<String, List<String>> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    e -> List.of(e.getValue())
                ));
            role.setAttributes(attrs);
        }

        ClientResource clientResource = getClientByClientId(op.getRealm(), op.getClientId());
        clientResource.roles().create(role);

        log.info("Client role '{}' created successfully", op.getName());
    }

    public void deleteRealmRole(String realm, String roleName) {
        log.info("Deleting realm role '{}' from realm '{}'", roleName, realm);
        keycloak.realm(realm).roles().deleteRole(roleName);
        log.info("Realm role '{}' deleted successfully", roleName);
    }

    public void deleteClientRole(String realm, String clientId, String roleName) {
        log.info("Deleting client role '{}' from client '{}' in realm '{}'", roleName, clientId, realm);
        ClientResource clientResource = getClientByClientId(realm, clientId);
        clientResource.roles().deleteRole(roleName);
        log.info("Client role '{}' deleted successfully", roleName);
    }

    // ==================== Group Operations ====================

    public void createGroup(CreateGroupOperation op) {
        log.info("Creating group '{}' in realm '{}'", op.getName(), op.getRealm());

        GroupRepresentation group = new GroupRepresentation();
        group.setName(op.getName());
        if (op.getPath() != null) group.setPath(op.getPath());

        if (op.getAttributes() != null) {
            Map<String, List<String>> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateUserOperation.UserAttribute::getKey,
                    CreateUserOperation.UserAttribute::getValues
                ));
            group.setAttributes(attrs);
        }

        Response response;
        if (op.getParentGroup() != null) {
            GroupRepresentation parent = findGroupByName(op.getRealm(), op.getParentGroup());
            response = keycloak.realm(op.getRealm()).groups().group(parent.getId()).subGroup(group);
        } else {
            response = keycloak.realm(op.getRealm()).groups().add(group);
        }

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create group: " + response.getStatusInfo().getReasonPhrase());
        }
        String groupId = extractIdFromLocation(response);
        response.close();

        // Assign realm roles if provided
        if (op.getRealmRoles() != null) {
            GroupResource groupResource = keycloak.realm(op.getRealm()).groups().group(groupId);
            List<RoleRepresentation> roles = op.getRealmRoles().stream()
                .map(name -> keycloak.realm(op.getRealm()).roles().get(name).toRepresentation())
                .toList();
            groupResource.roles().realmLevel().add(roles);
        }

        // Assign client roles if provided
        if (op.getClientRoles() != null) {
            GroupResource groupResource = keycloak.realm(op.getRealm()).groups().group(groupId);
            for (var clientRoleMapping : op.getClientRoles()) {
                ClientResource clientResource = getClientByClientId(op.getRealm(), clientRoleMapping.getClientId());
                String clientUuid = clientResource.toRepresentation().getId();
                List<RoleRepresentation> roles = clientRoleMapping.getRoles().stream()
                    .map(roleName -> clientResource.roles().get(roleName).toRepresentation())
                    .toList();
                groupResource.roles().clientLevel(clientUuid).add(roles);
            }
        }

        log.info("Group '{}' created successfully", op.getName());
    }

    public void deleteGroup(String realm, String groupName) {
        log.info("Deleting group '{}' from realm '{}'", groupName, realm);
        GroupRepresentation group = findGroupByName(realm, groupName);
        keycloak.realm(realm).groups().group(group.getId()).remove();
        log.info("Group '{}' deleted successfully", groupName);
    }

    // ==================== Client Scope Operations ====================

    public void createClientScope(CreateClientScopeOperation op) {
        log.info("Creating client scope '{}' in realm '{}'", op.getName(), op.getRealm());

        ClientScopeRepresentation scope = new ClientScopeRepresentation();
        scope.setName(op.getName());
        scope.setProtocol(op.getProtocol());

        if (op.getScopeDescription() != null) scope.setDescription(op.getScopeDescription());

        if (op.getAttributes() != null) {
            Map<String, String> attrs = op.getAttributes().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    CreateClientOperation.AttributeEntry::getValue
                ));
            if (op.getIncludeInTokenScope() != null) {
                attrs.put("include.in.token.scope", op.getIncludeInTokenScope().toString());
            }
            scope.setAttributes(attrs);
        }

        if (op.getProtocolMappers() != null) {
            List<ProtocolMapperRepresentation> mappers = op.getProtocolMappers().stream()
                .map(this::toProtocolMapperRepresentation)
                .toList();
            scope.setProtocolMappers(mappers);
        }

        Response response = keycloak.realm(op.getRealm()).clientScopes().create(scope);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create client scope: " + response.getStatusInfo().getReasonPhrase());
        }
        response.close();

        log.info("Client scope '{}' created successfully", op.getName());
    }

    public void deleteClientScope(String realm, String scopeName) {
        log.info("Deleting client scope '{}' from realm '{}'", scopeName, realm);
        ClientScopeRepresentation scope = findClientScopeByName(realm, scopeName);
        keycloak.realm(realm).clientScopes().get(scope.getId()).remove();
        log.info("Client scope '{}' deleted successfully", scopeName);
    }

    // ==================== Identity Provider Operations ====================

    public void createIdentityProvider(CreateIdentityProviderOperation op) {
        log.info("Creating identity provider '{}' in realm '{}'", op.getAlias(), op.getRealm());

        IdentityProviderRepresentation idp = new IdentityProviderRepresentation();
        idp.setAlias(op.getAlias());
        idp.setProviderId(op.getProviderId());
        idp.setEnabled(op.getEnabled());

        if (op.getDisplayName() != null) idp.setDisplayName(op.getDisplayName());
        if (op.getTrustEmail() != null) idp.setTrustEmail(op.getTrustEmail());
        if (op.getStoreToken() != null) idp.setStoreToken(op.getStoreToken());
        if (op.getAddReadTokenRoleOnCreate() != null) idp.setAddReadTokenRoleOnCreate(op.getAddReadTokenRoleOnCreate());
        if (op.getLinkOnly() != null) idp.setLinkOnly(op.getLinkOnly());
        if (op.getFirstBrokerLoginFlowAlias() != null) idp.setFirstBrokerLoginFlowAlias(op.getFirstBrokerLoginFlowAlias());
        if (op.getPostBrokerLoginFlowAlias() != null) idp.setPostBrokerLoginFlowAlias(op.getPostBrokerLoginFlowAlias());

        if (op.getConfig() != null) {
            Map<String, String> config = op.getConfig().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    CreateClientOperation.AttributeEntry::getValue
                ));
            idp.setConfig(config);
        }

        Response response = keycloak.realm(op.getRealm()).identityProviders().create(idp);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create identity provider: " + response.getStatusInfo().getReasonPhrase());
        }
        response.close();

        log.info("Identity provider '{}' created successfully", op.getAlias());
    }

    public void deleteIdentityProvider(String realm, String alias) {
        log.info("Deleting identity provider '{}' from realm '{}'", alias, realm);
        keycloak.realm(realm).identityProviders().get(alias).remove();
        log.info("Identity provider '{}' deleted successfully", alias);
    }

    // ==================== Protocol Mapper Operations ====================

    public void createProtocolMapper(CreateProtocolMapperOperation op) {
        log.info("Creating protocol mapper '{}' in realm '{}'", op.getName(), op.getRealm());

        ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
        mapper.setName(op.getName());
        mapper.setProtocol(op.getProtocol());
        mapper.setProtocolMapper(op.getProtocolMapper());

        if (op.getConfig() != null) {
            Map<String, String> config = op.getConfig().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    CreateClientOperation.AttributeEntry::getValue
                ));
            mapper.setConfig(config);
        }

        if (op.getClientId() != null) {
            ClientResource clientResource = getClientByClientId(op.getRealm(), op.getClientId());
            Response response = clientResource.getProtocolMappers().createMapper(mapper);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create protocol mapper: " + response.getStatusInfo().getReasonPhrase());
            }
            response.close();
        } else if (op.getClientScopeName() != null) {
            ClientScopeRepresentation scope = findClientScopeByName(op.getRealm(), op.getClientScopeName());
            Response response = keycloak.realm(op.getRealm()).clientScopes().get(scope.getId())
                .getProtocolMappers().createMapper(mapper);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create protocol mapper: " + response.getStatusInfo().getReasonPhrase());
            }
            response.close();
        } else {
            throw new IllegalArgumentException("Either clientId or clientScopeName must be specified");
        }

        log.info("Protocol mapper '{}' created successfully", op.getName());
    }

    public void deleteProtocolMapper(DeleteProtocolMapperOperation op) {
        log.info("Deleting protocol mapper '{}' from realm '{}'", op.getName(), op.getRealm());

        if (op.getClientId() != null) {
            ClientResource clientResource = getClientByClientId(op.getRealm(), op.getClientId());
            ProtocolMapperRepresentation mapper = clientResource.getProtocolMappers().getMappers().stream()
                .filter(m -> m.getName().equals(op.getName()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Protocol mapper not found: " + op.getName()));
            clientResource.getProtocolMappers().delete(mapper.getId());
        } else if (op.getClientScopeName() != null) {
            ClientScopeRepresentation scope = findClientScopeByName(op.getRealm(), op.getClientScopeName());
            ProtocolMapperRepresentation mapper = keycloak.realm(op.getRealm()).clientScopes().get(scope.getId())
                .getProtocolMappers().getMappers().stream()
                .filter(m -> m.getName().equals(op.getName()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Protocol mapper not found: " + op.getName()));
            keycloak.realm(op.getRealm()).clientScopes().get(scope.getId()).getProtocolMappers().delete(mapper.getId());
        } else {
            throw new IllegalArgumentException("Either clientId or clientScopeName must be specified");
        }

        log.info("Protocol mapper '{}' deleted successfully", op.getName());
    }

    // ==================== Authentication Flow Operations ====================

    public void createAuthenticationFlow(CreateAuthenticationFlowOperation op) {
        log.info("Creating authentication flow '{}' in realm '{}'", op.getAlias(), op.getRealm());

        AuthenticationFlowRepresentation flow = new AuthenticationFlowRepresentation();
        flow.setAlias(op.getAlias());
        flow.setProviderId(op.getProviderId());
        flow.setTopLevel(op.getTopLevel());
        flow.setBuiltIn(op.getBuiltIn());

        if (op.getFlowDescription() != null) flow.setDescription(op.getFlowDescription());

        Response response = keycloak.realm(op.getRealm()).flows().createFlow(flow);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create authentication flow: " + response.getStatusInfo().getReasonPhrase());
        }
        response.close();

        // Add executions if provided
        if (op.getExecutions() != null) {
            for (var execution : op.getExecutions()) {
                Map<String, Object> execData = new HashMap<>();
                execData.put("provider", execution.getAuthenticator());

                keycloak.realm(op.getRealm()).flows().addExecution(op.getAlias(), execData);

                // Update the requirement
                AuthenticationFlowRepresentation createdFlow = keycloak.realm(op.getRealm()).flows().getFlows().stream()
                    .filter(f -> f.getAlias().equals(op.getAlias()))
                    .findFirst()
                    .orElseThrow();

                // Note: Full execution configuration requires additional API calls
                // This is a simplified implementation
            }
        }

        log.info("Authentication flow '{}' created successfully", op.getAlias());
    }

    public void deleteAuthenticationFlow(String realm, String alias) {
        log.info("Deleting authentication flow '{}' from realm '{}'", alias, realm);

        AuthenticationFlowRepresentation flow = keycloak.realm(realm).flows().getFlows().stream()
            .filter(f -> f.getAlias().equals(alias))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Authentication flow not found: " + alias));

        keycloak.realm(realm).flows().deleteFlow(flow.getId());
        log.info("Authentication flow '{}' deleted successfully", alias);
    }

    // ==================== Realm Attributes for Tracking ====================

    public Map<String, String> getRealmAttributes(String realmName) {
        RealmRepresentation realm = keycloak.realm(realmName).toRepresentation();
        return realm.getAttributes() != null ? realm.getAttributes() : new HashMap<>();
    }

    public void setRealmAttribute(String realmName, String key, String value) {
        RealmResource realmResource = keycloak.realm(realmName);
        RealmRepresentation realm = realmResource.toRepresentation();

        Map<String, String> attributes = realm.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
        realm.setAttributes(attributes);

        realmResource.update(realm);
    }

    public boolean realmExists(String realmName) {
        try {
            keycloak.realm(realmName).toRepresentation();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    // ==================== Helper Methods ====================

    private ClientResource getClientByClientId(String realm, String clientId) {
        List<ClientRepresentation> clients = keycloak.realm(realm).clients()
            .findByClientId(clientId);
        if (clients.isEmpty()) {
            throw new NotFoundException("Client not found: " + clientId);
        }
        return keycloak.realm(realm).clients().get(clients.get(0).getId());
    }

    private UserResource getUserByUsername(String realm, String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users()
            .searchByUsername(username, true);
        if (users.isEmpty()) {
            throw new NotFoundException("User not found: " + username);
        }
        return keycloak.realm(realm).users().get(users.get(0).getId());
    }

    private GroupRepresentation findGroupByName(String realm, String groupName) {
        return keycloak.realm(realm).groups().groups().stream()
            .filter(g -> g.getName().equals(groupName))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Group not found: " + groupName));
    }

    private ClientScopeRepresentation findClientScopeByName(String realm, String scopeName) {
        return keycloak.realm(realm).clientScopes().findAll().stream()
            .filter(s -> s.getName().equals(scopeName))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Client scope not found: " + scopeName));
    }

    private ProtocolMapperRepresentation toProtocolMapperRepresentation(CreateClientScopeOperation.ProtocolMapperConfig config) {
        ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
        mapper.setName(config.getName());
        mapper.setProtocol(config.getProtocol());
        mapper.setProtocolMapper(config.getProtocolMapper());

        if (config.getConfig() != null) {
            Map<String, String> cfg = config.getConfig().stream()
                .collect(Collectors.toMap(
                    CreateClientOperation.AttributeEntry::getKey,
                    CreateClientOperation.AttributeEntry::getValue
                ));
            mapper.setConfig(cfg);
        }

        return mapper;
    }

    private String extractIdFromLocation(Response response) {
        String location = response.getHeaderString("Location");
        if (location != null) {
            return location.substring(location.lastIndexOf('/') + 1);
        }
        return null;
    }
}
