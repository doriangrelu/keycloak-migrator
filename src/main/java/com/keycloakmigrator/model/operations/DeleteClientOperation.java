package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteClient")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteClientOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String clientId;

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteClient(realm, clientId);
    }

    @Override
    public String getDescription() {
        return "Delete client '" + clientId + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
