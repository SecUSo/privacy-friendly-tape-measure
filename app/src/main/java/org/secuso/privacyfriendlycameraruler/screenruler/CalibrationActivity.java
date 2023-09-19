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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.secuso.privacyfriendlycameraruler.R;

/**
 * Activity for manual calibration of the screen ruler. Uses the activity_calibration layout.
 *
 * @author Roberts Kolosovs
 * Created by yonjuni on 25.08.15.
 * Shamelessly "borrowed" by rkolsovs on 21.05.16.
 */
public class CalibrationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        final View line = findViewById(R.id.line);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.calibrate);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#024265")));
        }
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                CharSequence emptyInputText = getResources().getString(R.string.noInput);
                CharSequence calibrationDoneText = getResources().getString(R.string.calibrationDone);
                int duration = Toast.LENGTH_SHORT;
                Toast emptyInputToast = Toast.makeText(context, emptyInputText, duration);
                Toast calibrationDoneToast = Toast.makeText(context, calibrationDoneText, duration);

                String inputText = ((EditText) findViewById(R.id.input)).getText().toString();

                if (inputText.isEmpty()) {
                    emptyInputToast.show();
                } else {
                    float length = Float.parseFloat(inputText);
                    boolean inchMode = ((RadioButton) findViewById(R.id.inchRadioButton)).isChecked();
                    if (inchMode) {
                        length = (float) (length * 25.4);
                    }
                    length = Math.min(40, Math.max(3, length));
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Log.d("Line Height", line.getHeight()+"");
                    float dpmm = line.getMeasuredHeight() / length;
                    prefs.edit().putFloat("dpmm", dpmm).apply();
                    calibrationDoneToast.show();
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), RulerActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

}
