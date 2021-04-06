package com.lemon.sleepsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.lemon.sleepsamplejava.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainApplication mainApplication;
    private MainViewModel mainViewModel;

    private String sleepSegmentOutput = "";
    private String sleepClassifyOutput = "";

    private boolean subscribedToSleepData = false;



    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainApplication = (MainApplication) getApplication();
        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(mainApplication.getRepository())).get(MainViewModel.class);

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

}