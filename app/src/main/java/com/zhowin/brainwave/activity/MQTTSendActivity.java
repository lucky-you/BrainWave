package com.zhowin.brainwave.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhowin.brainwave.R;
import com.zhowin.brainwave.service.MyService;

public class MQTTSendActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_send;
    private TextView textView;
    private EditText editText;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqttsend);

        btn_send = (Button) findViewById(R.id.btn_send);
        editText = (EditText) findViewById(R.id.edit_send_message);
        textView = (TextView) findViewById(R.id.tv_show_message);
        btn_send.setOnClickListener(this);

        //开启服务
        Intent startIntent = new Intent(MQTTSendActivity.this, MyService.class);
        startService(startIntent);

        //注册广播接收者
        IntentFilter filter = new IntentFilter("com.ww.xhu.mqtt");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View view) {
        MyService.sendMessage("topic1", editText.getText().toString());
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("MQTT");
            textView.setText("收到消息：" + message);
        }
    }
    @Override
    protected void onDestroy() {
        //注销广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
