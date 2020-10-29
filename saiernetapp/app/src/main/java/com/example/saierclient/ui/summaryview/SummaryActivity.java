package com.example.saierclient.ui.summaryview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.saierclient.R;
import com.example.saierclient.data.Result;
import com.example.saierclient.data.model.SensorAllVO;
import com.example.saierclient.ui.robotview.RobotActivity;
import com.example.saierclient.ui.wlsensornetview.WLSensorNetActivity;
import com.example.saierclient.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SummaryActivity extends AppCompatActivity implements Runnable{
    private SummaryViewModel summaryViewModel;

    private ListView listSensorView;
    private Thread timerThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summaryview);
        summaryViewModel = ViewModelProviders.of(this, new SummaryViewModelFactory())
                .get(SummaryViewModel.class);

        final Button accButton = findViewById(R.id.accimgbtn);
        final Button insButton = findViewById(R.id.insimgbtn);
        final ProgressBar loadingProgressBar = findViewById(R.id.loadBar);
        listSensorView = findViewById(R.id.listSensorView);

        summaryViewModel.getExchangeResult().observe(this, new Observer<SummaryResult>() {
            @Override
            public void onChanged(@Nullable SummaryResult exchangeResult) {
                if (exchangeResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (exchangeResult.getError() != null) {
                    showPullError(exchangeResult.getError());
                }
                if (exchangeResult.getFailure()!=null){
                    showPullFailed(exchangeResult.getFailure());
                }
                if (exchangeResult.getSuccess() != null) {
                    updateUiWithData(exchangeResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
            }
        });

        accButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setClass(SummaryActivity.this, RobotActivity.class);
                SummaryActivity.this.startActivity(intent);
//                summaryViewModel.pullAllSensorData();
            }
        });

        insButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setClass(SummaryActivity.this, WLSensorNetActivity.class);
                SummaryActivity.this.startActivity(intent);
            }
        });
        summaryViewModel.pullAllSensorData(getString(R.string.serverurlroot));
//        Timer timer=new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                summaryViewModel.pullAllSensorData();
//            }
//        }, 1000,1000);
        timerThread = new Thread(this);
        timerThread.start();
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            summaryViewModel.pullAllSensorData(getString(R.string.serverurlroot));
        }
    };

    public void run(){
        while (true){
            try{
                Thread.sleep(2000);
                //summaryViewModel.pullAllSensorData();
                handler.sendMessage(handler.obtainMessage());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void updateUiWithData(Result res) {
        //实现界面更新
        JSONArray sensorall = ((Result.Success<JSONArray>)res).getData();
        List<Map<String, Object>> items=new ArrayList<Map<String,Object>>();
        try{int i = 1;
        for (int j=0; j< sensorall.size(); j++) {
            Map<String, Object>item=new HashMap<String, Object>();
            JSONObject sensor = sensorall.getJSONObject(j);
            item.put("序号", i);
            item.put("设备名", sensor.get("devicename"));
            item.put("设备类型", sensor.get("typenote"));
            item.put("获取时间", sensor.get("rltime"));
            item.put("运行值", sensor.get("rvalue"));
            items.add(item);
            i++;
        }}catch (Exception e){
            return ;
        }
        SimpleAdapter adapter=new SimpleAdapter(this, items, R.layout.line, new String[]
                {"序号","设备名","设备类型","获取时间","运行值"}, new int[]{R.id.sensorID,R.id.sensorName,R.id.sensorType, R.id.sensorDate,R.id.sensorValue});

        listSensorView.setAdapter(adapter);
    }

    private void showPullFailed(String failureString) {
        Toast.makeText(getApplicationContext(), failureString, Toast.LENGTH_SHORT).show();
    }

    private void showPullError(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
