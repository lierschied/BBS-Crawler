import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String DISPLAY_NAME = "DisplayName=\"([^\"]*)\"";
    private static final String SENSOR_VALUES = "-> VALUE: DataValue\\((.*)\\)";
    private static final String SENSOR_ID = "NodeId=\"ns=3;s=\"([^\"]*)\"";

    private static final Pattern displayNamePattern = Pattern.compile(DISPLAY_NAME);
    private static final Pattern sensorValuesPattern = Pattern.compile(SENSOR_VALUES);
    private static final Pattern sensorIdPattern = Pattern.compile(SENSOR_ID);

    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println(args.length);
        String dirPath;
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
                if (line == null)
                    break;
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


    public static File[] loadFiles(String dirPath) {
        File directoryPath = new File(dirPath);
        //List of all files and directories
        File[] filesList = directoryPath.listFiles();
        assert filesList != null;
        System.out.println(String.format("Loaded %s file(s) from %s.", filesList.length, directoryPath));
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
