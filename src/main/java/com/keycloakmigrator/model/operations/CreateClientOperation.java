package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "createClient")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateClientOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String clientId;

    @XmlElement
    private String name;

    @XmlElement
    private String clientDescription;

    @XmlElement
    private Boolean enabled = true;

    @XmlElement
    private String protocol = "openid-connect";

    @XmlElement
    private Boolean publicClient = false;

    @XmlElement
    private Boolean bearerOnly = false;

    @XmlElement
    private Boolean consentRequired = false;

    @XmlElement
    private Boolean standardFlowEnabled = true;

    @XmlElement
    private Boolean implicitFlowEnabled = false;

    @XmlElement
    private Boolean directAccessGrantsEnabled = true;

    @XmlElement
    private Boolean serviceAccountsEnabled = false;

    @XmlElement
    private Boolean authorizationServicesEnabled = false;

    @XmlElement
    private String rootUrl;

    @XmlElement
    private String baseUrl;

    @XmlElement
    private String adminUrl;

    @XmlElementWrapper(name = "redirectUris")
    @XmlElement(name = "uri")
    private List<String> redirectUris;

    @XmlElementWrapper(name = "webOrigins")
    @XmlElement(name = "origin")
    private List<String> webOrigins;

    @XmlElement
    private String secret;

    @XmlElement
    private Integer accessTokenLifespan;

    @XmlElement
    private String clientAuthenticatorType;

    @XmlElementWrapper(name = "defaultClientScopes")
    @XmlElement(name = "scope")
    private List<String> defaultClientScopes;

    @XmlElementWrapper(name = "optionalClientScopes")
    @XmlElement(name = "scope")
    private List<String> optionalClientScopes;

    @XmlElement
    private Boolean frontchannelLogout = false;

    @XmlElement
    private Boolean fullScopeAllowed = true;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<AttributeEntry> attributes;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClientDescription() { return clientDescription; }
    public void setClientDescription(String clientDescription) { this.clientDescription = clientDescription; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public Boolean getPublicClient() { return publicClient; }
    public void setPublicClient(Boolean publicClient) { this.publicClient = publicClient; }

    public Boolean getBearerOnly() { return bearerOnly; }
    public void setBearerOnly(Boolean bearerOnly) { this.bearerOnly = bearerOnly; }

    public Boolean getConsentRequired() { return consentRequired; }
    public void setConsentRequired(Boolean consentRequired) { this.consentRequired = consentRequired; }

    public Boolean getStandardFlowEnabled() { return standardFlowEnabled; }
    public void setStandardFlowEnabled(Boolean standardFlowEnabled) { this.standardFlowEnabled = standardFlowEnabled; }

    public Boolean getImplicitFlowEnabled() { return implicitFlowEnabled; }
    public void setImplicitFlowEnabled(Boolean implicitFlowEnabled) { this.implicitFlowEnabled = implicitFlowEnabled; }

    public Boolean getDirectAccessGrantsEnabled() { return directAccessGrantsEnabled; }
    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) { this.directAccessGrantsEnabled = directAccessGrantsEnabled; }

    public Boolean getServiceAccountsEnabled() { return serviceAccountsEnabled; }
    public void setServiceAccountsEnabled(Boolean serviceAccountsEnabled) { this.serviceAccountsEnabled = serviceAccountsEnabled; }

    public Boolean getAuthorizationServicesEnabled() { return authorizationServicesEnabled; }
    public void setAuthorizationServicesEnabled(Boolean authorizationServicesEnabled) { this.authorizationServicesEnabled = authorizationServicesEnabled; }

    public String getRootUrl() { return rootUrl; }
    public void setRootUrl(String rootUrl) { this.rootUrl = rootUrl; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getAdminUrl() { return adminUrl; }
    public void setAdminUrl(String adminUrl) { this.adminUrl = adminUrl; }

    public List<String> getRedirectUris() { return redirectUris; }
    public void setRedirectUris(List<String> redirectUris) { this.redirectUris = redirectUris; }

    public List<String> getWebOrigins() { return webOrigins; }
    public void setWebOrigins(List<String> webOrigins) { this.webOrigins = webOrigins; }

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public Integer getAccessTokenLifespan() { return accessTokenLifespan; }
    public void setAccessTokenLifespan(Integer accessTokenLifespan) { this.accessTokenLifespan = accessTokenLifespan; }

    public String getClientAuthenticatorType() { return clientAuthenticatorType; }
    public void setClientAuthenticatorType(String clientAuthenticatorType) { this.clientAuthenticatorType = clientAuthenticatorType; }

    public List<String> getDefaultClientScopes() { return defaultClientScopes; }
    public void setDefaultClientScopes(List<String> defaultClientScopes) { this.defaultClientScopes = defaultClientScopes; }

    public List<String> getOptionalClientScopes() { return optionalClientScopes; }
    public void setOptionalClientScopes(List<String> optionalClientScopes) { this.optionalClientScopes = optionalClientScopes; }

    public Boolean getFrontchannelLogout() { return frontchannelLogout; }
    public void setFrontchannelLogout(Boolean frontchannelLogout) { this.frontchannelLogout = frontchannelLogout; }

    public Boolean getFullScopeAllowed() { return fullScopeAllowed; }
    public void setFullScopeAllowed(Boolean fullScopeAllowed) { this.fullScopeAllowed = fullScopeAllowed; }

    public List<AttributeEntry> getAttributes() { return attributes; }
    public void setAttributes(List<AttributeEntry> attributes) { this.attributes = attributes; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createClient(this);
    }

    @Override
    public String getDescription() {
        return "Create client '" + clientId + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AttributeEntry {
        @XmlAttribute(required = true)
        private String key;

        @XmlValue
        private String value;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
