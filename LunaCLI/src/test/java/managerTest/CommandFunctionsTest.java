package managerTest;


import org.cli.manager.CommandPackage;
import org.cli.sql.postgresql.ExecutePostgresql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class CommandFunctionsTest {



    @Test
    public void getArrayIndexOutOfBoundsExceptionIfPartsLengthLowerThanTwo() throws SQLException {
        Exception exception = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CommandPackage.command("luna");
        });

        String expectedMessage = "Index 1 out of bounds for length 1";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void shouldThrowNullPointerExceptionIfCommandExecutedBeforeConnection() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            ExecutePostgresql.command("testCommand");
        });

        String expectedMessage = "Cannot invoke \"java.sql.Connection.createStatement()\" because \"org.cli.conn.postgresql.ConnectToPostgresql.connection\" is null";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + exception.getMessage());
    }



}
