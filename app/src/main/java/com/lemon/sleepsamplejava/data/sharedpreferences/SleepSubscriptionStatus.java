package com.lemon.sleepsamplejava.data.sharedpreferences;


import android.content.SharedPreferences;

import com.lemon.sleepsamplejava.R;

/**
 * Saves the sleep data subscription status into a SharedPreferences.
 * Used to check if the app is still listening to changes in sleep data when the app is brought
 * back into the foreground.
 */

public class SleepSubscriptionStatus {

    private SharedPreferences sharedPref;
    private final String SUBSCRIBED_TO_SLEEP_DATA = "subscribed_to_sleep_data";

    public SleepSubscriptionStatus(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }

    public boolean subscribedToSleepData() {
        return sharedPref.getBoolean(SUBSCRIBED_TO_SLEEP_DATA, false);
    }

    // asynchronously update status
    public void updateSubscribedToSleepData(boolean subscribedToSleepData) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SUBSCRIBED_TO_SLEEP_DATA, subscribedToSleepData);
        editor.apply();
    }

}
