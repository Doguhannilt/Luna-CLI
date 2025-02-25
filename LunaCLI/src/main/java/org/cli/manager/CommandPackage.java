package org.cli.manager;

import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.entities.ConnectionEntity;
import org.cli.entities.SaveEntity;
import org.cli.sql.postgresql.ExecutePostgresql;

import java.sql.SQLException;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.sql.postgresql.ProcessCommandQueriesPostgresql.*;

public class CommandPackage {

    // luna connect postgresql username:postgres password:postgres database:managify
    // luna multiple (luna select-from users) (luna load users)


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
                ConnectToPostgresql.displayInfo();
                break;
            case "clone":
                handleForceUserLoadAndConnect(parts);
                break;
            case "schedule":
                handleSchedulerAndSchedule(parts);
                break;
            case "out":
                handleExportToCsv(parts);
                break;
            case "run":
                handleExecuteSqlFile(parts);
                break;
            case "multiple":
                handleMultipleQueries(parts);
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
