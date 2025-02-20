package org.cli.exceptions;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;

public class HandleChangePortException extends Exception{
    public HandleChangePortException() {
        super(INVALID_MESSAGE + "Invalid Port Number");
    }
}
