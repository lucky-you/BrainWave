package com.zhowin.brainwave;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface OnMessageArrivedListener {

    void onArrivedSuccess(String topic, MqttMessage message);

    void onDeliveryComplete(IMqttDeliveryToken arg0);
}
