package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createIdentityProvider")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateIdentityProviderOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String alias;

    @XmlAttribute(required = true)
    private String providerId;

    @XmlElement
    private String displayName;

    @XmlElement
    private Boolean enabled = true;

    @XmlElement
    private Boolean trustEmail = false;

    @XmlElement
    private Boolean storeToken = false;

    @XmlElement
    private Boolean addReadTokenRoleOnCreate = false;

    @XmlElement
    private Boolean linkOnly = false;

    @XmlElement
    private String firstBrokerLoginFlowAlias;

    @XmlElement
    private String postBrokerLoginFlowAlias;

    @XmlElementWrapper(name = "config")
    @XmlElement(name = "entry")
    private List<CreateClientOperation.AttributeEntry> config;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Boolean getTrustEmail() { return trustEmail; }
    public void setTrustEmail(Boolean trustEmail) { this.trustEmail = trustEmail; }

    public Boolean getStoreToken() { return storeToken; }
    public void setStoreToken(Boolean storeToken) { this.storeToken = storeToken; }

    public Boolean getAddReadTokenRoleOnCreate() { return addReadTokenRoleOnCreate; }
    public void setAddReadTokenRoleOnCreate(Boolean addReadTokenRoleOnCreate) { this.addReadTokenRoleOnCreate = addReadTokenRoleOnCreate; }

    public Boolean getLinkOnly() { return linkOnly; }
    public void setLinkOnly(Boolean linkOnly) { this.linkOnly = linkOnly; }

    public String getFirstBrokerLoginFlowAlias() { return firstBrokerLoginFlowAlias; }
    public void setFirstBrokerLoginFlowAlias(String firstBrokerLoginFlowAlias) { this.firstBrokerLoginFlowAlias = firstBrokerLoginFlowAlias; }

    public String getPostBrokerLoginFlowAlias() { return postBrokerLoginFlowAlias; }
    public void setPostBrokerLoginFlowAlias(String postBrokerLoginFlowAlias) { this.postBrokerLoginFlowAlias = postBrokerLoginFlowAlias; }

    public List<CreateClientOperation.AttributeEntry> getConfig() { return config; }
    public void setConfig(List<CreateClientOperation.AttributeEntry> config) { this.config = config; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createIdentityProvider(this);
    }

    @Override
    public String getDescription() {
        return "Create identity provider '" + alias + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
