package org.cli.conn.postgresql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.cli.entities.SnippetEntity;
import org.cli.utils.SnippetPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.cli.sql.postgresql.ExecutePostgresql.command;

public class SnippetManagerPostgresql {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves a new snippet with the provided key and value.
     *
     * <p>This method creates a new snippet entity with the specified key and value,
     * assigns a new ID to the snippet based on the existing number of snippets,
     * and stores it in the list of snippets. The updated list of snippets is then
     * saved to a file.</p>
     *
     * @param key The unique key for the snippet.
     * @param value The value associated with the snippet.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * luna snippetc name:MyWoahSnippet command:luna select-from users
     * </pre>
     */
    public static void saveSnippet(String key,String value) {
        SnippetEntity snippetEntity = new SnippetEntity();
        List<SnippetEntity>  connections = loadConnections();


        int newId = connections.size() + 1;
        snippetEntity.setId(newId);
        snippetEntity.setKey(key);
        snippetEntity.setValue(value);
        connections.add(snippetEntity);
        saveConnections(connections);
    }

    /**
     * Retrieves and returns a list of all saved snippets.
     *
     * <p>This method loads all the snippets from the file, converts them to a list,
     * and returns the list as a string representation.</p>
     *
     * @return A string representation of all saved snippets.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * luna snippetl
     * </pre>
     */
    public static String getAllSnippets() {
        List<SnippetEntity> connections = loadConnections();
        System.out.println(connections);
        return connections.toString();
    }
    /**
     * Saves a list of snippets to the file.
     *
     * <p>This method takes a list of snippet entities and writes it to the file,
     * preserving the structure of the snippets. The snippets are saved in a pretty-printed JSON format.</p>
     *
     * @param snippetEntities The list of snippet entities to save.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * saveConnections(snippets);
     * </pre>
     */
    private static void saveConnections(List<SnippetEntity> snippetEntities) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(SnippetPath.FILE_PATH), snippetEntities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves a specific snippet by its ID.
     *
     * <p>This method loads all snippets from the file and iterates through the list to find
     * the snippet with the specified ID. If the snippet is found, it is returned;
     * otherwise, null is returned.</p>
     *
     * @param id The ID of the snippet to retrieve.
     * @return The snippet entity corresponding to the provided ID, or null if not found.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * SnippetEntity snippet = getSnippet("1");
     * luna snippetg id:1
     * </pre>
     */
    public static SnippetEntity getSnippet(String id) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            List<SnippetEntity> snippets = mapper.readValue(new File("snippets.json"), mapper.getTypeFactory().constructCollectionType(List.class, SnippetEntity.class));

            for (SnippetEntity snippetEntity : snippets) {
                System.out.println("Checking snippet: " + snippetEntity.getId());
                if (snippetEntity.getId() == Integer.parseInt(id)) {
                    return snippetEntity;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return null;
    }
    /**
     * Executes the snippet with the given ID.
     *
     * <p>This method first retrieves the snippet by its ID using the `getSnippet` method.
     * Then, it executes the command associated with the snippet by calling the `command` method.</p>
     *
     * @param id The ID of the snippet to execute.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * executeSnippet("1");
     * </pre>
     */
    public static void executeSnippet(String id) {
        try{
            SnippetEntity person = getSnippet(id);
            command(person.getValue());
            System.out.println("Successfully connected: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Loads all snippets from the file.
     *
     * <p>This method attempts to read the JSON file containing the snippets and deserializes
     * it into a list of snippet entities. If the file does not exist or an error occurs
     * during reading, an empty list is returned.</p>
     *
     * @return A list of all saved snippets.
     *
     * <h3>Example Usage:</h3>
     * <pre>
     * List<SnippetEntity> snippets = loadConnections();
     * for (SnippetEntity snippet : snippets) {
     *     System.out.println(snippet.getKey());
     * }
     * </pre>
     */
    public static List<SnippetEntity> loadConnections() {
        File file = new File(SnippetPath.FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<SnippetEntity>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
