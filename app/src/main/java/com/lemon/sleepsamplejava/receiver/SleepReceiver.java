package com.lemon.sleepsamplejava.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;
import com.lemon.sleepsamplejava.MainApplication;
import com.lemon.sleepsamplejava.data.SleepRepository;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventEntity;

import java.util.List;

public class SleepReceiver extends BroadcastReceiver {

    private final String TAG = "SleepReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        SleepRepository repository = ((MainApplication)context.getApplicationContext()).getRepository();

        // Check whether the Intent has any Event.
        if (SleepSegmentEvent.hasEvents(intent)) {
            List<SleepSegmentEvent> sleepSegmentEvents =
                    SleepSegmentEvent.extractEvents(intent);
            Log.d(TAG, "SleepSegmentEvents list: " + sleepSegmentEvents);
            addSleepSegmentEventsToDataBase(repository, sleepSegmentEvents);
        } else if (SleepClassifyEvent.hasEvents(intent)){
            List<SleepClassifyEvent> sleepClassifyEvents =
                    SleepClassifyEvent.extractEvents(intent);
            Log.d(TAG, "SleepClassifyEvents list: " + sleepClassifyEvents);
            addSleepClassifyEventsToDataBase(repository, sleepClassifyEvents);
        }
    }



    private void addSleepSegmentEventsToDataBase(SleepRepository repository,
                                                 List<SleepSegmentEvent> sleepSegmentEvents) {
        if (!sleepSegmentEvents.isEmpty()) {
            
        }
    }

    private void addSleepClassifyEventsToDataBase(SleepRepository repository,
                                                  List<SleepClassifyEvent> sleepClassifyEvents) {


    }

    public static PendingIntent createSleepReceiverPendingIntent(Context context) {
        Intent sleepIntent = new Intent(context, SleepReceiver.class);
        return PendingIntent.getBroadcast(
                context, 0, sleepIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
