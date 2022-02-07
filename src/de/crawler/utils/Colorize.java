package de.crawler.utils;

/**
 * a util class for adding ANSI escaped color to the strings for colorizing the output
 */
public class Colorize {

    private static final String colorRed = (char) 27 + "[1;31m";
    private static final String colorGreen = (char) 27 + "[1;32m";
    private static final String colorBlue = (char) 27 + "[1;34m";
    private static final String colorYellow = (char) 27 + "[1;33m";
    private static final String colorReset = (char) 27 + "[0m";

    /**
     * positive/success
     *
     * @param str string to be colored green
     * @return colored string
     */
    public static String green(String str) {
        return colorGreen
                + str
                + colorReset;
    }

    /**
     * failure/error
     *
     * @param str string to be colored green
     * @return colored string
     */
    public static String red(String str) {
        return colorRed
                + str
                + colorReset;
    }

    /**
     * informative
     *
     * @param str string to be colored blue
     * @return colored string
     */
    public static <E> String blue(E str) {
        return colorBlue
                + str
                + colorReset;
    }

    /**
     * contrast
     *
     * @param str string to be colored yellow
     * @return colored string
     */
    public static String yellow(String str) {
        return colorYellow
                + str
                + colorReset;
    }
}
