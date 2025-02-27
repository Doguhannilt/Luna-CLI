package QueriesTest;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.conn.postgresql.ConnectToPostgresql.connection;
import static org.cli.sql.postgresql.QueriesPostgresql.beginTransaction;
import static org.cli.sql.postgresql.QueriesPostgresql.commitTransaction;
import static org.cli.utils.TestConfig.*;

public class CommitTransactionFunctionTest {



    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    @AfterAll
    public static void tearDownAfterAll() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        }
        connection.setAutoCommit(true);

    }

    @Test
    public void beginTransactionAndGetValidMessageTest() throws SQLException {


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Let's suppose we are executing a query
        connection.setAutoCommit(false);

        commitTransaction();
        System.out.println(outputStream);

        Assertions.assertTrue(
                outputStream.toString().contains("Transaction committed successfully.")
        );
    }

    @Test
    public void beginTransactionThrowsExceptionWhenConnectionIsNull() {
        connection = null;

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            commitTransaction();
        });

        String expectedMessage = "Commit Error: Connection is null or closed.";
        Assertions.assertTrue(
                exception.getMessage().contains(expectedMessage),
                "Expected exception message: " + expectedMessage + ", but got: " + exception.getMessage()
        );
    }


    @Test
    public void beginTransactionThrowsExceptionWhenConnectionIsClosed() throws SQLException {

        connection.close();

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            beginTransaction();
        });

        String expectedMessage = "Transaction Error: Connection is null or closed.";
        Assertions.assertTrue(
                exception.getMessage().contains(expectedMessage),
                "Expected: " + expectedMessage + ", but got: " + exception.getMessage()
        );
    }

}
