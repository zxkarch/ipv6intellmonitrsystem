package saiernet.server.srnserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import saiernet.server.srnserver.entity.recordlist;
import saiernet.server.srnserver.entity.sensorlastvalues;

@Repository
public interface sensordao {
    List<sensorlastvalues> selectAllSensorQuery();
    List<sensorlastvalues> selectCameraByTypeQuery(@Param("showtype") int showtype);
    // int insertSingleSensorData();
    // int insertBatchSensorData();
    int insertSensorDatainBatch(@Param("sensorlist") List<recordlist> sensorlist);
    Integer selectSensorIdbyNameType(@Param("devicename") String devicename, @Param("typenote") String typenote);
	int insertSensorData(@Param("rec") recordlist rec);
}