package org.secuso.privacyfriendlycameraruler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by roberts on 02.06.16.
 */
public class RulerActivity extends BaseActivity {

    // Helper
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.edit().putString("lastMode", "ruler").commit();
        mHandler = new Handler();

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
        // Inflate the menu; this adds items to the action bar if it is present.
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


//    Activity activity;
//    View rootView;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        rootView = inflater.inflate(R.layout.activity_ruler, container, false);
//        container.removeAllViews();
//
//        RelativeLayout rulerLayout = (RelativeLayout) rootView.findViewById(R.id.fragment_ruler);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
//        float dpmm = prefs.getFloat("dpmm", (float) (displayMetrics.ydpi/25.4));
//
//        RulerView rulerView = new RulerView(activity.getBaseContext(), dpmm,
//                dpmm*25.4/32, PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext()));
//        rulerLayout.addView(rulerView);
//
//        return rootView;
//    }
//
//    public void onResume() {
//        super.onResume();
//    }
//
//    public void onPause() {
//        super.onPause();
//    }

}
