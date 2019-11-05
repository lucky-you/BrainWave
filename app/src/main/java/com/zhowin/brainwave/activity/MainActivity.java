package com.zhowin.brainwave.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhowin.brainwave.R;
import com.zhowin.brainwave.permission.DialogHelper;
import com.zhowin.brainwave.permission.PermissionConstants;
import com.zhowin.brainwave.permission.PermissionUtils;
import com.zhowin.brainwave.service.MyMqttService;
import com.zhowin.brainwave.service.MyService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "xy";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnpublish).setOnClickListener(this::onClick);
        findViewById(R.id.startMqttActivity).setOnClickListener(this::onClick);
        findViewById(R.id.MPAndroidChart).setOnClickListener(this::onClick);
        requestPermission();
    }


    /**
     * 申请权限
     */
    public void requestPermission() {
        PermissionUtils.permission(PermissionConstants.PHONE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        Log.d(TAG, "onDenied: 权限被拒绝后弹框提示");
                        DialogHelper.showRationaleDialog(shouldRequest, MainActivity.this);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        mIntent = new Intent(MainActivity.this, MyMqttService.class);
                        //开启服务
                        startService(mIntent);
                        Log.d(TAG, "success: 权限获取成功");
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        Log.d(TAG, "onDenied: 权限被拒绝");
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog(MainActivity.this);
                        }
                    }
                })
                .request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止服务
        stopService(mIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnpublish:
                //模拟闸机设备发送消息过来
                MyMqttService.publish("tourist enter");
                break;
            case R.id.startMqttActivity:
                mIntent = new Intent(MainActivity.this, MQTTSendActivity.class);
                startActivity(mIntent);
                break;
            case R.id.MPAndroidChart:
                mIntent = new Intent(MainActivity.this, AndroidChartActivity.class);
                startActivity(mIntent);
                break;

        }
    }
}
