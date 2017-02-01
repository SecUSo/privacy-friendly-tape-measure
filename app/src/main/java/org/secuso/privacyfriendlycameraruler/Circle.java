package org.secuso.privacyfriendlycameraruler;

import static org.secuso.privacyfriendlycameraruler.CameraRulerView.TOUCHPOINT_RADIUS;

/**
 * Created by roberts on 23.01.17.
 */

public class Circle extends Shape {
    public Point center = new Point(0, 0);

    public float radius = 0;

    public Point radiusTouchPoint = new Point(0, 0);

    public Circle(Point center, float radius) {
        this.center = center;
        this.radius = radius;
        radiusTouchPoint = new Point(center.x+radius+TOUCHPOINT_RADIUS, center.y);
    }

    public float getArea() {
        return (float) (Math.PI * Math.pow(radius, 2));
    }
}
