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

package org.secuso.privacyfriendlycameraruler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.secuso.privacyfriendlycameraruler.cameraruler.CameraActivity;
import org.secuso.privacyfriendlycameraruler.screenruler.RulerActivity;
import org.secuso.privacyfriendlycameraruler.tutorial.PrefManager;
import org.secuso.privacyfriendlycameraruler.tutorial.DisclaimerDialog;

/**
 * Main activity of the app. Has no content but acts as a relay station between both co-main
 * activities (RulerActivity and CameraActivity) of the app. Launches the disclaimer dialog fragment
 * on first app start.
 *
 * @author Roberts Kolosovs
 */
public class MainActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);

        if (prefManager.isFirstTimeLaunch()) {
            FragmentManager fm = getSupportFragmentManager();
            DisclaimerDialog disclaimerDialog = new DisclaimerDialog();
            disclaimerDialog.show(fm, "DisclaimerDialog");
        } else {
            startLastMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefManager.isFirstTimeLaunch()) {
            startLastMode();
        }
    }

    private void startLastMode() {
        String lastMode = prefManager.getLastMode();
        Intent intent = new Intent();

        if (lastMode.equals("ruler")) {
            intent.setClass(getBaseContext(), RulerActivity.class);
        } else if (lastMode.equals("camera")) {
            intent.setClass(getBaseContext(), CameraActivity.class);
        }
        startActivityForResult(intent, 0);
        finish();
    }
}