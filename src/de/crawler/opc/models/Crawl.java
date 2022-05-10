package de.crawler.opc.models;

import de.crawler.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Crawl {

    public static void addCrawl(ExtendedSensor sensor, String filename) throws SQLException {
        Connection dbc = DbConnection.getConnection();
        PreparedStatement stm = dbc.prepareStatement("INSERT INTO `crawl`(sensor_id, data, filename) VALUES (?, ?, ?)");
        stm.setString(1, sensor.getId());
        stm.setString(2, sensor.getData().toString());
        stm.setString(3, filename);
        stm.executeUpdate();
    }
}
