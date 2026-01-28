package com.keycloakmigrator;

import com.keycloakmigrator.commands.MigrateCommand;
import com.keycloakmigrator.commands.StatusCommand;
import com.keycloakmigrator.commands.ValidateCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Keycloak Migrator CLI - A Liquibase-like tool for Keycloak realm migrations.
 */
@Command(
    name = "keycloak-migrator",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "A CLI tool for versioning and migrating Keycloak realms - like Liquibase for Keycloak",
    subcommands = {
        MigrateCommand.class,
        StatusCommand.class,
        ValidateCommand.class
    }
)
public class KeycloakMigratorCli implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    boolean verbose;

    @Override
    public void run() {
        // If no subcommand is provided, print usage
        CommandLine.usage(this, System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new KeycloakMigratorCli())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(args);
        System.exit(exitCode);
    }

    public boolean isVerbose() {
        return verbose;
    }
}
