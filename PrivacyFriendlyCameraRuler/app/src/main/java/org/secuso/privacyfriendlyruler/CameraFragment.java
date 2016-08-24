package org.secuso.privacyfriendlyruler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by roberts on 02.06.16.
 */
public class CameraFragment extends Fragment implements OnTouchListener {

    Activity activity;
    View rootView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri uri;

    private static final String TAG = "Touch";

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        container.removeAllViews();

        System.out.println("URI = "+uri);
        ImageView iv = (ImageView) rootView.findViewById(R.id.pictureView);
        iv.setImageURI(uri);
        FrameLayout fl = (FrameLayout) rootView.findViewById(R.id.fragment_camera);

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        matrix.postRotate(-90);
        iv.setImageMatrix(matrix);

        matrix.postTranslate(dm.widthPixels/2 - iv.getDrawable().getCurrent().getIntrinsicHeight()/2,
                dm.heightPixels/2 + iv.getDrawable().getCurrent().getIntrinsicWidth()/2);
        iv.setImageMatrix(matrix);

        iv.setOnTouchListener(this);

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

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        // make the image scalable as a matrix
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG" );
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE" );
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //second finger down
                oldDist = spacing(event); // calculates the distance between two points where user touched.
                Log.d(TAG, "oldDist=" + oldDist);
                // minimal distance between both the fingers
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched.
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM" );
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                { //movement of first finger
                    matrix.set(savedMatrix);
                    if (view.getLeft() >= -392)
                    {
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    }
                }
                else if (mode == ZOOM) { //pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist/oldDist; //thinking I need to play around with this value to limit it**
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        // Perform the transformation
        view.setImageMatrix(matrix);

        return true; // indicate event was handled
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}