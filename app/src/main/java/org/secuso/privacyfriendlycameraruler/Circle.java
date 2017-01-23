package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Circle extends Shape {
    public Point center = new Point(0, 0);

    public float radius = 0;

    public Point rediusTouchPoint = new Point(0, 0);

    public float getArea() {
        return (float) (Math.PI * Math.pow(radius, 2));
    }
}
