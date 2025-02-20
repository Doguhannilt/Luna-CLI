package org.cli.sql;

import org.cli.conn.ConnectToPostgreSQL;
import org.cli.exceptions.ConnectionNullException;

import java.sql.*;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;

public class ExecuteSQL extends Queries{

    public static void command(String sqlQuery) {
        try { if (ConnectToPostgreSQL.connection == null) { throw new ConnectionNullException(); }}
        catch (ConnectionNullException e) { System.out.println(e.getMessage());}


        try (Statement statement = ConnectToPostgreSQL.connection.createStatement()) {
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
