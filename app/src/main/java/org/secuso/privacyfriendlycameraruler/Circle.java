package org.secuso.privacyfriendlycameraruler;

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
        radiusTouchPoint = new Point(center.x+radius+50, center.y); //50 = touch point radius
    }

    public float getArea() {
        return (float) (Math.PI * Math.pow(radius, 2));
    }
}
