package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Line extends Shape {
    public Point[] ends = {new Point(0, 0), new Point(0, 0)};

    public Line(Point start, Point end){
        ends[0] = start;
        ends[1] = end;
    }

    public float getLength() {
        return ends[0].dist(ends[1]);
    }

    public Point intersects(Line other) {
        //TODO
        return new Point(0, 0);
    }

    public boolean contains(Point p) {
        //TODO
        return false;
    }
}
