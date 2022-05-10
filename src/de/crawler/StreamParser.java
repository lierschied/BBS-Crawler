package de.crawler;

import de.crawler.opc.ListSensor;
import de.crawler.opc.models.DataValue;
import de.crawler.opc.models.ExtendedSensor;
import de.crawler.utils.Colorize;
import de.judge.opc_ets.SensorList;
import de.judge.opc_ets.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StreamParser {
    private static final HashMap<String, ExtendedSensor> sensorList = new HashMap<>();
    private final Scanner stream;
    private final Pattern valuePattern = Pattern.compile("DataValue\\(.*\\)");
    private final Pattern namePattern = Pattern.compile("\"(.*)\"\\s--");

    public StreamParser(Station station) throws Exception {
        this.stream = OpcStreamAdapter.getStream(station);
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
            try {
                parse(stream.next());
            } catch (SQLException e) {
                e.printStackTrace();
                errorCount++;
            }
        }

        OpcStreamAdapter.disconnect();
        System.out.println(Colorize.red("Error count exceeded the limit"));
    }

    /**
     * @param item  '"+AM-MB2_StopperRechts" --  -> VALUE: DataValue(value=false, statusCode=GOOD (0x00000000) "The operation succeeded.", sourceTimestamp=02/17/15 21:05:45.8950977 GMT, sourcePicoseconds=0, serverTimestamp=02/17/15 21:05:45.8950977 GMT, serverPicoseconds=0)'
     * @throws SQLException @see ExtendedSensor.update()
     */
    private void parse(String item) throws SQLException {
        String sensorName = Main.firstGroupMatch(namePattern, item);

        String valuesMatch = Main.firstGroupMatch(valuePattern, item);
        DataValue dataValue = DataValue.generateDataValue(valuesMatch);

        ExtendedSensor currentSensor = sensorList.get(sensorName);
        currentSensor.setData(dataValue);
        currentSensor.update();
    }
}
