package org.cli;


import java.sql.SQLException;
import java.util.Scanner;

public class Start {
    public static void commandStart() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Luna CLI");
        System.out.println(":qa! - EXIT");
        while (true) {
            System.out.print("luna> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(":qa!")) {
                break;
            }
            ProcessCommand.command(input);
        }
        scanner.close();
    }
}
