package com.zhowin.brainwave.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.zhowin.brainwave.R;

import java.util.UUID;

public class MyService extends MQTTService {


    @Override
    public String getHost() {
        return "tcp://192.168.1.241:1883";
    }

    @Override
    public String getClientId() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }

    @Override
    protected String[] getTopics() {
        return new String[]{"topic1", "topic2", "topic3"};
    }

    @Override
    protected void handleMessage(String topic, String message) {
        Log.d("xy", "接收到的消息：" + message);
        //弹出通知栏
        showNotification(topic + ":" + message);
        //发送广播
        Intent intent = new Intent();
//        intent.putExtra("MQTT", topic + ":" + message);
        intent.putExtra("MQTT", message);
        intent.setAction("com.ww.xhu.mqtt");
        this.sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 显示通知栏
     */
    private void showNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("MQTT")//设置通知栏标题  
                .setContentText(message)//设置通知栏显示内容
                .setNumber(10)//设置通知集合的数量  
                .setTicker("通知来啦")//通知首次出现在通知栏，带上升动画效果的  
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级  
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
                .setOngoing(false)//ture，设置他为一个正在进行的通知  
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON  
        notificationManager.notify(1, mBuilder.build());
    }
}
