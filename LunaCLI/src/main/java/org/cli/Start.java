package org.cli;


import org.cli.exceptions.HandleChangePortException;
import org.cli.exceptions.ParamLengthException;
import org.cli.exceptions.handleForceUserLoadAndConnectException;
import org.cli.manager.CommandHistory;
import org.cli.manager.CommandPackage;

import java.sql.SQLException;
import java.util.Scanner;

import static org.cli.utils.Colors.*;

public class Start {
    /**
     * Starts the Luna Command Line Interface (CLI) and continuously listens for user input.
     * <p>
     * This method prints the initial CLI greeting message and enters an infinite loop, prompting the user
     * with a "luna>" command prompt. The method listens for user commands, processes them by calling the
     * {@link CommandPackage#command(String)} method, and prints the appropriate response.
     * The CLI will continue accepting commands until the user enters ":qa!" to exit the application.
     * </p>
     *
     * <p>
     * The following operations occur during the execution:
     * <ul>
     *     <li>Prints the "Luna CLI" header and the ":qa! - EXIT" command for quitting the CLI.</li>
     *     <li>Waits for user input, which is then trimmed and processed.</li>
     *     <li>If the user types ":qa!", the loop breaks and the program terminates.</li>
     *     <li>For any other input, the command is passed to the {@link CommandPackage#command(String)} method
     *         for further processing.</li>
     * </ul>
     * </p>
     *
     * @throws SQLException If a database-related error occurs during command processing.
     * @throws ParamLengthException If the provided command parameters have incorrect lengths.
     * @throws HandleChangePortException If an error occurs while attempting to change the port.
     * @throws handleForceUserLoadAndConnectException If an error occurs while forcing a user load and connection.
     */
    public static void commandStart() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println(GREEN+"Stats for nerds!: http://localhost:9091/metrics"+RESET);
        System.out.println(RED + ":qa! - EXIT" + RESET);
        System.out.println("---------------------");
        while (true) {
            System.out.print("luna> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(":qa!")) {
                break;
            }
            CommandPackage.command(input);
            CommandHistory.addCommand(input);

        }
        System.out.println("---------------------");
        scanner.close();
    }
}
