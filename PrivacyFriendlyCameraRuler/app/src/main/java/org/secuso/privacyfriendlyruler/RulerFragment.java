package org.secuso.privacyfriendlyruler;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by roberts on 02.06.16.
 */
public class RulerFragment extends Fragment {

    Activity activity;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ruler, container, false);
        container.removeAllViews();

        RelativeLayout rulerLayout = (RelativeLayout) rootView.findViewById(R.id.fragment_ruler);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        float dpmm = prefs.getFloat("dpmm", (float) (displayMetrics.ydpi/25.4));

        RulerView rulerView = new RulerView(activity.getBaseContext(), dpmm,
                dpmm*25.4/32, PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext()));
        rulerLayout.addView(rulerView);

        return rootView;
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

}
