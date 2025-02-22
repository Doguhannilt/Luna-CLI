package org.cli.utils;

public class Colors {
    /**
     * Resets the text color to the default terminal color.
     * <p>
     * This is used to reset any color formatting applied previously.
     * </p>
     */
    public final static String RESET = "\033[0m";
    /**
     * Red color code for terminal text.
     * <p>
     * This can be used to print text in red color, typically for warnings
     * or errors.
     * </p>
     */
    public final static String RED = "\033[31m";
    /**
     * Green color code for terminal text.
     * <p>
     * This can be used to print text in green color, often used for success
     * or informational messages.
     * </p>
     */
    public final static String GREEN = "\033[32m";
    /**
     * Yellow color code for terminal text.
     * <p>
     * This can be used to print text in yellow color, commonly used for
     * caution or attention-grabbing messages.
     * </p>
     */
    public final static String YELLOW = "\033[33m";
    /**
     * Blue color code for terminal text.
     * <p>
     * This can be used to print text in blue color, often used for informational
     * or general-purpose messages.
     * </p>
     */
    public final static String BLUE = "\033[34m";
}
