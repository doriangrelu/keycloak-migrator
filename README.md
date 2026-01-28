# Keycloak Migrator

A Liquibase-inspired CLI tool for versioning and migrating Keycloak realms.

## Overview

Keycloak Migrator allows you to manage your Keycloak configuration as code using XML changelog files. Just like Liquibase does for databases, this tool tracks applied changes and applies only the pending migrations.

### Key Features

- **Version Control**: Track all changes to your Keycloak realms in XML files
- **Incremental Migrations**: Only apply changes that haven't been applied yet
- **Multiple Operations**: Support for realms, clients, users, roles, groups, client scopes, identity providers, and more
- **Migration Tracking**: Migration history stored in realm attributes
- **Validation**: XML schema validation for changelog files
- **Dry Run**: Preview changes before applying them

## Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (for building)
- Access to a Keycloak server (version 22+)

### Building from Source

```bash
git clone https://github.com/your-org/keycloak-migrator.git
cd keycloak-migrator
mvn clean package
```

The executable JAR will be created at `target/keycloak-migrator-1.0.0-SNAPSHOT.jar`.

## Quick Start

### 1. Create a Changelog Directory

```
migrations/
  01-create-realm.xml
  02-create-clients.xml
  03-create-users.xml
```

### 2. Write Your First Changelog

```xml
<?xml version="1.0" encoding="UTF-8"?>
<changelog xmlns="http://keycloak-migrator.com/changelog">

    <changeset version="1" author="admin">
        <comment>Create the application realm</comment>
        <createRealm name="my-app">
            <displayName>My Application</displayName>
            <enabled>true</enabled>
        </createRealm>
    </changeset>

    <changeset version="2" author="admin">
        <comment>Create the frontend client</comment>
        <createClient realm="my-app" clientId="frontend">
            <name>Frontend Application</name>
            <publicClient>true</publicClient>
            <redirectUris>
                <uri>http://localhost:3000/*</uri>
            </redirectUris>
        </createClient>
    </changeset>

</changelog>
```

### 3. Run the Migration

```bash
java -jar keycloak-migrator.jar migrate ./migrations \
    --keycloak-url http://localhost:8080 \
    --client-id admin-cli \
    --client-secret your-secret
```

## Configuration

### CLI Arguments

| Argument | Description | Required |
|----------|-------------|----------|
| `--keycloak-url`, `-u` | Keycloak server URL | Yes* |
| `--client-id`, `-c` | Client ID for authentication | Yes* |
| `--client-secret`, `-s` | Client secret for authentication | Yes* |
| `--realm`, `-r` | Authentication realm (default: master) | No |
| `--target-realm`, `-t` | Target realm for migration tracking | No |
| `--dry-run` | Preview changes without applying | No |
| `--skip-validation` | Skip XML schema validation | No |

*Can also be set via environment variables.

### Environment Variables

```bash
export KEYCLOAK_URL=http://localhost:8080
export KEYCLOAK_REALM=master
export KEYCLOAK_CLIENT_ID=admin-cli
export KEYCLOAK_CLIENT_SECRET=your-secret
```

## Commands

### migrate

Apply pending migrations to Keycloak.

```bash
# Basic usage
java -jar keycloak-migrator.jar migrate ./migrations

# With explicit configuration
java -jar keycloak-migrator.jar migrate ./migrations \
    --keycloak-url http://localhost:8080 \
    --client-id admin-cli \
    --client-secret secret

# Dry run (preview only)
java -jar keycloak-migrator.jar migrate ./migrations --dry-run
```

### status

Show migration status for a realm.

```bash
java -jar keycloak-migrator.jar status ./migrations \
    --target-realm my-app \
    --show-history
```

### validate

Validate changelog XML files.

```bash
# Validate a directory
java -jar keycloak-migrator.jar validate ./migrations

# Validate a single file
java -jar keycloak-migrator.jar validate ./migrations/01-create-realm.xml
```

## Changelog Format

### Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<changelog xmlns="http://keycloak-migrator.com/changelog">

    <!-- Changesets are executed in order by version number -->
    <changeset version="1" author="username">
        <comment>Description of the change</comment>
        <!-- Operations go here -->
    </changeset>

    <!-- Include other changelog files -->
    <include file="other-changes.xml"/>

</changelog>
```

### Changeset Attributes

| Attribute | Description | Required |
|-----------|-------------|----------|
| `version` | Unique version number (integer) | Yes |
| `author` | Author of the changeset | Yes |
| `context` | Optional context for conditional execution | No |
| `failOnError` | Stop on error (default: true) | No |

## Supported Operations

### Realm Operations

```xml
<!-- Create a realm -->
<createRealm name="my-realm">
    <displayName>My Realm</displayName>
    <enabled>true</enabled>
    <!-- ... more options -->
</createRealm>

<!-- Update a realm -->
<updateRealm name="my-realm">
    <accessTokenLifespan>600</accessTokenLifespan>
</updateRealm>

<!-- Delete a realm -->
<deleteRealm name="my-realm"/>
```

### Client Operations

```xml
<!-- Create a client -->
<createClient realm="my-realm" clientId="my-client">
    <name>My Client</name>
    <publicClient>true</publicClient>
    <standardFlowEnabled>true</standardFlowEnabled>
    <redirectUris>
        <uri>http://localhost:3000/*</uri>
    </redirectUris>
</createClient>

<!-- Update a client -->
<updateClient realm="my-realm" clientId="my-client">
    <enabled>false</enabled>
</updateClient>

