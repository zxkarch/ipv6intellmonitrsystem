package com.example.saierclient.ui.wlsensornetview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saierclient.R;
import com.example.saierclient.data.ExchangeRepository;
import com.example.saierclient.data.Result;

public class WLSensorNetViewModel extends ViewModel{

    private MutableLiveData<WLSensorNetResult> exchangeResult = new MutableLiveData<WLSensorNetResult>();
    private ExchangeRepository exchangeRepository;

    WLSensorNetViewModel(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    LiveData<WLSensorNetResult> getExchangeResult() {
        return exchangeResult;
    }

    public void pullCameraDataByType(int showtype,String urlroot) {
        // can be launched in a separate asynchronous job
        Result result = exchangeRepository.pullCameraDataByType(showtype, urlroot);


        if (result instanceof Result.Success) {
            exchangeResult.setValue(new WLSensorNetResult(result));
        } else if(result instanceof Result.Failure) {
            exchangeResult.setValue(new WLSensorNetResult(((Result.Failure) result).getMsg()));
        } else {
            exchangeResult.setValue(new WLSensorNetResult(R.string.login_failed));
        }
    }
}
