package com.lemon.sleepsamplejava;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lemon.sleepsamplejava.data.SleepRepository;
import com.lemon.sleepsamplejava.data.db.SleepClassifyEventEntity;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventEntity;

import java.util.List;

public class MainViewModel extends ViewModel {

    private SleepRepository sleepRepository;
    public LiveData<Boolean> subscribedToSleepDataLiveData = sleepRepository.subscribedToSleepLiveData;

    public LiveData<List<SleepSegmentEventEntity>> allSleepSegments = sleepRepository.allSleepSegmentEvents;

    public LiveData<List<SleepClassifyEventEntity>> allSleepClassifyEventEntities
            = sleepRepository.allSleepClassifyEvents;

    public MainViewModel(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    public void updateSubscribedToSleepData(boolean subscribed) {
        sleepRepository.updateSubscribedToSleepData(subscribed);
    }


}
