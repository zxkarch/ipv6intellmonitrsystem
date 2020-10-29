package com.example.saierclient.ui.summaryview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.saierclient.data.ExchangeDataSource;
import com.example.saierclient.data.ExchangeRepository;

public class SummaryViewModelFactory implements ViewModelProvider.Factory{
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SummaryViewModel.class)) {
            return (T) new SummaryViewModel(ExchangeRepository.getInstance(new ExchangeDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
