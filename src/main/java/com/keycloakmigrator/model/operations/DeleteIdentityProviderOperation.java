package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteIdentityProvider")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteIdentityProviderOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String alias;

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteIdentityProvider(realm, alias);
    }

    @Override
    public String getDescription() {
        return "Delete identity provider '" + alias + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
