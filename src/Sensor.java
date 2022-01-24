import java.util.Date;

public class Sensor {
    private int id;
    private String sensorName;
    private String sensorZustand;
    private Date timestamp;
    private String sensorlokalisierung;
    private SensorTyp sensorTyp;


    public Sensor(int id, String sensorName, String sensorZustand, SensorTyp sensorTyp, Date timestamp,
                  String sensorlokalisierung) {
        this.id = id;
        this.sensorName = sensorName;
        this.sensorZustand = sensorZustand;
        this.sensorTyp = sensorTyp;
        this.timestamp = timestamp;
        this.sensorlokalisierung = sensorlokalisierung;
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


    public String getSensorZustand() {
        return sensorZustand;
    }


    public void setSensorZustand(String sensorZustand) {
        this.sensorZustand = sensorZustand;
    }


    public SensorTyp getSensorTyp() {
        return sensorTyp;
    }


    public void setSensorTyp(SensorTyp sensorTyp) {
        this.sensorTyp = sensorTyp;
    }


    public Date getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public String getSensorlokalisierung() {
        return sensorlokalisierung;
    }


    public void setSensorlokalisierung(String sensorlokalisierung) {
        this.sensorlokalisierung = sensorlokalisierung;
    }
}
