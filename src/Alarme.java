import java.util.Date;

public class Alarme {
    private int id;
    private int sensorId;
    private int stationId;
    private Date timestamp;

    public Alarme(int id, int sensorId, int stationId, Date timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.stationId = stationId;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
