package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createClientScope")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateClientScopeOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute(required = true)
    private String name;

    @XmlElement
    private String scopeDescription;

    @XmlElement
    private String protocol = "openid-connect";

    @XmlElement
    private Boolean includeInTokenScope = true;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    private List<CreateClientOperation.AttributeEntry> attributes;

    @XmlElementWrapper(name = "protocolMappers")
    @XmlElement(name = "mapper")
    private List<ProtocolMapperConfig> protocolMappers;

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScopeDescription() { return scopeDescription; }
    public void setScopeDescription(String scopeDescription) { this.scopeDescription = scopeDescription; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public Boolean getIncludeInTokenScope() { return includeInTokenScope; }
    public void setIncludeInTokenScope(Boolean includeInTokenScope) { this.includeInTokenScope = includeInTokenScope; }

    public List<CreateClientOperation.AttributeEntry> getAttributes() { return attributes; }
    public void setAttributes(List<CreateClientOperation.AttributeEntry> attributes) { this.attributes = attributes; }

    public List<ProtocolMapperConfig> getProtocolMappers() { return protocolMappers; }
    public void setProtocolMappers(List<ProtocolMapperConfig> protocolMappers) { this.protocolMappers = protocolMappers; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createClientScope(this);
    }

    @Override
    public String getDescription() {
        return "Create client scope '" + name + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProtocolMapperConfig {
        @XmlAttribute(required = true)
        private String name;

        @XmlAttribute(required = true)
        private String protocol;

        @XmlAttribute(required = true)
        private String protocolMapper;

        @XmlElement
        private Boolean consentRequired = false;

        @XmlElementWrapper(name = "config")
        @XmlElement(name = "entry")
        private List<CreateClientOperation.AttributeEntry> config;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getProtocol() { return protocol; }
        public void setProtocol(String protocol) { this.protocol = protocol; }
        public String getProtocolMapper() { return protocolMapper; }
        public void setProtocolMapper(String protocolMapper) { this.protocolMapper = protocolMapper; }
        public Boolean getConsentRequired() { return consentRequired; }
        public void setConsentRequired(Boolean consentRequired) { this.consentRequired = consentRequired; }
        public List<CreateClientOperation.AttributeEntry> getConfig() { return config; }
        public void setConfig(List<CreateClientOperation.AttributeEntry> config) { this.config = config; }
    }
}
