package org.cli.conn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cli.entities.SaveEntity;
import org.cli.utils.ConnectionPath;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.cli.conn.ConnectToPostgreSQL.*;

public class SaveEntityManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void savePerson(SaveEntity saveEntity) {

        List<SaveEntity>  connections = loadConnections();


        int newId = connections.size() + 1;
        saveEntity.setId("Person" + newId);

        // luna connect postgresql username:postgres password:postgres database:jpa
        // luna save username:postgres password:postgres database:jpa
        saveEntity.setUsername(saveEntity.getUsername());
        saveEntity.setPassword(saveEntity.getPassword());
        saveEntity.setDatabase(saveEntity.getDatabase());

        connections.add(saveEntity);
        saveConnections(connections);
        System.out.println("User saved: " + saveEntity.getId());
    }

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

    private static void saveConnections(List<SaveEntity> connections) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ConnectionPath.FILE_PATH), connections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAllPersons() {
        List<SaveEntity> connections = loadConnections();

        for (SaveEntity conn : connections) {
            conn.setUsername(conn.getUsername());
            conn.setPassword(conn.getPassword());
            conn.setDatabase(conn.getDatabase());
        }

        return connections.toString();
    }

    public static SaveEntity getPerson(String id) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SaveEntity> users = mapper.readValue(new File("connections.json"), mapper.getTypeFactory().constructCollectionType(List.class, SaveEntity.class));

            for (SaveEntity user : users) {
                System.out.println("Checking user: " + user.getId()); // Kullanıcıları kontrol et
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return null;
    }

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
