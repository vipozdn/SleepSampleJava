package com.lemon.sleepsamplejava;

import android.app.Application;
import android.content.Context;

import com.lemon.sleepsamplejava.data.db.SleepDatabase;
import com.lemon.sleepsamplejava.data.sharedpreferences.SleepSubscriptionStatus;

public class MainApplication extends Application {

    private SleepDatabase getDatabase() {
        return SleepDatabase.getDatabase(getApplicationContext());
    }

    public SleepRepository getRepository() {
        SleepRepository sleepRepository = new SleepRepository(
                new SleepSubscriptionStatus(
                        getSharedPreferences(
                                getString(R.string.preference_sleep_key), Context.MODE_PRIVATE)),
                        getDatabase().sleepSegmentEventDao(),
                        getDatabase().sleepClassifyEventDao()
                );
        return sleepRepository;
    }

}
