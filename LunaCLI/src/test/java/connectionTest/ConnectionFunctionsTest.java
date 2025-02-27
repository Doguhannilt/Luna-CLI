package connectionTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.cli.utils.TestConfig.*;
import static org.cli.conn.postgresql.ConnectToPostgresql.*;
import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;
import static org.cli.exceptions.CustomMessages.VALID_MESSAGE;

public class ConnectionFunctionsTest {

    @Test
    public void connectionToDatabaseAndGetValidMessage() throws SQLException {

        String username = "postgres";
        String password = "postgres";
        String database = "managify";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        connectToDatabase(username,password,database);

        System.out.println("Actual Console Output: " + outContent.toString());

        String expectedMessage = VALID_MESSAGE + "Connected to database: PostgreSQL";
        Assertions.assertTrue(outContent.toString().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + outContent.toString());
    }

    @Test
    public void connectionToDatabaseTwiceAndGetInvalidMessage() {


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            connectToDatabase(TEST_USERNAME, TEST_PASSWORD, TEST_DATABASE);
            connectToDatabase(TEST_USERNAME, TEST_PASSWORD, TEST_DATABASE);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();

        String expectedMessage = INVALID_MESSAGE + "Already connected to: managify";
        Assertions.assertTrue(output.contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + output);
    }


    @Test
    public void giveInvalidDatabaseName() {
        String username = "postgres";
        String password = "postgres";
        String database = "invalidDatabaseName";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        connectToDatabase(username, password, database);

        String expectedMessage = INVALID_MESSAGE + "Connection Error: FATAL: database \"" + "invalidDatabaseName" + "\" does not exist";
        Assertions.assertTrue(outContent.toString().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + outContent);
    }

    @Test
    public void closeConnectionAndGetValidMessage() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/managify", "postgres", "postgres"); // Geçerli bir bağlantı oluştur.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        closeConnection();
        System.out.println(outContent.toString());
        String expectedMessage = VALID_MESSAGE + "Database connection closed.";
        Assertions.assertTrue(outContent.toString().contains(expectedMessage));
    }

    @Test
    public void DontGetValidMessageForTryingCloseConnectionBeforeConnectToDatabase(){
        connection = null;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        closeConnection();

        String expectedMessage = VALID_MESSAGE + "Database connection closed.";
        Assertions.assertFalse(outContent.toString().contains(expectedMessage),
                "Unexpected message found! Got: '" + outContent.toString().trim() + "'");
    }

    @Test
    public void createConnectionAndGetTrue() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/managify", "postgres", "postgres"); // Geçerli bir bağlantı oluştur.

        boolean connected = isConnected();

        Assertions.assertTrue(connected);
    }

    @Test
    public void createConnectionAndGetFalse() {
        connection = null;

        boolean connected = isConnected();

        Assertions.assertFalse(connected);
    }
}

