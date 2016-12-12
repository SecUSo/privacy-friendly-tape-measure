package org.secuso.privacyfriendlycameraruler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!prefs.contains("lastMode")) {
            prefs.edit().putString("lastMode", "camera").commit();

            WelcomeDialog welcomeDialog = new WelcomeDialog();
            welcomeDialog.show(this.getSupportFragmentManager(), "WelcomeDialog");

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), CameraActivity.class);
            startActivityForResult(intent, 0);
            finish();
        } else {
            startLastMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLastMode();
    }

    private void startLastMode() {
        String lastMode = prefs.getString("lastMode", "camera");
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