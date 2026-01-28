package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteUserOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String username;

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteUser(realm, username);
    }

    @Override
    public String getDescription() {
        return "Delete user '" + username + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
