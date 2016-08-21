package org.secuso.privacyfriendlyruler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by yonjuni on 25.08.15.
 * Shamelessly "borrowed" by roberts on 21.05.16.
 */
public class CalibrationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.calibrate);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#024265")));
    }

    protected void onConfirm(View v) {
        Context context = getApplicationContext();
        CharSequence emptyInputText = getResources().getString(R.string.noInput);
        CharSequence calibrationDoneText = getResources().getString(R.string.calibrationDone);
        int duration = Toast.LENGTH_SHORT;
        Toast emptyInputToast = Toast.makeText(context, emptyInputText, duration);
        Toast calibrationDoneToast = Toast.makeText(context, calibrationDoneText, duration);

        String inputText = ((EditText)findViewById(R.id.input)).getText().toString();

        if (inputText.isEmpty()){ emptyInputToast.show(); }
        else {
            float length = Float.parseFloat(inputText);
            boolean inchMode = ((RadioButton)findViewById(R.id.inchRadioButton)).isChecked();
            if (inchMode) {length = (float)(length * 25.4);}
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
            float dpmm = 300/length;
            prefs.edit().putFloat("dpmm", dpmm).commit();
            calibrationDoneToast.show();
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }

}
