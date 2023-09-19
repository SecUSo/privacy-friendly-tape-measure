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

package org.secuso.privacyfriendlycameraruler.screenruler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.core.content.ContextCompat;
import android.view.View;

import org.secuso.privacyfriendlycameraruler.R;

/**
 * View for drawing the markings of a screen ruler.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 02.06.16.
 */
public class RulerView extends View {

    double dpmm;
    double heightPx;
    double heightmm;
    double heightFracInch;
    double widthPx;
    double dpfi;
    float lineWidth;
    int textSize ;
    int db;
    Paint paint;
    SharedPreferences sharedPreferences;

    public RulerView(Context c){
        super(c);
    }

    public RulerView(Context context, double ydpmm, double ydpi, SharedPreferences prefs) {
        super(context);

        sharedPreferences = prefs;
        dpmm = ydpmm;
        dpfi = ydpi;

        db = ContextCompat.getColor(context, R.color.darkblue);
        textSize = (int)(dpmm *2.5);
        lineWidth = (float)(dpmm*0.15);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(db);
        paint.setAlpha(255);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        heightPx = this.getHeight();
        widthPx = this.getWidth();
        heightmm = heightPx/dpmm;
        heightFracInch = heightPx/dpfi;

        String prefLeftruler = sharedPreferences.getString("pref_leftruler", "cm");
        switch (prefLeftruler) {
            case "cm":
                drawLeftCm(canvas, paint);
                break;
            case "inch":
                drawLeftIn(canvas, paint);
                break;
            case "degree":
                drawAngleMeasureDeg(canvas, paint);
                break;
            case "radian":
                drawAngleMeasureRad(canvas, paint);
                break;
        }

        String prefRightruler = sharedPreferences.getString("pref_rightruler", "inch");
        if (prefRightruler.equals("cm")){
            drawRightCm(canvas, paint);
        } else if (prefRightruler.equals("inch")) {
            drawRightIn(canvas, paint);
        }
    }

