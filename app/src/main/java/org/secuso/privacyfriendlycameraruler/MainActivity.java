package org.secuso.privacyfriendlycameraruler;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getFragmentManager();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!prefs.contains("lastMode")) {
            prefs.edit().putString("lastMode", "camera").commit();

//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            float dpmm = (float) (displayMetrics.ydpi / 25.4);
//            prefs.edit().putFloat("dpmm", dpmm).commit();

            WelcomeDialog welcomeDialog = new WelcomeDialog();
            welcomeDialog.show(this.getSupportFragmentManager(), "WelcomeDialog");

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), CameraActivity.class);
            startActivityForResult(intent, 0);

            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        } else {
            startLastMode();
        }

    }
//
//    @Override
//    public void onBackPressed() {
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.ruler_action_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_calibration) {
//            Intent intent = new Intent();
//            intent.setClass(getBaseContext(), CalibrationActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        }
//        if (id == R.id.action_resetcalibration) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            float dpmm = (float) (displayMetrics.ydpi / 25.4);
//            prefs.edit().putFloat("dpmm", dpmm).commit();
//            startLastMode();
//            Context context = getApplicationContext();
//            CharSequence calibrationResetText = getResources().getString(R.string.action_reset_calibration);
//            int duration = Toast.LENGTH_SHORT;
//            Toast calibrationResetToast = Toast.makeText(context, calibrationResetText, duration);
//            calibrationResetToast.show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    protected int getNavigationDrawerID()
//    {
//        return R.id.nav_ruler;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        startLastMode();
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        Intent intent = new Intent();
//
//        if (id == R.id.nav_ruler) {
//            intent.setClass(getBaseContext(), RulerActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        } else if (id == R.id.nav_camera) {
//            intent.setClass(getBaseContext(), CameraActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        } else if (id == R.id.nav_settings) {
//            intent.setClass(getBaseContext(), SettingsActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        } else if (id == R.id.nav_help) {
//            intent.setClass(getBaseContext(), HelpActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        } else if (id == R.id.nav_about) {
//            intent.setClass(getBaseContext(), AboutActivity.class);
//            startActivityForResult(intent, 0);
//            return true;
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private void startLastMode() {
        String lastMode = prefs.getString("lastMode", "camera");
        Intent intent = new Intent();

        if (lastMode.equals("ruler")) {
            intent.setClass(getBaseContext(), RulerActivity.class);
            startActivityForResult(intent, 0);
        } else if (lastMode.equals("camera")) {
            intent.setClass(getBaseContext(), CameraActivity.class);
            startActivityForResult(intent, 0);
        }
    }
}