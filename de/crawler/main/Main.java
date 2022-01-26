package de.crawler.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.crawler.models.Sensor;

public class Main {
    //regex patterns
    private static final String DISPLAY_NAME = "DisplayName=\"([^\"]*)\"";
    private static final String SENSOR_VALUES = "-> VALUE: DataValue\\((.*)\\)";
    private static final String SENSOR_ID = "NodeId=\"ns=3;s=\"([^\"]*)\"";

    //compiled regex patterns to avoid recompiling them every time for each loop cycle (file)
    private static final Pattern displayNamePattern = Pattern.compile(DISPLAY_NAME);
    private static final Pattern sensorValuesPattern = Pattern.compile(SENSOR_VALUES);
    private static final Pattern sensorIdPattern = Pattern.compile(SENSOR_ID);

    private static final List<Sensor> sensors = new ArrayList<Sensor>();
    private static final List<String> changes = new ArrayList<String>();    

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
            parseText(f);
        }

        System.out.println("-----------------\n-----------------\nCHANGES:");
        for (String change : changes) {
            System.out.println(change);
        }
    }

    /**
     * parsing the opc_output data for sensor values
     *
     * @throws IOException if file fails to open
     */
    private static void parseText(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.contains("BrowseName=\"3:+")) {
                    String sensorId = firstGroupMatch(sensorIdPattern, line);
                    System.out.println("ID: " + sensorId);

                    String displayName = firstGroupMatch(displayNamePattern, line);
                    System.out.println("Name: " + displayName);

                    String sensorValues = firstGroupMatch(sensorValuesPattern, line);
                    String[] values = sensorValues.split(",");

                    String value = firstGroupMatch(Pattern.compile("value=(.+)"), values[0]);
                    System.out.println("Value: " + value);

                    boolean sensorZustand = value == "true";
                    
                    addCreateSensor(file, sensorId, displayName, sensorZustand);
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
    
    private static void addCreateSensor(File file, String sensorId, String sensorName, boolean sensorZustand) {
    	Sensor sensor = new Sensor(sensorId, sensorName, sensorZustand, null, null);

        if (sensors.contains(sensor)) {
        	Optional<Sensor> oldSensor = sensors.stream()
        			.filter(s -> s.getId() == sensor.getId())
        			.findFirst();
        	
        	if ((oldSensor.get() != null) && (sensorZustand != oldSensor.get().getSensorZustand())) {
                String result = String.format("Change detected for Sensor \"%s\" in File \"%s\":\nold value = %s\nnew value = %s", 
                		sensorName,
                		file.getName(),
                		oldSensor.get().getSensorZustand(),
                		sensorZustand);
                
                changes.add(result);
                sensors.remove(oldSensor.get());
        	}
        }
        
        sensors.add(sensor);
    }

}
