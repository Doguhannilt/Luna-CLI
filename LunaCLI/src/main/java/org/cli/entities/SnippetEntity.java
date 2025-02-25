package org.cli.entities;

public class SnippetEntity {
    private  String key;
    private  String value;
    private int id;
    @Override
    public String toString() {
        return "SnippetEntity{id=" + id + ", key='" + key + "', value='" + value + "'}";
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
