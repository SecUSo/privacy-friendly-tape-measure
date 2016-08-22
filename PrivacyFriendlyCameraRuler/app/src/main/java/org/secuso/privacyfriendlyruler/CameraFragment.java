package org.secuso.privacyfriendlyruler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by roberts on 02.06.16.
 */
public class CameraFragment extends Fragment {

    Activity activity;
    View rootView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        container.removeAllViews();

        System.out.println("URI = "+uri);
        ImageView iv = (ImageView) rootView.findViewById(R.id.pictureView);
        iv.setImageURI(uri);

        return rootView;
    }

    public void setUri(Uri val) {
        uri = val;
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