package org.cli.conn.postgresql;

import org.cli.sql.Info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;
import static org.cli.sql.Info.getBaseUrlForPostgresql;


public class ConnectToPostgresql {

    static Info info = new Info();
    public static Connection connection = null;

    /**
     * Checks if there is an active database connection.
     * <p>
     * This method attempts to check whether the current database connection is not null and is not closed.
     * If there is an active connection, it returns true; otherwise, it returns false.
     * If a {@link SQLException} is encountered during the check, the method catches it and returns false.
     * </p>
     *
     * @return true if the database connection is active and open, false otherwise.
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * Changes the port number for the database connection.
     * <p>
     * This method sets the new port number by updating the {@link info} object with the provided port value.
     * After the port is changed, a message is printed notifying the user about the port change and indicating
     * that the CLI should be restarted. The existing database connection is closed to apply the change.
     * </p>
     *
     * @param C_PORT The new port number to set for the database connection.
     */
    public static void changePort(int C_PORT) {
        try {
            info.setPORT(C_PORT);
            System.out.println("PORT has been changed:" + info.getPORT() );
            System.out.println("You should restart your CLI");
            closeConnection();
        } catch (Exception e) {
            System.out.println("Invalid PORT command" + e.getMessage() );
            return;
        }
    }

    /**
     * Displays the current database connection information.
     * <p>
     * This method prints out detailed information about the current database connection, including:
     * <ul>
     *     <li>Database Name</li>
     *     <li>Port Number</li>
     *     <li>Base URL for PostgreSQL</li>
     *     <li>Connection Status (whether it is connected or not)</li>
     * </ul>
     * The connection status is determined based on whether the connection is open or closed.
     * </p>
     *
     * @throws SQLException if an error occurs while fetching connection information or if the database is unreachable.
     */
    public static void displayInfo() throws SQLException {
        System.out.printf("""
                Database Information:
                ---------------------
                DATABASE NAME: %s
                PORT: %d
                BASE_URL: %s
                Connection Status: %s
                %n""" ,info.getDatabaseName(), info.getPORT(), getBaseUrlForPostgresql(), (connection != null && !connection.isClosed() ? "Connected" : "Not Connected"));
    }

    /**
     * Establishes a connection to the specified PostgreSQL database using the provided credentials.
     * <p>
     * This method attempts to connect to the database using the provided username, password, and database name.
     * If there is already an active connection, it prevents reconnecting and prints a message indicating that the
     * connection is already established. Upon successful connection, a confirmation message with the database product name
     * is printed. If the connection fails, an error message is displayed with the exception details.
     * </p>
     *
     * @param username The username to authenticate with the database.
     * @param password The password to authenticate with the database.
     * @param database The name of the database to connect to.
     */
    public static void connectToDatabase(String username, String password, String database) {
        String urlForConnection = getBaseUrlForPostgresql() + database;

        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println(INVALID_MESSAGE +  "Already connected to: " + database);
                return;
            }
            connection = DriverManager.getConnection(urlForConnection, username, password);
            System.out.println(VALID_MESSAGE + "Connected to database: " + connection.getMetaData().getDatabaseProductName());
        }
        catch (SQLException e) {System.out.println(INVALID_MESSAGE + "Connection Error: " + e.getMessage());}
    }

    /**
     * Closes the current active database connection.
     * <p>
     * This method checks whether a connection is currently open. If it is, the connection is closed, and a success message
     * is printed. If the connection is not open or an error occurs while closing, an error message is displayed.
     * </p>
     *
     * @throws SQLException if an error occurs while closing the connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(VALID_MESSAGE + "Database connection closed.");
            }
        }
        catch (SQLException e) { System.out.println(INVALID_MESSAGE + "Error closing connection: " + e.getMessage());}
    }
}
