package org.secuso.privacyfriendlycameraruler;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

/**
 * Created by roberts on 12.12.16.
 */

public class CameraActivity extends BaseActivity {

    // Helper
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.edit().putString("lastMode", "camera").commit();
        mHandler = new Handler();

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() { return R.id.nav_camera; }
}
