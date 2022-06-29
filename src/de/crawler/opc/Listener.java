package de.crawler.opc;

import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredEventItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionNotificationListener;
import com.prosysopc.ua.stack.builtintypes.*;
import com.prosysopc.ua.stack.core.NotificationData;
import de.crawler.opc.models.Crawl;
import de.crawler.opc.models.ExtendedSensor;

import java.sql.SQLException;
import java.util.HashMap;

public class Listener implements SubscriptionNotificationListener {

    private HashMap<String, ExtendedSensor> sensorList;


    public Listener(HashMap<String, ExtendedSensor> sensorList) {
        this.sensorList = sensorList;
    }

    @Override
    public void onBufferOverflow(Subscription subscription, UnsignedInteger unsignedInteger, ExtensionObject[] extensionObjects) {

    }

    @Override
    public void onDataChange(Subscription subscription, MonitoredDataItem monitoredDataItem, DataValue dataValue) {
        String msg = String.format("Item '%s' changed its value from '%s' to '%s'",
                monitoredDataItem.getNodeId().getValue(),
                monitoredDataItem.getValue().getValue(),
                dataValue.getValue());

        ExtendedSensor sensor = sensorList.get(monitoredDataItem.getNodeId().getValue());
        if (sensor != null) {
            de.crawler.opc.models.DataValue dv = sensor.getData();
            dv.setValue(String.valueOf(dataValue.getValue()));
            sensor.setData(dv);

            try {
                sensor.update();
                Crawl.addChangeEvent(sensor.getId(), msg, "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Subscription subscription, Object o, Exception e) {

    }

    @Override
    public void onEvent(Subscription subscription, MonitoredEventItem monitoredEventItem, Variant[] variants) {

    }

    @Override
    public long onMissingData(Subscription subscription, UnsignedInteger unsignedInteger, long l, long l1, StatusCode statusCode) {
        return 0;
    }

    @Override
    public void onNotificationData(Subscription subscription, NotificationData notificationData) {

    }

    @Override
    public void onStatusChange(Subscription subscription, StatusCode statusCode, StatusCode statusCode1, DiagnosticInfo diagnosticInfo) {

    }
}