<!-- Delete a client -->
<deleteClient realm="my-realm" clientId="my-client"/>
```

### User Operations

```xml
<!-- Create a user -->
<createUser realm="my-realm" username="john">
    <email>john@example.com</email>
    <firstName>John</firstName>
    <lastName>Doe</lastName>
    <enabled>true</enabled>
    <password>initial-password</password>
    <temporaryPassword>true</temporaryPassword>
    <realmRoles>
        <role>user</role>
    </realmRoles>
    <groups>
        <group>Employees</group>
    </groups>
</createUser>

<!-- Update a user -->
<updateUser realm="my-realm" username="john">
    <addRealmRoles>
        <role>admin</role>
    </addRealmRoles>
    <removeGroups>
        <group>Employees</group>
    </removeGroups>
</updateUser>

<!-- Delete a user -->
<deleteUser realm="my-realm" username="john"/>
```

### Role Operations

```xml
<!-- Create a realm role -->
<createRealmRole realm="my-realm" name="admin">
    <roleDescription>Administrator role</roleDescription>
</createRealmRole>

<!-- Create a client role -->
<createClientRole realm="my-realm" clientId="my-client" name="editor">
    <roleDescription>Editor role</roleDescription>
</createClientRole>

<!-- Create a composite role -->
<createRealmRole realm="my-realm" name="super-admin">
    <roleDescription>Super administrator</roleDescription>
    <composite>true</composite>
    <composites>
        <role>admin</role>
        <role>user</role>
    </composites>
</createRealmRole>

<!-- Delete a role -->
<deleteRole realm="my-realm" name="old-role"/>
<deleteRole realm="my-realm" name="client-role" clientId="my-client"/>
```

### Group Operations

```xml
<!-- Create a group -->
<createGroup realm="my-realm" name="Administrators">
    <realmRoles>
        <role>admin</role>
    </realmRoles>
</createGroup>

<!-- Create a nested group -->
<createGroup realm="my-realm" name="Team A" parentGroup="Engineering"/>

<!-- Delete a group -->
<deleteGroup realm="my-realm" name="OldGroup"/>
```

### Client Scope Operations

```xml
<!-- Create a client scope -->
<createClientScope realm="my-realm" name="custom-scope">
    <scopeDescription>Custom scope description</scopeDescription>
    <protocol>openid-connect</protocol>
    <protocolMappers>
        <mapper name="custom-claim" protocol="openid-connect"
                protocolMapper="oidc-hardcoded-claim-mapper">
            <config>
                <entry key="claim.name">custom</entry>
                <entry key="claim.value">value</entry>
            </config>
        </mapper>
    </protocolMappers>
</createClientScope>

<!-- Delete a client scope -->
<deleteClientScope realm="my-realm" name="old-scope"/>
```

### Identity Provider Operations

```xml
<!-- Create an identity provider -->
<createIdentityProvider realm="my-realm" alias="google" providerId="google">
    <displayName>Sign in with Google</displayName>
    <enabled>true</enabled>
    <config>
        <entry key="clientId">your-client-id</entry>
        <entry key="clientSecret">your-client-secret</entry>
    </config>
</createIdentityProvider>

<!-- Delete an identity provider -->
<deleteIdentityProvider realm="my-realm" alias="old-provider"/>
```

### Protocol Mapper Operations

```xml
<!-- Add mapper to a client -->
<createProtocolMapper realm="my-realm" clientId="my-client"
                      name="groups" protocol="openid-connect"
                      protocolMapper="oidc-group-membership-mapper">
    <config>
        <entry key="claim.name">groups</entry>
        <entry key="access.token.claim">true</entry>
    </config>
</createProtocolMapper>

<!-- Delete a mapper -->
<deleteProtocolMapper realm="my-realm" clientId="my-client" name="old-mapper"/>
```

## Migration Tracking

Keycloak Migrator tracks applied migrations using realm attributes:

- `migrator.lastVersion`: The last applied version number
- `migrator.history`: JSON array of all applied migrations with timestamps

This allows the tool to determine which changesets need to be applied on subsequent runs.

## Best Practices

1. **Use Sequential Versions**: Always use sequential version numbers to ensure proper ordering.

2. **One Concern Per Changeset**: Keep changesets focused on a single logical change.

3. **Use Descriptive Comments**: Always include a `<comment>` explaining what the changeset does.

4. **Test in Development First**: Always test migrations in a development environment before production.

5. **Use Dry Run**: Preview changes with `--dry-run` before applying.

6. **Version Control Your Changelogs**: Store changelog files in your version control system.

7. **Never Modify Applied Changesets**: Once a changeset is applied, don't modify it. Create a new one instead.

## Examples

See the `examples/` directory for comprehensive examples:

- `01-create-realm.xml` - Realm creation with all options
- `02-create-clients.xml` - Different client types
- `03-roles-and-groups.xml` - Roles, composite roles, and groups
- `04-users.xml` - User creation and management
- `05-client-scopes-and-mappers.xml` - Custom scopes and claims
- `06-identity-providers.xml` - OAuth/OIDC providers
- `07-updates-and-deletes.xml` - Updating and removing resources

## Troubleshooting

### Common Issues

**"Client not found" error**

Ensure the client ID used for authentication has proper permissions. The client needs:
- Service account enabled
- `realm-management` roles assigned

**"Realm does not exist" error**

For tracking to work, at least one realm must exist. The first changeset should create a realm.

**Duplicate version error**

Each changeset must have a unique version number across all changelog files.

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## License

This project is licensed under the MIT License.
