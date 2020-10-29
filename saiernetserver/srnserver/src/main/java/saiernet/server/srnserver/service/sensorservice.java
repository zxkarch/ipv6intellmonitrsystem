package saiernet.server.srnserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saiernet.server.srnserver.dao.sensordao;
import saiernet.server.srnserver.entity.SensorVO;
import saiernet.server.srnserver.entity.recordlist;
import saiernet.server.srnserver.entity.sensorlastvalues;

@Service
public class sensorservice {
    @Autowired
    sensordao sensordaoobj;

    public List<sensorlastvalues> selectAllSensorQuery(){
        return sensordaoobj.selectAllSensorQuery();
    }

    public List<sensorlastvalues> selectCameraByTypeQuery(int showtype){
        if (showtype!=1&&showtype!=2)
            return null;
        return sensordaoobj.selectCameraByTypeQuery(showtype);
    }

	public int insertSensorDatainBatch(List<SensorVO> sensorlist) {
        List<recordlist> reclist = new ArrayList<recordlist>();
        
        for(SensorVO sensor:sensorlist){
            recordlist rec = new recordlist();
            Integer id = sensordaoobj.selectSensorIdbyNameType(sensor.getDevicename(), sensor.getTypenote());
            if (id==null){ return -1;}
            rec.setIddev(id.intValue());
            rec.setRtime(sensor.getRtime());
            rec.setRvalue(sensor.getRvalue());
            rec.setImgurl(sensor.getImgurl());
            reclist.add(rec);
        }
        return sensordaoobj.insertSensorDatainBatch(reclist);
    }

	public int insertSensorData(SensorVO sensor) {
        recordlist rec = new recordlist();
        Integer id = sensordaoobj.selectSensorIdbyNameType(sensor.getDevicename(), sensor.getTypenote());
        if (id==null){ return -1;}
        rec.setIddev(id.intValue());
        rec.setRtime(sensor.getRtime());
        rec.setRvalue(sensor.getRvalue());
        rec.setImgurl(sensor.getImgurl());
		return sensordaoobj.insertSensorData(rec);
	}
    
    
}