package QueriesTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.cli.conn.postgresql.ConnectToPostgresql.connection;
import static org.cli.sql.postgresql.QueriesPostgresql.exportToCSV;
import static org.cli.utils.Colors.GREEN;
import static org.cli.utils.Colors.RESET;
import static org.cli.utils.TestConfig.*;

public class ExportToCsvFunctionTest {


    static final String command = "SELECT * FROM users";
    static final String filePath = "data.csv";
    static final String invalid_command = "SEELEEECT * FROM users";

    @BeforeEach
    public void init() throws SQLException {
        connection = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
            connection.close();
    }

    @Test
    public void testExportToCsvSuccessfully() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        exportToCSV(command,filePath);

        Assertions.assertTrue(
                outputStream.toString().contains(GREEN + "Data successfully exported to " + filePath + RESET)
        );
    }

    @Test
    public void testExportToCsvWithInvalidCommand() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,() -> {exportToCSV(invalid_command,filePath);}
        );

        String expectedMessage = "ERROR: syntax error at or near \"SEELEEECT\"";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + exception.getMessage());
    }
}
