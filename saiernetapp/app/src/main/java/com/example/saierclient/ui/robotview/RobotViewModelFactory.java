package com.example.saierclient.ui.robotview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.saierclient.data.ExchangeDataSource;
import com.example.saierclient.data.ExchangeRepository;

public class RobotViewModelFactory implements ViewModelProvider.Factory{
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RobotViewModel.class)) {
            return (T) new RobotViewModel(ExchangeRepository.getInstance(new ExchangeDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
