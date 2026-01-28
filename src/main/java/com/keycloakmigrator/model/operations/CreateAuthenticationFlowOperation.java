package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createAuthenticationFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateAuthenticationFlowOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String alias;

    @XmlElement
    private String flowDescription;

    @XmlElement
    private String providerId = "basic-flow";

    @XmlElement
    private Boolean topLevel = true;

    @XmlElement
    private Boolean builtIn = false;

    @XmlElementWrapper(name = "executions")
    @XmlElement(name = "execution")
    private List<AuthenticationExecution> executions;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getFlowDescription() { return flowDescription; }
    public void setFlowDescription(String flowDescription) { this.flowDescription = flowDescription; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public Boolean getTopLevel() { return topLevel; }
    public void setTopLevel(Boolean topLevel) { this.topLevel = topLevel; }

    public Boolean getBuiltIn() { return builtIn; }
    public void setBuiltIn(Boolean builtIn) { this.builtIn = builtIn; }

    public List<AuthenticationExecution> getExecutions() { return executions; }
    public void setExecutions(List<AuthenticationExecution> executions) { this.executions = executions; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createAuthenticationFlow(this);
    }

    @Override
    public String getDescription() {
        return "Create authentication flow '" + alias + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AuthenticationExecution {
        @XmlAttribute(required = true)
        private String authenticator;

        @XmlAttribute
        private String requirement = "REQUIRED";

        @XmlAttribute
        private Integer priority;

        @XmlElement
        private Boolean authenticatorFlow = false;

        @XmlElement
        private String flowAlias;

        @XmlElementWrapper(name = "config")
        @XmlElement(name = "entry")
        private List<CreateClientOperation.AttributeEntry> config;

        public String getAuthenticator() { return authenticator; }
        public void setAuthenticator(String authenticator) { this.authenticator = authenticator; }
        public String getRequirement() { return requirement; }
        public void setRequirement(String requirement) { this.requirement = requirement; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public Boolean getAuthenticatorFlow() { return authenticatorFlow; }
        public void setAuthenticatorFlow(Boolean authenticatorFlow) { this.authenticatorFlow = authenticatorFlow; }
        public String getFlowAlias() { return flowAlias; }
        public void setFlowAlias(String flowAlias) { this.flowAlias = flowAlias; }
        public List<CreateClientOperation.AttributeEntry> getConfig() { return config; }
        public void setConfig(List<CreateClientOperation.AttributeEntry> config) { this.config = config; }
    }
}
