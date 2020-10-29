package com.example.saierclient.ui.robotview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.example.saierclient.ui.common.MyImageListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotActivity extends AppCompatActivity implements Runnable{
    private RobotViewModel robotViewModel;

    private ListView listCameraView;
    private Thread timerThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robotview);
        robotViewModel = ViewModelProviders.of(this, new RobotViewModelFactory())
                .get(RobotViewModel.class);
        
        listCameraView = findViewById(R.id.listRobotView);

        robotViewModel.getExchangeResult().observe(this, new Observer<RobotResult>() {
            @Override
            public void onChanged(@Nullable RobotResult exchangeResult) {
                if (exchangeResult == null) {
                    return;
                }
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

        robotViewModel.pullCameraDataByType(2, getString(R.string.serverurlroot));
        timerThread = new Thread(this);
        timerThread.start();
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            robotViewModel.pullCameraDataByType(2, getString(R.string.serverurlroot));
        }
    };

    public void run(){
        while (true){
            try{
                Thread.sleep(5000);
                //robotViewModel.pullAllSensorData();
                handler.sendMessage(handler.obtainMessage());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void updateUiWithData(Result res) {
        //实现界面更新
        JSONArray sensorall = ((Result.Success<JSONArray>)res).getData();
        List<Map<String, String>> items=new ArrayList<Map<String,String>>();
        String urlRoot = getString(R.string.imgserverurlroot_r);
        try{int i = 1;
            for (int j=0; j< sensorall.size(); j++) {
                Map<String, String>item=new HashMap<String, String>();
                JSONObject sensor = sensorall.getJSONObject(j);
                item.put("设备名", sensor.get("devicename").toString());
                item.put("获取时间", sensor.get("rltime").toString());
                item.put("运行值", sensor.get("rvalue").toString());
                item.put("图片地址", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3827616703,913778115&fm=26&gp=0.jpg");
//                item.put("图片地址",urlRoot+sensor.get("imgurl").toString());
                items.add(item);
                i++;
            }}catch (Exception e){
            return ;
        }
//        SimpleAdapter adapter=new SimpleAdapter(this, items, R.layout.cameraline, new String[]
//                {"设备名","获取时间","运行值"}, new int[]{R.id.cameraName,R.id.cameraDate,R.id.cameraValue});
        MyImageListAdapter adapter = new MyImageListAdapter(this, R.layout.cameraline, items);
        listCameraView.setAdapter(adapter);
    }

    private void showPullFailed(String failureString) {
        Toast.makeText(getApplicationContext(), failureString, Toast.LENGTH_SHORT).show();
    }

    private void showPullError(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}