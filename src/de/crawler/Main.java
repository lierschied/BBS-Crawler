package de.crawler;

import de.judge.opc_ets.Station;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {

        //path can also be invoked as the first command line parameter
        if (args.length > 0) {
            String dirPath = args[0];
            assert !dirPath.equals("") : "No directory path specified";
            parseFiles(dirPath);
        } else {
            parseStream();
        }
    }

    private static void parseStream() throws Exception {
        new StreamParser(Station.RL).start();
    }

    private static void parseFiles(String dirPath) {
        new FileParser(dirPath).start();
    }

    public static String firstGroupMatch(Pattern p, String toMatch) {
        Matcher m = p.matcher(toMatch);

        if (m.find()) {
            return m.group(1);
        }

        return "no match found!";
    }

}
