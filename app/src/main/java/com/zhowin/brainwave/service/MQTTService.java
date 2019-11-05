package com.zhowin.brainwave.service;

import android.app.Service;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class MQTTService extends Service {
    //客户端
    private static MqttClient mClient;
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;

    /**
     * 设置连接服务器地址和端口
     *
     * @return
     */
    public abstract String getHost();

    /**
     * 设置clientId(客户端ID)
     *
     * @return
     */
    public abstract String getClientId();

    /**
     * 订阅的主题 数组
     *
     * @return
     */
    protected abstract String[] getTopics();

    /**
     * 处理服务发来的消息
     *
     * @param topic   主题
     * @param message 对应主题发送的消息
     */
    protected abstract void handleMessage(String topic, String message);

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mClient = new MqttClient(getHost(), getClientId(), new MemoryPersistence());
            //消息的配置参数
            options = new MqttConnectOptions();
            //是否清除在此以前的消息
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(10);
            mClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    //设置重连
                    reconnect();
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    //处理消息
                    handleMessage(s, mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            //连接
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("xy", e.getMessage());
        }
    }

    /**
     * 连接
     */
    private void connect() {
        if (mClient != null) {
            if (!mClient.isConnected()) {
                if (!mClient.isConnected()) {
                    try {
                        mClient.connect(options);  //连接
                        mClient.subscribe(getTopics()); //订阅
                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.d("xy", e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 重连接
     */
    private void reconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 发送消息
     *
     * @param topic   主题
     * @param message 消息
     * @return
     */
    public static boolean sendMessage(String topic, String message) {
        if (mClient == null) {
            return false;
        }
        if (!mClient.isConnected()) {
            return false;
        }
        MqttTopic temperatureTopic = mClient.getTopic(topic);
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        try {
            temperatureTopic.publish(mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("xy", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (mClient.isConnected()) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
                Log.d("xy", e.getMessage());
            }
        }
    }
}
