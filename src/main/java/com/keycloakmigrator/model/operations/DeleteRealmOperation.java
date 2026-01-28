package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "deleteRealm")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteRealmOperation implements Operation {

    @XmlAttribute(required = true)
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.deleteRealm(name);
    }

    @Override
    public String getDescription() {
        return "Delete realm: " + name;
    }

    @Override
    public String getTargetRealm() {
        return name;
    }
}
