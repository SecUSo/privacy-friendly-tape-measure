package org.secuso.privacyfriendlycameraruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by roberts on 26.12.16.
 */

public class CameraRulerView extends View {

    public final static float TOUCHPOINT_RADIUS = 150;

    Paint paint = new Paint();
    Paint touchPointPaint = new Paint();
    Shape measure = null;
    Shape reference;
//    float[] point1 = {0, 0};
//    float[] point2 = {300, 300};
//    boolean p1 = false;
//    boolean p2 = false;

    public CameraRulerView(Context context) {
        super(context);

        paint.setColor(ContextCompat.getColor(context, R.color.darkblue));
        paint.setAlpha(255);
        paint.setStrokeWidth(12);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        touchPointPaint.setColor(ContextCompat.getColor(context, R.color.lightblue));
        touchPointPaint.setAlpha(123);
        touchPointPaint.setStrokeWidth(8);
        touchPointPaint.setAntiAlias(true);

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("<<<<<<<<<<<<<<CLICK>>>>>>>>>>>>>>");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
            if(clickInTouchpoint(event)) {
                System.out.println("<<<<<<<<<<<<<<CLICK>>>>>>>>>>>>>>");
            }
        }
//        if (event.getAction() == ACTION_UP){
//            float x = event.getX();
//            float y = event.getY();
//            if (!p1) {
//                point1[0] = x;
//                point1[1] = y;
//                p1 = true;
//            } else if (!p2) {
//                point2[0] = x;
//                point2[1] = y;
//                p2 = true;
//            } else {
//                point1[0] = x;
//                point1[1] = y;
//                p2 = false;
//            }
//            this.invalidate();
//        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (measure instanceof Line) {
            Point[] ends = ((Line) measure).ends;
            canvas.drawLine(ends[0].x, ends[0].y, ends[1].x, ends[1].y, paint);
            drawTouchPoint(canvas, ends[0]);
            drawTouchPoint(canvas, ends[1]);
        } else if (measure instanceof Triangle) {
            Point[] corners = ((Triangle) measure).corners;
            float[] points = {corners[0].x, corners[0].y, corners[1].x, corners[1].y,
                    corners[1].x, corners[1].y, corners[2].x, corners[2].y,
                    corners[2].x, corners[2].y, corners[0].x, corners[0].y};
            canvas.drawLines(points, paint);
            drawTouchPoint(canvas, corners[0]);
            drawTouchPoint(canvas, corners[1]);
            drawTouchPoint(canvas, corners[2]);
        } else if (measure instanceof Tetragon) {
            Point[] corners = ((Tetragon) measure).corners;
            float[] points = {corners[0].x, corners[0].y, corners[1].x, corners[1].y,
                    corners[1].x, corners[1].y, corners[2].x, corners[2].y,
                    corners[2].x, corners[2].y, corners[3].x, corners[3].y,
                    corners[3].x, corners[3].y, corners[0].x, corners[0].y};
            canvas.drawLines(points, paint);
            drawTouchPoint(canvas, corners[0]);
            drawTouchPoint(canvas, corners[1]);
            drawTouchPoint(canvas, corners[2]);
            drawTouchPoint(canvas, corners[3]);
        } else if (measure instanceof Circle) {
            Circle circle = (Circle) measure;
            canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
            drawTouchPoint(canvas, circle.center);
            drawTouchPoint(canvas, circle.radiusTouchPoint);
        }
    }

    private void drawTouchPoint(Canvas canvas, Point point) {
        canvas.drawCircle(point.x, point.y, TOUCHPOINT_RADIUS, touchPointPaint);
    }

    private boolean clickInTouchpoint(MotionEvent event){
        Point click = new Point(event.getX(), event.getY());
        boolean result = false;

        if (measure == null) {
            result = false;
        } else if (measure instanceof Line) {
            Point[] ends = ((Line) measure).ends;
            if (click.dist(ends[0]) <= TOUCHPOINT_RADIUS ||
                    click.dist(ends[1]) <= TOUCHPOINT_RADIUS) {
                result = true;
            }
        } else if (measure instanceof Tetragon) {
            Point[] corners = ((Tetragon) measure).corners;
            if (click.dist(corners[0]) <= TOUCHPOINT_RADIUS ||
                    click.dist(corners[1]) <= TOUCHPOINT_RADIUS ||
                    click.dist(corners[2]) <= TOUCHPOINT_RADIUS ||
                    click.dist(corners[3]) <= TOUCHPOINT_RADIUS) {
                result = true;
            }
        } else if (measure instanceof Triangle) {
            Point[] corners = ((Triangle) measure).corners;
            if (click.dist(corners[0]) <= TOUCHPOINT_RADIUS ||
                    click.dist(corners[1]) <= TOUCHPOINT_RADIUS ||
                    click.dist(corners[2]) <= TOUCHPOINT_RADIUS) {
                result = true;
            }
        } else if (measure instanceof Circle) {
            Circle circle = (Circle) measure;
            if (click.dist(circle.center) <= TOUCHPOINT_RADIUS ||
                    click.dist(circle.radiusTouchPoint) <= TOUCHPOINT_RADIUS) {
                result = true;
            }
        } else {result = false;}

        return result;
    }

    protected void newTetragon(){
        float centreX = this.getWidth()/2;
        float offsetX = centreX/4;
        float centreY = this.getHeight()/2;
        float offsetY = centreY/4;
        measure = new Tetragon(new Point(centreX-offsetX, centreY-offsetY),
                new Point(centreX+offsetX, centreY-offsetY),
                new Point(centreX+offsetX, centreY+offsetY),
                new Point(centreX-offsetX, centreY+offsetY));
        this.invalidate();
    }

    protected void newTriangle(){
        float centreX = this.getWidth()/2;
        float offsetX = centreX/4;
        float centreY = this.getHeight()/2;
        float offsetY = centreY/4;
        measure = new Triangle(new Point(centreX, centreY-offsetY),
                new Point(centreX-offsetX, centreY+offsetY),
                new Point(centreX+offsetX, centreY+offsetY));
        this.invalidate();
    }

    protected void newCircle(){
        float centreX = this.getWidth()/2;
        float offsetX = centreX/4;
        float centreY = this.getHeight()/2;
        measure = new Circle(new Point(centreX, centreY), offsetX);
        this.invalidate();
    }

    protected void newLine(){
        float centreX = this.getWidth()/2;
        float offsetX = centreX/4;
        float centreY = this.getHeight()/2;
        float offsetY = centreY/4;
        measure = new Line(new Point(centreX-offsetX, centreY-offsetY),
                new Point(centreX+offsetX, centreY+offsetY));
        this.invalidate();
    }



}
