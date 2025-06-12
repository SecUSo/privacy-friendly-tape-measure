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

package org.secuso.privacyfriendlycameraruler.cameraruler;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Build;

import org.secuso.privacyfriendlycameraruler.BaseActivity;
import org.secuso.privacyfriendlycameraruler.BuildConfig;
import org.secuso.privacyfriendlycameraruler.GoodbyeGoogleHelperKt;
import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.database.PFASQLiteHelper;
import org.secuso.privacyfriendlycameraruler.database.ReferenceManager;
import org.secuso.privacyfriendlycameraruler.database.ReferenceObject;
import org.secuso.privacyfriendlycameraruler.database.UserDefinedReferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.os.Build.VERSION.SDK_INT;

/**
 * Main activity for the Camera Ruler functionality.
 * Controls switching between different phases of camera ruler functionality, visibility of UI
 * for those phases, UI functionality and interaction with external camera and gallery apps.
 *
 * @author Roberts Kolosovs
 *         Created by rkolosovs on 12.12.16.
 */

public class CameraActivity extends BaseActivity {

    private Status status = Status.MODE_CHOICE;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST = 2;
    private static final int ACTIVITY_REQUEST_CODE = 101;

    private Activity thisActivity = this;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private ImageView pictureView;
    private CameraRulerView drawView;
    private FloatingActionsMenu newMeasureButton;
    private FloatingActionButton confirmButton;
    private Menu refsMenu;
    private RelativeLayout modeChoiceLayout;
    private Toolbar toolbar;
    private View discriptorText;
    private View cameraLabel;
    private View galleryLabel;
    private Bitmap photo;
    private Uri mPhotoUri;
    String mCurrentPhotoPath;

    private ArrayList<ReferenceObject> refs;
    private ArrayList<UserDefinedReferences> udrefs;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private String referenceObjectShape = "circle";
    private String referenceObjectName = "";
    private float referenceObjectSize = 1;

