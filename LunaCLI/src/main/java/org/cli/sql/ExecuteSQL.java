package org.cli.sql;

import org.cli.conn.ConnectToPostgreSQL;

import java.sql.*;

public class ExecuteSQL extends Queries {

    public static void command(String sqlQuery) {
        if (ConnectToPostgreSQL.connection == null) {
            System.err.println("No active database connection. Please connect first.");
            return;
        }

        try (Statement statement = ConnectToPostgreSQL.connection.createStatement()) {
            String trimmedQuery = sqlQuery.trim().toLowerCase();

            if (trimmedQuery.startsWith("begin-transaction")) {
                beginTransaction();
            } else if (trimmedQuery.startsWith("commit")) {
                commitTransaction();
            } else if (trimmedQuery.startsWith("rollback")) {
                rollbackTransaction();
            } else if (trimmedQuery.startsWith("call-procedure")) {
                String procedureName = trimmedQuery.split(" ")[1];
                callProcedure(procedureName);
            } else if (trimmedQuery.startsWith("call-function")) {
                String functionName = trimmedQuery.split(" ")[1];
                callFunction(functionName);
            } else if (trimmedQuery.startsWith("create-table")) {
                String[] parts = trimmedQuery.split(" ", 3);
                String tableName = parts[1];
                String columns = parts[2];
                createTable(tableName, columns);
            } else if (trimmedQuery.startsWith("drop-table")) {
                String tableName = trimmedQuery.split(" ")[1];
                dropTable(tableName);
            } else if (trimmedQuery.startsWith("create-schema")) {
                String schemaName = trimmedQuery.split(" ")[1];
                createSchema(schemaName);
            } else if (trimmedQuery.startsWith("insert-into")) {
                String[] parts = trimmedQuery.split(" ", 3);
                String tableName = parts[1];
                String values = parts[2];
                insertInto(tableName, values);
            } else if (trimmedQuery.startsWith("select-from")) {
                String[] parts = trimmedQuery.split(" ", 3);
                String tableName = parts[1];
                String condition = parts.length > 2 ? parts[2] : "";
                selectFrom(tableName, condition);
            } else if (trimmedQuery.startsWith("update")) {
                String[] parts = trimmedQuery.split(" ", 4);
                String tableName = parts[1];
                String setClause = parts[2];
                String condition = parts.length > 3 ? parts[3] : "";
                update(tableName, setClause, condition);
            } else if (trimmedQuery.startsWith("delete-from")) {
                String[] parts = trimmedQuery.split(" ", 3);
                String tableName = parts[1];
                String condition = parts.length > 2 ? parts[2] : "";
                deleteFrom(tableName, condition);
            } else if (trimmedQuery.startsWith("backup-database")) {
                String filePath = trimmedQuery.split(" ")[1];
                backupDatabase(filePath);
            } else if (trimmedQuery.startsWith("restore-database")) {
                String filePath = trimmedQuery.split(" ")[1];
                restoreDatabase(filePath);
            } else if (trimmedQuery.startsWith("help")) {
                help();
            } else {
                statement.execute(sqlQuery);
                System.out.println("SQL Executed Successfully: " + sqlQuery);
            }
        } catch (SQLException e) {
            System.out.println("SQL Execution Error: " + e.getMessage());
        }
    }
}