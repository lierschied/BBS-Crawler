import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        parseText();
    }

    private static void parseText() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/opc_output_rl.t"));
        int c = 0;
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.contains("BrowseName=\"3:+")) {
                    c++;
                    Pattern p = Pattern.compile(".+DisplayName=(.+)");
                    Matcher m = p.matcher(line);
                    if (m.matches()) {
                        System.out.println(m.group(1));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        System.out.println(c);

    }

}
