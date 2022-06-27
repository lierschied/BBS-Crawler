package de.crawler.opc.enums;

public enum Station {
    Leitstand("Leitstand", "opc.tcp://192.168.0.200:4840"),
    Palettenlager("Palettenlager", "opc.tcp://192.168.0.2:4840"),
    Rohlager("Rohlager", "opc.tcp://192.168.0.10:4840"),
    Handling("Handling", "opc.tcp://192.168.0.20:4840"),
    Presse("Presse", "opc.tcp://192.168.0.30:4840"),
    Test("Test", "opc.tcp://127.0.0.1:4855");

    private final int NODE_ID = 3;
    private final String name;
    private final String url;

    Station(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
