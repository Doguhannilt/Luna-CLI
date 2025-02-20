package org.cli.sql;

import org.cli.conn.ConnectToPostgreSQL;
import org.cli.entities.SaveEntity;
import org.cli.conn.SaveEntityManager;
import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;
import static org.cli.managers.ProcessCommand.connectionEntity;
import static org.cli.managers.ProcessCommand.saveEntity;

public class ProcessCommandQueries {

    /**
     * Handles database connection commands.
     *
     * @param parts The split command array.
     */
    public static void handleDatabaseConnection(String[] parts) {
        try {
            if (parts.length < 4) {throw new ParamLengthException();}

            String dbType = parts[2].toLowerCase();
            LinkedList<String> params = extractParameters(parts, 3);

            for (String param : params) {
                if (param.startsWith("username:"))
                { connectionEntity.setUsername(param.substring("username:".length()));
                } else if (param.startsWith("password:")) {connectionEntity.setPassword(param.substring("password:".length()));
                } else if (param.startsWith("database:")) {connectionEntity.setDatabase(param.substring("database:".length()));}
            }

            if (connectionEntity.getUsername() != null || connectionEntity.getDatabase() != null) {
                /*
                * PostgresSQL Connection
                */
                if ("postgresql".equals(dbType)) {ConnectToPostgreSQL.connectToDatabase(connectionEntity.getUsername(),connectionEntity.getPassword(),connectionEntity.getDatabase());

            } else {System.out.println(INVALID_MESSAGE + "Unsupported Database: " + dbType);}
            } else {System.out.println(INVALID_MESSAGE + "Connection Failed");}
        } catch (ParamLengthException e) {System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) {System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }

    /**
     * Handles saving an entity to the database.
     *
     * @param parts The split command array.
     */
    public static void handleSaveEntity(String[] parts) {
        try {
            if (parts.length < 4) {
                throw new ParamLengthException();
            }

            LinkedList<String> saveParams = extractParameters(parts, 2);

            for (String param : saveParams) {
                if (param.startsWith("username:")) {
                    saveEntity.setUsername(param.substring("username:".length()));
                } else if (param.startsWith("password:")) {
                    saveEntity.setPassword(param.substring("password:".length()));
                } else if (param.startsWith("database:")) {
                    saveEntity.setDatabase(param.substring("database:".length()));
                }
            }

            if (connectionEntity.getUsername() != null && connectionEntity.getDatabase() != null) {
                SaveEntityManager.savePerson(saveEntity);
            } else {
                System.out.println(INVALID_MESSAGE + "Invalid save parameters.");
            }
        } catch (ParamLengthException e) {
            System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Handles loading all users from the database.
     *
     * @param parts The split command array.
     */
    public static void handleLoadEntities(String[] parts) {
        try {
            if (parts.length >= 3 && "users".equalsIgnoreCase(parts[2])) {
                String persons = SaveEntityManager.getAllPersons();
                System.out.println(persons);
            } else {
                throw new ParamLengthException();
            }
        } catch (ParamLengthException e) {System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) {System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }

    /**
     * Handles retrieving a specific user from the database.
     *
     * @param parts The split command array.
     */
    public static void handleForceUserLoad(String[] parts) {
        try {
            if (parts.length < 3) {throw new ParamLengthException();}

            SaveEntity entity = new SaveEntity();
            LinkedList<String> saveParams = extractParameters(parts, 2);

            for (String param : saveParams) {
                if (param.startsWith("user:")) {
                    String userId = param.substring("user:".length());
                    if (userId.startsWith("\"") && userId.endsWith("\"")) {
                        userId = userId.substring(1, userId.length() - 1);
                    }
                    entity.setId(userId);
                }
            }

            if (connectionEntity.getUsername() != null && connectionEntity.getDatabase() != null) {
                SaveEntity user = SaveEntityManager.getPerson(entity.getId());
                if (user != null) {System.out.println(VALID_MESSAGE + "User found: " + user);}
                else {System.out.println(INVALID_MESSAGE + "User not found.");}
            }
            else {System.out.println(INVALID_MESSAGE + "Invalid user retrieval parameters.");}
        } catch (ParamLengthException e) {System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) {System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }

    /**
     * Handles changing the database port.
     *
     * @param parts The split command array.
     */
    public static void handleChangePort(String[] parts) {
        try {
            if (parts.length != 2 || !parts[1].contains(":")) {throw new ParamLengthException();}

            int newPort = Integer.parseInt(parts[1].split(":")[1]);
            if (newPort < 1024 || newPort > 65535) {throw new HandleChangePortException();}

            ConnectToPostgreSQL.changePort(newPort);

        } catch (ParamLengthException | HandleChangePortException e) { System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) { System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }

    /**
     * Extracts parameters from a command starting at a given index.
     *
     * @param parts       The split command array.
     * @param startIndex  The starting index to extract parameters.
     * @return A list of parameters extracted from the command.
     */
    public static LinkedList<String> extractParameters(String[] parts, int startIndex) {
        try {
            LinkedList<String> params = new LinkedList<>();
            for (int i = startIndex; i < parts.length; i++) {
                params.add(parts[i]);
            }
            return params;
        } catch (Exception e) {
            System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * Connect your saved clone entity.
     *
     * @param parts The split command array.
     */
    public static void handleForceUserLoadAndConnect(String[] parts) {
        try {
            if (parts.length < 3) {throw new ParamLengthException();}

            SaveEntity entity = new SaveEntity();
            LinkedList<String> saveParams = extractParameters(parts, 2);

            for (String param : saveParams) {
                if (param.startsWith("user:")) {
                    String userId = param.substring("user:".length());
                    if (userId.startsWith("\"") && userId.endsWith("\"")) {
                        userId = userId.substring(1, userId.length() - 1);
                    }
                    entity.setId(userId);
                }
            }

            if (connectionEntity.getUsername() != null && connectionEntity.getDatabase() != null) {
                ConnectToPostgreSQL.connection.close();
                SaveEntityManager.cloneUser(entity.getId());
            }
            else {throw new handleForceUserLoadAndConnectException(connectionEntity.getDatabase());}
        } catch (ParamLengthException | handleForceUserLoadAndConnectException | SQLException e) {System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) { System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }
}