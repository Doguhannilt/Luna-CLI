package org.cli;

import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        new Thread(() -> {
            try {
                org.cli.prometheus.MetricsServer.main(new String[]{});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Start.commandStart();
    }
}