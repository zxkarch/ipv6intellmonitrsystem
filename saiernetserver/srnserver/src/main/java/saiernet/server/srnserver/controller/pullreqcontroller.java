package saiernet.server.srnserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saiernet.server.srnserver.entity.ReqPack;
import saiernet.server.srnserver.entity.RespPack;
import saiernet.server.srnserver.entity.sensorlastvalues;
import saiernet.server.srnserver.service.sensorservice;

@RestController
@RequestMapping(value = "/pull")
public class pullreqcontroller {
    @Autowired
    sensorservice sensorserviceobj;

    @PostMapping(value = "/all")
    public RespPack selectAllSensor(@RequestBody ReqPack reqPack) {
        System.err.println("pull all sensor data.");
        List<sensorlastvalues> resraw=null;

        try {
            resraw = sensorserviceobj.selectAllSensorQuery();
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "查询意外");
        }

        return RespPack.respPackSuc(resraw);
    }

    @PostMapping(value = "/camera")
    public RespPack selectSingleSensor(@RequestBody ReqPack reqPack) {
        System.err.println("pull camera data.");
        List<sensorlastvalues> resraw=null;
        int showtype=0;

        try {
            showtype = reqPack.getData().getIntValue("showtype");
            System.err.println(showtype);
            if (showtype!=1&&showtype!=2){
                return RespPack.respPackFail(3, "参数错误");
            }

        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(2, "参数解析异常");
        }

        try {
            resraw = sensorserviceobj.selectCameraByTypeQuery(showtype);
        } catch (Exception e) {
            //throw UnifiedException.create(1, "参数异常", SysUtil.getCurLineInfo());
            return RespPack.respPackFail(1, "查询意外");
        }

        return RespPack.respPackSuc(resraw);
    }
}