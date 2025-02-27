package QueriesTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.cli.conn.postgresql.ConnectToPostgresql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.sql.postgresql.QueriesPostgresql.beginTransaction;
import static org.cli.utils.TestConfig.*;

public class BeginTransactionFunctionTest {


    @Test
    public void beginTransactionAndGetValidMessageTest() throws SQLException {
        connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        beginTransaction();

        Assertions.assertTrue(outputStream.toString().contains("Transaction started."));
    }

    @Test
    public void beginTransactionThrowsExceptionWhenConnectionIsNull() {
        connection = null;

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            beginTransaction();
        });

        String expectedMessage = "Transaction Error: Connection is null or closed.";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected exception message: " + expectedMessage + ", but got: " + exception.getMessage());
    }


    @Test
    public void beginTransactionThrowsExceptionWhenConnectionIsClosed() throws SQLException {
        connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        connection.close();

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            beginTransaction();
        });

        String expectedMessage = "Transaction Error: Connection is null or closed.";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected: " + expectedMessage + ", but got: " + exception.getMessage());
    }
}
