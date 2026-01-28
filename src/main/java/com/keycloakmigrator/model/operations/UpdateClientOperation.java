package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "updateClient")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateClientOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String clientId;

    @XmlElement
    private String name;

    @XmlElement
    private String clientDescription;

    @XmlElement
    private Boolean enabled;

    @XmlElement
    private Boolean publicClient;

    @XmlElement
    private Boolean bearerOnly;

    @XmlElement
    private Boolean consentRequired;

    @XmlElement
    private Boolean standardFlowEnabled;

    @XmlElement
    private Boolean implicitFlowEnabled;

    @XmlElement
    private Boolean directAccessGrantsEnabled;

    @XmlElement
    private Boolean serviceAccountsEnabled;

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
    private Boolean frontchannelLogout;

    @XmlElement
    private Boolean fullScopeAllowed;

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

    public Boolean getFrontchannelLogout() { return frontchannelLogout; }
    public void setFrontchannelLogout(Boolean frontchannelLogout) { this.frontchannelLogout = frontchannelLogout; }

    public Boolean getFullScopeAllowed() { return fullScopeAllowed; }
    public void setFullScopeAllowed(Boolean fullScopeAllowed) { this.fullScopeAllowed = fullScopeAllowed; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.updateClient(this);
    }

    @Override
    public String getDescription() {
        return "Update client '" + clientId + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
