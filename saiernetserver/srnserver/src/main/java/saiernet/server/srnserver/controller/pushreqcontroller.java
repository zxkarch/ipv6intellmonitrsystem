package saiernet.server.srnserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saiernet.server.srnserver.entity.ReqPack;
import saiernet.server.srnserver.entity.ReqPack2;
import saiernet.server.srnserver.entity.RespPack;
import saiernet.server.srnserver.entity.SensorVO;
import saiernet.server.srnserver.service.sensorservice;
import utils.SysUtil;

@RestController
@RequestMapping(value = "/push")
public class pushreqcontroller {
    @Autowired
    sensorservice sensorserviceobj;

    @PostMapping(value = "/batch")
    public RespPack insertSensorDatainBatch(@RequestBody ReqPack2 reqPack) {
        System.err.println("push sensor data in batch.");
        
        List<SensorVO> sensorlist=null;
        try {
             sensorlist= SysUtil.convertJSONArraytoSensorVOList(reqPack.getData());
             
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "参数解析异常");
        }
        if(sensorlist==null || sensorlist.size()==0){
            return RespPack.respPackFail(2, "参数错误");
        }

        int rid=sensorserviceobj.insertSensorDatainBatch(sensorlist);
        if(rid<=0){
            return RespPack.respPackFail(3, "未知添加异常");
        }
        return RespPack.respPackSuc(1);
    }

    @PostMapping(value = "/one")
    public RespPack insertSensorData(@RequestBody ReqPack reqPack) {
        System.err.println("push sensor data.");
        SensorVO sensor=null;
        try {
             sensor= SysUtil.convertJSONObjecttoSensorVO(reqPack.getData());
             
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "参数解析异常");
        }
        if(sensor==null){
            return RespPack.respPackFail(1, "参数错误");
        }
        
        int rid=sensorserviceobj.insertSensorData(sensor);
        if(rid<=0){
            return RespPack.respPackFail(3, "未知添加异常");
        }
        return RespPack.respPackSuc(1);
    }

}