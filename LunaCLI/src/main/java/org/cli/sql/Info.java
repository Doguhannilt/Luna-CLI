package org.cli.sql;

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

    public static String getBaseUrlForPostgresql() {
        return "jdbc:" + info.getDatabaseName() + "://localhost:" + info.getPORT() + "/";
    }

   // jdbc:mysql://localhost:3306/mydatabase

    public static String getBaseUrlForMysql() {
        return "jdbc:mysql://localhost:3306/";
    }

}
