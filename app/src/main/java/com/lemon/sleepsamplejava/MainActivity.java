package com.lemon.sleepsamplejava;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.lemon.sleepsamplejava.data.db.SleepClassifyEventEntity;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventEntity;
import com.lemon.sleepsamplejava.databinding.ActivityMainBinding;
import com.lemon.sleepsamplejava.receiver.SleepReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainApplication mainApplication;
    private MainViewModel mainViewModel;

    private String sleepSegmentOutput = "";
    private String sleepClassifyOutput = "";
    private boolean subscribedToSleepData = false;

    private PendingIntent sleepPendingIntent;
    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainApplication = (MainApplication) getApplication();
        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(mainApplication.getRepository())).get(MainViewModel.class);

        mainViewModel.subscribedToSleepDataLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newSubscribedToSleepData) {
                if (subscribedToSleepData != newSubscribedToSleepData) {
                    Log.d(TAG, "newSubscribedToSleepData: " + newSubscribedToSleepData);
                    setSubscribedToSleepData(newSubscribedToSleepData);
                }
            }
        });

        mainViewModel.allSleepSegments.observe(this, new Observer<List<SleepSegmentEventEntity>>() {
            @Override
            public void onChanged(List<SleepSegmentEventEntity> sleepSegmentEventEntities) {
                Log.d(TAG, "sleepSegmentEventEntities: " + sleepSegmentEventEntities);

                if (!sleepSegmentEventEntities.isEmpty()) {
                    //sleepSegmentOutput = sleepSegmentEventEntities.toString();
                    sleepSegmentOutput = sleepSegmentEventEntities.stream()
                            .map(a -> "startTime: " + millisToTime(a.startTimeMillis) + ", " +
                                    "endTime: " + millisToTime(a.endTimeMillis) + ", " +
                                    "status: " + a.status)
                            .collect(Collectors.joining("\n\n"));
                    updateOutput();
                }
                Log.d(TAG, "sleepSegmentOutput: " + sleepSegmentOutput);
            }
        });

        mainViewModel.allSleepClassifyEventEntities.observe(this, new Observer<List<SleepClassifyEventEntity>>() {
            @Override
            public void onChanged(List<SleepClassifyEventEntity> sleepClassifyEventEntities) {
                Log.d(TAG, "sleepClassifyEventEntities: " + sleepClassifyEventEntities);

                if (!sleepClassifyEventEntities.isEmpty()) {
                    sleepClassifyOutput = sleepClassifyEventEntities.stream()
                            .map(a -> "timestampMillis: " + millisToTime(a.timestampMillis)  + ", " +
                                    "confidence: " + a.confidence + ", " +
                                    "motion: " + a.motion + ", " +
                                    "light: " + a.light)
                            .collect(Collectors.joining("\n\n"));
                    updateOutput();
                }
                Log.d(TAG, "sleepClassifyOutput: " + sleepClassifyOutput);
            }
        });

        sleepPendingIntent = SleepReceiver.createSleepReceiverPendingIntent(this);
    }

    public void onClickRequestSleepData(View view) {
        if (activityRecognitionPermissionApproved()) {
            if (subscribedToSleepData) {
                Log.d(TAG, "current state - subscribedToSleepData: unsubscribing");
                unsubscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            } else {
                Log.d(TAG, "current state - unsubscribedToSleepData: subscribing");
                subscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }

    }

    // Permission is checked before this method is called.
    @SuppressLint("MissingPermission")
    private void subscribeToSleepSegmentUpdates(Context context, PendingIntent pendingIntent) {
        Log.d(TAG, "requestSleepSegmentUpdates()");

        Task<Void> task = ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
                pendingIntent,
                // Registers for both [SleepSegmentEvent] and [SleepClassifyEvent] data.
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
        );

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mainViewModel.updateSubscribedToSleepData(true);
                Log.d(TAG, "Successfully subscribed to sleep data.");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Exception when subscribing to sleep data: " + e);
            }
        });
    }

    private void unsubscribeToSleepSegmentUpdates(Context context, PendingIntent pendingIntent) {
        Log.d(TAG, "unsubscribeToSleepSegmentUpdates()");
        Task<Void> task = ActivityRecognition.getClient(context).removeSleepSegmentUpdates(pendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mainViewModel.updateSubscribedToSleepData(false);
                Log.d(TAG, "Successfully unsubscribed to sleep data.");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Exception when unsubscribing to sleep data: " + e);
            }
        });
    }

    private boolean activityRecognitionPermissionApproved() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACTIVITY_RECOGNITION);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    binding.outputTextView.setText(getString(R.string.permission_approved));
                } else {
                    displayPermissionSettingsSnackBar();
                }
            });

    private void displayPermissionSettingsSnackBar() {
        Snackbar.make(
                binding.getRoot(),
                R.string.permission_rational,
                Snackbar.LENGTH_LONG
        )
                .setAction(R.string.action_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts(
                                "package",
                                getPackageName(),
                                null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.d(TAG, "uri: " + uri.toString());
                    }
                }).show();
    }


    private void setSubscribedToSleepData(boolean newSubscribedToSleepData) {
        this.subscribedToSleepData = newSubscribedToSleepData;
        if (newSubscribedToSleepData) {
            binding.button.setText(getString(R.string.sleep_button_unsubscribe_text));
        } else {
            binding.button.setText(getString(R.string.sleep_button_subscribe_text));
        }
        updateOutput();
    }

    private void updateOutput() {
        Log.d(TAG, "updateOutput()");

        String header = "";

        if (subscribedToSleepData) {
            String timestamp = Calendar.getInstance().getTime().toString();
            header = getString(R.string.main_output_header1_subscribed_sleep_data, timestamp);
        } else {
            header = getString(R.string.main_output_header1_unsubscribed_sleep_data);
        }

        String sleepData = getString(
                R.string.main_output_header2_and_sleep_data,
                sleepSegmentOutput,
                sleepClassifyOutput
        );

        String newOutput = header + sleepData;
        binding.outputTextView.setText(newOutput);
    }

    private String millisToTime(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }


}