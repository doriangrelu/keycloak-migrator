package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteProtocolMapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteProtocolMapperOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute
    private String clientId;

    @XmlAttribute
    private String clientScopeName;

    @XmlAttribute(required = true)
    private String name;

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientScopeName() { return clientScopeName; }
    public void setClientScopeName(String clientScopeName) { this.clientScopeName = clientScopeName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteProtocolMapper(this);
    }

    @Override
    public String getDescription() {
        if (clientId != null) {
            return "Delete protocol mapper '" + name + "' from client '" + clientId + "' in realm '" + realm + "'";
        }
        return "Delete protocol mapper '" + name + "' from client scope '" + clientScopeName + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