    // These matrices will be used to move and zoom image
    Matrix tmpMatrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    Matrix zoomMatrix = new Matrix();

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
        modeChoiceLayout = (RelativeLayout) findViewById(R.id.camera_ruler_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        discriptorText = findViewById(R.id.camera_gallery_choice_text);
        cameraLabel = findViewById(R.id.camera_button_label);
        galleryLabel = findViewById(R.id.gallery_button_label);

        newMeasureButton = (FloatingActionsMenu) findViewById(R.id.new_measure_fam);
        FloatingActionButton newTetragonButton = (FloatingActionButton) findViewById(R.id.new_tetragon_fab);
        FloatingActionButton newTriangleButton = (FloatingActionButton) findViewById(R.id.new_triangle_fab);
        FloatingActionButton newCircleButton = (FloatingActionButton) findViewById(R.id.new_circle_fab);
        FloatingActionButton newLineButton = (FloatingActionButton) findViewById(R.id.new_line_fab);

        drawView = new CameraRulerView(getBaseContext(), toolbar, this);
        drawView.ctxStatus = status;
        drawView.setVisibility(GONE);
        modeChoiceLayout.addView(drawView);

        pictureView.setImageMatrix(new Matrix());

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoUri = FileProvider.getUriForFile(CameraActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setClipData(ClipData.newRawUri("A photo", mPhotoUri));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
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
                drawView.measure = drawView.newTetragon();
                drawView.invalidate();
            }
        });

        newTriangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newTriangle();
                drawView.invalidate();
            }
        });

        newCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newCircle();
                drawView.invalidate();
            }
        });

        newLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newLine();
                drawView.invalidate();
            }
        });

        overridePendingTransition(0, 0);
        GoodbyeGoogleHelperKt.checkGoodbyeGoogle(this, getLayoutInflater());
    }

    private void openImagePicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, getResources().getString(R.string.select_image_from_gallery));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST);
            } else {
                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
            }
        } else {
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchPoint = drawView.clickInTouchpoint(event);
        if (mode == NONE && touchPoint >= 0) { //click in touchpoint while no other gesture active
            drawView.activeTouchpoint = touchPoint;
            drawView.executeTouch(event);
        } else if (drawView.activeTouchpoint >= 0) { //further movements of grabbed touchpoint
            drawView.executeTouch(event);
        } else { //not in touchpoint or gesture already active
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (drawView.reference != null) {
                        drawView.reference.endMove();
                    }
                    if (drawView.measure != null) {
                        drawView.measure.endMove();
                    }
                    mode = NONE;
                    savedMatrix.set(tmpMatrix);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        tmpMatrix.set(savedMatrix);
                        tmpMatrix.postTranslate(event.getX() - start.x,
                                event.getY() - start.y);
                        if (drawView.reference != null) {
                            drawView.reference.move(event.getX() - start.x,
                                    event.getY() - start.y);
                        }
                        if (drawView.measure != null) {
                            drawView.measure.move(event.getX() - start.x,
                                    event.getY() - start.y);
                        }
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            tmpMatrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            tmpMatrix.postScale(scale, scale, mid.x, mid.y);
                            zoomMatrix.setScale(scale, scale, mid.x, mid.y);
                            if (drawView.reference != null) {
                                drawView.reference.zoom(zoomMatrix);
                                setScale();
                            }
                            if (drawView.measure != null) {
                                drawView.measure.zoom(zoomMatrix);
                            }
                        }
                    }
                    break;
            }
            drawView.invalidate();
            pictureView.setImageMatrix(tmpMatrix);
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((x * x + y * y));
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        refsMenu = menu;

        //add active user defined objects
        for (int i = 0; i < udrefs.size(); i++) {
            menu.add(0, i, Menu.NONE, udrefs.get(i).getUDR_NAME());
            menu.getItem(i).setVisible(false);
        }

        //add active predefined objects
        for (int i = 0; i < refs.size(); i++) {
            menu.add(0, i + udrefs.size(), Menu.NONE, refs.get(i).nameId);
            menu.getItem(i).setVisible(false);
        }

        //set topmost item as active
        if (!udrefs.isEmpty()) {
            referenceObjectShape = udrefs.get(0).getUDR_SHAPE();
            referenceObjectSize = udrefs.get(0).getUDR_SIZE();
            referenceObjectName = udrefs.get(0).getUDR_NAME();
        } else if (!refs.isEmpty()) {
            referenceObjectShape = refs.get(0).type.shape;
            referenceObjectSize = refs.get(0).size;
            referenceObjectName = getString(refs.get(0).nameId);
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
            referenceObjectName = refObj.getUDR_NAME();
        } else {
            ReferenceObject refObj = refs.get(itemId - udrefs.size());
            referenceObjectShape = refObj.type.shape;
            referenceObjectSize = refObj.size;
            referenceObjectName = getString(refObj.nameId);
        }

        toolbar.setSubtitle(referenceObjectName);

        if (referenceObjectShape.equals("circle") && !(drawView.reference instanceof Circle)) {
            drawView.reference = drawView.newCircle();
        } else if (referenceObjectShape.equals("tetragon") && !(drawView.reference instanceof Tetragon)) {
            drawView.reference = drawView.newTetragon();
        } else if (referenceObjectShape.equals("line") && !(drawView.reference instanceof Line)) {
            drawView.reference = drawView.newLine();
        }

        drawView.invalidate();
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Receive response from external camera and gallery apps.
     *
     * @param requestCode code of the request sent to the activity
     * @param resultCode result code returned by the activity
     * @param data data returned by the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null) {
                try {
                    if (photo != null) {
                        photo.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    photo = BitmapFactory.decodeStream(stream);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            pictureView.post(new Runnable() {
                                @Override
                                public void run() {
                                    computeTransformation(photo.getWidth(), photo.getHeight());
                                }
                            });
                        }
                    }).start();

                    pictureView.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (requestCode == ACTIVITY_REQUEST_CODE) {
                pictureView.setImageBitmap(null);

                // Image saved to a generated MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                pictureView.setImageURI(mPhotoUri);
                final Drawable d = pictureView.getDrawable();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pictureView.post(new Runnable() {
                            @Override
                            public void run() {
                                computeTransformation(d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            }
                        });
                    }
                }).start();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pictureView.post(new Runnable() {
                        @Override
                        public void run() {
                            startImageFragment();
                        }
                    });
                }
            }).start();
        } else {
            if (resultCode != RESULT_CANCELED) {
                if (requestCode == ACTIVITY_REQUEST_CODE) {
                    Log.e("Camera App crashed.", "Returned result code: " + resultCode);
                    Toast.makeText(this, R.string.camera_crash, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("Gallery App crashed.", "Returned result code: " + resultCode);
                    Toast.makeText(this, R.string.gallery_crash, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Computes how a picture should be transformed to best fit the space available in the app.
     * Sets the transformation matrix of pictureView accordingly.
     *
     * @param picWidth  original width of the image
     * @param picHeight original height of the image
     */

    private void computeTransformation(float picWidth, float picHeight) {
        float height = picHeight;
        float width = picWidth;

        Matrix matrix = new Matrix();
        if (height < width) {
            matrix.postRotate(90, 0f, 0f);
            //noinspection SuspiciousNameCombination
            height = width;
            //noinspection SuspiciousNameCombination
            width = picHeight;
            matrix.postTranslate(width, 0f);
        }
        float scaleW = displayMetrics.widthPixels / width;
        float scaleH = modeChoiceLayout.getHeight() / height;
        float scale = Math.max(scaleW, scaleH);
        height *= scale;
        width *= scale;
        matrix.postScale(scale, scale);

        if (scaleW < scaleH) { //width overscaled
            matrix.postTranslate(-(width - displayMetrics.widthPixels) / 2, 0f);
        } else if (scaleH < scaleW) { //height overscaled
            matrix.postTranslate(0f, -(height - modeChoiceLayout.getHeight()) / 2);
        }
        savedMatrix = matrix;
        pictureView.setImageMatrix(matrix);
    }

    /**
     * Starts the first page of the actual camera ruler functionality, the reference selection
     * and confirmation. Hides the explanatory text and the buttons of the source selection phase.
     * Shows the picture view and fills it with the picture fetched from the external app.
     * Shows the view for drawing shapes on top of the picture view, the button for confirming
     * the reference object and the menu for selecting the reference object.
     */
    public void startImageFragment() {
        status = Status.REFERENCE;
        drawView.ctxStatus = status;
        cameraButton.setVisibility(GONE);
        cameraButton.setClickable(false);
        galleryButton.setVisibility(GONE);
        galleryButton.setClickable(false);
        discriptorText.setVisibility(GONE);
        cameraLabel.setVisibility(GONE);
        galleryLabel.setVisibility(GONE);
        pictureView.setVisibility(VISIBLE);
        drawView.setVisibility(VISIBLE);
        drawView.setClickable(true);
        drawView.bringToFront();
        confirmButton.setVisibility(VISIBLE);
        toolbar.setTitle(R.string.reference_phase_title);
        toolbar.setSubtitle(referenceObjectName);
        showMenu();
    }

    /**
     * Starts the second phase of teh main camera ruler functionality, the measurement. Computes the
     * real world unit to pixel ration from the size of the reference shape and the active reference
     * object. Hides the reference confirmation button and reference object selection menu. Shows
     * the action button group for measurement shape selection. Sets the current measure to a line.
     * Forces the draw view to redraw.
     */
    public void setReference() {
        setScale();

        status = Status.MEASUREMENT;
        drawView.ctxStatus = status;
        confirmButton.setVisibility(GONE);
        newMeasureButton.setVisibility(VISIBLE);
        drawView.measure = drawView.newLine();
        drawView.reference.active = false;
        toolbar.setTitle(R.string.measurement_phase_title);
        toolbar.setSubtitle(referenceObjectName);
        drawView.invalidate();
        hideMenu();
    }

    private void setScale() {
        if (drawView.reference instanceof Circle) {
            drawView.scale = referenceObjectSize / (((Circle) drawView.reference).radius * 2);
        } else if (drawView.reference instanceof Line) {
            drawView.scale = referenceObjectSize / ((Line) drawView.reference).getLength();
        } else {
            drawView.scale = (float) Math.sqrt(referenceObjectSize / ((Polygon) drawView.reference).getArea());
        }
    }

    /**
     * Goes backwards through the phases of camera ruler functionality hiding and showing
     * appropriate UI elements.
     */
    @Override
    public void onBackPressed() {
        if (status == Status.REFERENCE) {
            status = Status.MODE_CHOICE;
            drawView.ctxStatus = status;
            cameraButton.setVisibility(VISIBLE);
            cameraButton.setClickable(true);
            galleryButton.setVisibility(VISIBLE);
            galleryButton.setClickable(true);
            discriptorText.setVisibility(VISIBLE);
            cameraLabel.setVisibility(VISIBLE);
            galleryLabel.setVisibility(VISIBLE);
            drawView.setVisibility(GONE);
            drawView.setClickable(false);
            pictureView.setVisibility(GONE);
            pictureView.setImageURI(Uri.EMPTY);
            confirmButton.setVisibility(GONE);
            toolbar.setTitle(R.string.camera_ruler);
            toolbar.setSubtitle("");
            hideMenu();
        } else if (status == Status.MEASUREMENT) {
            status = Status.REFERENCE;
            drawView.ctxStatus = status;
            newMeasureButton.collapseImmediately();
            newMeasureButton.setVisibility(GONE);
            drawView.measure = null;
            drawView.reference.active = true;
            drawView.invalidate();
            confirmButton.setVisibility(VISIBLE);
            toolbar.setTitle(R.string.reference_phase_title);
            toolbar.setSubtitle(referenceObjectName);
            showMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Sets every item in the menu of available reference objects to be visible.
     */
    private void showMenu() {
        for (int i = 0; i < refsMenu.size(); i++) {
            refsMenu.getItem(i).setVisible(true);
        }
    }

    /**
     * Sets every item in the menu of available reference objects to be invisible.
     */
    private void hideMenu() {
        for (int i = 0; i < refsMenu.size(); i++) {
            refsMenu.getItem(i).setVisible(false);
        }
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getFilesDir(), "images/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_camera;
    }
}
