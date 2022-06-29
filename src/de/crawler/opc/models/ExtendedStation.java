package de.crawler.opc.models;

import de.crawler.DbConnection;
import de.judge.opc_test.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtendedStation {
    private int id;
    private String name;
    private String state;
    public Station station;

    private static Connection dbc;

    static {
        try {
            dbc = DbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String shortName;

    public ExtendedStation(int id, String name) {
        this.id = id;
        this.name = name;
        this.station = stationFromString(name);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void fetch() throws SQLException {
        PreparedStatement stm = dbc.prepareStatement("SELECT * FROM station WHERE name = ?");
        stm.setString(1, this.name);
        stm.setString(2, this.shortName);
        ResultSet rs = stm.executeQuery();
        if (!rs.isBeforeFirst()) {
            create();
            return;
        }

        rs.next();

        this.id = rs.getInt("id");
        this.name = rs.getString("name");
    }

    private void create() throws SQLException {
        PreparedStatement stm = dbc.prepareStatement("INSERT INTO station(name) VALUES(?)");
        stm.setString(1, this.name);
        stm.executeUpdate();
    }

}
