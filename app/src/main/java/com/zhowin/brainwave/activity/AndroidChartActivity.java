package com.zhowin.brainwave.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.zhowin.brainwave.R;
import com.zhowin.brainwave.model.CompositeIndexBean;
import com.zhowin.brainwave.model.LineIncomeBean;
import com.zhowin.brainwave.model.LineIncomeList;
import com.zhowin.brainwave.service.MyMqttService;
import com.zhowin.brainwave.service.MyService;
import com.zhowin.brainwave.utils.LocalJsonResolutionUtils;
import com.zhowin.brainwave.view.LineChartManager;

import java.util.List;

/**
 * 绘制曲线
 */
public class AndroidChartActivity extends AppCompatActivity {

    private LineChart lineChart;
    private LineChart lineChartTwo;
    private LineIncomeBean lineIncomeBean;
    private List<CompositeIndexBean> incomeBeanList;
    private LineChartManager lineChartManager1;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_chart);
        initViews();
        initDateToViews();
    }

    private void initDateToViews() {

        //开启服务
        Intent startIntent = new Intent(AndroidChartActivity.this, MyService.class);
        startService(startIntent);

        //注册广播接收者
        IntentFilter filter = new IntentFilter("com.ww.xhu.mqtt");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("MQTT");
//            Toast.makeText(getApplicationContext(), "收到消息: " + message, Toast.LENGTH_LONG).show();
            if (!TextUtils.isEmpty(message)) {
                LineIncomeBean incomeList = LocalJsonResolutionUtils.JsonToObject(message, LineIncomeBean.class);
                if (incomeList != null) {
                    incomeBeanList = incomeList.getResult().getCompositeIndexGEM();
                    lineChartManager1 = new LineChartManager(lineChart);
                    lineChartManager1.showLineChart(incomeBeanList, "", getResources().getColor(R.color.blue));
                    lineChartManager1.addLine(incomeBeanList, "", getResources().getColor(R.color.green));
                }
            }
        }
    }

    private void initViews() {
        lineChart = findViewById(R.id.lineChart);
        lineChartTwo = findViewById(R.id.lineChartTwo);

        LineIncomeBean lineIncomeBean = LocalJsonResolutionUtils.JsonToObject(this, "line_chart.json", LineIncomeBean.class);
//        List<CompositeIndexBean> incomeBeanList = lineIncomeBean.getResult().getCompositeIndexShanghai();
//        List<CompositeIndexBean> incomeBeanList = lineIncomeBean.getResult().getCompositeIndexShenzhen();
        List<CompositeIndexBean> incomeBeanList = lineIncomeBean.getResult().getClientAccumulativeRate();

        lineChartManager1 = new LineChartManager(lineChartTwo);
        lineChartManager1.showLineChart(incomeBeanList, "", getResources().getColor(R.color.blue));
        lineChartManager1.addLine(incomeBeanList, "", getResources().getColor(R.color.green));


    }


    @Override
    protected void onDestroy() {
        //注销广播
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
