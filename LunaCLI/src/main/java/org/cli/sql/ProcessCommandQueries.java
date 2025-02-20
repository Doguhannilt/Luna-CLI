package org.cli.sql;


import org.cli.conn.ConnectToPostgreSQL;
import org.cli.entities.SaveEntity;
import org.cli.conn.SaveEntityManager;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.cli.managers.ProcessCommand.connectionEntity;
import static org.cli.managers.ProcessCommand.saveEntity;

public class ProcessCommandQueries   {
    /**
     * Handles database connection commands.
     *
     * @param parts The split command array.
     */
    public static void handleDatabaseConnection(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Invalid connection command format.");
            return;
        }

        String dbType = parts[2].toLowerCase();
        LinkedList<String> params = extractParameters(parts, 3);

        for (String param : params) {
            if (param.startsWith("username:")) {
                connectionEntity.setUsername(param.substring("username:".length()));
            } else if (param.startsWith("password:")) {
                connectionEntity.setPassword(param.substring("password:".length()));
            } else if (param.startsWith("database:")) {
                connectionEntity.setDatabase(param.substring("database:".length()));
            }
        }

        if (connectionEntity.getUsername() != null && connectionEntity.getDatabase() != null) {
            if ("postgresql".equals(dbType)) {
                ConnectToPostgreSQL.connectToDatabase(
                        connectionEntity.getUsername(),
                        connectionEntity.getPassword(),
                        connectionEntity.getDatabase()
                );
            } else {
                System.out.println("Unsupported Database: " + dbType);
            }
        } else {
            System.out.println("Invalid connection parameters.");
        }
    }

    /**
     * Handles saving an entity to the database.
     *
     * @param parts The split command array.
     */
    public static void handleSaveEntity(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Invalid save command format.");
            return;
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
            System.out.println("Invalid save parameters.");
        }
    }

    /**
     * Handles loading all users from the database.
     *
     * @param parts The split command array.
     */
    public static void handleLoadEntities(String[] parts) {
        if (parts.length >= 3 && "users".equalsIgnoreCase(parts[2])) {
            String persons = SaveEntityManager.getAllPersons();
            System.out.println(persons);
        } else {
            System.out.println("Invalid load command.");
        }
    }

    /**
     * Handles retrieving a specific user from the database.
     *
     * @param parts The split command array.
     */
    public static void handleForceUserLoad(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Invalid force command format.");
            return;
        }

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
            if (user != null) {
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found.");
            }
        } else {
            System.out.println("Invalid user retrieval parameters.");
        }
    }

    /**
     * Handles changing the database port.
     *
     * @param parts The split command array.
     */
    public static void handleChangePort(String[] parts) {
        if (parts.length != 2 || !parts[1].contains(":")) {
            System.out.println("Invalid port command format.");
            return;
        }

        try {
            int newPort = Integer.parseInt(parts[1].split(":")[1]);
            ConnectToPostgreSQL.changePort(newPort);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
        }
    }

    /**
     * Extracts parameters from a command starting at a given index.
     *
     * @param parts       The split command array.
     * @param startIndex  The starting index to extract parameters.
     * @return A list of parameters extracted from the command.
     */
    public static LinkedList<String> extractParameters(String[] parts, int startIndex) {
        LinkedList<String> params = new LinkedList<>();
        for (int i = startIndex; i < parts.length; i++) {
            params.add(parts[i]);
        }
        return params;
    }

    public static void handleForceUserLoadAndConnect(String[] parts) throws SQLException {
        if (parts.length < 3) {
            System.out.println("Invalid force command format.");
            return;
        }

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
        } else {
            System.out.println("Invalid User Clone");
        }
    }
}
