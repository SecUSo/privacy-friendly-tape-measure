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
    float[] point1 = {0, 0};
    float[] point2 = {0, 0};
    boolean p1 = false;
    boolean p2 = false;

    public CameraRulerView(Context context) {
        super(context);

        paint.setColor(ContextCompat.getColor(context, R.color.darkblue));
        paint.setAlpha(255);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("<<<<<<<<<<<<<<CLICK>>>>>>>>>>>>>>");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == ACTION_UP){
            System.out.println("<<<<<<<<<<<<<<CLICK>>>>>>>>>>>>>>");
            float x = event.getX();
            float y = event.getY();
            if (!p1) {
                point1[0] = x;
                point1[1] = y;
                p1 = true;
            } else if (!p2) {
                point2[0] = x;
                point2[1] = y;
                p2 = true;
            } else {
                point1[0] = x;
                point1[1] = y;
                p2 = false;
            }
            this.invalidate();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (p1 && p2){
            canvas.drawLine(point1[0], point1[1], point2[0], point2[1], paint);
        }
    }



}
