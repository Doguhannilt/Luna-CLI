package org.cli.exceptions;

import static org.cli.exceptions.CustomMessages.INVALID_MESSAGE;

public class handleForceUserLoadAndConnectException extends Exception {
    public handleForceUserLoadAndConnectException(String error) {
        super(INVALID_MESSAGE + "Connection Issue ->" + error);
    }
}
