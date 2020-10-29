package com.example.saierclient.ui.summaryview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.saierclient.R;
import com.example.saierclient.data.ExchangeRepository;
import com.example.saierclient.data.Result;


public class SummaryViewModel extends ViewModel {
    private MutableLiveData<SummaryResult> exchangeResult = new MutableLiveData<SummaryResult>();
    private ExchangeRepository exchangeRepository;

    SummaryViewModel(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    LiveData<SummaryResult> getExchangeResult() {
        return exchangeResult;
    }

    public void pullAllSensorData(String urlroot) {
        // can be launched in a separate asynchronous job
        Result result = exchangeRepository.pullAllSensorData(urlroot);


        if (result instanceof Result.Success) {
            exchangeResult.setValue(new SummaryResult(result));
        } else if(result instanceof Result.Failure) {
            exchangeResult.setValue(new SummaryResult(((Result.Failure) result).getMsg()));
        } else {
            exchangeResult.setValue(new SummaryResult(R.string.login_failed));
        }
    }

}
