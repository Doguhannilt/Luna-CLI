package org.cli.conn.postgresql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cli.entities.SaveEntity;
import org.cli.utils.ConnectionPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.cli.conn.postgresql.ConnectToPostgresql.*;

public class SaveEntityManagerPostgresql {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves a {@link SaveEntity} (Person) to the connections list and assigns a unique ID to the entity.
     * <p>
     * This method generates a new ID for the person being saved, based on the size of the existing connections list.
     * It then sets the username, password, and database properties from the provided {@link SaveEntity} object and
     * saves it to the list of connections. The list of connections is then persisted by calling {@link #saveConnections(List)}.
     * </p>
     * <p>
     * If the save operation is successful, a confirmation message is printed with the saved person's ID.
     * </p>
     *
     * @param saveEntity The {@link SaveEntity} object to save, which contains the person's details such as username, password, and database.
     */
    public static void savePerson(SaveEntity saveEntity) {

        List<SaveEntity>  connections = loadConnections();

        int newId = connections.size() + 1;
        saveEntity.setId("Person" + newId);

        saveEntity.setUsername(saveEntity.getUsername());
        saveEntity.setPassword(saveEntity.getPassword());
        saveEntity.setDatabase(saveEntity.getDatabase());

        connections.add(saveEntity);
        saveConnections(connections);
        System.out.println("User saved: " + saveEntity.getId());
    }

    /**
     * Loads the list of connections from the file specified by the {@link ConnectionPath#FILE_PATH}.
     * <p>
     * This method reads the JSON file that contains the saved connections and deserializes it into a list of {@link SaveEntity}.
     * If the file does not exist, it returns an empty list. If an error occurs during file reading or deserialization,
     * an empty list is returned as a fallback.
     * </p>
     *
     * @return A list of {@link SaveEntity} representing all saved connections, or an empty list if an error occurs or the file doesn't exist.
     */
    public static List<SaveEntity> loadConnections() {
        File file = new File(ConnectionPath.FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<SaveEntity>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Saves the list of connections to the file specified by the {@link ConnectionPath#FILE_PATH}.
     * <p>
     * This method serializes the provided list of {@link SaveEntity} objects and writes them to a JSON file. The data is written
     * with the default pretty printer to ensure a readable format. If an error occurs during the file writing process, the error is
     * printed to the console.
     * </p>
     *
     * @param connections A list of {@link SaveEntity} representing the connections to be saved.
     */
    private static void saveConnections(List<SaveEntity> connections) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ConnectionPath.FILE_PATH), connections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a string representation of all saved persons (connections).
     * <p>
     * This method loads all connections from the file using {@link #loadConnections()} and iterates over the list,
     * printing each person's details. It then returns the string representation of the entire list.
     * </p>
     * <p>
     * If an error occurs while loading the connections, an empty list is returned.
     * </p>
     *
     * @return A string representing all saved persons (connections). If there is an error loading the data, an empty list is returned.
     */
    public static String getAllPersons() {
        List<SaveEntity> connections = loadConnections();

        for (SaveEntity conn : connections) {
            conn.setUsername(conn.getUsername());
            conn.setPassword(conn.getPassword());
            conn.setDatabase(conn.getDatabase());
        }

        return connections.toString();
    }

    /**
     * Retrieves a person (SaveEntity) by their unique ID.
     * <p>
     * This method loads the list of all saved persons from the JSON file, iterates through them, and returns the person
     * that matches the given ID. If the person is not found or if an error occurs during file reading, the method returns null.
     * </p>
     *
     * @param id The unique ID of the person to retrieve.
     * @return The {@link SaveEntity} representing the person with the specified ID, or null if the person is not found.
     */
    public static SaveEntity getPerson(String id) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SaveEntity> users = mapper.readValue(new File("connections.json"), mapper.getTypeFactory().constructCollectionType(List.class, SaveEntity.class));

            for (SaveEntity user : users) {
                System.out.println("Checking user: " + user.getId());
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Clones a user by establishing a connection to their respective database.
     * <p>
     * This method retrieves a person (user) using their ID by calling {@link #getPerson(String)}. Once the user is found,
     * it attempts to connect to the specified database using the stored username, password, and database information from
     * the {@link SaveEntity}. If the connection is successful, a confirmation message is printed. Otherwise, the error message is shown.
     * </p>
     *
     * @param personId The unique ID of the person to clone.
     */
    public static void cloneUser(String personId) {
        try{
            SaveEntity person = getPerson(personId);
            connectToDatabase(person.getUsername(), person.getPassword(), person.getDatabase());
            System.out.println("Successfully connected: " + personId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
