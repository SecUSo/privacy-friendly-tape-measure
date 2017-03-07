/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 * <p>
 * The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 * In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 * License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 * header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.MainActivity;
import org.secuso.privacyfriendlycameraruler.database.PFASQLiteHelper;
import org.secuso.privacyfriendlycameraruler.database.UserDefinedReferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.util.Locale.*;

/**
 * Class structure taken from tutorial at http://www.androidhive.info/2016/05/android-build-intro-slider-app/
 * Displays the tutorial in the activity_tutorial layout.
 *
 * @author Karola Marky
 * @version 20161214
 */

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        } else {
            Runnable initPrefs = new Runnable() {
                @Override
                public void run() {
                    prefManager.addHiddenPrefs();
                }
            };
            Runnable fillDB = new Runnable() {
                @Override
                public void run() {
                    fillDatabase();
                }
            };
            Runnable smartPrefSetup = new Runnable() {
                @Override
                public void run() {
                    smartPreferenceInitialize();
                }
            };
            new Thread(initPrefs).run();
            new Thread(fillDB).run();
            new Thread(smartPrefSetup).run();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_tutorial);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.tutorial_slide1,
                R.layout.tutorial_slide2,
                R.layout.tutorial_slide3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.okay));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * Puts ten empty, inactive user defined reference objects into the database.
     */
    private void fillDatabase() {
        PFASQLiteHelper dbHelper = new PFASQLiteHelper(getBaseContext());
        for (int i = 0; i < 10; i++) {
            dbHelper.addUserDefinedRef(new UserDefinedReferences());
        }
    }

    /**
     * Sets the screen rulers, the camera ruler unit of measurement and the active reference objects
     * according to the phone's language.
     */
    private void smartPreferenceInitialize() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = pref.edit();

        Locale loc = Locale.getDefault();

        ArrayList<Locale> eurozoneLocales = new ArrayList<>();
        eurozoneLocales.add(new Locale("de"));
        eurozoneLocales.add(new Locale("fr"));
        eurozoneLocales.add(new Locale("nl"));
        eurozoneLocales.add(new Locale("et"));
        eurozoneLocales.add(new Locale("fi"));
        eurozoneLocales.add(new Locale("el"));
        eurozoneLocales.add(new Locale("it"));
        eurozoneLocales.add(new Locale("lv"));
        eurozoneLocales.add(new Locale("lt"));
        eurozoneLocales.add(new Locale("pt"));
        eurozoneLocales.add(new Locale("sk"));
        eurozoneLocales.add(new Locale("sl"));
        eurozoneLocales.add(new Locale("es"));
        eurozoneLocales.add(new Locale("lb"));
        eurozoneLocales.add(new Locale("mt"));
        eurozoneLocales.add(new Locale("ga"));
        eurozoneLocales.add(new Locale("tr"));

        Set<String> activePrefs = new HashSet<>();

        if (loc.equals(US)) { //set units and rulers to imperial and activate US paper
            editor.putString("pref_units_of_measurement", "in");
            editor.putString("pref_leftruler", "inch");
            editor.putString("pref_rightruler", "inch");
            activePrefs.add("us-paper");
        } else { //set units and rulers to SI and activate ISO 216 paper
            editor.putString("pref_units_of_measurement", "mm");
            editor.putString("pref_leftruler", "cm");
            editor.putString("pref_rightruler", "cm");
            activePrefs.add("iso216-paper");
        }

        if (loc.equals(US)) { //set US currency
            activePrefs.add("us-coins");
            activePrefs.add("us-notes");
        } else if (loc.equals(UK)) { //set UK currency
            activePrefs.add("gb-coins");
            activePrefs.add("gb-notes");
        } else if (eurozoneLocales.contains(loc)) { //set EU currency
            activePrefs.add("eu-coins");
            activePrefs.add("eu-notes");
        } else { //set EU and USD currency
            activePrefs.add("us-coins");
            activePrefs.add("us-notes");
            activePrefs.add("eu-coins");
            activePrefs.add("eu-notes");
        }

        editor.putStringSet("pref_type_selection", activePrefs);
        editor.commit();
    }
}