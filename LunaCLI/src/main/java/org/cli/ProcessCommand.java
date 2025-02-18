package org.cli;


import org.cli.conn.ConnectToPostgreSQL;
import org.cli.conn.ConnectionVariables;
import org.cli.sql.ExecuteSQL;

import java.sql.SQLException;
import java.util.LinkedList;

public class ProcessCommand {

    static Info info = new Info();
    static ConnectionVariables connectionVariables = new ConnectionVariables();

    // luna connect postgresql username:postgres password: database:jpa

    public static void command(String command) throws SQLException {

        String[] parts = command.split(" ");

        if (
                parts.length >= 4
                        && parts[0].equalsIgnoreCase("luna")
                        && parts[1].equalsIgnoreCase("connect")
        ) {
            String dbType = parts[2];

            LinkedList<String> params = new LinkedList<>();
            for (int i = 3; i < parts.length; i++) {
                params.add(parts[i]);
            }

            for (String param : params) {
                if (param.startsWith("username:")) {
                    connectionVariables.setUsername(param.substring("username:".length()));
                } else if (param.startsWith("password:")) {
                    connectionVariables.setPassword(param.substring("password:".length()));
                } else if (param.startsWith("database:")) {
                    connectionVariables.setDatabase(param.substring("database:".length()));
                }
            }
            if (connectionVariables.getUsername() != null && connectionVariables.getDatabase() != null) {
                if (dbType.equalsIgnoreCase("postgresql")) {ConnectToPostgreSQL.connectToDatabase(connectionVariables.getUsername(), connectionVariables.getPassword(), connectionVariables.getDatabase());}
                else {System.out.println("Unsupported Database: " + dbType);}}
            else {System.out.println("Invalid command");}
        }

        else if (parts.length == 2 && parts[0].equalsIgnoreCase("luna") && parts[1].startsWith("port")) {
            int newPort = Integer.parseInt(parts[1].split(":")[1]);
            ConnectToPostgreSQL.changePort(newPort);
        }
        else if (parts.length == 2 && parts[0].equalsIgnoreCase("luna") && parts[1].startsWith("info")) {
            ConnectToPostgreSQL.displayInfo();
        }
        else if (ConnectToPostgreSQL.isConnected()) {
            ExecuteSQL.command(command);
        }
        else {
            System.out.println("Invalid Command or No Active Connection");
        }
    }
}
