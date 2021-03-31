package com.lemon.sleepsamplejava.data;

import androidx.lifecycle.LiveData;

import com.lemon.sleepsamplejava.data.db.SleepClassifyEventDao;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventDao;
import com.lemon.sleepsamplejava.data.sharedpreferences.SleepSubscriptionStatus;

public class SleepRepository {

    private SleepSubscriptionStatus sleepSubscriptionStatus;
    private SleepSegmentEventDao sleepSegmentEventDao;
    private SleepClassifyEventDao sleepClassifyEventDao;

    public LiveData<Boolean> subscribedToSleepLiveData;

    public SleepRepository(SleepSubscriptionStatus sleepSubscriptionStatus,
                           SleepSegmentEventDao sleepSegmentEventDao,
                           SleepClassifyEventDao sleepClassifyEventDao) {
        this.sleepSubscriptionStatus = sleepSubscriptionStatus;
        this.sleepSegmentEventDao = sleepSegmentEventDao;
        this.sleepClassifyEventDao = sleepClassifyEventDao;

        subscribedToSleepLiveData = sleepSubscriptionStatus;
    }

    public void updateSubscribedToSleepData(boolean subscribedToSleepData) {
        sleepSubscriptionStatus.updateSubscribedToSleepData(subscribedToSleepData);
    }

}
