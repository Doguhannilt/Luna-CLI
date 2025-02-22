package org.cli.sql.postgresql;

import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.exceptions.ConnectionNullException;

import java.sql.*;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;
import static org.cli.manager.CommandHistory.showHistory;

public class ExecutePostgresql extends QueriesPostgresql {

    /**
     * Executes the given SQL query and processes it based on the command type.
     * <p>
     * This method handles different types of SQL commands including transaction management, table operations,
     * stored procedure calls, and more. If the query doesn't match any predefined command, it's executed as a
     * general SQL statement.
     * </p>
     * <h2>Supported Commands</h2>
     * <a href="https://github.com/Doguhannilt/LunaCLI-Postgresql?tab=readme-ov-file#available-commands">Github Readme Link</a>
     * <p>
     * Each command is parsed, and the corresponding method is called to handle it. If the query is not recognized
     * as a specific command, it is executed as a regular SQL query using the {@link Statement#execute(String)} method.
     * </p>
     *
     * @param sqlQuery The SQL query to execute.
     */
    public static void command(String sqlQuery) {
        try { if (ConnectToPostgresql.connection == null) { throw new ConnectionNullException(); }}
        catch (ConnectionNullException e) { System.out.println(e.getMessage());}


        try (Statement statement = ConnectToPostgresql.connection.createStatement()) {
            String trimmedQuery = sqlQuery.trim().toLowerCase();
            String[] parts = trimmedQuery.split(" ", 3);
            String commandType = parts[0];

            switch (commandType) {
                case "begin-transaction":
                    beginTransaction();
                    break;
                case "commit":
                    commitTransaction();
                    break;
                case "rollback":
                    rollbackTransaction();
                    break;
                case "call-procedure":
                    callProcedure(parts[1]);
                    break;
                case "call-function":
                    callFunction(parts[1]);
                    break;
                case "create-table":
                    createTable(parts[1], parts[2]);
                    break;
                case "drop-table":
                    dropTable(parts[1]);
                    break;
                case "create-schema":
                    createSchema(parts[1]);
                    break;
                case "insert-into":
                    insertInto(parts[1], parts[2]);
                    break;
                case "select-from":
                    if (parts.length < 2) {
                        System.out.println(INVALID_MESSAGE + "Syntax Error: select-from requires a table name.");
                        return;
                    }
                    selectFrom(parts[1], parts.length > 2 ? parts[2] : "");
                    break;
                case "update":
                    update(parts[1], parts[2], parts.length > 3 ? parts[3] : "");
                    break;
                case "delete-from":
                    deleteFrom(parts[1], parts.length > 2 ? parts[2] : "");
                    break;
                case "backup-database":
                    backupDatabase(parts[1]);
                    break;
                case "restore-database":
                    restoreDatabase(parts[1]);
                    break;
                case "history":
                    showHistory();
                    break;
                case "help":
                    help();
                    break;
                default:
                    statement.execute(sqlQuery);
                    System.out.println(VALID_MESSAGE + sqlQuery);
                    break;
            }
        } catch (SQLException e) {
            System.out.println(INVALID_MESSAGE + e.getMessage());
        }
    }
}
