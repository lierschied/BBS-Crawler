import java.util.Date;

public class Sensor {
    private int id;
    private String sensorName;
    private String sensorState;
    private Date timestamp;
    private SensorType sensorType;


    public Sensor(int id, String sensorName, String sensorZustand, SensorType sensorTyp, Date timestamp) {
        this.id = id;
        this.sensorName = sensorName;
        this.sensorState = sensorZustand;
        this.sensorType = sensorTyp;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getSensorName() {
        return sensorName;
    }


    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }


    public String getSensorState() {
        return sensorState;
    }


    public void setSensorState(String sensorState) {
        this.sensorState = sensorState;
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
