package com.example.saierclient.ui.robotview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saierclient.R;
import com.example.saierclient.data.ExchangeRepository;
import com.example.saierclient.data.Result;


public class RobotViewModel extends ViewModel {
    private MutableLiveData<RobotResult> exchangeResult = new MutableLiveData<RobotResult>();
    private ExchangeRepository exchangeRepository;

    RobotViewModel(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    LiveData<RobotResult> getExchangeResult() {
        return exchangeResult;
    }

    public void pullCameraDataByType(int showtype, String urlroot) {
        // can be launched in a separate asynchronous job
        Result result = exchangeRepository.pullCameraDataByType(showtype, urlroot);


        if (result instanceof Result.Success) {
            exchangeResult.setValue(new RobotResult(result));
        } else if(result instanceof Result.Failure) {
            exchangeResult.setValue(new RobotResult(((Result.Failure) result).getMsg()));
        } else {
            exchangeResult.setValue(new RobotResult(R.string.login_failed));
        }
    }
}
