package com.lemon.sleepsamplejava;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lemon.sleepsamplejava.data.db.SleepClassifyEventEntity;
import com.lemon.sleepsamplejava.data.db.SleepSegmentEventEntity;
import com.lemon.sleepsamplejava.databinding.ActivityMainBinding;
import com.lemon.sleepsamplejava.receiver.SleepReceiver;

import java.util.Calendar;
import java.util.List;

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
                    subscribedToSleepData = newSubscribedToSleepData;
                }
            }
        });

        mainViewModel.allSleepSegments.observe(this, new Observer<List<SleepSegmentEventEntity>>() {
            @Override
            public void onChanged(List<SleepSegmentEventEntity> sleepSegmentEventEntities) {
                Log.d(TAG, "sleepSegmentEventEntities: " + sleepSegmentEventEntities);

                // Todo: format String
                if (!sleepSegmentEventEntities.isEmpty()) {
                    sleepSegmentOutput = sleepSegmentEventEntities.toString();
                    updateOutput();
                }
            }
        });

        mainViewModel.allSleepClassifyEventEntities.observe(this, new Observer<List<SleepClassifyEventEntity>>() {
            @Override
            public void onChanged(List<SleepClassifyEventEntity> sleepClassifyEventEntities) {
                Log.d(TAG, "sleepClassifyEventEntities: " + sleepClassifyEventEntities);

                // Todo: format String
                if (!sleepClassifyEventEntities.isEmpty()) {
                    sleepClassifyOutput = sleepClassifyEventEntities.toString();
                    updateOutput();
                }
            }
        });

        sleepPendingIntent = SleepReceiver.createSleepReceiverPendingIntent(this);
    }

    public void onClickRequestSleepData(View view) {
        if(activityRecognitionPermissionApproved()) {
            if (subscribedToSleepData) {
                unsubscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            } else {
                subscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }
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

}