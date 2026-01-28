package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "createUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserOperation implements Operation {

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
    private Boolean enabled = true;

    @XmlElement
    private Boolean emailVerified = false;

    @XmlElement
    private String password;

    @XmlElement
    private Boolean temporaryPassword = false;

    @XmlElementWrapper(name = "realmRoles")
    @XmlElement(name = "role")
    private List<String> realmRoles;

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<String> groups;

    @XmlElementWrapper(name = "requiredActions")
    @XmlElement(name = "action")
    private List<String> requiredActions;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<UserAttribute> attributes;

    @XmlElementWrapper(name = "clientRoles")
    @XmlElement(name = "client")
    private List<ClientRoleMapping> clientRoles;

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getTemporaryPassword() { return temporaryPassword; }
    public void setTemporaryPassword(Boolean temporaryPassword) { this.temporaryPassword = temporaryPassword; }

    public List<String> getRealmRoles() { return realmRoles; }
    public void setRealmRoles(List<String> realmRoles) { this.realmRoles = realmRoles; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }

    public List<String> getRequiredActions() { return requiredActions; }
    public void setRequiredActions(List<String> requiredActions) { this.requiredActions = requiredActions; }

    public List<UserAttribute> getAttributes() { return attributes; }
    public void setAttributes(List<UserAttribute> attributes) { this.attributes = attributes; }

    public List<ClientRoleMapping> getClientRoles() { return clientRoles; }
    public void setClientRoles(List<ClientRoleMapping> clientRoles) { this.clientRoles = clientRoles; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createUser(this);
    }

    @Override
    public String getDescription() {
        return "Create user '" + username + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class UserAttribute {
        @XmlAttribute(required = true)
        private String key;

        @XmlElement(name = "value")
        private List<String> values;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public List<String> getValues() { return values; }
        public void setValues(List<String> values) { this.values = values; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ClientRoleMapping {
        @XmlAttribute(required = true)
        private String clientId;

        @XmlElement(name = "role")
        private List<String> roles;

        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }
}
