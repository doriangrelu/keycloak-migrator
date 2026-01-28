package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "updateUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateUserOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String username;

    @XmlElement
    private String email;

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private Boolean enabled;

    @XmlElement
    private Boolean emailVerified;

    @XmlElementWrapper(name = "addRealmRoles")
    @XmlElement(name = "role")
    private List<String> addRealmRoles;

    @XmlElementWrapper(name = "removeRealmRoles")
    @XmlElement(name = "role")
    private List<String> removeRealmRoles;

    @XmlElementWrapper(name = "addGroups")
    @XmlElement(name = "group")
    private List<String> addGroups;

    @XmlElementWrapper(name = "removeGroups")
    @XmlElement(name = "group")
    private List<String> removeGroups;

    @XmlElementWrapper(name = "requiredActions")
    @XmlElement(name = "action")
    private List<String> requiredActions;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<CreateUserOperation.UserAttribute> attributes;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public List<String> getAddRealmRoles() { return addRealmRoles; }
    public void setAddRealmRoles(List<String> addRealmRoles) { this.addRealmRoles = addRealmRoles; }

    public List<String> getRemoveRealmRoles() { return removeRealmRoles; }
    public void setRemoveRealmRoles(List<String> removeRealmRoles) { this.removeRealmRoles = removeRealmRoles; }

    public List<String> getAddGroups() { return addGroups; }
    public void setAddGroups(List<String> addGroups) { this.addGroups = addGroups; }

    public List<String> getRemoveGroups() { return removeGroups; }
    public void setRemoveGroups(List<String> removeGroups) { this.removeGroups = removeGroups; }

    public List<String> getRequiredActions() { return requiredActions; }
    public void setRequiredActions(List<String> requiredActions) { this.requiredActions = requiredActions; }

    public List<CreateUserOperation.UserAttribute> getAttributes() { return attributes; }
    public void setAttributes(List<CreateUserOperation.UserAttribute> attributes) { this.attributes = attributes; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.updateUser(this);
    }

    @Override
    public String getDescription() {
        return "Update user '" + username + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
