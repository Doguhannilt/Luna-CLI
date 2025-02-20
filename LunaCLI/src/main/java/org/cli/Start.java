package org.cli;


import org.cli.managers.ProcessCommand;

import java.sql.SQLException;
import java.util.Scanner;

import static org.cli.utils.Colors.*;

public class Start {
    public static void commandStart() throws SQLException {


        Scanner scanner = new Scanner(System.in);


        System.out.println("---------------------");
        System.out.println(GREEN + "Luna CLI" + RESET);
        System.out.println(RED + ":qa! - EXIT" + RESET);
        System.out.println("---------------------");

        while (true) {
            System.out.print(BLUE + "luna> " + RESET);  
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(":qa!")) {
                break;
            }
            ProcessCommand.command(input);
        }

        System.out.println("---------------------");
        scanner.close();
    }
}
