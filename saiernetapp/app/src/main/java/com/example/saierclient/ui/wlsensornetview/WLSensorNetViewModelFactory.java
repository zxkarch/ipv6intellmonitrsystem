package com.example.saierclient.ui.wlsensornetview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.saierclient.data.ExchangeDataSource;
import com.example.saierclient.data.ExchangeRepository;

public class WLSensorNetViewModelFactory implements ViewModelProvider.Factory{
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WLSensorNetViewModel.class)) {
            return (T) new WLSensorNetViewModel(ExchangeRepository.getInstance(new ExchangeDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
