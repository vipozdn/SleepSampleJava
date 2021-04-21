package com.lemon.sleepsamplejava.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.lemon.sleepsamplejava.MainApplication;
import com.lemon.sleepsamplejava.data.SleepRepository;

public class BootReceiver extends BroadcastReceiver {

    private final String TAG = "BootReceiver";
    private LiveData<Boolean> subscribedToSleepLiveData;
    private boolean subscribedToSleepData;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive action: " + intent.getAction());

        SleepRepository repository = ((MainApplication)context.getApplicationContext()).getRepository();
        subscribedToSleepLiveData = repository.subscribedToSleepLiveData;
        subscribedToSleepLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newSubscribedToSleepData) {
                if (subscribedToSleepData != newSubscribedToSleepData) {
                    subscribedToSleepData = newSubscribedToSleepData;
                }
            }
        });

        if (subscribedToSleepData) {
            subscribeToSleepSegmentUpdates();
        }
    }


}
