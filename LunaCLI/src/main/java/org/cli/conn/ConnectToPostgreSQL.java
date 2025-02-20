package org.cli.conn;

import org.cli.sql.Info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.sql.Info.getBaseUrl;


public class ConnectToPostgreSQL {

    static Info info = new Info();
    public static  Connection connection = null;

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

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

    public static void displayInfo() throws SQLException {
        System.out.printf("""
                Database Information:
                ---------------------
                DATABASE NAME: %s
                PORT: %d
                BASE_URL: %s
                Connection Status: %s
                %n""" ,info.getDatabaseName(), info.getPORT(), getBaseUrl(), (connection != null && !connection.isClosed() ? "Connected" : "Not Connected"));
    }

    public static void connectToDatabase(String username, String password, String database) {
        String urlForConnection = getBaseUrl() + database;

        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Already connected to: " + database);
                return;
            }

            connection = DriverManager.getConnection(urlForConnection, username, password);
            System.out.println("Connected to database: " + connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
