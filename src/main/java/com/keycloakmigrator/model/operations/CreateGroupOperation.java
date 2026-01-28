package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateGroupOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String name;

    @XmlElement
    private String path;

    @XmlElement
    private String parentGroup;

    @XmlElementWrapper(name = "realmRoles")
    @XmlElement(name = "role")
    private List<String> realmRoles;

    @XmlElementWrapper(name = "clientRoles")
    @XmlElement(name = "client")
    private List<CreateUserOperation.ClientRoleMapping> clientRoles;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<CreateUserOperation.UserAttribute> attributes;

    @XmlElementWrapper(name = "subGroups")
    @XmlElement(name = "group")
    private List<SubGroup> subGroups;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getParentGroup() { return parentGroup; }
    public void setParentGroup(String parentGroup) { this.parentGroup = parentGroup; }

    public List<String> getRealmRoles() { return realmRoles; }
    public void setRealmRoles(List<String> realmRoles) { this.realmRoles = realmRoles; }

    public List<CreateUserOperation.ClientRoleMapping> getClientRoles() { return clientRoles; }
    public void setClientRoles(List<CreateUserOperation.ClientRoleMapping> clientRoles) { this.clientRoles = clientRoles; }

    public List<CreateUserOperation.UserAttribute> getAttributes() { return attributes; }
    public void setAttributes(List<CreateUserOperation.UserAttribute> attributes) { this.attributes = attributes; }

    public List<SubGroup> getSubGroups() { return subGroups; }
    public void setSubGroups(List<SubGroup> subGroups) { this.subGroups = subGroups; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createGroup(this);
    }

    @Override
    public String getDescription() {
        return "Create group '" + name + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SubGroup {
        @XmlAttribute(required = true)
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
