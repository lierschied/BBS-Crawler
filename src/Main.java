import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String DISPLAY_NAME = "DisplayName=\"([^\"]*)\"";
    private static final String SENSOR_VALUES = "-> VALUE: DataValue\\((.*)\\)";

    private static final Pattern displayNamePattern = Pattern.compile(DISPLAY_NAME);
    private static final Pattern sensorValuesPattern = Pattern.compile(SENSOR_VALUES);

    public static void main(String[] args) throws IOException {
        parseText();
    }

    private static void parseText() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/opc_output_rl.t"))) {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.contains("BrowseName=\"3:+")) {
                    String displayName = firstGroupMatch(displayNamePattern, line);
                    System.out.println(displayName);
                    String sensorValues = firstGroupMatch(sensorValuesPattern, line);
                    System.out.println(sensorValues);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param p compiled regex Pattern
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
