package com.lemon.sleepsamplejava.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lemon.sleepsamplejava.MainApplication;
import com.lemon.sleepsamplejava.data.SleepRepository;


/**
 * Resubscribes to Sleep data if the device is rebooted.
 */
public class BootReceiver extends BroadcastReceiver {

    private final String TAG = "BootReceiver";
    private LiveData<Boolean> subscribedToSleepLiveData;
    private boolean subscribedToSleepData;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive action: " + intent.getAction());

        SleepRepository repository = ((MainApplication)context.getApplicationContext()).getRepository();
        subscribedToSleepLiveData = repository.subscribedToSleepLiveData;
        subscribedToSleepLiveData.observe((LifecycleOwner) this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newSubscribedToSleepData) {
                if (subscribedToSleepData != newSubscribedToSleepData) {
                    subscribedToSleepData = newSubscribedToSleepData;
                }
            }
        });

        if (subscribedToSleepData) {
            subscribeToSleepSegmentUpdates(context, SleepReceiver.createSleepReceiverPendingIntent(context));
        }
    }

    @SuppressLint("MissingPermission")
    private void subscribeToSleepSegmentUpdates(Context context, PendingIntent pendingIntent) {
        Log.d(TAG, "subscribeToSleepSegmentUpdates()");

        if (activityRecognitionPermissionApproved(context)) {
            Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
                    pendingIntent,
                    SleepSegmentRequest.getDefaultSleepSegmentRequest()
            );

            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Successfully subscribed to sleep data from boot.");
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Exception when subscribing to sleep data from boot: " +  e);
                    unsubscribeStatusForSleepData(((MainApplication)context.getApplicationContext()).getRepository());
                }
            });

        } else {
            Log.d(TAG, "Failed to subscribed to sleep data from boot; Permission removed.");
            unsubscribeStatusForSleepData(((MainApplication)context.getApplicationContext()).getRepository());
        }
    }

    /**
     * Updates the app's boolean for sleep subscription status.
     *
     * Note: This happens because an exception occurred or the permission was removed, so the app
     * is no longer subscribed to sleep data.
     */
    private void unsubscribeStatusForSleepData(SleepRepository repository) {
        repository.updateSubscribedToSleepData(false);
    }

    private boolean activityRecognitionPermissionApproved(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION);
    }

}

