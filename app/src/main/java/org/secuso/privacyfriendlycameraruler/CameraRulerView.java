package org.secuso.privacyfriendlycameraruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by roberts on 26.12.16.
 */

public class CameraRulerView extends View {

    Paint paint = new Paint();
    Shape measure;
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

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("<<<<<<<<<<<<<<CLICK>>>>>>>>>>>>>>");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        } else if (measure instanceof Triangle) {
            Point[] corners = ((Triangle) measure).corners;
            float[] points = {corners[0].x, corners[0].y, corners[1].x, corners[1].y,
                    corners[1].x, corners[1].y, corners[2].x, corners[2].y,
                    corners[2].x, corners[2].y, corners[0].x, corners[0].y};
            canvas.drawLines(points, paint);
        } else if (measure instanceof Tetragon) {
            Point[] corners = ((Tetragon) measure).corners;
            float[] points = {corners[0].x, corners[0].y, corners[1].x, corners[1].y,
                    corners[1].x, corners[1].y, corners[2].x, corners[2].y,
                    corners[2].x, corners[2].y, corners[3].x, corners[3].y,
                    corners[3].x, corners[3].y, corners[0].x, corners[0].y};
            canvas.drawLines(points, paint);
        } else if (measure instanceof Circle) {
            Circle circle = (Circle) measure;
            canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
        }
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
