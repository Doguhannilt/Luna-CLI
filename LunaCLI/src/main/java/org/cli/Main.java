package org.cli;

import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ParamLengthException, HandleChangePortException, handleForceUserLoadAndConnectException {Start.commandStart();}
}