package org.secuso.privacyfriendlycameraruler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.secuso.privacyfriendlycameraruler.cameraruler.CameraActivity;
import org.secuso.privacyfriendlycameraruler.screenruler.RulerActivity;
import org.secuso.privacyfriendlycameraruler.tutorial.PrefManager;
import org.secuso.privacyfriendlycameraruler.tutorial.DisclaimerDialog;

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
        System.out.println(lastMode);
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