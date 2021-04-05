package com.lemon.sleepsamplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.lemon.sleepsamplejava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainApplication mainApplication;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainApplication = (MainApplication) getApplication();
        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(mainApplication.getRepository())).get(MainViewModel.class);


    }
}