package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteAuthenticationFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteAuthenticationFlowOperation implements Operation {

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
        client.deleteAuthenticationFlow(realm, alias);
    }

    @Override
    public String getDescription() {
        return "Delete authentication flow '" + alias + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
