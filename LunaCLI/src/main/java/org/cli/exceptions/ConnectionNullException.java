package org.cli.exceptions;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;

public class ConnectionNullException extends Exception{
    public ConnectionNullException() {
        super(INVALID_MESSAGE + "No active database connection. Please connect first.");
    }
}
