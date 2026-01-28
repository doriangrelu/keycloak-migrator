package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createRealmRole")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateRealmRoleOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String name;

    @XmlElement
    private String roleDescription;

    @XmlElement
    private Boolean composite = false;

    @XmlElementWrapper(name = "composites")
    @XmlElement(name = "role")
    private List<String> compositeRoles;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<CreateClientOperation.AttributeEntry> attributes;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoleDescription() { return roleDescription; }
    public void setRoleDescription(String roleDescription) { this.roleDescription = roleDescription; }

    public Boolean getComposite() { return composite; }
    public void setComposite(Boolean composite) { this.composite = composite; }

    public List<String> getCompositeRoles() { return compositeRoles; }
    public void setCompositeRoles(List<String> compositeRoles) { this.compositeRoles = compositeRoles; }

    public List<CreateClientOperation.AttributeEntry> getAttributes() { return attributes; }
    public void setAttributes(List<CreateClientOperation.AttributeEntry> attributes) { this.attributes = attributes; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createRealmRole(this);
    }

    @Override
    public String getDescription() {
        return "Create realm role '" + name + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
