/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.cameraruler;

import android.content.Intent;
import android.graphics.Matrix;
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

import org.secuso.privacyfriendlycameraruler.BaseActivity;
import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.database.PFASQLiteHelper;
import org.secuso.privacyfriendlycameraruler.database.ReferenceManager;
import org.secuso.privacyfriendlycameraruler.database.ReferenceObject;
import org.secuso.privacyfriendlycameraruler.database.UserDefinedReferences;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Main activity for the Camera Ruler functionality.
 * Controls switching between different phases of camera ruler functionality, visibility of UI
 * for those phases, UI functionality and interaction with external camera and gallery apps.
 * <p>
 * @author Roberts Kolosovs
 * Created by rkolosovs on 12.12.16.
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
    private TextView tutorialText;
    private Menu refsMenu;

    private ArrayList<ReferenceObject> refs;
    private ArrayList<UserDefinedReferences> udrefs;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private String referenceObjectShape = "circle";
    private float referenceObjectSize = 1;

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

        refs = ReferenceManager.getAllActiveRefPredefObjects(getBaseContext());
        PFASQLiteHelper dbHelper = new PFASQLiteHelper(getBaseContext());
        udrefs = dbHelper.getAllUDefRef();
        for (int i = udrefs.size() - 1; i >= 0; i--) {
            if (!udrefs.get(i).getUDR_ACTIVE()) {
                udrefs.remove(i);
            }
        }

        prefManager.putLastMode("camera");
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        cameraButton = (ImageButton) findViewById(R.id.from_camera_button);
        galleryButton = (ImageButton) findViewById(R.id.from_gallery_button);
        pictureView = (ImageView) findViewById(R.id.pictureView);
        confirmButton = (FloatingActionButton) findViewById(R.id.confirm_reference);
        output = (TextView) findViewById(R.id.output_tf);
        tutorialText = (TextView) findViewById(R.id.camera_gallery_choice_text);

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
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
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
                if (drawView.reference instanceof Polygon
                        && ((Polygon) drawView.reference).isSelfIntersecting()) {
                    Toast.makeText(getBaseContext(), getString(R.string.reference_self_intersecting), Toast.LENGTH_LONG).show();
                } else {
                    setReference();
                }
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

        //add active user defined objects
        for (int i = 0; i < udrefs.size(); i++) {
            menu.add(0, i, Menu.NONE, udrefs.get(i).getUDR_NAME());//.setIcon(R.drawable.your-add-icon) to add icon to menu item
            menu.getItem(i).setVisible(false);
        }

        //add active predefined objects
        for (int i = 0; i < refs.size(); i++) {
            menu.add(0, i+udrefs.size(), Menu.NONE, refs.get(i).name);//.setIcon(R.drawable.your-add-icon) to add icon to menu item
            menu.getItem(i).setVisible(false);
        }

        //set topmost item as active
        if (!udrefs.isEmpty()) {
            referenceObjectShape = udrefs.get(0).getUDR_SHAPE();
            referenceObjectSize = udrefs.get(0).getUDR_SIZE();
        } else if (!refs.isEmpty()) {
            referenceObjectShape = refs.get(0).type.shape;
            referenceObjectSize = refs.get(0).size;
        }
        if (referenceObjectShape.equals("tetragon")) {
            drawView.reference = new Tetragon(new Point(400, 400), new Point(800, 400), new Point(800, 800), new Point(400, 800));
        } else if (referenceObjectShape.equals("line")) {
            drawView.reference = new Line(new Point(400, 400), new Point(800, 800));
        }
        drawView.invalidate();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();

        if (itemId < udrefs.size()) {
            UserDefinedReferences refObj = udrefs.get(itemId);
            referenceObjectShape = refObj.getUDR_SHAPE();
            referenceObjectSize = refObj.getUDR_SIZE();
        } else {
            ReferenceObject refObj = refs.get(itemId-udrefs.size());
            referenceObjectShape = refObj.type.shape;
            referenceObjectSize = refObj.size;
        }

        if (referenceObjectShape.equals("circle") && !(drawView.reference instanceof Circle)) {
            drawView.newReferenceCircle();
        } else if (referenceObjectShape.equals("tetragon") && !(drawView.reference instanceof Tetragon)) {
            drawView.newReferenceTetragon();
        } else if (referenceObjectShape.equals("line") && !(drawView.reference instanceof Line)) {
            drawView.newReferenceLine();
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
        tutorialText.setVisibility(GONE);
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
            drawView.scale = referenceObjectSize / (((Circle) drawView.reference).radius * 2);
        } else if (drawView.reference instanceof Line) {
            drawView.scale = referenceObjectSize / ((Line) drawView.reference).getLength();
        } else {
            drawView.scale = (float) Math.sqrt(referenceObjectSize / ((Polygon) drawView.reference).getArea());
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
            tutorialText.setVisibility(VISIBLE);
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
