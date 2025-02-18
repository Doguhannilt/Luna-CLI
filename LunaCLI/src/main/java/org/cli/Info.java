package org.cli;

public class Info {
    static Info info = new Info();
    private String databaseName = "postgresql";

    private int PORT=5432;

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    // BASE_URL: jdbc:postgresql://localhost:5432/
    public static String getBaseUrl() {
        return "jdbc:" + info.getDatabaseName() + "://localhost:" + info.getPORT() + "/";
    }
}
