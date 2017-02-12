package org.secuso.privacyfriendlycameraruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by roberts on 26.12.16.
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
    Shape measure = null;
    Shape reference = null;
    private int activeTouchpoint = -1; // -1 when inactive, 0 for circle center, 1 for circle radius

    public CameraRulerView(Context context, TextView tw) {
        super(context);

        output = tw;

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
        System.out.println("STATUS: "+ctxStatus);
        Circle refCircle = (Circle) reference;
        canvas.drawCircle(refCircle.center.x, refCircle.center.y, refCircle.radius, referencePaint);

        if (ctxStatus == CameraActivity.Status.REFERENCE) {
            drawTouchPoint(canvas, refCircle.center, referenceTouchPointPaint);
            drawTouchPoint(canvas, refCircle.radiusTouchPoint, referenceTouchPointPaint);
        } else {
            if (measure == null) {//draw nothing
            } else if (measure instanceof Line) {
                Point[] ends = ((Line) measure).ends;
                canvas.drawLine(ends[0].x, ends[0].y, ends[1].x, ends[1].y, paint);
                drawTouchPoint(canvas, ends[0], touchPointPaint);
                drawTouchPoint(canvas, ends[1], touchPointPaint);
                output.setText(getResources().getString(R.string.length) + ((Line) measure).getLength() + "px");
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
                    output.setText(getResources().getString(R.string.area) + ((Polygon) measure).getArea() + "px^2");
                }

                for (int i = 0; i < length; i++) {
                    drawTouchPoint(canvas, corners[i], touchPointPaint);
                }
            } else if (measure instanceof Circle) {
                Circle circle = (Circle) measure;
                canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
                drawTouchPoint(canvas, circle.center, touchPointPaint);
                drawTouchPoint(canvas, circle.radiusTouchPoint, touchPointPaint);
                output.setText(getResources().getString(R.string.area) + circle.getArea() + "px^2");
            }
        }
    }

    private void drawTouchPoint(Canvas canvas, Point point, Paint p) {
        canvas.drawCircle(point.x, point.y, TOUCHPOINT_RADIUS, p);
    }

    private boolean clickInTouchpoint(MotionEvent event) {
        int pc = event.getPointerCount() - 1;
        Point click = new Point(event.getX(pc), event.getY(pc));
        boolean result = false;

        if (ctxStatus == CameraActivity.Status.REFERENCE) {
            Circle circle = (Circle) reference;
            if (click.dist(circle.center) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 0;
                result = true;
            } else if (click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                activeTouchpoint = 1;
                result = true;
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

    protected void newCircle() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        measure = new Circle(new Point(centreX, centreY), offsetX);
        this.invalidate();
    }

    protected void newLine() {
        float centreX = this.getWidth() / 2;
        float offsetX = centreX / 4;
        float centreY = this.getHeight() / 2;
        float offsetY = centreY / 4;
        measure = new Line(new Point(centreX - offsetX, centreY - offsetY),
                new Point(centreX + offsetX, centreY + offsetY));
        this.invalidate();
    }


}
