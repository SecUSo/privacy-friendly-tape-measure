package org.secuso.privacyfriendlyruler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getFragmentManager();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!prefs.contains("lastMode")){
            prefs.edit().putString("lastMode", "ruler").commit();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float dpmm = (float) (displayMetrics.ydpi/25.4);
            prefs.edit().putFloat("dpmm", dpmm).commit();


            WelcomeDialog welcomeDialog = new WelcomeDialog();
            welcomeDialog.show(this.getSupportFragmentManager(), "WelcomeDialog");
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new RulerFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        } else {
            startLastMode();
        }
    }

    public static class WelcomeDialog extends DialogFragment {

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater i = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(i.inflate(R.layout.welcome_dialog, null));
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(getActivity().getString(R.string.welcome));
            builder.setPositiveButton(getActivity().getString(R.string.okay), null);
            builder.setNegativeButton(getActivity().getString(R.string.viewhelp), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getActivity(), HelpActivity.class);
                    getActivity().startActivity(i);
                    dialog.cancel();
                }
            });

            return builder.create();
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            float dpmm = (float) (displayMetrics.ydpi/25.4);
            prefs.edit().putFloat("dpmm", dpmm).commit();
            startLastMode();
            Context context = getApplicationContext();
            CharSequence calibrationResetText = getResources().getString(R.string.calibrationReset);
            int duration = Toast.LENGTH_SHORT;
            Toast calibrationResetToast = Toast.makeText(context, calibrationResetText, duration);
            calibrationResetToast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLastMode();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();

        if (id == R.id.nav_ruler) {
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new RulerFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            prefs.edit().putString("lastMode", "ruler").commit();
            return true;
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new GalleryFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            prefs.edit().putString("lastMode", "gallery").commit();
            return true;
        } else if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new CameraFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            prefs.edit().putString("lastMode", "camera").commit();
            return true;
        } else if (id == R.id.nav_settings) {
            intent.setClass(getBaseContext(), SettingsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.nav_help) {
            intent.setClass(getBaseContext(), HelpActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.nav_about) {
            intent.setClass(getBaseContext(), AboutActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startLastMode() {
        String lastMode = prefs.getString("lastMode", "ruler");
        if (lastMode.equals("ruler")) {
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new RulerFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        } else if (lastMode.equals("gallery")) {
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new GalleryFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        } else { //if (lastMode.equals("camera"))
            fragmentManager.beginTransaction().
                    replace(R.id.content_main, new CameraFragment()).commit();
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        }
    }
}
