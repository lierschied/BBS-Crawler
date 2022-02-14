package de.crawler.utils;

/**
 * a util class for adding ANSI escaped color to the strings for colorizing the output
 */
public class Colorize {

    private static final char ESCAPE = (char) 27;
    private static final String COLOR_RED = ESCAPE + "[1;31m";
    private static final String COLOR_GREEN = ESCAPE + "[1;32m";
    private static final String COLOR_BLUE = ESCAPE + "[1;34m";
    private static final String COLOR_YELLOW = ESCAPE + "[1;33m";
    private static final String COLOR_RESET = ESCAPE + "[0m";

    /**
     * positive/success
     *
     * @param str string to be colored green
     * @return colored string
     */
    public static String green(String str) {
        return COLOR_GREEN
                + str
                + COLOR_RESET;
    }

    /**
     * failure/error
     *
     * @param str string to be colored green
     * @return colored string
     */
    public static String red(String str) {
        return COLOR_RED
                + str
                + COLOR_RESET;
    }

    /**
     * informative
     *
     * @param str string to be colored blue
     * @return colored string
     */
    public static <E> String blue(E str) {
        return COLOR_BLUE
                + str
                + COLOR_RESET;
    }

    /**
     * contrast
     *
     * @param str string to be colored yellow
     * @return colored string
     */
    public static String yellow(String str) {
        return COLOR_YELLOW
                + str
                + COLOR_RESET;
    }
}
