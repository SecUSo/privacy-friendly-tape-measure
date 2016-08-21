package org.secuso.privacyfriendlyruler;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by roberts on 21.05.16.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}