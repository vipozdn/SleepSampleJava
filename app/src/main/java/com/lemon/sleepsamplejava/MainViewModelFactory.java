package com.lemon.sleepsamplejava;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lemon.sleepsamplejava.MainViewModel;
import com.lemon.sleepsamplejava.data.SleepRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SleepRepository sleepRepository;

    public MainViewModelFactory(SleepRepository sleepRepository) {
        super();
        this.sleepRepository = sleepRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(sleepRepository);
        }
        throw new IllegalArgumentException();
    }
}
