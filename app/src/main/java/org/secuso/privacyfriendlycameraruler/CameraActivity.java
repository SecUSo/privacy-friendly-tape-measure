package org.secuso.privacyfriendlycameraruler;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by roberts on 12.12.16.
 */

public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSharedPreferences.edit().putString("lastMode", "camera").commit();

        ImageButton cameraButton = (ImageButton) findViewById(R.id.from_camera_button);
        ImageButton galleryButton = (ImageButton) findViewById(R.id.from_gallery_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() { return R.id.nav_camera; }
}
