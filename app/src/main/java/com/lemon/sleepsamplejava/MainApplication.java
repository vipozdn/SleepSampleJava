package com.lemon.sleepsamplejava;

import android.app.Application;
import com.lemon.sleepsamplejava.data.datastore.SLEEP_PREFERENCES_NAME;


import com.lemon.sleepsamplejava.data.db.SleepDatabase;

public class MainApplication extends Application {

    private SleepDatabase getDatabase() {
        return SleepDatabase.getDatabase(getApplicationContext());
    }

    public SleepRepository getRepository() {
        SleepRepository sleepRepository = new SleepRepository(
                new SleepSubscriptionStatus(
                        new RxPreferenceDataStoreBuilder(getApplicationContext(), SLEEP_PREFERENCES_NAME, "settings").build(),
                        getDatabase().sleepSegmentEventDao(),
                        getDatabase().sleepClassifyEventDao()
                )
        );
        return sleepRepository;
    }

}
