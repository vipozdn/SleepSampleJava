package com.lemon.sleepsamplejava;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lemon.sleepsamplejava.data.SleepRepository;

public class MainViewModel extends ViewModel {

    private SleepRepository sleepRepository;
    public LiveData<Boolean> subscribedToSleepDataLiveData = sleepRepository.subscribedToSleepLiveData;

    public MainViewModel(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    public void updateSubscribedToSleepData(boolean subscribed) {
        sleepRepository.updateSubscribedToSleepData(subscribed);
    }

}
