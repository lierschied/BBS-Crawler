import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    //regex patterns
    private static final String DISPLAY_NAME = "DisplayName=\"([^\"]*)\"";
    private static final String SENSOR_VALUES = "-> VALUE: DataValue\\((.*)\\)";
    private static final String SENSOR_ID = "NodeId=\"ns=3;s=\"([^\"]*)\"";

    //compiled regex patterns to avoid recompiling them every time for each loop cycle (file)
    private static final Pattern displayNamePattern = Pattern.compile(DISPLAY_NAME);
    private static final Pattern sensorValuesPattern = Pattern.compile(SENSOR_VALUES);
    private static final Pattern sensorIdPattern = Pattern.compile(SENSOR_ID);

    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        String dirPath;

        //path can also be invoked as the first command line parameter
        if (args.length > 0) {
            dirPath = args[0];
        } else {
            System.out.print("Specify directory to parse files from: ");
            dirPath = s.nextLine();
        }

        File[] files = loadFiles(dirPath);

        for (File f : files) {
            System.out.println(f.getName());
            parseText(f.getAbsolutePath());
        }
    }

    /**
     * parsing the opc_output data for sensor values
     *
     * @throws IOException if file fails to open
     */
    private static void parseText(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.contains("BrowseName=\"3:+")) {
                    System.out.println("ID: " + firstGroupMatch(sensorIdPattern, line));

                    String displayName = firstGroupMatch(displayNamePattern, line);
                    System.out.println(displayName);

                    String sensorValues = firstGroupMatch(sensorValuesPattern, line);
                    String[] values = sensorValues.split(",");
                    System.out.println(Arrays.toString(values));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * for loading the opc_output_rl_* files from the data_ready dir
     *
     * @param dirPath path to the data_ready directory
     * @return list of all files and directories
     */
    public static File[] loadFiles(String dirPath) {
        File directoryPath = new File(dirPath);
        File[] filesList = directoryPath.listFiles();
        assert filesList != null;
        System.out.printf("Loaded %s file(s) from %s.%n", filesList.length, directoryPath);
        return filesList;
    }

    /**
     * @param p       compiled regex Pattern
     * @param toMatch string to match the pattern against
     * @return first captured group from a regex
     */
    private static String firstGroupMatch(Pattern p, String toMatch) {
        Matcher m = p.matcher(toMatch);

        if (m.find()) {
            return m.group(1);
        }

        return "no match found!";
    }

}