    /**
     * Draws markings for an angle measure in degrees on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawAngleMeasureDeg(Canvas canvas, Paint paint){
        float radius = (float)widthPx/2;
        float half = (float)(heightPx/2);
        float degInRad = (float)Math.PI /180;
        Path path = new Path();

        canvas.drawLine(0, half-radius, 0, half+radius, paint);
        canvas.drawLine(0, half, radius, half, paint);
        canvas.drawLine(0, half, (float)Math.sin(45*degInRad)*radius, (float)(half-Math.cos(45*degInRad)*radius), paint);
        canvas.drawLine(0, half, (float)Math.sin(135*degInRad)*radius, (float)(half-Math.cos(135*degInRad)*radius), paint);

        for (int i = 0; i <= 180; i++){
            if (i%10 == 0){
                canvas.drawLine((float)Math.sin(i*degInRad)*radius, (float)(half-Math.cos(i*degInRad)*radius),
                        (float)(Math.sin(i*degInRad)*(radius+dpmm*8)), (float)(half-Math.cos(i*degInRad)*(radius+dpmm*8)), paint);
                //draw a number every 10 deg (except 0 and 180)
                if (i != 0 && i != 180){
                    path.reset();
                    path.moveTo((float)(Math.sin(i*degInRad)*(radius+dpmm*8)+textSize/5),
                            (float)(half-Math.cos(i*degInRad)*(radius+dpmm*8)-textSize*0.75));
                    path.lineTo((float)(Math.sin(i*degInRad)*(radius+dpmm*8)+textSize/5),
                            (float)(half-Math.cos(i*degInRad)*(radius+dpmm*8)+textSize*0.6));
                    canvas.drawTextOnPath(""+i, path, 0, 0, paint);
                }
            } else if (i%5 == 0){
                canvas.drawLine((float)Math.sin(i*degInRad)*radius, (float)(half-Math.cos(i*degInRad)*radius),
                        (float)(Math.sin(i*degInRad)*(radius+dpmm*5)), (float)(half-Math.cos(i*degInRad)*(radius+dpmm*5)), paint);
            } else {
                canvas.drawLine((float)Math.sin(i*degInRad)*radius, (float)(half-Math.cos(i*degInRad)*radius),
                        (float)(Math.sin(i*degInRad)*(radius+dpmm*3)), (float)(half-Math.cos(i*degInRad)*(radius+dpmm*3)), paint);
            }
        }
    }

    /**
     * Draws markings for an angle measure in radian on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawAngleMeasureRad(Canvas canvas, Paint paint){
        float radius = (float)widthPx/2;
        float half = (float)(heightPx/2);
        float piOver24 = (float)Math.PI /24;
        Path path = new Path();
        String[] labels = {"0", "π/12", "π/6", "π/4", "π/3", "5π/12", "π/2", "7π/12", "2π/3", "3π/4", "5π/6", "11π/12", "π"};

        canvas.drawLine(0, half-radius, 0, half+radius, paint);
        canvas.drawLine(0, half, radius, half, paint);
        canvas.drawLine(0, half, (float)Math.sin(6*piOver24)*radius, (float)(half-Math.cos(6*piOver24)*radius), paint);
        canvas.drawLine(0, half, (float)Math.sin(18*piOver24)*radius, (float)(half-Math.cos(18*piOver24)*radius), paint);

        for (int i = 0; i <= 24; i++){
            if (i%6 == 0){
                canvas.drawLine((float)Math.sin(i*piOver24)*radius, (float)(half-Math.cos(i*piOver24)*radius),
                        (float)(Math.sin(i*piOver24)*(radius+dpmm*8)), (float)(half-Math.cos(i*piOver24)*(radius+dpmm*8)), paint);
                if (i!= 0 && i != 24) {
                    path.reset();
                    path.moveTo((float) (Math.sin(i * piOver24) * (radius + dpmm * 8) + textSize / 5),
                            (float) (half - Math.cos(i * piOver24) * (radius + dpmm * 8) - textSize * 0.85));
                    path.lineTo((float) (Math.sin(i * piOver24) * (radius + dpmm * 8) + textSize / 5),
                            (float) (half - Math.cos(i * piOver24) * (radius + dpmm * 8) + textSize * 0.85));
                    canvas.drawTextOnPath(labels[i / 2], path, 0, 0, paint);
                }
            } else if (i%2 == 0){
                canvas.drawLine((float)Math.sin(i*piOver24)*radius, (float)(half-Math.cos(i*piOver24)*radius),
                        (float)(Math.sin(i*piOver24)*(radius+dpmm*5)), (float)(half-Math.cos(i*piOver24)*(radius+dpmm*5)), paint);
                path.reset();
                path.moveTo((float) (Math.sin(i * piOver24) * (radius + dpmm * 5) + textSize / 4),
                        (float) (half - Math.cos(i * piOver24) * (radius + dpmm * 5) - textSize * 1.4));
                path.lineTo((float) (Math.sin(i * piOver24) * (radius + dpmm * 5) + textSize / 4),
                        (float) (half - Math.cos(i * piOver24) * (radius + dpmm *5) + textSize * 1.4));
                canvas.drawTextOnPath(labels[i / 2], path, 0, 0, paint);
            } else {
                canvas.drawLine((float)Math.sin(i*piOver24)*radius, (float)(half-Math.cos(i*piOver24)*radius),
                        (float)(Math.sin(i*piOver24)*(radius+dpmm*3)), (float)(half-Math.cos(i*piOver24)*(radius+dpmm*3)), paint);
            }
        }
    }

    /**
     * Draws markings for an centimeter ruler on the left side of the screen on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawLeftCm(Canvas canvas, Paint paint){
        for (int i = 0; i < heightmm; i++){
            if (i%10 == 0) {
                //draw 8mm line every cm
                canvas.drawLine(0, (float)dpmm*i, (float)dpmm*8, (float)dpmm*i, paint);
                //draw a number every cm
                canvas.drawText(""+i/10, (float)dpmm*8+(textSize/5), (float)(dpmm*i+textSize), paint);
            } else if (i%5 == 0) {
                //draw 5mm line every 5mm
                canvas.drawLine(0, (float)dpmm*i, (float)dpmm*5, (float)dpmm*i, paint);
            } else {
                //draw 3mm line every mm
                canvas.drawLine(0, (float)dpmm*i, (float)dpmm*3, (float)dpmm*i, paint);
            }
        }
    }

    /**
     * Draws markings for an inch ruler on the left side of the screen on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawLeftIn(Canvas canvas, Paint paint){
        for (int i = 0; i < (heightFracInch); i++){
            if (i%32 == 0) {
                //draw 8mm line every inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*8), (float) dpfi *i, paint);
                //draw a number every inch
                canvas.drawText(""+i/32, (float)dpmm*8+(textSize/5), (float)(dpfi *i+textSize), paint);
            } else if (i%16 == 0) {
                //draw 6mm line every 1/2 inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*6), (float) dpfi *i, paint);
            } else if (i%8 == 0) {
                //draw 4mm line every 1/4 inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*4), (float) dpfi *i, paint);
            } else if (i%4 == 0) {
                //draw 3mm line every 1/8 inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*3), (float) dpfi *i, paint);
            } else if (i%2 == 0) {
                //draw 2mm line every 1/16 inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*2), (float) dpfi *i, paint);
            } else {
                //draw 1.5mm line every 1/32 inch
                canvas.drawLine(0, (float) dpfi *i, (float)(dpmm*1.5), (float) dpfi *i, paint);
            }
        }
    }

    /**
     * Draws markings for an centimeter ruler on the right side of the screen on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawRightCm(Canvas canvas, Paint paint){
        Path path = new Path();

        for (int i = 0; i < heightmm; i++){
            if (i%10 == 0) {
                //draw 8mm line every cm
                canvas.drawLine((float)(widthPx-dpmm*(8)), (float)(heightPx-dpmm*(i)),
                        (float)(widthPx), (float)(heightPx-dpmm*(i)), paint);
                //draw a number every cm
                path.reset();
                path.moveTo((float)(widthPx-dpmm*(8)-textSize/5), (float)(heightPx-dpmm*(i)-textSize*0.25));
                path.lineTo((float)(widthPx-dpmm*(8)-textSize/5), (float)(heightPx-dpmm*(i)-textSize));
                canvas.drawTextOnPath(""+i/10, path, 0, 0, paint);
            } else if (i%5 == 0) {
                //draw 5mm line every 5mm
                canvas.drawLine((float)(widthPx-dpmm*(5)), (float)(heightPx-dpmm*(i)),
                        (float)(widthPx), (float)(heightPx-dpmm*(i)), paint);
            } else {
                //draw 3mm line every mm
                canvas.drawLine((float)(widthPx-dpmm*(3)), (float)(heightPx-dpmm*(i)),
                        (float)(widthPx), (float)(heightPx-dpmm*(i)), paint);
            }
        }
    }

    /**
     * Draws markings for an inch ruler on the right side of the screen on a given canvas.
     *
     * @param canvas to draw the ruler markings on.
     * @param paint to draw the ruler markings with.
     */
    private void drawRightIn(Canvas canvas, Paint paint){
        Path path = new Path();

        for (int i = 0; i < (heightFracInch); i++){
            if (i%32 == 0) {
                //draw 8mm line every inch
                canvas.drawLine((float)(widthPx-dpmm*(8)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx-dpfi*(i)), paint);
                //draw a number every inch
                path.reset();
                path.moveTo((float)(widthPx-dpmm*(8)-textSize/5), (float)(heightPx- dpfi *(i)-textSize*0.25));
                path.lineTo((float)(widthPx-dpmm*(8)-textSize/5), (float)(heightPx- dpfi *(i)-textSize));
                canvas.drawTextOnPath(""+i/32, path, 0, 0, paint);
            } else if (i%16 == 0) {
                //draw 6mm line every 1/2 inch
                canvas.drawLine((float)(widthPx-dpmm*(6)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx- dpfi *(i)), paint);
            } else if (i%8 == 0) {
                //draw 4mm line every 1/4 inch
                canvas.drawLine((float)(widthPx-dpmm*(4)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx- dpfi *(i)), paint);
            } else if (i%4 == 0) {
                //draw 3mm line every 1/8 inch
                canvas.drawLine((float)(widthPx-dpmm*(3)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx- dpfi *(i)), paint);
            } else if (i%2 == 0) {
                //draw 2mm line every 1/16 inch
                canvas.drawLine((float)(widthPx-dpmm*(2)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx- dpfi *(i)), paint);
            } else {
                //draw 1.5mm line every 1/32 inch
                canvas.drawLine((float)(widthPx-dpmm*(1.5)), (float)(heightPx- dpfi *(i)),
                        (float)(widthPx), (float)(heightPx- dpfi *(i)), paint);
            }
        }
    }
}
