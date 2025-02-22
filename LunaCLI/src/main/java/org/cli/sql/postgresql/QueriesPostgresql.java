package org.cli.sql.postgresql;
import static org.cli.utils.Colors.*;

import org.cli.conn.postgresql.ConnectToPostgresql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.cli.sql.postgresql.ExecutePostgresql.command;

public class QueriesPostgresql {

    public static void beginTransaction() {
        try {
            ConnectToPostgresql.connection.setAutoCommit(false);
            System.out.println("Transaction started.");
        } catch (SQLException e) {
            System.out.println("Transaction Error: " + e.getMessage());
        }
    }

    public static void commitTransaction() {
        try {
            ConnectToPostgresql.connection.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            System.out.println("Commit Error: " + e.getMessage());
        }
    }

    public static void rollbackTransaction() {
        try {
            ConnectToPostgresql.connection.rollback();
            System.out.println("Transaction rolled back successfully.");
        } catch (SQLException e) {
            System.out.println("Rollback Error: " + e.getMessage());
        }
    }

    public static void callProcedure(String procedureName) {
        try (CallableStatement callableStatement = ConnectToPostgresql.connection.prepareCall("{call " + procedureName + "}")) {
            callableStatement.execute();
            System.out.println("Procedure '" + procedureName + "' executed successfully.");
        } catch (SQLException e) {
            System.out.println("Procedure Error: " + e.getMessage());
        }
    }

    public static void callFunction(String functionName) {
        try (CallableStatement callableStatement = ConnectToPostgresql.connection.prepareCall("{? = call " + functionName + "}")) {
            callableStatement.registerOutParameter(1, Types.OTHER);
            callableStatement.execute();
            System.out.println("Function '" + functionName + "' returned: " + callableStatement.getObject(1));
        } catch (SQLException e) {
            System.out.println("Function Error: " + e.getMessage());
        }
    }

    public static void createTable(String tableName, String columns) {
        String sql = "CREATE TABLE " + tableName + " (" + columns + ")";
        command(sql);
    }

    public static void dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName;
        command(sql);
    }

    public static void createSchema(String schemaName) {
        String sql = "CREATE SCHEMA " + schemaName;
        command(sql);
    }

    public static void insertInto(String tableName, String values) {
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        command(sql);
    }

    public static void selectFrom(String tableName, String condition) {
        String sql = "SELECT * FROM " + tableName + (condition.isEmpty() ? "" : " WHERE " + condition);


        /*
        * Print Table
        */
        try (Statement statement = ConnectToPostgresql.connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<String[]> rows = new ArrayList<>();
            int[] columnWidths = new int[columnCount];

            String[] headers = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                headers[i] = metaData.getColumnName(i + 1);
                columnWidths[i] = headers[i].length();
            }
            rows.add(headers);

            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                    if (row[i] == null) row[i] = "NULL"; // Null değerleri yönet
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
                rows.add(row);
            }

            if (rows.size() == 1) {
                System.out.println("No data found in the table.");
                return;
            }

            printTable(rows, columnWidths);

        } catch (SQLException e) {
            System.out.println("SQL Execution Error: " + e.getMessage());
        }
    }
    
    public static void update(String tableName, String setClause, String condition) {
        String sql = "UPDATE " + tableName + " SET " + setClause + (condition.isEmpty() ? "" : " WHERE " + condition);
        command(sql);
    }

    public static void deleteFrom(String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + (condition.isEmpty() ? "" : " WHERE " + condition);
        command(sql);
    }

    public static void backupDatabase(String filePath) {
        String sql = "pg_dump -U postgres -d jpa -f " + filePath;
        executeSystemCommand(sql);
    }

    public static void restoreDatabase(String filePath) {
        String sql = "psql -U postgres -d jpa -f " + filePath;
        executeSystemCommand(sql);
    }

    public static void executeSystemCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Command executed successfully.");
            } else {
                System.out.println("Command failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.out.println("System Command Error: " + e.getMessage());
        }
    }

    public static void help() {
        System.out.println("Available commands:");
        System.out.println("-----------------------------------------------------");
        System.out.println(GREEN + "QUERIES" + RESET);
        System.out.println("- begin-transaction: Start a new transaction.");
        System.out.println("- commit: Commit the current transaction.");
        System.out.println("- rollback: Rollback the current transaction.");
        System.out.println("- call-procedure <procedure_name>: Call a stored procedure.");
        System.out.println("- call-function <function_name>: Call a function.");
        System.out.println("- create-table <table_name> <columns>: Create a new table.");
        System.out.println("- drop-table <table_name>: Drop a table.");
        System.out.println("- create-schema <schema_name>: Create a new schema.");
        System.out.println("- insert-into <table_name> <values>: Insert data into a table.");
        System.out.println("- select-from <table_name> [condition]: Select data from a table.");
        System.out.println("- update <table_name> <set_clause> [condition]: Update data in a table.");
        System.out.println("- delete-from <table_name> [condition]: Delete data from a table.");
        System.out.println("- backup-database <file_path>: Backup the database.");
        System.out.println("- restore-database <file_path>: Restore the database.");
        System.out.println(RED + "- help: Show this help message." + RESET);
        System.out.println("-----------------------------------------------------");
        System.out.println(GREEN + "ENTITY MANAGER" + RESET);
        System.out.println("- save username:<username> password:<password> database:<database> | Save User");
        System.out.println("- load users | Display all users");
        System.out.println("- force user:<EntityId> | Get user by Id");
        System.out.println("- clone user:<EntityId> | Connect a cloned user");
    }

    private static void printTable(List<String[]> rows, int[] columnWidths) {
        StringBuilder separator = new StringBuilder("+");

        for (int width : columnWidths) {separator.append("-".repeat(width + 2)).append("+");}
        System.out.println(separator);

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String[] row = rows.get(rowIndex);
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                System.out.printf(" %-"+columnWidths[i]+"s |", row[i]);
            }
            System.out.println();
            if (rowIndex == 0) {
                System.out.println(separator);
            }
        }
        System.out.println(separator);
    }
}
