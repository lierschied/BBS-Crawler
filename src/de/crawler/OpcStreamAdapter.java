package de.crawler;

import de.crawler.opc.ListSensor;
import de.judge.opc_ets.OPCClientETS;
import de.judge.opc_ets.SensorList;
import de.judge.opc_ets.Station;

import java.io.InputStream;
import java.util.Scanner;

public class OpcStreamAdapter {

    private static OpcStreamAdapter instance;
    private static Scanner stream;
    private final OPCClientETS opcClient;
    private static Station station;

    protected OpcStreamAdapter(Station station) throws Exception {
        OpcStreamAdapter.station = station;
        this.opcClient = OPCClientETS.getInstance();
        this.opcClient.connectToMachine(station);

        SensorList sensorList = ListSensor.getSensorList();
        opcClient.browseOPCServer(sensorList);
    }

    private static OpcStreamAdapter getInstance(Station station) throws Exception {
        if (instance == null) {
            instance = new OpcStreamAdapter(station);
        }
        return instance;
    }

    public static Scanner getStream(Station station) throws Exception {
        if (stream == null) {
            OpcStreamAdapter adapter = getInstance(station);
            InputStream in = adapter.opcClient.getInputStream();
            stream = new Scanner(in);
        }
        return stream;
    }

    public static void disconnect() {
        try {
            getInstance(station).opcClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
