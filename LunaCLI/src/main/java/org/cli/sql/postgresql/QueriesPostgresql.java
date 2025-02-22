package org.cli.sql.postgresql;
import static org.cli.utils.Colors.*;

import org.cli.conn.postgresql.ConnectToPostgresql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


import static org.cli.sql.postgresql.ExecutePostgresql.command;

public class QueriesPostgresql {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    /**
     * <h1>Begin a Database Transaction</h1>
     * <p>
     * This method starts a new database transaction by setting the auto-commit mode to false.
     * It is useful for batch processing and ensures that multiple database operations are executed
     * atomically.
     * </p>
     * <p>
     * <strong>Usage:</strong> This method should be called at the beginning of a series of database
     * operations that you want to commit as a single transaction.
     * </p>
     */
    public static void beginTransaction() {
        try {
            ConnectToPostgresql.connection.setAutoCommit(false);
            System.out.println("Transaction started.");
        } catch (SQLException e) {
            System.out.println("Transaction Error: " + e.getMessage());
        }
    }

    /**
     * <h1>Commit the Current Transaction</h1>
     * <p>
     * This method commits the current database transaction, making all changes made during the
     * transaction permanent. It is typically called after a series of successful operations
     * to save the changes to the database.
     * </p>
     * <p>
     * <strong>Usage:</strong> This method should be called after successfully completing a series
     * of database operations that need to be persisted.
     * </p>
     */
    public static void commitTransaction() {
        try {
            ConnectToPostgresql.connection.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            System.out.println("Commit Error: " + e.getMessage());
        }
    }

    /**
     * <h1>Rollback the Current Transaction</h1>
     * <p>
     * This method rolls back the current transaction, discarding all changes made since
     * the transaction started. It is useful for undoing changes in case of errors.
     * </p>
     * <p>
     * <strong>Usage:</strong> This method should be called if an error occurs during a transaction
     * and you need to revert all the changes made during the transaction.
     * </p>
     */
    public static void rollbackTransaction() {
        try {
            ConnectToPostgresql.connection.rollback();
            System.out.println("Transaction rolled back successfully.");
        } catch (SQLException e) {
            System.out.println("Rollback Error: " + e.getMessage());
        }
    }

    /**
     * <h1>Call a Stored Procedure</h1>
     * <p>
     * This method executes a stored procedure in the database. A stored procedure is a precompiled
     * SQL query stored in the database for repeated use.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>procedureName</strong> - The name of the stored procedure to call.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to call a stored procedure with no return value
     * or with output parameters.
     * </p>
     */
    public static void callProcedure(String procedureName) {
        try (CallableStatement callableStatement = ConnectToPostgresql.connection.prepareCall("{call " + procedureName + "}")) {
            callableStatement.execute();
            System.out.println("Procedure '" + procedureName + "' executed successfully.");
        } catch (SQLException e) {
            System.out.println("Procedure Error: " + e.getMessage());
        }
    }

    /**
     * <h1>Call a Database Function</h1>
     * <p>
     * This method executes a function in the database and returns its result. A function in SQL
     * is similar to a stored procedure but typically returns a value.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>functionName</strong> - The name of the function to call.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method should be used to call database functions that return
     * a value, such as aggregate functions or custom SQL functions.
     * </p>
     */
    public static void callFunction(String functionName) {
        try (CallableStatement callableStatement = ConnectToPostgresql.connection.prepareCall("{? = call " + functionName + "}")) {
            callableStatement.registerOutParameter(1, Types.OTHER);
            callableStatement.execute();
            System.out.println("Function '" + functionName + "' returned: " + callableStatement.getObject(1));
        } catch (SQLException e) {
            System.out.println("Function Error: " + e.getMessage());
        }
    }

    /**
     * <h1>Create a New Table in the Database</h1>
     * <p>
     * This method creates a new table in the database with the specified columns.
     * The columns should be defined as a comma-separated string of column definitions.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>tableName</strong> - The name of the table to create.</li>
     *     <li><strong>columns</strong> - A comma-separated list of column definitions (e.g., "id INT PRIMARY KEY, name VARCHAR(255)").</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to define and create new tables in the database.
     * </p>
     */
    public static void createTable(String tableName, String columns) {
        String sql = "CREATE TABLE " + tableName + " (" + columns + ")";
        command(sql);
    }

