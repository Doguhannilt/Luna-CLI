package QueriesTest;



import org.cli.conn.postgresql.ConnectToPostgresql;
import org.cli.sql.postgresql.ExecutePostgresql;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.cli.utils.TestConfig.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectFromFunctionTest {


    private static final String TEST_TABLE = "test_table";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void init() throws SQLException {
        ConnectToPostgresql.connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        ConnectToPostgresql.connection.setAutoCommit(false);

        try (Statement stmt = ConnectToPostgresql.connection.createStatement()) {
            stmt.execute("CREATE TABLE " + TEST_TABLE + " (id SERIAL PRIMARY KEY, name VARCHAR(100))");
        }
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        if (ConnectToPostgresql.connection != null) {
            try (Statement stmt = ConnectToPostgresql.connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS " + TEST_TABLE);
            }
            ConnectToPostgresql.connection.rollback();
            ConnectToPostgresql.connection.close();
        }
        System.setOut(originalOut);
    }

    @Test
    public void testSelectFromReturnsData() throws SQLException {
        System.setOut(new PrintStream(outContent));

        // Test verisi ekle
        try (Statement stmt = ConnectToPostgresql.connection.createStatement()) {
            stmt.execute("INSERT INTO " + TEST_TABLE + " (name) VALUES ('John Doe'), ('Jane Doe')");
        }

        // Metodu çağır
        ExecutePostgresql.selectFrom(TEST_TABLE, "");

        String output = outContent.toString();
        assertTrue(output.contains("John Doe") && output.contains("Jane Doe"), "Data selection should return valid results.");
    }

    @Test
    public void testSelectFromWithNoData() {
        System.setOut(new PrintStream(outContent));

        ExecutePostgresql.selectFrom(TEST_TABLE, "");

        String output = outContent.toString();
        assertTrue(output.contains("No data found in the table."), "No data message should be printed.");
    }
}

