package de.crawler;

import de.crawler.opc.ListSensor;
import de.judge.opc_test.OPCClientETS;
import de.judge.opc_test.SensorList;
import de.judge.opc_test.Station;

import java.io.InputStream;
import java.util.Scanner;

public class OpcStreamAdapter {

    private static OpcStreamAdapter instance;
    private static Scanner stream;
    private final OPCClientETS opcClient;
    private static Station station = Station.RL;

    protected OpcStreamAdapter() throws Exception {
        this.opcClient = OPCClientETS.getInstance();
        this.opcClient.connectToMachine(station);

        SensorList sensorList = ListSensor.getSensorList();
        opcClient.browseOPCServer(sensorList);
    }

    private static OpcStreamAdapter getInstance() throws Exception {
        if (instance == null) {
            instance = new OpcStreamAdapter();
        }
        return instance;
    }

    public static void setStation(Station s) {
        station = s;
    }

    public static Scanner getStream() throws Exception {
        if (stream == null) {
            OpcStreamAdapter adapter = getInstance();
            InputStream in = adapter.opcClient.getInputStream();
            stream = new Scanner(in);
        }
        return stream;
    }

    public static void disconnect() {
        try {
            getInstance().opcClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
