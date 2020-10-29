package com.example.saierclient.ui.wlsensornetview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
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

public class WLSensorNetActivity extends AppCompatActivity implements Runnable{
    private WLSensorNetViewModel wlsensornetViewModel;

    private ListView listCameraView;
    private Thread timerThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlsensornetview);
        wlsensornetViewModel = ViewModelProviders.of(this, new WLSensorNetViewModelFactory())
                .get(WLSensorNetViewModel.class);

        listCameraView = findViewById(R.id.listWlsensorView);

        wlsensornetViewModel.getExchangeResult().observe(this, new Observer<WLSensorNetResult>() {
            @Override
            public void onChanged(@Nullable WLSensorNetResult exchangeResult) {
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

        wlsensornetViewModel.pullCameraDataByType(1, getString(R.string.serverurlroot));
        timerThread = new Thread(this);
        timerThread.start();
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            wlsensornetViewModel.pullCameraDataByType(1, getString(R.string.serverurlroot));
        }
    };

    public void run(){
        while (true){
            try{
                Thread.sleep(3000);
                //wlsensornetViewModel.pullAllSensorData();
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
        String urlroot_w = getString(R.string.imgserverurlroot_w);
        try{int i = 1;
            for (int j=0; j< sensorall.size(); j++) {
                Map<String, String>item=new HashMap<String, String>();
                JSONObject sensor = sensorall.getJSONObject(j);
                item.put("设备名", sensor.get("devicename").toString());
                item.put("获取时间", sensor.get("rltime").toString());
                item.put("运行值", sensor.get("rvalue").toString());
                item.put("图片地址","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598832280508&di=62b47f8a7495eaf341d4b509186615ff&imgtype=0&src=http%3A%2F%2Fpmo77baa6.pic35.websiteonline.cn%2Fupload%2F4.jpg");
//                item.put("图片地址",urlroot_w+sensor.get("imgurl").toString());
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
