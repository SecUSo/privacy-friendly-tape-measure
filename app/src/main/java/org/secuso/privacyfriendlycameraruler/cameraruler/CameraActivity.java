package org.secuso.privacyfriendlycameraruler.cameraruler;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.secuso.privacyfriendlycameraruler.BaseActivity;
import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.database.ReferenceManager;
import org.secuso.privacyfriendlycameraruler.database.ReferenceObject;

import java.util.ArrayList;

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
    private Menu refsMenu;

    private ArrayList<ReferenceObject> refs;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private String referenceObjectShape = "circle";
    private float referenceObjectSize;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();

    //variables for image transformation
//    Matrix savedMatrix = new Matrix();
//    Uri uri;
//    private static final String TAG = "Touch";
//    // We can be in one of these 3 states
//    static final int NONE = 0;
//    static final int DRAG = 1;
//    static final int ZOOM = 2;
//    int mode = NONE;
//    // Remember some things for zooming
//    PointF start = new PointF();
//    PointF mid = new PointF();
//    float oldDist = 1f;

    enum Status {
        MODE_CHOICE,
        REFERENCE,
        MEASUREMENT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        prefManager.putLastMode("camera");
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        refs = ReferenceManager.getAllActiveRefPredefObjects(getBaseContext());

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

        drawView = new CameraRulerView(getBaseContext(), output);
        drawView.ctxStatus = status;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        refsMenu = menu;
        for (int i = 0; i < refs.size(); i++) {
            menu.add(0, i, Menu.NONE, refs.get(i).name);//.setIcon(R.drawable.your-add-icon) to add icon to menu item
            menu.getItem(i).setVisible(false);
        }
        referenceObjectShape = refs.get(0).type.shape;
        referenceObjectSize = refs.get(0).size;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        ReferenceObject refObj = refs.get(item.getItemId());
        referenceObjectShape = refObj.type.shape;
        referenceObjectSize = refObj.size;

        if (referenceObjectShape.equals("circle") && !(drawView.reference instanceof Circle)) {
            drawView.reference = new Circle(new Point(400, 400), 100);
        } else if (referenceObjectShape.equals("tetragon") && !(drawView.reference instanceof Tetragon)) {
            drawView.reference = new Tetragon(new Point(400, 400), new Point(600, 400),
                    new Point(600, 600), new Point(400, 600));
        }

        drawView.invalidate();
        return false;
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
        drawView.ctxStatus = status;
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
        showMenu();
    }

    public void setReference() {
        if (drawView.reference instanceof Circle) {
            drawView.scale = referenceObjectSize/(((Circle) drawView.reference).radius*2);
        } else {
            drawView.scale = (float) Math.sqrt(referenceObjectSize/((Polygon) drawView.reference).getArea());
        }

        status = Status.MEASUREMENT;
        drawView.ctxStatus = status;
        confirmButton.setVisibility(GONE);
        newMeasureButton.setVisibility(VISIBLE);
        output.setVisibility(VISIBLE);
        drawView.invalidate();
        hideMenu();
    }

    @Override
    public void onBackPressed() {
        if (status == Status.REFERENCE) {
            status = Status.MODE_CHOICE;
            drawView.ctxStatus = status;
            cameraButton.setVisibility(VISIBLE);
            cameraButton.setClickable(true);
            galleryButton.setVisibility(VISIBLE);
            galleryButton.setClickable(true);
            drawView.setVisibility(GONE);
            drawView.setClickable(false);
            pictureView.setVisibility(GONE);
            pictureView.setImageURI(Uri.EMPTY);
            confirmButton.setVisibility(GONE);
            hideMenu();
        } else if (status == Status.MEASUREMENT) {
            status = Status.REFERENCE;
            drawView.ctxStatus = status;
            newMeasureButton.collapseImmediately();
            newMeasureButton.setVisibility(GONE);
            output.setVisibility(GONE);
            drawView.measure = null;
            drawView.invalidate();
            confirmButton.setVisibility(VISIBLE);
            showMenu();
        } else {
            super.onBackPressed();
        }
    }

    private void showMenu() {
        for (int i = 0; i < refsMenu.size(); i++) {
            refsMenu.getItem(i).setVisible(true);
        }
    }

    private void hideMenu() {
        for (int i = 0; i < refsMenu.size(); i++) {
            refsMenu.getItem(i).setVisible(false);
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_camera;
    }
}
