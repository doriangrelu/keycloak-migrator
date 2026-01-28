package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteGroupOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String name;

    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteGroup(realm, name);
    }

    @Override
    public String getDescription() {
        return "Delete group '" + name + "' from realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
