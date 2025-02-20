package org.cli.managers;

import org.cli.conn.ConnectToPostgreSQL;
import org.cli.entities.ConnectionEntity;
import org.cli.entities.SaveEntity;
import org.cli.sql.ExecuteSQL;
import org.cli.sql.Info;

import java.sql.SQLException;

import static org.cli.sql.ProcessCommandQueries.*;

public class ProcessCommand {

    static Info info = new Info();
    static public ConnectionEntity connectionEntity = new ConnectionEntity();
    static public SaveEntity saveEntity = new SaveEntity();

    /**
     * Parses and executes the given command for database operations.
     *
     * @param command The command input by the user.
     * @throws SQLException If a database access error occurs.
     */
    public static void command(String command) throws SQLException {
        String[] parts = command.split(" ");

        if (parts.length < 2) {
            System.out.println("Invalid Command");
            return;
        }

        String mainCommand = parts[0].toLowerCase();
        String subCommand = parts[1].toLowerCase();

        switch (mainCommand) {
            case "luna":
                handleLunaCommand(subCommand, parts);
                break;
            default:
                System.out.println("Invalid Command");
        }
    }

    /**
     * Handles commands that start with "luna".
     *
     * @param subCommand The second word in the command string.
     * @param parts      The split command array.
     * @throws SQLException If a database access error occurs.
     */
    public static void handleLunaCommand(String subCommand, String[] parts) throws SQLException {
        switch (subCommand) {
            case "connect":
                handleDatabaseConnection(parts);
                break;
            case "save":
                handleSaveEntity(parts);
                break;
            case "load":
                handleLoadEntities(parts);
                break;
            case "force":
                handleForceUserLoad(parts);
                break;
            case "port":
                handleChangePort(parts);
                break;
            case "info":
                ConnectToPostgreSQL.displayInfo();
                break;
            case "clone":
                handleForceUserLoadAndConnect(parts);
                break;
            default:
                if (ConnectToPostgreSQL.isConnected()) {
                    String sqlCommand = String.join(" ", parts).substring(5); // "luna " kelimesini kaldır
                    ExecuteSQL.command(sqlCommand);
                } else {
                    System.out.println("Invalid Command or No Active Connection");
                }

        }
    }
}