    /**
     * <h1>Drop a Table from the Database</h1>
     * <p>
     * This method drops (deletes) a table from the database.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>tableName</strong> - The name of the table to drop.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to remove an existing table from the database.
     * </p>
     */
    public static void dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName;
        command(sql);
    }

    /**
     * <h1>Create a New Schema in the Database</h1>
     * <p>
     * This method creates a new schema in the database.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>schemaName</strong> - The name of the schema to create.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to create a new schema, which is a container
     * for database objects like tables and views.
     * </p>
     */
    public static void createSchema(String schemaName) {
        String sql = "CREATE SCHEMA " + schemaName;
        command(sql);
    }

    /**
     * <h1>Insert Data into a Table</h1>
     * <p>
     * This method inserts data into a specified table in the database. The values are passed as a
     * comma-separated string corresponding to the table columns.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>tableName</strong> - The name of the table into which the data will be inserted.</li>
     *     <li><strong>values</strong> - A comma-separated list of values to insert into the table.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to insert records into an existing table in the database.
     * </p>
     */
    public static void insertInto(String tableName, String values) {
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        command(sql);
    }

    /**
     * <h1>Select Data from a Table</h1>
     * <p>
     * This method retrieves data from a specified table in the database. An optional condition
     * can be provided to filter the results.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>tableName</strong> - The name of the table to retrieve data from.</li>
     *     <li><strong>condition</strong> - The optional condition to filter the results (e.g., "age > 30").</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong> This method is used to query data from a table, with optional
     * filtering based on the condition.
     * </p>
     */
    public static void selectFrom(String tableName, String condition) {
        String sql = "SELECT * FROM " + tableName + (condition.isEmpty() ? "" : " WHERE " + condition);

        // Code for printing the table results...
    }


    /**
     * <h1>Updates the specified table with the given set clause and condition.</h1>
     *
     * @param tableName The name of the table to update.
     * @param setClause The SET clause specifying the columns and values to update.
     * @param condition The condition to specify which rows to update.
     */
    public static void update(String tableName, String setClause, String condition) {
        String sql = "UPDATE " + tableName + " SET " + setClause + (condition.isEmpty() ? "" : " WHERE " + condition);
        command(sql);
    }

    /**
     * <h1>Deletes rows from the specified table based on the given condition.</h1>
     *
     * @param tableName The name of the table to delete from.
     * @param condition The condition to specify which rows to delete.
     */
    public static void deleteFrom(String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + (condition.isEmpty() ? "" : " WHERE " + condition);
        command(sql);
    }

    /**
     * <h1>Backs up the database to a specified file.</h1>
     *
     * @param filePath The file path where the backup will be stored.
     */
    public static void backupDatabase(String filePath) {
        String sql = "pg_dump -U postgres -d jpa -f " + filePath;
        executeSystemCommand(sql);
    }

    /**
     * <h1>Restores the database from the specified backup file.</h1>
     *
     * @param filePath The file path of the backup to restore from.
     */
    public static void restoreDatabase(String filePath) {
        String sql = "psql -U postgres -d jpa -f " + filePath;
        executeSystemCommand(sql);
    }

    /**
     * <h1>Executes a system command.</h1>
     *
     * @param command The command to be executed in the system shell.
     */
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

    /**
     * <h2>Displays a help message listing all available commands.</h2>
     */
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

    /**
     * <h1>Schedules a command to be executed after a specified delay.</h1>
     *
     * @param getCommand The command to execute.
     * @param delay The delay before execution (in the specified unit).
     * @param unit The unit of time for the delay (1 for seconds, 2 for minutes, 3 for hours).
     */
    public static void scheduleWithCommand(String getCommand, long delay, int unit) {
        TimeUnit timeUnit;

        switch (unit) {
            case 1:
                timeUnit = TimeUnit.SECONDS;
                break;
            case 2:
                timeUnit = TimeUnit.MINUTES;
                break;
            case 3:
                timeUnit = TimeUnit.HOURS;
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit. Use 1 (Seconds), 2 (Minutes), or 3 (Hours).");
        }

        ScheduledFuture<?> schedule = scheduler.schedule(() -> {
            System.out.println("Executing scheduled command: " + getCommand);
            command(getCommand);
        }, delay, timeUnit);
    }

    /**
     * <h1>Prints a table with the given rows and column widths.</h1>
     *
     * @param rows The rows of the table, where each row is an array of strings.
     * @param columnWidths The widths of the columns.
     */
    private static void printTable(List<String[]> rows, int[] columnWidths) {
        StringBuilder separator = new StringBuilder("+");

        for (int width : columnWidths) {
            separator.append("-".repeat(width + 2)).append("+");
        }
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

    /**
     * <h1>Export Your Data To CSV</h1>
     * <p>
     * This method exports the data retrieved from the provided SQL query to a CSV file.
     * It executes a standard SQL query (e.g., SELECT) and writes the resulting data
     * to a CSV file at the specified location.
     * </p>
     * <p>
     * <strong>Parameters:</strong>
     * </p>
     * <ul>
     *     <li><strong>getCommand</strong> - A valid SQL query string (e.g., "SELECT * FROM table_name")
     *     that retrieves data from the database.</li>
     *     <li><strong>Export</strong> - The file path where the resulting data will be saved as a CSV file.</li>
     * </ul>
     * <p>
     * <strong>Usage:</strong>
     * This method is intended to be used with standard SQL queries and not with custom commands
     * like "select-from" or "insert-into". It operates with typical SQL queries to fetch data
     * and store it in CSV format, ensuring ease of use and compatibility.
     * </p>
     * <p>
     * <strong>Note:</strong>
     * The method expects a well-formed SQL query and a valid file path. Errors will be thrown if
     * there are issues with the SQL execution or file writing process.
     * </p>
     */

    public static void exportToCSV(String getCommand, String filePath) {
        try {
            Statement stmt = ConnectToPostgresql.connection.createStatement();
            ResultSet rs = stmt.executeQuery(getCommand);

            try (FileWriter writer = new FileWriter(filePath)) {


                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    writer.append(rs.getMetaData().getColumnName(i)).append(",");
                }
                writer.append("\n");

                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        writer.append(rs.getString(i)).append(",");
                    }
                    writer.append("\n");
                }

                System.out.println(GREEN + "Data successfully exported to " + filePath + RESET);
        }
            catch (SQLException e) {throw new RuntimeException(e);}
    }
        catch (SQLException e) {throw new RuntimeException(e);}
        catch (IOException e) {throw new RuntimeException(e);}
    }
}
