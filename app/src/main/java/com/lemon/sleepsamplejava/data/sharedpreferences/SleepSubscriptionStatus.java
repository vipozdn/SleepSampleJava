package com.lemon.sleepsamplejava.data.sharedpreferences;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.lemon.sleepsamplejava.R;

/**
 * Saves the sleep data subscription status into a SharedPreferences.
 * Used to check if the app is still listening to changes in sleep data when the app is brought
 * back into the foreground.
 */

public class SleepSubscriptionStatus extends LiveData<Boolean> {

    private SharedPreferences sharedPref;
    private final String SUBSCRIBED_TO_SLEEP_DATA = "subscribed_to_sleep_data";
    private final String TAG = "SleepSubscriptionStatus";

    public SleepSubscriptionStatus(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    Log.d(TAG, "LiveData: set value " + subscribedToSleepData());
                    setValue(subscribedToSleepData());
                }
            };

    public boolean subscribedToSleepData() {
        return sharedPref.getBoolean(SUBSCRIBED_TO_SLEEP_DATA, false);
    }

    // asynchronously update status
    public void updateSubscribedToSleepData(boolean subscribedToSleepData) {
        Log.d(TAG, "updateSubscribedToSleepData: new status is " + subscribedToSleepData);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SUBSCRIBED_TO_SLEEP_DATA, subscribedToSleepData);
        editor.apply();
    }

    @Override
    protected void onActive() {
        super.onActive();
        setValue(subscribedToSleepData());
        sharedPref.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }
}
