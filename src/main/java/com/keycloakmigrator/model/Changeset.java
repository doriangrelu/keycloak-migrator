package com.keycloakmigrator.model;

import com.keycloakmigrator.model.operations.*;
import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single changeset containing one or more operations.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Changeset {

    @XmlAttribute(required = true)
    private int version;

    @XmlAttribute(required = true)
    private String author;

    @XmlAttribute
    private String context;

    @XmlAttribute
    private Boolean failOnError = true;

    @XmlElement
    private String comment;

    @XmlElements({
        @XmlElement(name = "createRealm", type = CreateRealmOperation.class),
        @XmlElement(name = "updateRealm", type = UpdateRealmOperation.class),
        @XmlElement(name = "deleteRealm", type = DeleteRealmOperation.class),
        @XmlElement(name = "createClient", type = CreateClientOperation.class),
        @XmlElement(name = "updateClient", type = UpdateClientOperation.class),
        @XmlElement(name = "deleteClient", type = DeleteClientOperation.class),
        @XmlElement(name = "createUser", type = CreateUserOperation.class),
        @XmlElement(name = "updateUser", type = UpdateUserOperation.class),
        @XmlElement(name = "deleteUser", type = DeleteUserOperation.class),
        @XmlElement(name = "createRealmRole", type = CreateRealmRoleOperation.class),
        @XmlElement(name = "createClientRole", type = CreateClientRoleOperation.class),
        @XmlElement(name = "deleteRole", type = DeleteRoleOperation.class),
        @XmlElement(name = "createGroup", type = CreateGroupOperation.class),
        @XmlElement(name = "deleteGroup", type = DeleteGroupOperation.class),
        @XmlElement(name = "createClientScope", type = CreateClientScopeOperation.class),
        @XmlElement(name = "deleteClientScope", type = DeleteClientScopeOperation.class),
        @XmlElement(name = "createIdentityProvider", type = CreateIdentityProviderOperation.class),
        @XmlElement(name = "deleteIdentityProvider", type = DeleteIdentityProviderOperation.class),
        @XmlElement(name = "createProtocolMapper", type = CreateProtocolMapperOperation.class),
        @XmlElement(name = "deleteProtocolMapper", type = DeleteProtocolMapperOperation.class),
        @XmlElement(name = "createAuthenticationFlow", type = CreateAuthenticationFlowOperation.class),
        @XmlElement(name = "deleteAuthenticationFlow", type = DeleteAuthenticationFlowOperation.class)
    })
    private List<Operation> operations = new ArrayList<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Boolean getFailOnError() {
        return failOnError;
    }

    public void setFailOnError(Boolean failOnError) {
        this.failOnError = failOnError;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    /**
     * Get a unique identifier for this changeset.
     */
    public String getId() {
        return version + ":" + author;
    }

    @Override
    public String toString() {
        return "Changeset{version=" + version + ", author='" + author + "', operations=" + operations.size() + "}";
    }
}
