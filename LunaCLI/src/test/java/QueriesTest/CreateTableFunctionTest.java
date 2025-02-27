package QueriesTest;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.cli.conn.postgresql.ConnectToPostgresql.connection;
import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.sql.postgresql.QueriesPostgresql.createTable;

public class CreateTableFunctionTest {

    String URL = "jdbc:postgresql://localhost:5432/managify";
    String USER = "postgres";
    String PASSWORD = "postgres";
    static final String TEST_TABLE = "test_table";

    @BeforeEach
    public void init() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS " + TEST_TABLE);
            }
            connection.rollback();
            connection.close();
        }
    }

    @Test
    public void testCreateTableSuccessfully() throws SQLException {

        createTable(TEST_TABLE, "id SERIAL PRIMARY KEY, name VARCHAR(100)");


        boolean tableExists = checkIfTableExists(TEST_TABLE);
        Assertions.assertTrue(tableExists, "Table should be created successfully.");
    }


    @Test
    public void testCreateTableWithInvalidSQLThrowsException() throws SQLException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            createTable(TEST_TABLE, "INVALID SQL");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
        }

        String output = outContent.toString();
        System.out.println(output);
        Assertions.assertTrue(output.contains(INVALID_MESSAGE + "ERROR"),
                "Beklenen hata mesajı yazdırılmadı: " + output);
    }
    
    private boolean checkIfTableExists(String tableName) throws SQLException {
        String query = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = '" + tableName + "')";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }
        return false;
    }
}
