package org.cli.sql.postgresql;


import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.entities.SaveEntity;
import org.cli.conn.postgresql.SaveEntityManagerPostgresql;
import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.cli.conn.postgresql.SnippetManagerPostgresql.executeSnippet;
import static org.cli.conn.postgresql.SnippetManagerPostgresql.saveSnippet;
import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;
import static org.cli.manager.CommandPackage.*;
import static org.cli.sql.postgresql.QueriesPostgresql.*;
import static org.cli.utils.ExportPath.filePath;

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
    public static void handleGetAllUsers(String[] parts) {
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
    public static void handleDisplayUsers(String[] parts) {
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
    public static void handleGetUserIdAndConnect(String[] parts) {
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
    public static void handleSchedulerAndSchedule(String[] parts) {
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
    /**
     * <h1>Export Your Data</h1>
     * <p>
     * This method export your data by given query.
     * </p>
     *
     * <h2>Example Command Line Input:</h2>
     * <pre>
     * {@code
     * luna export command:select * from user export:data.csv
     * }
     * </pre>
     *
     * <h2>Explanations:</h2>
     * <ol>
     *   <li><b>{@code command:}</b> - Specifies the start of the command. In this example, the part {@code select * from user} will be treated as the command.</li>
     *   <li><b>{@code export:}</b> - Specifies the location before the command is executed. In this example, the location that your '.csv' file saved is {@code C:\Users\(?)\fileName_data.csv}</li>
     * </ol>
     *
     *
     * <h2>Expected Output:</h2>
     * <pre>
     * {@code
     * Data successfully exported to C:\Users\yourFolderName/exported_data.csv
     * }
     * </pre>
     */
    public static void handleExportToCsv(String[] parts) {
        try {
            if (parts.length < 3) {throw new ParamLengthException();}
            StringBuilder commandBuilder = new StringBuilder();
            String export = null;
            boolean isCommand = false;



            for (String param : parts) {
                if (param.startsWith("command:")) {
                    isCommand = true;
                    commandBuilder.append(param.substring(8)).append(" ");
                }
                else if (param.startsWith("export:")) {
                    export = param.substring(7);
                    isCommand = false;
                }
                else if (isCommand) { commandBuilder.append(param).append(" ");}
            }

            String getCommand = commandBuilder.toString().trim();
            if (getCommand.isEmpty() || export.isEmpty()) {throw new ParamLengthException();}

            System.out.println("Export: " + export);
            System.out.println("Commands: " + getCommand);

            /*
            * ExportPath.java
            * public static String documentsPath = System.getProperty("user.home");
            * public static String filePath = documentsPath;
            * */

            String filePathGivenByUser = filePath + export;
            exportToCSV(getCommand, filePathGivenByUser);
        }

        catch (ParamLengthException e) {throw new RuntimeException(e);}
    }

    /**
     * Handles the execution of an SQL file.
     * <p>
     * This method extracts the file path from the given parameters and executes the SQL commands in the file.
     * It validates the parameters, checks for the existence of the file, and then calls {@code executeSqlFile}.
     * </p>
     *
     * <h3>Usage Example:</h3>
     * <pre>
     * String[] parts = {"luna", "run", "filepath:/path/to/sqlfile.sql"};
     * handleExecuteSqlFile(parts);
     * </pre>
     *
     * @param parts An array of command-line arguments where one of the elements should be in the format {@code filepath:<path-to-file>}.
     *
     * @throws ParamLengthException If the provided parameters are insufficient.
     * @throws IllegalArgumentException If the file path is missing or empty.
     * @throws FileNotFoundException If the specified SQL file does not exist.
     * @throws SQLException If an error occurs while executing the SQL file.
     */
    public static void handleExecuteSqlFile(String[] parts) {
        String filePathForExecute = null;
        try {
            if (parts.length < 3) {
                throw new ParamLengthException();
            }

            for (String param : parts) {
                if (param.startsWith("filepath:")) {
                    filePathForExecute = param.substring("filepath:".length());
                }
            }


            if (filePathForExecute == null || filePathForExecute.isEmpty()) {
                throw new IllegalArgumentException("Error: File path is missing. Use 'filepath:<path-to-file>' format.");
            }


            File file = new File(filePathForExecute);
            if (!file.exists() || !file.isFile()) {
                throw new FileNotFoundException("Error: SQL file not found at path: " + filePathForExecute);
            }

            executeSqlFile(filePathForExecute);
        } catch (ParamLengthException | SQLException | FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles multiple SQL queries by parsing a string array and extracting commands enclosed in parentheses.
     * <p>
     * This method processes the provided string array, extracts queries surrounded by parentheses, and executes each query.
     * It validates the length of the input array and performs regex matching to find and trim the queries. If valid queries
     * are found, they are executed in sequence.
     * </p>
     *
     * @param parts An array of strings containing the input data, where each string can contain a command and other arguments.
     *              The queries to be executed should be enclosed in parentheses, and the method expects at least 2 elements in the input array.
     *
     * @throws ParamLengthException if the length of the input array is less than 2.
     * @throws RuntimeException if there is a problem during query execution.
     * @throws SQLException if an error occurs during the SQL query execution.
     *
     * @see Pattern
     * @see Matcher
     */
    public static void handleMultipleQueries(String[] parts) {
        try {
            if (parts.length < 2) {
                throw new ParamLengthException();
            }

            ArrayList<String> queryList = new ArrayList<>();

            String input = String.join(" ", parts);


            Pattern pattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = pattern.matcher(input);


            while (matcher.find()) {
                String query = matcher.group(1).trim();
                if (!query.isEmpty()) {
                    queryList.add(query);
                }
            }


            if (!queryList.isEmpty()) {
                for (String query : queryList) {
                    System.out.println("Executing SQL: " + query);
                    command(query);
                }
            }

        } catch (ParamLengthException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Handles the creation of a new snippet by extracting the necessary information
     * (command and name) from the given input parameters and storing it in a HashMap.
     * The snippet is then saved to persistent storage.
     *
     * <p>This method processes an array of strings, where it looks for parameters
     * prefixed with "name:" for the snippet name and "command:" for the corresponding command.
     * After extracting these details, the method stores the snippet in a HashMap and
     * saves it to storage using the `saveSnippet` method.</p>
     *
     * @param parts An array of strings containing the command and parameters.
     *              The array is expected to contain at least the snippet type ("create"),
     *              the name of the snippet, and the command associated with it.
     *
     * @throws RuntimeException If an error occurs during processing or saving the snippet.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * String[] parts = {"luna", "snippet", "create", "name:select", "command:SELECT * FROM users"};
     * handleSaveSnippetQuery(parts);
     * </pre>
     * <h3>Parameters:</h3>
     * <ul>
     *   <li><strong>parts:</strong> The array containing the command parameters,
     *   where the snippet name and command are extracted.</li>
     * </ul>
     */
    public static void handleSaveSnippetQuery(String[] parts) {
        String commandSnippet = "";
        String snippetName = "";
        String key = "";
        String value = "";

        try {
            System.out.println(parts[2]);
            if (parts.length >= 3 && "create".equalsIgnoreCase(parts[2])) {
                HashMap<String, String> hashMap = new HashMap<>();

                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].startsWith("command:")) {

                        commandSnippet = parts[i].substring("command:".length());
                        while (++i < parts.length) {
                            commandSnippet += " " + parts[i];
                        }
                        commandSnippet = commandSnippet.trim();
                    } else if (parts[i].startsWith("name:")) {
                        snippetName = parts[i].substring("name:".length()).trim();
                    }
                }

                if (!snippetName.isEmpty()) {hashMap.put(snippetName, commandSnippet);}

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                }
                saveSnippet(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Executes a snippet based on its unique ID, which is extracted from the provided input parameters.
     * This method processes the command input, finds the snippet ID, and calls the `executeSnippet`
     * method to execute the corresponding snippet.
     *
     * <p>This method extracts the parameters from the input, looks for the "id:" prefix,
     * and uses the ID to identify the snippet to execute.</p>
     *
     * @param parts An array of strings containing the command and parameters.
     *              The array must contain an "id:" parameter to specify the snippet to execute.
     *
     * @throws RuntimeException If an error occurs during processing or snippet execution.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * String[] parts = {"luna", "snippet", "execute", "id:1"};
     * handleExecuteSnippetById(parts);
     * </pre>
     * <h3>Parameters:</h3>
     * <ul>
     *   <li><strong>parts:</strong> The array containing the command parameters,
     *   including the snippet ID to execute.</li>
     * </ul>
     */
    public static void handleExecuteSnippetById(String[] parts) {
        LinkedList<String> saveParams = extractParameters(parts, 2);
        for (String param : saveParams) {
            if (param.startsWith("id:")) {
                String snippetId = param.substring("id:".length());
         executeSnippet(snippetId);
            }
        }
    }
}