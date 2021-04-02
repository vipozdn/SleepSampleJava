package com.lemon.sleepsamplejava.data;

import androidx.lifecycle.LiveData;

import com.lemon.sleepsamplejava.data.db.SleepClassifyEventDao;
import com.lemon.sleepsamplejava.data.db.SleepClassifyEventEntity;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventDao;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventEntity;
import com.lemon.sleepsamplejava.data.sharedpreferences.SleepSubscriptionStatus;

import java.util.List;

public class SleepRepository {

    private SleepSubscriptionStatus sleepSubscriptionStatus;
    private SleepSegmentEventDao sleepSegmentEventDao;
    private SleepClassifyEventDao sleepClassifyEventDao;

    public LiveData<Boolean> subscribedToSleepLiveData;

    public LiveData<List<SleepSegmentEventEntity>> allSleepSegmentEvents = sleepSegmentEventDao.getAll();
    public LiveData<List<SleepClassifyEventEntity>> allSleepClassifyEvents = sleepClassifyEventDao.getAll();


    public SleepRepository(SleepSubscriptionStatus sleepSubscriptionStatus,
                           SleepSegmentEventDao sleepSegmentEventDao,
                           SleepClassifyEventDao sleepClassifyEventDao) {
        this.sleepSubscriptionStatus = sleepSubscriptionStatus;
        this.sleepSegmentEventDao = sleepSegmentEventDao;
        this.sleepClassifyEventDao = sleepClassifyEventDao;

        subscribedToSleepLiveData = sleepSubscriptionStatus;
    }

    // Methods for SleepSegmentEventDao

    public void updateSubscribedToSleepData(boolean subscribedToSleepData) {
        sleepSubscriptionStatus.updateSubscribedToSleepData(subscribedToSleepData);
    }

    public void insertSleepSegment(SleepSegmentEventEntity sleepSegmentEventEntity) {
        sleepSegmentEventDao.insert(sleepSegmentEventEntity);
    }

    public void insertSleepSegments(List<SleepSegmentEventEntity> sleepSegmentEventEntities) {
        sleepSegmentEventDao.insertAll(sleepSegmentEventEntities);
    }

    // Methods for SleepClassifyEventDao

    public void insertSleepClassifyEvent(SleepClassifyEventEntity sleepClassifyEventEntity) {
        sleepClassifyEventDao.insert(sleepClassifyEventEntity);
    }

    public void insertSleepClassifyEvents(List<SleepClassifyEventEntity> sleepClassifyEventEntities) {
        sleepClassifyEventDao.insertAll(sleepClassifyEventEntities);
    }
}
