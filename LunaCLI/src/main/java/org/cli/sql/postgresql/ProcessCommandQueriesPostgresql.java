package org.cli.sql.postgresql;


import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.entities.SaveEntity;
import org.cli.conn.postgresql.SaveEntityManagerPostgresql;
import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;
import static org.cli.manager.CommandPackage.connectionEntity;
import static org.cli.manager.CommandPackage.saveEntity;
import static org.cli.sql.postgresql.QueriesPostgresql.scheduleWithCommand;

public class ProcessCommandQueriesPostgresql {
    public static String dbType;

    /**
     * Handles database connection commands.
     *
     * @param parts The split command array.
     */
    public static void handleDatabaseConnection(String[] parts) {
        try {
            if (parts.length < 4) {throw new ParamLengthException();}

            dbType = parts[2].toLowerCase();

            LinkedList<String> params = extractParameters(parts, 3);

            for (String param : params) {
                if (param.startsWith("username:"))
                { connectionEntity.setUsername(param.substring("username:".length()));
                } else if (param.startsWith("password:")) {connectionEntity.setPassword(param.substring("password:".length()));
                } else if (param.startsWith("database:")) {connectionEntity.setDatabase(param.substring("database:".length()));}
            }

            if (connectionEntity.getUsername() != null && connectionEntity.getPassword() != null && connectionEntity.getDatabase() != null) {
                if ("postgresql".equals(dbType)) {ConnectToPostgresql.connectToDatabase(connectionEntity.getUsername(), connectionEntity.getPassword(), connectionEntity.getDatabase());}
                else { System.out.println(INVALID_MESSAGE + "Unsupported Database: " + dbType);}
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
                SaveEntityManagerPostgresql.savePerson(saveEntity);
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
                String persons = SaveEntityManagerPostgresql.getAllPersons();
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
                SaveEntity user = SaveEntityManagerPostgresql.getPerson(entity.getId());
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

            ConnectToPostgresql.changePort(newPort);

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
     * <h1>Connect your saved clone entity.</h1>
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
                ConnectToPostgresql.connection.close();
                SaveEntityManagerPostgresql.cloneUser(entity.getId());
            }
            else {throw new handleForceUserLoadAndConnectException(connectionEntity.getDatabase());}
        } catch (ParamLengthException | handleForceUserLoadAndConnectException | SQLException e) {System.err.println(INVALID_MESSAGE + "Error: " + e.getMessage());
        } catch (Exception e) { System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }


    /**
     * <h1>Schedule Your Command</h1>
     * <p>
     * This method schedules a command to be executed after a specified delay.
     * </p>
     *
     * <h2>Example Command Line Input:</h2>
     * <pre>
     * {@code
     * luna schedule command:insert-into users name:John age:25 city:NewYork delay:10 unit:1
     * }
     * </pre>
     *
     * <h2>Explanations:</h2>
     * <ol>
     *   <li><b>{@code command:}</b> - Specifies the start of the command. In this example, the part {@code insert-into users name:John age:25 city:NewYork} will be treated as the command.</li>
     *   <li><b>{@code delay:}</b> - Specifies the delay before the command is executed. In this example, the delay is {@code 10} units.</li>
     *   <li><b>{@code unit:}</b> - Specifies the unit of the delay. In this example, the unit is {@code 1}.</li>
     * </ol>
     *
     * <h2>How It Works:</h2>
     * <ul>
     *   <li>The method extracts everything after {@code command:} until it encounters {@code delay:} and treats it as the command.</li>
     *   <li>The {@code delay:} and {@code unit:} values are extracted separately.</li>
     *   <li>The command, delay, and unit are then passed to the {@code scheduleWithCommand} method for execution.</li>
     * </ul>
     *
     * <h2>Expected Output:</h2>
     * <pre>
     * {@code
     * Executing scheduled command: insert-into users name:John age:25 city:NewYork
     * }
     * </pre>
     *
     * <p>
     * This command will be executed after a delay of {@code 10} units (as specified by {@code delay:10 unit:1}).
     * </p>
     *
     * <h2>Flexibility:</h2>
     * <p>
     * This approach allows you to schedule any type of command, not just {@code select}. For example:
     * </p>
     * <ul>
     *   <li>{@code command:delete-from users where age > 30 delay:5 unit:1}</li>
     *   <li>{@code command:update users set city=LosAngeles where name=John delay:15 unit:2}</li>
     * </ul>
     */
    public static void HandleSchedulerAndSchedule(String[] parts) {
        try {
            if (parts.length < 4) {throw new ParamLengthException();}

            StringBuilder commandBuilder = new StringBuilder();
            int delay = 0;
            int unit = 0;
            boolean isCommand = false;

            for (String param : parts) {
                if (param.startsWith("command:")) {
                    isCommand = true;
                    commandBuilder.append(param.substring(8)).append(" ");
                } else if (param.startsWith("delay:")) {
                    delay = Integer.parseInt(param.substring(6));
                    isCommand = false;
                } else if (param.startsWith("unit:")) {
                    unit = Integer.parseInt(param.substring(5));
                    isCommand = false;
                } else if (isCommand) {
                    commandBuilder.append(param).append(" ");
                }
            }

            String command = commandBuilder.toString().trim();

            if (command.isEmpty() || delay == 0 || unit == 0) {throw new ParamLengthException();}

            scheduleWithCommand(command, delay, unit);
        }

        catch (ParamLengthException e) {throw new RuntimeException(e);}
        catch (Exception e) {System.err.println(INVALID_MESSAGE + "Unexpected Error: " + e.getMessage());}
    }
}