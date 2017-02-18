package org.secuso.privacyfriendlycameraruler.tutorial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * Class structure taken from tutorial at http://www.androidhive.info/2016/05/android-build-intro-slider-app/
 */
public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LAST_MODE = "lastMode";
    private static final String UNITS_OF_MEASUREMENT = "pref_units_of_measurement";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void addHiddenPrefs() {
        editor.putString(LAST_MODE, "camera").commit();
        editor.commit();
    }

    public String getLastMode() {
        return pref.getString(LAST_MODE, "camera");
    }

//    public String getUnitOfMeasurement() {
//        return pref.getString(UNITS_OF_MEASUREMENT, "in");
//    }

    public void putLastMode(String value) {
        editor.putString(LAST_MODE, value).commit();
        editor.commit();
    }
}