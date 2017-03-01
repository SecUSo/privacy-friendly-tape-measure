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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlycameraruler.R;
import org.secuso.privacyfriendlycameraruler.tutorial.PrefManager;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * View overlaid over the activity_camera layout to enable drawing shapes on top of it.
 * Handles drawing of shaped for the reference objects, measurement objects, touchpoints for
 * the active object and display of obtained length/area.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 26.12.16.
 */

public class CameraRulerView extends View {

    public final static float TOUCHPOINT_RADIUS = 120;

    private TextView output;
    CameraActivity.Status ctxStatus;
    private Paint paint = new Paint();
    private Paint referencePaint = new Paint();
    private Paint warningPaint = new Paint();
    private Paint touchPointPaint = new Paint();
    private Paint referenceTouchPointPaint = new Paint();
    private String unitOfMeasurement = getResources().getString(R.string.pref_uom_default);
    Shape measure = null;
    Shape reference = new Circle(new Point(400, 400), 100);
    float scale = 1;
    private int activeTouchpoint = -1; // -1 when inactive, 0 for circle center, 1 for circle radius

    public CameraRulerView(Context context, TextView outputView) {
        super(context);

        output = outputView;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        unitOfMeasurement = pref.getString("pref_units_of_measurement", "mm");

        paint.setColor(ContextCompat.getColor(context, R.color.darkblue));
        paint.setAlpha(255);
        paint.setStrokeWidth(12);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        referencePaint.setColor(ContextCompat.getColor(context, R.color.green));
        referencePaint.setAlpha(255);
        referencePaint.setStrokeWidth(12);
        referencePaint.setAntiAlias(true);
        referencePaint.setStyle(Paint.Style.STROKE);

        warningPaint.setColor(ContextCompat.getColor(context, R.color.red));
        warningPaint.setAlpha(255);
        warningPaint.setStrokeWidth(12);
        warningPaint.setAntiAlias(true);
        warningPaint.setStyle(Paint.Style.STROKE);

        touchPointPaint.setColor(ContextCompat.getColor(context, R.color.lightblue));
        touchPointPaint.setAlpha(123);
        touchPointPaint.setStrokeWidth(8);
        touchPointPaint.setAntiAlias(true);

        referenceTouchPointPaint.setColor(ContextCompat.getColor(context, R.color.green));
        referenceTouchPointPaint.setAlpha(123);
        referenceTouchPointPaint.setStrokeWidth(8);
        referenceTouchPointPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
            clickInTouchpoint(event);
        } else if (event.getAction() == ACTION_UP || event.getAction() == ACTION_CANCEL) {
            activeTouchpoint = -1;
        } else if (event.getAction() == ACTION_MOVE && activeTouchpoint >= 0) {
            if (ctxStatus == CameraActivity.Status.REFERENCE) {
                if (reference instanceof Circle) {
                    Circle circle = (Circle) reference;
                    switch (activeTouchpoint) {
                        case 0:
                            Point center = circle.center;
                            float oldCenterX = center.x;
                            float oldCenterY = center.y;
                            center.x = event.getX();
                            center.y = event.getY();
                            circle.radiusTouchPoint.x = circle.radiusTouchPoint.x + (center.x - oldCenterX);
                            circle.radiusTouchPoint.y = circle.radiusTouchPoint.y + (center.y - oldCenterY);
                            break;
                        case 1:
                            Point radiusTouchpoint = circle.radiusTouchPoint;
                            radiusTouchpoint.x = event.getX();
                            radiusTouchpoint.y = event.getY();
                            circle.radius = radiusTouchpoint.dist(circle.center) - TOUCHPOINT_RADIUS;
                            break;
                        default:
                            break;
                    }
                } else if (reference instanceof Line) {
                    Point end = ((Line) reference).ends[activeTouchpoint];
                    end.x = event.getX();
                    end.y = event.getY();
                } else {
                    Point corner = ((Polygon) reference).corners[activeTouchpoint];
                    corner.x = event.getX();
                    corner.y = event.getY();
                }
            } else if (measure instanceof Line) {
                Point end = ((Line) measure).ends[activeTouchpoint];
                end.x = event.getX();
                end.y = event.getY();
            } else if (measure instanceof Polygon) {
                Point corner = ((Polygon) measure).corners[activeTouchpoint];
                corner.x = event.getX();
                corner.y = event.getY();
            } else if (measure instanceof Circle) {
                Circle circle = (Circle) measure;
                switch (activeTouchpoint) {
                    case 0:
                        Point center = circle.center;
                        float oldCenterX = center.x;
                        float oldCenterY = center.y;
                        center.x = event.getX();
                        center.y = event.getY();
                        circle.radiusTouchPoint.x = circle.radiusTouchPoint.x + (center.x - oldCenterX);
                        circle.radiusTouchPoint.y = circle.radiusTouchPoint.y + (center.y - oldCenterY);
                        break;
                    case 1:
                        Point radiusTouchpoint = circle.radiusTouchPoint;
                        radiusTouchpoint.x = event.getX();
                        radiusTouchpoint.y = event.getY();
                        circle.radius = radiusTouchpoint.dist(circle.center) - TOUCHPOINT_RADIUS;
                        break;
                    default:
                        break;
                }
            }
            this.invalidate();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (reference instanceof Circle) {
            Circle refCircle = (Circle) reference;
            canvas.drawCircle(refCircle.center.x, refCircle.center.y, refCircle.radius, referencePaint);
        } else if (reference instanceof Line) {
            Point[] ends = ((Line) reference).ends;
            canvas.drawLine(ends[0].x, ends[0].y, ends[1].x, ends[1].y, referencePaint);
        } else {
            Point[] corners = ((Polygon) reference).corners;
            int length = corners.length;
            float[] points = new float[length * 4];
            for (int i = 0; i < length; i++) {
                points[i * 4] = corners[i].x;
                points[i * 4 + 1] = corners[i].y;
                points[i * 4 + 2] = corners[(i + 1) % length].x;
                points[i * 4 + 3] = corners[(i + 1) % length].y;
            }
            if (((Polygon) reference).isSelfIntersecting()) {
                canvas.drawLines(points, warningPaint);
            } else {
                canvas.drawLines(points, referencePaint);
            }
        }

        if (ctxStatus == CameraActivity.Status.REFERENCE) {
            if (reference instanceof Circle) {
                Circle refCircle = (Circle) reference;
                drawTouchPoint(canvas, refCircle.center, referenceTouchPointPaint);
                drawTouchPoint(canvas, refCircle.radiusTouchPoint, referenceTouchPointPaint);
            } else if (reference instanceof Line) {
                Point[] ends = ((Line) reference).ends;
                drawTouchPoint(canvas, ends[0], referenceTouchPointPaint);
                drawTouchPoint(canvas, ends[1], referenceTouchPointPaint);
            } else {
                Point[] corners = ((Polygon) reference).corners;
                int length = corners.length;
                for (int i = 0; i < length; i++) {
                    drawTouchPoint(canvas, corners[i], referenceTouchPointPaint);
                }
            }
        } else {
            if (measure == null) {//draw nothing
            } else if (measure instanceof Line) {
                Point[] ends = ((Line) measure).ends;
                canvas.drawLine(ends[0].x, ends[0].y, ends[1].x, ends[1].y, paint);
                drawTouchPoint(canvas, ends[0], touchPointPaint);
                drawTouchPoint(canvas, ends[1], touchPointPaint);
                float length = ((Line) measure).getLength() * scale;
                if (unitOfMeasurement.equals("in")) {
                    length = (float) (length / 25.4);
                }
                output.setText(getResources().getString(R.string.length) + length + unitOfMeasurement);
            } else if (measure instanceof Polygon) {
                Point[] corners = ((Polygon) measure).corners;
                int length = corners.length;
                float[] points = new float[length * 4];
                for (int i = 0; i < length; i++) {
                    points[i * 4] = corners[i].x;
                    points[i * 4 + 1] = corners[i].y;
                    points[i * 4 + 2] = corners[(i + 1) % length].x;
                    points[i * 4 + 3] = corners[(i + 1) % length].y;
                }

                if (((Polygon) measure).isSelfIntersecting()) {
                    canvas.drawLines(points, warningPaint);
                    output.setText(R.string.self_intersection_warning);
                } else {
                    canvas.drawLines(points, paint);
                    float area = ((Polygon) measure).getArea() * scale * scale;
                    if (unitOfMeasurement.equals("in")) {
                        area = (float) (area / Math.pow(25.4, 2));
                    }
                    output.setText(getResources().getString(R.string.area) + area + unitOfMeasurement + "²");
                }

                for (int i = 0; i < length; i++) {
                    drawTouchPoint(canvas, corners[i], touchPointPaint);
                }
            } else if (measure instanceof Circle) {
                Circle circle = (Circle) measure;
                canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
                drawTouchPoint(canvas, circle.center, touchPointPaint);
                drawTouchPoint(canvas, circle.radiusTouchPoint, touchPointPaint);
                float area = circle.getArea() * scale * scale;
                if (unitOfMeasurement.equals("in")) {
                    area = (float) (area / Math.pow(25.4, 2));
                }
                output.setText(getResources().getString(R.string.area) + area + unitOfMeasurement + "²");
            }
        }
    }

