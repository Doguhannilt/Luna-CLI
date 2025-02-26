package org.cli.manager;

import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.entities.ConnectionEntity;
import org.cli.entities.SaveEntity;
import org.cli.sql.postgresql.ExecutePostgresql;

import java.sql.SQLException;

import static org.cli.conn.postgresql.SnippetManagerPostgresql.getAllSnippets;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.sql.postgresql.ProcessCommandQueriesPostgresql.*;

public class CommandPackage {

    static public ConnectionEntity connectionEntity = new ConnectionEntity();
    static public SaveEntity saveEntity = new SaveEntity();

    /**
     * <p>Parses and executes the given command for database operations.</p>
     *
     * @param command The command input by the user.
     * @throws SQLException If a database access error occurs.
     */
    public static void command(String command) throws SQLException {
        String[] parts = command.split(" ");

        if (parts.length < 2) { System.out.println(INVALID_MESSAGE + "Invalid Command");}

        String mainCommand = parts[0].toLowerCase();
        String subCommand = parts[1].toLowerCase();

        switch (mainCommand) {
            case "luna":
                handleLunaCommand(subCommand, parts);
                break;
            default:
                System.out.println(INVALID_MESSAGE + "Invalid Command");
        }
    }
    /**
     * <h1>Handles commands that start with "luna".</h1>
     *
     * @param subCommand The second word in the command string.
     * @param parts      The split command array.
     * @throws SQLException If a database access error occurs.
     */
    public static void handleLunaCommand(String subCommand, String[] parts) throws SQLException{
        switch (subCommand) {
            // Connection
            case "connect":
                handleDatabaseConnection(parts);
                break;
            // Save an Entity
            case "entityc":
                handleSaveEntity(parts);
                break;
            // Load Entites
            case "entityl":
                handleGetAllUsers(parts);
                break;
            // Display an entity
            case "entityg":
                handleDisplayUsers(parts);
                break;
            // Get an entity and use it
            case "clone":
                handleGetUserIdAndConnect(parts);
                break;
            // Change Port
            case "port":
                handleChangePort(parts);
                break;
            // Display Info
            case "info":
                ConnectToPostgresql.displayInfo();
                break;
            // Scheduling a given command by user
            case "schedule":
                handleSchedulerAndSchedule(parts);
                break;
            // Export
            case "out":
                handleExportToCsv(parts);
                break;
            // Execute a sql file
            case "run":
                handleExecuteSqlFile(parts);
                break;
            // Execute multiple queries
            case "multiple":
                handleMultipleQueries(parts);
                break;
            // Create a snippet
            case "snippetc":
                handleSaveSnippetQuery(parts);
                break;
            // List all snippets
            case "snippetl":
                getAllSnippets();
                break;
            // Get a snippet by ID
            case "snippetg":
                handleExecuteSnippetById(parts);
                break;
            default:
                if (ConnectToPostgresql.isConnected()) {
                    String sqlCommand = String.join(" ", parts).substring(5);
                    ExecutePostgresql.command(sqlCommand);
                } else {
                    System.out.println(INVALID_MESSAGE + "Unknown Command");
                }
        }
    }
}
