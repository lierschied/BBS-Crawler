package de.crawler.opc.models;
//https://node-opcua.github.io/api_doc/0.2.0/classes/DataValue.html
public class DataValue {

    private final String value;
    private final String valueType;
    private final String statusCode;
    private final String sourceTimestamp;
    private final String serverTimestamp;

    public DataValue(String value, String valueType, String statusCode, String sourceTimestamp, String serverTimestamp) {
        this.value = value;
        this.valueType = valueType;
        this.statusCode = statusCode;
        this.sourceTimestamp = sourceTimestamp;
        this.serverTimestamp = serverTimestamp;
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
