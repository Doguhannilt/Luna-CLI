package org.cli.manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private static final List<String> history = new ArrayList<>();
    /**
     * Adds a new command to the command history list.
     * <p>
     * This method appends the given command to the history, allowing you to track
     * previously entered commands in the application. The command is stored as a
     * string for future reference.
     * </p>
     *
     * @param command The command to be added to the history list.
     *               This should be a non-null string representing the entered command.
     */
    public static void addCommand(String command) {history.add(command);}

    /**
     * Displays the list of all commands stored in the history.
     * <p>
     * This method iterates through the command history and prints each command
     * along with its position in the list. It is useful for viewing the sequence
     * of commands that have been entered so far.
     * </p>
     *
     * <p>
     * The commands are printed in the following format:
     * <code>index. command</code>
     * </p>
     */
    public static void showHistory() {
        for (int i = 0; i < history.size(); i++) {System.out.println((i + 1) + ". " + history.get(i));}
    }

    /**
     * Get history size
     * */
    public static int getHistorySize() {
        return history.size();
    }

}
