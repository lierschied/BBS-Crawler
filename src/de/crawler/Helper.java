package de.crawler;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.stack.builtintypes.LocalizedText;
import com.prosysopc.ua.stack.builtintypes.NodeId;
import com.prosysopc.ua.stack.core.ApplicationDescription;
import com.prosysopc.ua.stack.core.ApplicationType;
import com.prosysopc.ua.stack.core.EndpointDescription;
import com.prosysopc.ua.stack.transport.security.SecurityMode;
import de.crawler.opc.Listener;
import de.crawler.opc.enums.Station;
import de.crawler.opc.models.ExtendedSensor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class Helper {
    public static final HashMap<String, ExtendedSensor> sensorList = new HashMap();

    private static void init() throws SQLException {
        Connection dbc = DbConnection.getConnection();
        PreparedStatement stm = dbc.prepareStatement("SELECT id, name FROM sensor");
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            String name = rs.getNString("name");

            ExtendedSensor sensor = new ExtendedSensor(name);
            String id = rs.getString("id");
            sensor.setId(id);
            sensorList.put(name, sensor);
        }
    }

    public static void test() {
        Station station = Station.Test;
        try {
            UaClient client = new UaClient(station.getUrl());
            client.setSecurityMode(SecurityMode.NONE);
            if (station != Station.Test) {
                client.setUserIdentity(new UserIdentity("MES", "training"));
            }

            ApplicationDescription applicationDescription = new ApplicationDescription();
            applicationDescription.setApplicationName(new LocalizedText("Sample Client", Locale.US));
            applicationDescription.setApplicationType(ApplicationType.Client);
            ApplicationIdentity identity = new ApplicationIdentity();
            identity.setApplicationDescription(applicationDescription);
            client.setApplicationIdentity(identity);

            EndpointDescription[] discoverEndpoints = client.discoverEndpoints();

            if (station == Station.Test) {
                client.setEndpoint(discoverEndpoints[11]);
            }

            Subscription subscription = new Subscription();
            MonitoredDataItem[] items = getItems();
            subscription.addItems(items);
            Helper.init();
            subscription.addNotificationListener(new Listener(sensorList));
            client.addSubscription(subscription);

            client.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MonitoredDataItem[] getItems() {
        String[] sensors = {
                "\"+AL-BG2_Bandsensor_Rechts\"",
                "\"+AL-BG1_Bandsensor_Links\"",
                "\"+AL-BG4_Stopper_Rechts\"",
                "\"+AL-BG3_Stopper_Links\"",
                "\"+AO-BG1_PosLinks\"",
                "\"+AO-BG2_PosRechts\"",
                "\"+AO-BG3_StopperLinks\"",
                "\"+AO-BG4_StopperRechts\"",
                "\"+AO-BG5_VereinzelerOffen\"",
                "\"+AO-BG6_HubUnten\"",
                "\"+AO-BG7_HubMitte\"",
                "\"+AO-BG8_HubOben\"",
                "\"+AM-BG1_PosLinks\"",
                "\"+AM-BG2_PosRechts\"",
                "\"+AM-BG3_StopperLinks\"",
                "\"+AM-BG4_StopperRechts\"",
                "\"+AM-BG5_VereinzelerOffen\"",
                "\"+AM-BG6_HubUnten\"",
                "\"+AM-BG7_HubMitte\"",
                "\"+AM-BG8_HubOben\"",
                "\"+AN-BG1_InduktivUnten\"",
                "\"+AM-BL1_Füllstand\"",
                "\"+AO-BL2_Füllstand\"",
                "\"+AL-MA1.Rechtslauf\"",
                "\"+AL-MA1.Linkslauf\"",
                "\"+AL-MB2_Stopper_Rechts\"",
                "\"+AL-MB1_Stopper_Links\"",
                "\"+AL-MA1.Vmax\"",
                "\"+AO-MB1_StopperLinks\"",
                "\"+AO-MB2_StopperRechts\"",
                "\"+AO-MB3_Vereinzeler\"",
                "\"+AO-MB4_HubUnten\"",
                "\"+AO-MB5_HubOben\"",
                "\"+AM-MB1_StopperLinks\"",
                "\"+AM-MB2_StopperRechts\"",
                "\"+AM-MB3_Vereinzeler\"",
                "\"+AM-MB4_HubUnten\"",
                "\"+AM-MB5_HubOben\"",
        };

        return Arrays.stream(sensors).map(s -> {
            NodeId nodeId = new NodeId(3, s);
            return new MonitoredDataItem(nodeId);
        }).toArray(MonitoredDataItem[]::new);
    }
}
