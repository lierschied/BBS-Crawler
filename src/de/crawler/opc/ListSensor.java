package de.crawler.opc;

import de.crawler.DbConnection;
import de.judge.opc_test.Sensor;
import de.judge.opc_test.SensorList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListSensor extends Sensor {
    public ListSensor(String s) {
        super(3, s);
    }

    public static SensorList getSensorList() throws SQLException {
        Connection dbc = DbConnection.getConnection();
        PreparedStatement stm = dbc.prepareStatement("SELECT name FROM sensor");
        SensorList sensorList = new SensorList();
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            String name = rs.getNString("name");

            name = String.format("\"%s\"", name);

            sensorList.addSensor(new ListSensor(name));
        }
        return sensorList;
    }
}
