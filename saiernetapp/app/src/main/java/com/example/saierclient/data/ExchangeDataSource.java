package com.example.saierclient.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.saierclient.data.model.CameraVO;
import com.example.saierclient.data.model.ReqVO;
import com.example.saierclient.data.model.ResponseVO;
import com.example.saierclient.data.model.SensorAllVO;
import com.example.saierclient.utils.MyHttpClientUtils;

import java.io.IOException;
import java.util.List;

public class ExchangeDataSource {
    public Result<List<SensorAllVO>> pullAllSensorData(String urlroot) {

        try {
            // TODO: handle loggedInUser authentication
            //String baseurl = this.getString(R.string.mess_1);
//            String url = "http://192.168.0.100:8080/pull/all";
            String url = urlroot+"/pull/all";
            JSONObject json = new JSONObject();
            json.put("version","2.0");
            json.put("data",new ReqVO(-1));
            String post = json.toString();
            String respcontent = MyHttpClientUtils.createHttpsPostByjson(url,post,"application/json");

            ResponseVO<JSONArray> responvo = JSONObject.parseObject(respcontent, ResponseVO.class);
            if (responvo.getCode()==0) {
                JSONArray sensorAllVOList= responvo.getData();
                return new Result.Success<>(sensorAllVOList);
            }else{
                return new Result.Failure(responvo.getDesc());
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("意外发生：", e));
        }
    }

    public Result<List<CameraVO>> pullCameraDataByType(int showtype,String urlroot){

        try {
            // TODO: handle loggedInUser authentication
//            String url = "http://192.168.0.100:8080/pull/camera";
            String url = urlroot+"/pull/camera";
            JSONObject json = new JSONObject();
            json.put("version","2.0");
            json.put("data",new ReqVO(showtype));
            String post = json.toString();
            String respcontent = MyHttpClientUtils.createHttpsPostByjson(url,post,"application/json");
            ResponseVO<List<CameraVO>> responvo= JSONObject.parseObject(respcontent, ResponseVO.class);
            if (responvo.getCode()==0) {
                return new Result.Success<>(responvo.getData());
            }else{
                return new Result.Failure(responvo.getDesc());
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("意外发生：", e));
        }
    }
}
