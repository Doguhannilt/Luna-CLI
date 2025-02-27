package QueriesTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.cli.conn.postgresql.ConnectToPostgresql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.sql.postgresql.QueriesPostgresql.beginTransaction;

public class BeginTransactionFunctionTest {


    @Test
    public void beginTransactionAndGetValidMessageTest() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/managify", "postgres", "postgres");
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
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/managify", "postgres", "postgres");
        connection.close();

        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            beginTransaction();
        });

        String expectedMessage = "Transaction Error: Connection is null or closed.";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected: " + expectedMessage + ", but got: " + exception.getMessage());
    }
}
