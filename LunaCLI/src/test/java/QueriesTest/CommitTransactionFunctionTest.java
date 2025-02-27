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

public class CommitTransactionFunctionTest {

    String URL = "jdbc:postgresql://localhost:5432/managify";
    String USER = "postgres";
    String PASSWORD = "postgres";


    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @AfterAll
    public static void tearDownAfterAll() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/managify", "postgres", "postgres");
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
