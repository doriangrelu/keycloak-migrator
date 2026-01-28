package com.keycloakmigrator.model.operations;

import com.keycloakmigrator.client.KeycloakAdminClient;
import jakarta.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "createRealm")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateRealmOperation implements Operation {

    @XmlAttribute(required = true)
    private String name;

    @XmlElement
    private String displayName;

    @XmlElement
    private String displayNameHtml;

    @XmlElement
    private Boolean enabled = true;

    @XmlElement
    private Boolean registrationAllowed;

    @XmlElement
    private Boolean registrationEmailAsUsername;

    @XmlElement
    private Boolean verifyEmail;

    @XmlElement
    private Boolean resetPasswordAllowed;

    @XmlElement
    private Boolean loginWithEmailAllowed;

    @XmlElement
    private Boolean duplicateEmailsAllowed;

    @XmlElement
    private Boolean rememberMe;

    @XmlElement
    private Boolean bruteForceProtected;

    @XmlElement
    private Integer maxFailureWaitSeconds;

    @XmlElement
    private Integer minimumQuickLoginWaitSeconds;

    @XmlElement
    private Integer waitIncrementSeconds;

    @XmlElement
    private Integer quickLoginCheckMilliSeconds;

    @XmlElement
    private Integer maxDeltaTimeSeconds;

    @XmlElement
    private Integer failureFactor;

    @XmlElement
    private String defaultRole;

    @XmlElement
    private String sslRequired;

    @XmlElement
    private Integer accessTokenLifespan;

    @XmlElement
    private Integer accessTokenLifespanForImplicitFlow;

    @XmlElement
    private Integer ssoSessionIdleTimeout;

    @XmlElement
    private Integer ssoSessionMaxLifespan;

    @XmlElement
    private Integer offlineSessionIdleTimeout;

    @XmlElement
    private Integer accessCodeLifespan;

    @XmlElement
    private Integer accessCodeLifespanUserAction;

    @XmlElement
    private Integer accessCodeLifespanLogin;

    @XmlElement
    private String passwordPolicy;

    @XmlElementWrapper(name = "defaultDefaultClientScopes")
    @XmlElement(name = "scope")
    private List<String> defaultDefaultClientScopes;

    @XmlElementWrapper(name = "defaultOptionalClientScopes")
    @XmlElement(name = "scope")
    private List<String> defaultOptionalClientScopes;

    @XmlElement
    private String browserFlow;

    @XmlElement
    private String registrationFlow;

    @XmlElement
    private String directGrantFlow;

    @XmlElement
    private String resetCredentialsFlow;

    @XmlElement
    private String clientAuthenticationFlow;

    @XmlElement
    private String dockerAuthenticationFlow;

    @XmlElement
    private SmtpConfig smtpServer;

    @XmlElement
    private String loginTheme;

    @XmlElement
    private String accountTheme;

    @XmlElement
    private String adminTheme;

    @XmlElement
    private String emailTheme;

    @XmlElement
    private Boolean internationalizationEnabled;

    @XmlElementWrapper(name = "supportedLocales")
    @XmlElement(name = "locale")
    private List<String> supportedLocales;

    @XmlElement
    private String defaultLocale;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDisplayNameHtml() { return displayNameHtml; }
    public void setDisplayNameHtml(String displayNameHtml) { this.displayNameHtml = displayNameHtml; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Boolean getRegistrationAllowed() { return registrationAllowed; }
    public void setRegistrationAllowed(Boolean registrationAllowed) { this.registrationAllowed = registrationAllowed; }

    public Boolean getRegistrationEmailAsUsername() { return registrationEmailAsUsername; }
    public void setRegistrationEmailAsUsername(Boolean registrationEmailAsUsername) { this.registrationEmailAsUsername = registrationEmailAsUsername; }

    public Boolean getVerifyEmail() { return verifyEmail; }
    public void setVerifyEmail(Boolean verifyEmail) { this.verifyEmail = verifyEmail; }

    public Boolean getResetPasswordAllowed() { return resetPasswordAllowed; }
    public void setResetPasswordAllowed(Boolean resetPasswordAllowed) { this.resetPasswordAllowed = resetPasswordAllowed; }

    public Boolean getLoginWithEmailAllowed() { return loginWithEmailAllowed; }
    public void setLoginWithEmailAllowed(Boolean loginWithEmailAllowed) { this.loginWithEmailAllowed = loginWithEmailAllowed; }

    public Boolean getDuplicateEmailsAllowed() { return duplicateEmailsAllowed; }
    public void setDuplicateEmailsAllowed(Boolean duplicateEmailsAllowed) { this.duplicateEmailsAllowed = duplicateEmailsAllowed; }

    public Boolean getRememberMe() { return rememberMe; }
    public void setRememberMe(Boolean rememberMe) { this.rememberMe = rememberMe; }

    public Boolean getBruteForceProtected() { return bruteForceProtected; }
    public void setBruteForceProtected(Boolean bruteForceProtected) { this.bruteForceProtected = bruteForceProtected; }

    public String getSslRequired() { return sslRequired; }
    public void setSslRequired(String sslRequired) { this.sslRequired = sslRequired; }

    public Integer getAccessTokenLifespan() { return accessTokenLifespan; }
    public void setAccessTokenLifespan(Integer accessTokenLifespan) { this.accessTokenLifespan = accessTokenLifespan; }

    public Integer getSsoSessionIdleTimeout() { return ssoSessionIdleTimeout; }
    public void setSsoSessionIdleTimeout(Integer ssoSessionIdleTimeout) { this.ssoSessionIdleTimeout = ssoSessionIdleTimeout; }

    public Integer getSsoSessionMaxLifespan() { return ssoSessionMaxLifespan; }
    public void setSsoSessionMaxLifespan(Integer ssoSessionMaxLifespan) { this.ssoSessionMaxLifespan = ssoSessionMaxLifespan; }

    public String getPasswordPolicy() { return passwordPolicy; }
    public void setPasswordPolicy(String passwordPolicy) { this.passwordPolicy = passwordPolicy; }

    public List<String> getDefaultDefaultClientScopes() { return defaultDefaultClientScopes; }
    public void setDefaultDefaultClientScopes(List<String> defaultDefaultClientScopes) { this.defaultDefaultClientScopes = defaultDefaultClientScopes; }

    public List<String> getDefaultOptionalClientScopes() { return defaultOptionalClientScopes; }
    public void setDefaultOptionalClientScopes(List<String> defaultOptionalClientScopes) { this.defaultOptionalClientScopes = defaultOptionalClientScopes; }

    public SmtpConfig getSmtpServer() { return smtpServer; }
    public void setSmtpServer(SmtpConfig smtpServer) { this.smtpServer = smtpServer; }

    public String getLoginTheme() { return loginTheme; }
    public void setLoginTheme(String loginTheme) { this.loginTheme = loginTheme; }

    public String getAccountTheme() { return accountTheme; }
    public void setAccountTheme(String accountTheme) { this.accountTheme = accountTheme; }

    public String getAdminTheme() { return adminTheme; }
    public void setAdminTheme(String adminTheme) { this.adminTheme = adminTheme; }

    public String getEmailTheme() { return emailTheme; }
    public void setEmailTheme(String emailTheme) { this.emailTheme = emailTheme; }

    public Boolean getInternationalizationEnabled() { return internationalizationEnabled; }
    public void setInternationalizationEnabled(Boolean internationalizationEnabled) { this.internationalizationEnabled = internationalizationEnabled; }

    public List<String> getSupportedLocales() { return supportedLocales; }
    public void setSupportedLocales(List<String> supportedLocales) { this.supportedLocales = supportedLocales; }

    public String getDefaultLocale() { return defaultLocale; }
    public void setDefaultLocale(String defaultLocale) { this.defaultLocale = defaultLocale; }

    @Override
    public void execute(KeycloakAdminClient client) throws Exception {
        client.createRealm(this);
    }

    @Override
    public String getDescription() {
        return "Create realm: " + name;
    }

    @Override
    public String getTargetRealm() {
        return name;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SmtpConfig {
        @XmlElement
        private String host;
        @XmlElement
        private String port;
        @XmlElement
        private String from;
        @XmlElement
        private String fromDisplayName;
        @XmlElement
        private Boolean ssl;
        @XmlElement
        private Boolean starttls;
        @XmlElement
        private Boolean auth;
        @XmlElement
        private String user;
        @XmlElement
        private String password;

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public String getPort() { return port; }
        public void setPort(String port) { this.port = port; }
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getFromDisplayName() { return fromDisplayName; }
        public void setFromDisplayName(String fromDisplayName) { this.fromDisplayName = fromDisplayName; }
        public Boolean getSsl() { return ssl; }
        public void setSsl(Boolean ssl) { this.ssl = ssl; }
        public Boolean getStarttls() { return starttls; }
        public void setStarttls(Boolean starttls) { this.starttls = starttls; }
        public Boolean getAuth() { return auth; }
        public void setAuth(Boolean auth) { this.auth = auth; }
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
