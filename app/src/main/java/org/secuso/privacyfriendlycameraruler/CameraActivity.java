package org.secuso.privacyfriendlycameraruler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by roberts on 12.12.16.
 */

public class CameraActivity extends BaseActivity {

    private Status status = Status.MODE_CHOICE;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private ImageView pictureView;
    private CameraRulerView drawView;
    private FloatingActionsMenu newMeasureButton;
    private FloatingActionButton newTetragonButton;
    private FloatingActionButton newTriangleButton;
    private FloatingActionButton newCircleButton;
    private FloatingActionButton newLineButton;
    private FloatingActionButton confirmButton;
    private TextView output;
    Uri uri;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    private float scale;

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

    enum Status {
        MODE_CHOICE,
        REFERENCE,
        MEASUREMENT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSharedPreferences.edit().putString("lastMode", "camera").commit();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        cameraButton = (ImageButton) findViewById(R.id.from_camera_button);
        galleryButton = (ImageButton) findViewById(R.id.from_gallery_button);
        pictureView = (ImageView) findViewById(R.id.pictureView);
        confirmButton = (FloatingActionButton) findViewById(R.id.confirm_reference);
        output = (TextView) findViewById(R.id.output_tf);

        newMeasureButton = (FloatingActionsMenu) findViewById(R.id.new_measure_fam);
        newTetragonButton = (FloatingActionButton) findViewById(R.id.new_tetragon_fab);
        newTriangleButton = (FloatingActionButton) findViewById(R.id.new_triangle_fab);
        newCircleButton = (FloatingActionButton) findViewById(R.id.new_circle_fab);
        newLineButton = (FloatingActionButton) findViewById(R.id.new_line_fab);

        drawView = new CameraRulerView(getBaseContext(), output, status);
        drawView.setVisibility(GONE);
        RelativeLayout cl = (RelativeLayout) findViewById(R.id.camera_ruler_layout);
        cl.addView(drawView);

        matrix.postRotate(90);
        pictureView.setImageMatrix(matrix);
        matrix.postScale(2.5f, 2.5f);
        pictureView.setImageMatrix(matrix);
        matrix.postTranslate(displayMetrics.widthPixels,
                pictureView.getDrawable().getCurrent().getIntrinsicWidth());
        pictureView.setImageMatrix(matrix);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, false);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReference();
            }
        });

        newTetragonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.newTetragon();
            }
        });

        newTriangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.newTriangle();
            }
        });

        newCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.newCircle();
            }
        });

        newLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.newLine();
            }
        });

        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                startImageFragment(data.getData());
            } else {
                if (resultCode != RESULT_CANCELED) {
                    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                        Toast.makeText(this, R.string.camera_crash, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.gallery_crash, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void startImageFragment(Uri uri) {
        status = Status.REFERENCE;
        cameraButton.setVisibility(GONE);
        cameraButton.setClickable(false);
        galleryButton.setVisibility(GONE);
        galleryButton.setClickable(false);
        pictureView.setImageURI(uri);
        pictureView.setVisibility(VISIBLE);
        drawView.setVisibility(VISIBLE);
        drawView.setClickable(true);
        drawView.bringToFront();
        confirmButton.setVisibility(VISIBLE);
    }

    public void setReference() {
        //TODO: compute scale from reference and saved object size
        status = Status.MEASUREMENT;
        confirmButton.setVisibility(GONE);
        newMeasureButton.setVisibility(VISIBLE);
        output.setVisibility(VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (status == Status.REFERENCE) {
            status = Status.MODE_CHOICE;
            cameraButton.setVisibility(VISIBLE);
            cameraButton.setClickable(true);
            galleryButton.setVisibility(VISIBLE);
            galleryButton.setClickable(true);
            drawView.setVisibility(GONE);
            drawView.setClickable(false);
            pictureView.setVisibility(GONE);
            pictureView.setImageURI(Uri.EMPTY);
            confirmButton.setVisibility(GONE);
        } else if (status == Status.MEASUREMENT) {
            status = Status.REFERENCE;
            newMeasureButton.collapseImmediately();
            newMeasureButton.setVisibility(GONE);
            output.setVisibility(GONE);
            drawView.measure = null;
            drawView.invalidate();
            confirmButton.setVisibility(VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_camera;
    }
}
