package com.example.saierclient.data;

//import com.example.saierclient.data.model.CameraVO;
//import com.example.saierclient.data.model.SensorAllVO;

import java.util.List;

public class ExchangeRepository {
    private static volatile ExchangeRepository instance;

    private ExchangeDataSource dataSource;

//    private List<SensorAllVO> sensorAllVO = null;
//
//    private void setSensorAllVO(List<SensorAllVO> sensorAllVO) {
//        this.sensorAllVO = sensorAllVO;
//    }

//    public List<SensorAllVO> getSensorAllVO() {
//        return sensorAllVO;
//    }
//
//    public void setNullSensorAllVO() {
//        this.sensorAllVO = null;
//    }

//    private List<CameraVO> cameraVO = null;
//
//    private void setCameraVO(List<CameraVO> cameraVO) {
//        this.cameraVO = cameraVO;
//    }
//
//    public void setNullCameraVO() {
//        this.cameraVO = null;
//    }
//
//    public List<CameraVO> getCameraVO() {
//        return cameraVO;
//    }

    // 私有构造函数：单例访问
    private ExchangeRepository(ExchangeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ExchangeRepository getInstance(ExchangeDataSource dataSource) {
        if (instance == null) {
            instance = new ExchangeRepository(dataSource);
        }
        return instance;
    }

    public Result pullAllSensorData(String urlroot) {
        Result result = dataSource.pullAllSensorData(urlroot);
//        if (result instanceof Result.Success) {
//            setSensorAllVO(((Result.Success<List<SensorAllVO>>) result).getData());
//        }
        return result;
    }

    public Result pullCameraDataByType(int showtype, String urlroot){
        Result result = dataSource.pullCameraDataByType(showtype, urlroot);
//        if (result instanceof Result.Success) {
//            setCameraVO(((Result.Success<List<CameraVO>>) result).getData());
//        }
        return result;
    }

}
