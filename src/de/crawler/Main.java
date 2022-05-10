package de.crawler;

import de.crawler.utils.Colorize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {

        //path can be invoked as the first command line parameter
        if (args.length <= 2) {
            String command = args[0];

            switch (command) {
                case "files" -> {
                    assert !args[1].equals("") : "No directory path specified";
                    parseFiles(args[1]);
                }
                case "stream" -> {
                    assert !args[1].equals("") : "No station to connect to specified";
                    parseStream(args[1]);
                }
                case "help" -> printHelp();
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
        StreamParser streamParser = new StreamParser(station);
        streamParser.start();
    }

    private static void parseFiles(String dirPath) {
        new FileParser(dirPath).start();
    }

    private static void printHelp() {
        System.out.println(Colorize.green("Available commands:\n"));
        String[] commands = {
                String.format("files {%s} | %s",Colorize.yellow("pathToFiles"), Colorize.blue("parses sensors out of files within specified directory")),
                String.format("stream {%s} | %s",Colorize.yellow("station"), Colorize.blue("connects to a station an parses sensors")),
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
