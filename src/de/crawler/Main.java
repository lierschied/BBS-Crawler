package de.crawler;

import de.crawler.utils.Colorize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static String FILES = "files";
    final static String STREAM = "stream";
    final static String TEST = "test";
    final static String HELP = "help";

    public static void main(String[] args) throws Exception {
        if (args.length >= 1) {
            String command = args[0];

            switch (command) {
                case FILES-> {
                    assert !args[1].equals("") : "No directory path specified";
                    parseFiles(args[1]);
                }
                case STREAM -> {
                    assert !args[1].equals("") : "No station to connect to specified";
                    parseStream(args[1]);
                }
                case TEST -> Helper.test();
                case HELP -> printHelp();
                default -> {
                    System.out.println(Colorize.red(String.format("Unknown command: %s", command)));
                    printHelp();
                }
            }
        } else {
            System.out.println(Colorize.red("Too few arguments to function\n"));
            printHelp();
        }
    }

    private static void parseStream(String station) throws Exception {
        new StreamParser(station).start();
    }

    private static void parseFiles(String dirPath) {
        new FileParser(dirPath).start();
    }

    private static void printHelp() {
        System.out.println(Colorize.green("Available commands:\n"));
        String[] commands = {
                String.format("files {%s} | %s", Colorize.yellow("pathToFiles"),
                        Colorize.blue("parses sensors out of files within specified directory")),
                String.format("stream {%s} | %s", Colorize.yellow("station"),
                        Colorize.blue("connects to a station an parses sensors")),
        };

        for (String line : commands) {
            System.out.println(line);
        }
    }

    public static String firstGroupMatch(Pattern p, String toMatch) {
        Matcher m = p.matcher(toMatch);

        if (m.find()) {
            return m.group(1);
        }

        return "no match found!";
    }

}
