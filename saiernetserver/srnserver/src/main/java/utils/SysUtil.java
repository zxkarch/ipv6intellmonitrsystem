package utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import saiernet.server.srnserver.entity.SensorVO;

public class SysUtil {
    public static String getCurLineInfo() {

        int originStackIndex = 2;
        
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[originStackIndex];

        String retStr = "【文件】" + element.getFileName() + "【类】" + element.getClassName() +
                "【方法】" + element.getMethodName() + "【行号】" + element.getLineNumber() + "\n";

        return retStr;
    }
    public static SensorVO convertJSONObjecttoSensorVO(JSONObject obj){
        SensorVO sensorvo =new SensorVO();
        sensorvo.setDevicename(obj.getString("devicename"));
        sensorvo.setTypenote(obj.getString("typenote"));
        sensorvo.setRtime(obj.getString("rtime"));
        sensorvo.setRvalue(obj.getString("rvalue"));
        sensorvo.setImgurl(obj.getString("imgurl"));

        return sensorvo;
    }
    public static List<SensorVO> convertJSONArraytoSensorVOList(JSONArray array){
        List<SensorVO> sensorList = new ArrayList<SensorVO>();
        for(int i=0; i< array.size(); i++){
            SensorVO sensorvo = convertJSONObjecttoSensorVO(array.getJSONObject(i));
            sensorList.add(sensorvo);
        }
        return sensorList;
    }
}