    /**
     * Draws a circle representing a touchpoint of a shape.
     *
     * @param canvas to draw the touchpoint on.
     * @param point the center of the touchpoint.
     * @param p paint to fill the touchpoint with. Should be semitransparent.
     */
    private void drawTouchPoint(Canvas canvas, Point point, Paint p) {
        canvas.drawCircle(point.x, point.y, TOUCHPOINT_RADIUS, p);
    }

    /**
     * Determines if a touch event was in an active touchpoint and sets active touchpoint to the
     * proper value according to the shape the touchpoint belongs to.
     * @param event
     * @return
     */
    private boolean clickInTouchpoint(MotionEvent event) {
        int pc = event.getPointerCount() - 1;
        Point click = new Point(event.getX(pc), event.getY(pc));
        boolean result = false;

        if (ctxStatus == CameraActivity.Status.REFERENCE) {
            if (reference instanceof Circle) {
                Circle circle = (Circle) reference;
                if (click.dist(circle.center) <= TOUCHPOINT_RADIUS) {
                    activeTouchpoint = 0;
                    result = true;
                } else if (click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                    activeTouchpoint = 1;
                    result = true;
                }
            } else if (reference instanceof Line) {
                Point[] ends = ((Line) reference).ends;
                if (click.dist(ends[0]) <= TOUCHPOINT_RADIUS) {
                    activeTouchpoint = 0;
                    result = true;
                } else if (click.dist(ends[1]) <= TOUCHPOINT_RADIUS) {
                    activeTouchpoint = 1;
                    result = true;
                }
            } else {
                Point[] corners = ((Polygon) reference).corners;
                for (int i = 0; i < corners.length; i++) {
                    if (click.dist(corners[i]) <= TOUCHPOINT_RADIUS) {
                        activeTouchpoint = i;
                        result = true;
                    }
                }
            }
        } else if (measure == null) {
            return false;
        } else if (measure instanceof Line) {
            Point[] ends = ((Line) measure).ends;
            if (click.dist(ends[0]) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 0;
                result = true;
            } else if (click.dist(ends[1]) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 1;
                result = true;
            }
        } else if (measure instanceof Polygon) {
            Point[] corners = ((Polygon) measure).corners;
            for (int i = 0; i < corners.length; i++) {
                if (click.dist(corners[i]) <= TOUCHPOINT_RADIUS) {
                    activeTouchpoint = i;
                    result = true;
                }
            }
        } else if (measure instanceof Circle) {
            Circle circle = (Circle) measure;
            if (click.dist(circle.center) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 0;
                result = true;
            } else if (click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 1;
                result = true;
            }
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Sets the measure to a new tetragon.
     */
    protected void newTetragon() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        measure = new Tetragon(new Point(centreX - offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY + offsetY),
                new Point(centreX - offsetX, centreY + offsetY));
        this.invalidate();
    }

    /**
     * Sets the measure to a new triangle.
     */
    protected void newTriangle() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        measure = new Triangle(new Point(centreX, centreY - offsetY),
                new Point(centreX - offsetX, centreY + offsetY),
                new Point(centreX + offsetX, centreY + offsetY));
        this.invalidate();
    }

    /**
     * Sets the measure to a new circle.
     */
    protected void newCircle() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        measure = new Circle(new Point(centreX, centreY), offsetX);
        this.invalidate();
    }

    /**
     * Sets the measure to a new line.
     */
    protected void newLine() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        measure = new Line(new Point(centreX - offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY + offsetY));
        this.invalidate();
    }

    /**
     * Sets the reference to a new tetragon.
     */
    protected void newReferenceTetragon() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        reference = new Tetragon(new Point(centreX - offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY + offsetY),
                new Point(centreX - offsetX, centreY + offsetY));
        this.invalidate();
    }

    /**
     * Sets the reference to a new circle.
     */
    protected void newReferenceCircle() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        reference = new Circle(new Point(centreX, centreY), offsetX);
        this.invalidate();
    }

    /**
     * Sets the reference to a new line.
     */
    protected void newReferenceLine() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        reference = new Line(new Point(centreX - offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY + offsetY));
        this.invalidate();
    }

}
