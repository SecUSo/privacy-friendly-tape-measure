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

import android.graphics.Matrix;
import android.util.Log;

import static org.secuso.privacyfriendlycameraruler.cameraruler.CameraRulerView.TOUCHPOINT_RADIUS;

/**
 * A class representing a circular shape. Consists of a center point, a radius length and
 * a point for the thouchpoint for radius adjustment to be drawn on.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
 */

public class Circle extends Shape {
    private Point oldCenter = null;
    private Point oldRadiusTangent = null;
    private Point oldRadiusTouch = null;

    public Point center = new Point(0, 0);

    public float radius = 0;

    public Point radiusTouchPoint = new Point(0, 0);

    public Circle(Point center, float radius) {
        this.center = center;
        this.radius = radius;
        radiusTouchPoint = new Point(center.x+radius+TOUCHPOINT_RADIUS, center.y);
    }

    /**
     * Computes the area of the circle.
     * @return The circle's area.
     */
    public float getArea() {
        return (float) (Math.PI * Math.pow(radius, 2));
    }

    @Override
    public void move(float x, float y) {
        if (oldCenter == null || oldRadiusTouch == null) {
            oldCenter = new Point(center);
            oldRadiusTouch = new Point(radiusTouchPoint);
        }
        center.x = oldCenter.x+x;
        center.y = oldCenter.y+y;
        radiusTouchPoint.x = oldRadiusTouch.x+x;
        radiusTouchPoint.y = oldRadiusTouch.y+y;
    }

    @Override
    public void endMove() {
        oldCenter = null;
        oldRadiusTangent = null;
        oldRadiusTouch = null;
    }

    @Override
    public void zoom(float scale, float x, float y) {
        if (oldCenter == null || oldRadiusTangent == null) {
            oldCenter = new Point(center);
            oldRadiusTangent = new Point(getRadiusTangentPoint());
        }
        float[] points = {oldCenter.x, oldCenter.y, oldRadiusTangent.x, oldRadiusTangent.y};
        Matrix m = new Matrix();
        m.setScale(scale, scale, x, y);
        m.mapPoints(points);
        center.x = points[0];
        center.y = points[1];
        radius = center.dist(new Point(points[2], points[3]));
        adjustRadiusTouchPoint(points[2], points[3]);
    }

    private Point getRadiusTangentPoint() {
        float factor = radius/(radius+TOUCHPOINT_RADIUS);
        return new Point(center.x+factor*(radiusTouchPoint.x-center.x),
                center.y+factor*(radiusTouchPoint.y-center.y));
    }

    private void adjustRadiusTouchPoint(float x, float y){
        float factor = (radius+TOUCHPOINT_RADIUS)/radius;
        radiusTouchPoint = new Point(center.x+factor*(x-center.x),
                center.y+factor*(y-center.y));
    }
}
