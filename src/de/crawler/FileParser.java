package de.crawler;

import de.crawler.opc.models.DataValue;
import de.crawler.opc.models.ExtendedSensor;
import de.crawler.utils.Colorize;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static de.crawler.Main.firstGroupMatch;

public class FileParser {
    //regex patterns
    private final String DISPLAY_NAME = "BrowseName=\"3:([^\"]*)\"";
    private final String SENSOR_VALUES = "-> VALUE: DataValue\\((.*)\\)";
    private final String SENSOR_ID = "NodeId=\"ns=3;s=\"([^\"]*)\"";
    //private static final String ULTIMATE = SENSOR_ID + ".+" + DISPLAY_NAME + ".+" + SENSOR_VALUES; //almost 0.3ms slower than 3 individual regex

    //compiled regex patterns to avoid recompiling them every time for each loop cycle (file)
    private final Pattern displayNamePattern = Pattern.compile(DISPLAY_NAME);
    private final Pattern sensorValuesPattern = Pattern.compile(SENSOR_VALUES);
    private final Pattern sensorIdPattern = Pattern.compile(SENSOR_ID);

    private final List<String> changes = new ArrayList<String>();
    private final HashMap<String, ExtendedSensor> sensorList = new HashMap<>();
    private final String dirPath;
    private long totalTime;
    private File[] files;
    private Connection dbc;

    public FileParser(String dirPath) {
        this.dirPath = dirPath;
    }

    /**
     * old runtime approx. 11s total, 5.62ms for each file
     * improved runtime approx. 2-5s total, 1.42-2.78ms for each file when skipping the rest of the file if all sensors are collected ()
     */
    public void start() {
        files = loadFiles(dirPath);

        long startTime = System.nanoTime();
        for (File f : files) {
            System.out.println("Parsing from file: " + Colorize.blue(f.getName()));
            parseFile(f);
            printSensorData();
        }
        totalTime = System.nanoTime() - startTime;
    }

    /**
     * console output total file, sensor count and runtime
     */
    private void printStats() {
        System.out.println("\n-----------------\n-----------------\n\n" + Colorize.yellow("Runtime:"));
        System.out.printf("Total file count: %s\n", Colorize.blue(files.length));
        System.out.printf("Total time in seconds: %ss\n", Colorize.blue(TimeUnit.NANOSECONDS.toSeconds(totalTime)));
        String avg = String.format("%.2f", ((double) TimeUnit.NANOSECONDS.toMillis(totalTime) / files.length));
        System.out.printf("Avg. parse time per file in milliseconds: %sms\n", Colorize.blue(avg));
    }

    /**
     * console output for the sensor data for all objects within the sensorList hashmap
     */
    private void printSensorData() {
        sensorList.forEach((key, sensor) -> System.out.printf("%s\n\n", sensor));
    }

    /**
     * console output for the detected changes
     */
    private void printChanges() {
        System.out.println("\n-----------------\n-----------------\n\n" + Colorize.yellow("CHANGES:"));

        System.out.printf("A total of %s change(s) were detected within %s file(s) containing %s sensor(s)\n", Colorize.blue(changes.size()), Colorize.blue(files.length), Colorize.blue(sensorList.size()));

        for (String change : changes) {
            System.out.println(change);
        }
    }

    /**
     * parsing the opc_output data for sensor values
     * initializes a sensor within the sensor list or updates the data
     * every detected change will be put into the changes list
     *
     * @param file current file to be processed
     */
    private void parseFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;

            boolean found = false;
            while ((line = reader.readLine()) != null && !found) {
                //setting line pointer to line x
                found = line.contains("NodeId=\"ns=3;s=Inputs\"");
            }

            //read only first occurrence of sensors within the first NodeId="ns=3;s=Inputs"
            //together with the first while this reduces parsing time by 45-50%
            //maybe this?? => && line.contains("_ _ _ _ _")
            int c = 0; // this counter is to abort after 38 sensors are found (current max sensors)
            while ((line = reader.readLine()) != null && c <= 38) {
                if (!line.contains("BrowseName=\"3:+")) continue;
                c++;
                String sensorId = firstGroupMatch(sensorIdPattern, line);

                String displayName = firstGroupMatch(displayNamePattern, line);

                String sensorValues = firstGroupMatch(sensorValuesPattern, line);

                DataValue data = getDataValue(sensorValues);

                ExtendedSensor sensor;
                if (sensorList.containsKey(sensorId)) {
                    sensor = sensorList.get(sensorId);
                    //currentSensorData != oldSensorData
                } else {
                    sensor = new ExtendedSensor(displayName, data);
                    sensor.fetch();

                    sensorList.put(sensorId, sensor);
                }

                if (!sensor.getData().getValue().equals(data.getValue())) {
                    changes.add(String.format("Change detected for Sensor \"%s\" in File \"%s\":\n- old value = %s\n+ new value = %s\n", sensor.getSensorName(), Colorize.blue(file.getName()), Colorize.red(sensor.getData().getValue()), Colorize.green(data.getValue())));
                }
                sensor.setData(data);
                sensor.update();
                //Crawl.addCrawl(sensor, file.getName());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Depending on the following structure "index => expected string"
     * 0 => value, 1 => statusCode, 2 => sourceTimestamp, 4 => serverTimestamp
     *
     * @param str raw sensorValues string
     * @return DataValue Object
     */
    private DataValue getDataValue(String str) {
        String[] dataValues = str.split(",");
        assert dataValues.length >= 5 : "To few data values to work with!";
        dataValues[0] = dataValues[0].replace("value=", "");
        dataValues[1] = dataValues[1].replace("statusCode=", "");
        dataValues[2] = dataValues[2].replace("sourceTimestamp=", "");
        dataValues[4] = dataValues[4].replace("serverTimestamp=", "");

        //TODO: remove temporary solution with some sort of advanced type detection
        String valueType = dataValues[0].equals("false") || dataValues[0].equals("true") ? "Boolean" : "Integer";

        return new DataValue(dataValues[0], valueType, dataValues[1], dataValues[2], dataValues[4]);
    }

    /**
     * for loading the opc_output_rl_* files from the data_ready dir
     *
     * @param dirPath path to the data_ready directory
     * @return list of all files starting with opc_output_rl_*
     */
    public File[] loadFiles(String dirPath) {
        File directoryPath = new File(dirPath);
        FileFilter filter = f -> !f.isDirectory() && f.getName().startsWith("opc_output_rl_");
        File[] filesList = directoryPath.listFiles(filter);
        assert filesList != null;
        System.out.printf("Loaded %s file(s) from %s\n", filesList.length, directoryPath);
        return filesList;
    }
}
