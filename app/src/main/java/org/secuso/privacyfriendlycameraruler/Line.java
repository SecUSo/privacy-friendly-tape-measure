package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Line extends Shape {
    public Point[] ends = {new Point(0, 0), new Point(0, 0)};

    public float getLength() {
        return ends[0].dist(ends[1]);
    }
}
