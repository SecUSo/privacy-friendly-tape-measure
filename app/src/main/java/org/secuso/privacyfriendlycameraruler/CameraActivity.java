package org.secuso.privacyfriendlycameraruler;

import android.os.Bundle;

/**
 * Created by roberts on 12.12.16.
 */

public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSharedPreferences.edit().putString("lastMode", "camera").commit();

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() { return R.id.nav_camera; }
}
