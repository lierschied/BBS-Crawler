package de.crawler.models;

import java.util.Date;

import de.crawler.enums.SensorType;

public class Sensor {
    private String id;
    private String sensorName;
    private Date timestamp;
    private boolean sensorZustand;
    private SensorType sensorType;
 
    public Sensor(String id, String sensorName, boolean sensorZustand, SensorType sensorTyp, Date timestamp) {
        this.id = id;
        this.sensorName = sensorName;
        this.sensorZustand = sensorZustand;
        this.sensorType = sensorTyp;
        this.timestamp = timestamp;
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

    public boolean getSensorZustand() {
        return sensorZustand;
    }

    public void setSensorZustand(boolean sensorZustand) {
        this.sensorZustand = sensorZustand;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
