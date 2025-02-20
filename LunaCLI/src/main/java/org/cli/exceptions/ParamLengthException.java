package org.cli.exceptions;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;

public class ParamLengthException extends Exception {
    public ParamLengthException() {
        super(INVALID_MESSAGE + "Parameters are not valid -> 'luna help'");
    }
}
