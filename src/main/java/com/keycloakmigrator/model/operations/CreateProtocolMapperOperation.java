package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "createProtocolMapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateProtocolMapperOperation implements Operation {

    @XmlAttribute(required = true)
    private String realm;

    @XmlAttribute
    private String clientId;

    @XmlAttribute
    private String clientScopeName;

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

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientScopeName() { return clientScopeName; }
    public void setClientScopeName(String clientScopeName) { this.clientScopeName = clientScopeName; }

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

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createProtocolMapper(this);
    }

    @Override
    public String getDescription() {
        if (clientId != null) {
            return "Create protocol mapper '" + name + "' for client '" + clientId + "' in realm '" + realm + "'";
        }
        return "Create protocol mapper '" + name + "' for client scope '" + clientScopeName + "' in realm '" + realm + "'";
    }

    @Override
    public String getTargetRealm() {
        return realm;
    }
}
