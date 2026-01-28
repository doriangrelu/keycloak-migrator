package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteRole")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteRoleOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private String clientId; // If null, it's a realm role

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        if (clientId != null) {
            client.deleteClientRole(realm, clientId, name);
        } else {
            client.deleteRealmRole(realm, name);
        }
    }

    @Override
    public String getDescription() {
        if (clientId != null) {
            return "Delete client role '" + name + "' from client '" + clientId + "' in realm '" + realm + "'";
        }
        return "Delete realm role '" + name + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
