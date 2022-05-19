package de.crawler;

import de.crawler.opc.models.DataValue;
import de.crawler.opc.models.ExtendedSensor;
import de.crawler.utils.Colorize;
import de.judge.opc_test.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import static de.crawler.Main.firstGroupMatch;

public class StreamParser {
    private static final HashMap<String, ExtendedSensor> sensorList = new HashMap<>();
    private final Scanner stream;
    private final Pattern valuePattern = Pattern.compile("DataValue\\(.*\\)");
    private final Pattern namePattern = Pattern.compile("\"(.*)\"\\s--");

    public StreamParser(String stationName) throws Exception {
        this(stationFromString(stationName));

    }

    public StreamParser(Station station) throws Exception {
        OpcStreamAdapter.setStation(station);
        this.stream = OpcStreamAdapter.getStream();
    }

    private static Station stationFromString(String stationName) {
        return switch (stationName) {
            case "Leitstand", "cps1" -> Station.Controller;
            case "Palettenlager", "PL" -> Station.PL;
            case "Rohlager", "RL" -> Station.RL;
            case "Handling", "HL" -> Station.HL;
            case "Presse", "PR" -> Station.PR;
            default -> throw new RuntimeException(String.format("Station %s not found", stationName));
        };
    }

    private void init() throws SQLException {
        Connection dbc = DbConnection.getConnection();
        PreparedStatement stm = dbc.prepareStatement("SELECT id, name FROM sensor");
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            String name = rs.getNString("name");

            ExtendedSensor sensor = new ExtendedSensor(name);
            String id = rs.getString("id");
            sensor.setId(id);
            sensorList.put(name, sensor);
        }
    }

    public void start() {
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int errorCount = 0;

        while (errorCount <= 10) {
                System.out.println(stream.next());
            try {
                Thread.sleep(1000);
                //parse(stream.next());
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
            }
        }

        OpcStreamAdapter.disconnect();
        System.out.println(Colorize.red("Error count exceeded the limit"));
    }

    /**
     * @param item '"+AM-MB2_StopperRechts" --  -> VALUE: DataValue(value=false, statusCode=GOOD (0x00000000) "The operation succeeded.", sourceTimestamp=02/17/15 21:05:45.8950977 GMT, sourcePicoseconds=0, serverTimestamp=02/17/15 21:05:45.8950977 GMT, serverPicoseconds=0)'
     * @throws SQLException @see ExtendedSensor.update()
     */
    private void parse(String item) throws SQLException {
        System.out.println(item);
        String sensorName = firstGroupMatch(namePattern, item);

        String valuesMatch = firstGroupMatch(valuePattern, item);
        DataValue dataValue = DataValue.generateDataValue(valuesMatch);

        ExtendedSensor currentSensor = sensorList.get(sensorName);
        currentSensor.setData(dataValue);
        currentSensor.update();
    }
}
