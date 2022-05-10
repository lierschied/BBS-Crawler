package de.crawler.opc.models;

public class DataValue {

    private final String value;
    private final String valueType;
    private final String statusCode;
    private final String sourceTimestamp;
    private final String serverTimestamp;

    public DataValue() {
        this("", "", "", "", "");
    }

    public DataValue(String value, String valueType, String statusCode, String sourceTimestamp, String serverTimestamp) {
        this.value = value;
        this.valueType = valueType;
        this.statusCode = statusCode;
        this.sourceTimestamp = sourceTimestamp;
        this.serverTimestamp = serverTimestamp;
    }

    public static DataValue generateDataValue(String string) {
        String[] dataValues = string.split(",");
        assert dataValues.length >= 5 : "To few data values to work with!";
        dataValues[0] = dataValues[0].replace("value=", "");
        dataValues[1] = dataValues[1].replace("statusCode=", "");
        dataValues[2] = dataValues[2].replace("sourceTimestamp=", "");
        dataValues[4] = dataValues[4].replace("serverTimestamp=", "");

        String valueType = dataValues[0].equals("false") || dataValues[0].equals("true") ? "Boolean" : "Integer";
        return new DataValue(dataValues[0], valueType, dataValues[1], dataValues[2], dataValues[4]);
    }

    public String toString() {
        return String.format("value: %s, valueType: %s, status code: %s, source timestamp: %s, server timestamp: %s", value, valueType, statusCode, sourceTimestamp, serverTimestamp);
    }

    public String getValueType() {
        return valueType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getSourceTimestamp() {
        return sourceTimestamp;
    }

    public String getServerTimestamp() {
        return serverTimestamp;
    }

    public String getValue() {
        return value;
    }
}
