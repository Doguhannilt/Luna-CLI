package org.cli;


import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;
import org.cli.manager.CommandPackage;

import java.sql.SQLException;
import java.util.Scanner;

import static org.cli.utils.Colors.*;

public class Start {
    public static void commandStart() throws SQLException, ParamLengthException, HandleChangePortException, handleForceUserLoadAndConnectException {


        Scanner scanner = new Scanner(System.in);


        System.out.println("---------------------");
        System.out.println("Luna CLI" );
        System.out.println(RED + ":qa! - EXIT" + RESET);
        System.out.println("---------------------");

        while (true) {
            System.out.print(BLUE + "luna> " + RESET);  
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(":qa!")) {
                break;
            }
            CommandPackage.command(input);
        }

        System.out.println("---------------------");
        scanner.close();
    }
}
