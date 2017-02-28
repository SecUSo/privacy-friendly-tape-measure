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

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by yonjuni on 15.06.16.
 */
public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(BaseActivity.MAIN_CONTENT_FADEIN_DURATION);
        }

        overridePendingTransition(0, 0);
    }

    //@Override
    //protected int getNavigationDrawerID() {
    //    return R.id.nav_about;
    //}
}

