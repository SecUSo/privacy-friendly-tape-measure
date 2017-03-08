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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;

import org.secuso.privacyfriendlycameraruler.R;

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

    private CameraActivity parent;
    private Toolbar toolbar;
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
    float touchOffsetX;
    float touchOffsetY;
    protected int activeTouchpoint = -1; // -1 when inactive, 0 for circle center, 1 for circle radius

    public CameraRulerView(Context context, Toolbar toolbar, CameraActivity parent) {
        super(context);

        this.parent = parent;
        this.toolbar = toolbar;
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
        parent.onTouchEvent(event);
        return true;
    }

    protected void executeTouch(MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
        } else if (event.getAction() == ACTION_UP || event.getAction() == ACTION_CANCEL) {
            activeTouchpoint = -1;
            touchOffsetX = 0;
            touchOffsetY = 0;
        } else if (event.getAction() == ACTION_MOVE && activeTouchpoint >= 0) {
            if (ctxStatus == CameraActivity.Status.REFERENCE) {
                if (reference instanceof Circle) {
                    Circle circle = (Circle) reference;
                    switch (activeTouchpoint) {
                        case 0:
                            Point center = circle.center;
                            float oldCenterX = center.x;
                            float oldCenterY = center.y;
                            center.x = event.getX() - touchOffsetX;
                            center.y = event.getY() - touchOffsetY;
                            circle.radiusTouchPoint.x = circle.radiusTouchPoint.x + (center.x - oldCenterX);
                            circle.radiusTouchPoint.y = circle.radiusTouchPoint.y + (center.y - oldCenterY);
                            break;
                        case 1:
                            Point radiusTouchpoint = circle.radiusTouchPoint;
                            radiusTouchpoint.x = event.getX() - touchOffsetX;
                            radiusTouchpoint.y = event.getY() - touchOffsetY;
                            circle.radius = radiusTouchpoint.dist(circle.center) - TOUCHPOINT_RADIUS;
                            break;
                        default:
                            break;
                    }
                } else if (reference instanceof Line) {
                    Point end = ((Line) reference).ends[activeTouchpoint];
                    end.x = event.getX() - touchOffsetX;
                    end.y = event.getY() - touchOffsetY;
                } else {
                    Point corner = ((Polygon) reference).corners[activeTouchpoint];
                    corner.x = event.getX() - touchOffsetX;
                    corner.y = event.getY() - touchOffsetY;
                }
            } else if (measure instanceof Line) {
                Point end = ((Line) measure).ends[activeTouchpoint];
                end.x = event.getX() - touchOffsetX;
                end.y = event.getY() - touchOffsetY;
            } else if (measure instanceof Polygon) {
                Point corner = ((Polygon) measure).corners[activeTouchpoint];
                corner.x = event.getX() - touchOffsetX;
                corner.y = event.getY() - touchOffsetY;
            } else if (measure instanceof Circle) {
                Circle circle = (Circle) measure;
                switch (activeTouchpoint) {
                    case 0:
                        Point center = circle.center;
                        float oldCenterX = center.x;
                        float oldCenterY = center.y;
                        center.x = event.getX() - touchOffsetX;
                        center.y = event.getY() - touchOffsetY;
                        circle.radiusTouchPoint.x = circle.radiusTouchPoint.x + (center.x - oldCenterX);
                        circle.radiusTouchPoint.y = circle.radiusTouchPoint.y + (center.y - oldCenterY);
                        break;
                    case 1:
                        Point radiusTouchpoint = circle.radiusTouchPoint;
                        radiusTouchpoint.x = event.getX() - touchOffsetX;
                        radiusTouchpoint.y = event.getY() - touchOffsetY;
                        circle.radius = radiusTouchpoint.dist(circle.center) - TOUCHPOINT_RADIUS;
                        break;
                    default:
                        break;
                }
            }
            this.invalidate();
        }
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
                String unit = unitOfMeasurement;
                if (unitOfMeasurement.equals("in")) {
                    length = (float) (length / 25.4);
                    if (length >= 18) {
                        length /= 36;
                        unit = "yd";
                    } else if (length >= 6) {
                        length /= 12;
                        unit = "ft";
                    } else if (length <= 0.01) {
                        length *= 1000;
                        unit = "th";
                    }
                } else {
                    if (length >= 50000000) {
                        length /= 100000000;
                        unit = "km";
                    } else if (length >= 500) {
                        length /= 1000;
                        unit = "m";
                    } else if (length >= 5) {
                        length /= 10;
                        unit = "cm";
                    }
                }
                length = Math.round(length * 100f) / 100f;
                toolbar.setSubtitle(getResources().getString(R.string.length) + " " + length + unit);
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
                    toolbar.setSubtitle(R.string.self_intersection_warning);
                } else {
                    canvas.drawLines(points, paint);
                    float area = ((Polygon) measure).getArea() * scale * scale;
                    String unit = unitOfMeasurement;
                    if (unitOfMeasurement.equals("in")) {
                        area = (float) (area / Math.pow(25.4, 2));
                        if (area >= 648) {
                            area /= 1296;
                            unit = "yd";
                        } else if (area >= 72) {
                            area /= 144;
                            unit = "ft";
                        } else if (area <= 0.000002) {
                            area *= 1000000;
                            unit = "th";
                        }
                    } else {
                        if (area >= 500000) {
                            area /= 1000000;
                            unit = "m";
                        } else if (area >= 50) {
                            area /= 100;
                            unit = "cm";
                        }
                    }
                    area = Math.round(area * 100f) / 100f;
                    toolbar.setSubtitle(getResources().getString(R.string.area) + " " + area + unit + "²");
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
                String unit = unitOfMeasurement;
                if (unitOfMeasurement.equals("in")) {
                    area = (float) (area / Math.pow(25.4, 2));
                    if (area >= 648) {
                        area /= 1296;
                        unit = "yd";
                    } else if (area >= 72) {
                        area /= 144;
                        unit = "ft";
                    } else if (area <= 0.000002) {
                        area *= 1000000;
                        unit = "th";
                    }
                } else {
                    if (area >= 500000) {
                        area /= 1000000;
                        unit = "m";
                    } else if (area >= 50) {
                        area /= 100;
                        unit = "cm";
                    }
                }
                area = Math.round(area * 100f) / 100f;
                toolbar.setSubtitle(getResources().getString(R.string.area) + " " + area + unit + "²");
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
     * Determines if a touch event was in an active touchpoint and returns the currently active
     * touchpoint's number according to the shape the touchpoint belongs to.
     * @param event
     * @return active touchpoint or -1 if click not in touchpoint
     */
    protected int clickInTouchpoint(MotionEvent event) {
        int pc = event.getPointerCount() - 1;
        Point click = new Point(event.getX(pc), event.getY(pc));
        int result = -1;

        if (ctxStatus == CameraActivity.Status.REFERENCE) {
            if (reference instanceof Circle) {
                Circle circle = (Circle) reference;
                if (click.dist(circle.center) <= TOUCHPOINT_RADIUS) {
                    if (activeTouchpoint < 0) {
                        touchOffsetX = event.getX() - circle.center.x;
                        touchOffsetY = event.getY() - circle.center.y;
                    }
                    result = 0;
                } else if (click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                    if (activeTouchpoint < 0) {
                        touchOffsetX = event.getX() - circle.radiusTouchPoint.x;
                        touchOffsetY = event.getY() - circle.radiusTouchPoint.y;
                    }
                    result = 1;
                }
            } else if (reference instanceof Line) {
                Point[] ends = ((Line) reference).ends;
                if (click.dist(ends[0]) <= TOUCHPOINT_RADIUS) {
                    if (activeTouchpoint < 0) {
                        touchOffsetX = event.getX() - ends[0].x;
                        touchOffsetY = event.getY() - ends[0].y;
                    }
                    result = 0;
                } else if (click.dist(ends[1]) <= TOUCHPOINT_RADIUS) {
                    if (activeTouchpoint < 0) {
                        touchOffsetX = event.getX() - ends[1].x;
                        touchOffsetY = event.getY() - ends[1].y;
                    }
                    result = 1;
                }
            } else {
                Point[] corners = ((Polygon) reference).corners;
                for (int i = 0; i < corners.length; i++) {
                    if (click.dist(corners[i]) <= TOUCHPOINT_RADIUS) {
                        if (activeTouchpoint < 0) {
                            touchOffsetX = event.getX() - corners[i].x;
                            touchOffsetY = event.getY() - corners[i].y;
                        }
                        result = i;
                    }
                }
            }
        } else if (measure == null) {
            return -1;
        } else if (measure instanceof Line) {
            Point[] ends = ((Line) measure).ends;
            if (click.dist(ends[0]) <= TOUCHPOINT_RADIUS) {
                if (activeTouchpoint < 0) {
                    touchOffsetX = event.getX() - ends[0].x;
                    touchOffsetY = event.getY() - ends[0].y;
                }
                result = 0;
            } else if (click.dist(ends[1]) <= TOUCHPOINT_RADIUS) {
                if (activeTouchpoint < 0) {
                    touchOffsetX = event.getX() - ends[1].x;
                    touchOffsetY = event.getY() - ends[1].y;
                }
                result = 1;
            }
        } else if (measure instanceof Polygon) {
            Point[] corners = ((Polygon) measure).corners;
            for (int i = 0; i < corners.length; i++) {
                if (click.dist(corners[i]) <= TOUCHPOINT_RADIUS) {
                    if (activeTouchpoint < 0) {
                        touchOffsetX = event.getX() - corners[i].x;
                        touchOffsetY = event.getY() - corners[i].y;
                    }
                    result = i;
                }
            }
        } else if (measure instanceof Circle) {
            Circle circle = (Circle) measure;
            if (click.dist(circle.center) <= TOUCHPOINT_RADIUS) {
                if (activeTouchpoint < 0) {
                    touchOffsetX = event.getX() - circle.center.x;
                    touchOffsetY = event.getY() - circle.center.y;
                }
                result = 0;
            } else if (click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                if (activeTouchpoint < 0) {
                    touchOffsetX = event.getX() - circle.radiusTouchPoint.x;
                    touchOffsetY = event.getY() - circle.radiusTouchPoint.y;
                }
                result = 1;
            }
        } else {
            result = -1;
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
