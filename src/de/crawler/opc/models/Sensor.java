package de.crawler.opc.models;

import de.crawler.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sensor {
    private String id;
    private String sensorName;
    private DataValue data;

    private Connection dbc = DbConnection.getConnection();

    public Sensor(String sensorName, DataValue data) throws SQLException {
        this.sensorName = sensorName;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public DataValue getData() {
        return data;
    }

    public void setData(DataValue data) {
        this.data = data;
    }

    public String toString() {
        return String.format("Id: %s\nName: %s\nData: [%s]", id, sensorName, data.toString());
    }

    public void update() throws SQLException {
        PreparedStatement stm = dbc.prepareStatement("UPDATE sensor SET name = ?, data_type = ?, updated_at = NOW() WHERE id = ?");
        stm.setString(1, this.sensorName);
        stm.setString(2, this.data.getValueType());
        stm.setString(3, this.id);
        stm.executeUpdate();
    }

    public void create() throws SQLException {
        PreparedStatement stm = dbc.prepareStatement("INSERT INTO sensor(name, data_type) VALUES(?, ?)");
        stm.setString(1, this.sensorName);
        stm.setString(2, this.data.getValueType());
        stm.executeUpdate();
    }

    public void fetch() throws SQLException {
        PreparedStatement stm = dbc.prepareStatement("SELECT * FROM sensor WHERE name = ?");
        stm.setString(1, this.sensorName);
        ResultSet rs = stm.executeQuery();
        if (!rs.isBeforeFirst()) {
            create();
            return;
        }
        rs.next();

        this.id = rs.getString("id");
    }
}
