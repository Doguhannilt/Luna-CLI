package QueriesTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.cli.conn.postgresql.ConnectToPostgresql.connection;
import static org.cli.sql.postgresql.QueriesPostgresql.*;
import static org.cli.utils.TestConfig.*;

public class DropTableFunctionTest {


    static final String TEST_TABLE = "test_table";

    @BeforeEach
    public void init() throws SQLException {
        connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        connection.setAutoCommit(false);

        createTable(TEST_TABLE, "id SERIAL PRIMARY KEY, name VARCHAR(100)");
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS " + TEST_TABLE);
            }
            connection.close();
        }
    }

    @Test
    public void testdropTableSuccessfully() throws SQLException {
        dropTable(TEST_TABLE);

        boolean tableExists = checkIfTableExists(TEST_TABLE);
        Assertions.assertFalse(tableExists, "Table has been dropped");
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
