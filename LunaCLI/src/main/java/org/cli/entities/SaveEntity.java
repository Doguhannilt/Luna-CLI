package org.cli.entities;

public class SaveEntity {

    private String id;
    private String username;
    private String password;
    private String database;

    public SaveEntity() {
    }

    public SaveEntity(String id, String username, String password, String database) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public String toString() {
        return "SaveEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
