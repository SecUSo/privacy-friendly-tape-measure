/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.screenruler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.secuso.privacyfriendlycameraruler.BaseActivity;
import org.secuso.privacyfriendlycameraruler.R;

/**
 * Created by roberts on 02.06.16.
 */
public class RulerActivity extends BaseActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefManager.putLastMode("ruler");

        RelativeLayout rulerLayout = (RelativeLayout) findViewById(R.id.ruler_content);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float dpmm = mSharedPreferences.getFloat("dpmm", (float) (displayMetrics.ydpi/25.4));

        RulerView rulerView = new RulerView(getBaseContext(), dpmm,
                dpmm*25.4/32, PreferenceManager.getDefaultSharedPreferences(getBaseContext()));
        rulerLayout.addView(rulerView);

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_ruler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ruler_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_calibration) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), CalibrationActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.action_resetcalibration) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float dpmm = (float) (displayMetrics.ydpi / 25.4);
            mSharedPreferences.edit().putFloat("dpmm", dpmm).commit();
            Context context = getApplicationContext();
            CharSequence calibrationResetText = getResources().getString(R.string.action_reset_calibration);
            int duration = Toast.LENGTH_SHORT;
            Toast calibrationResetToast = Toast.makeText(context, calibrationResetText, duration);
            calibrationResetToast.show();
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), RulerActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
