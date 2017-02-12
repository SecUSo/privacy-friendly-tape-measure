package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Line extends Shape {
    public Point[] ends = {new Point(0, 0), new Point(0, 0)};

    public Line(Point start, Point end) {
        ends[0] = start;
        ends[1] = end;
    }

    public float getLength() {
        return ends[0].dist(ends[1]);
    }

    /**
     * Checks if two lines intersect and gives their intersection point.
     *
     * @param other The line which intersection with this line is to be tested.
     * @return The intersection point or null.
     */
    public Point intersects(Line other) {
        float m1 = this.gradient();
        float m2 = other.gradient();
        float b1 = this.ends[0].y - this.ends[0].x * m1;
        float b2 = other.ends[0].y - other.ends[0].x * m2;
        if (Math.abs(m1) != Math.abs(m2)) {
            float intersectX = (b2 - b1)/(m1 - m2);
            float intersectY = intersectX * m1 + b1;
            return new Point(intersectX, intersectY);
        } else {
            return null;
        }
    }

    /**
     * Tests if a Point which is on the line this line segment is a part of is on this line segment.
     *
     * @param p A Point on the line.
     * @return true if Point p is on the line segment.
     */
    public boolean contains(Point p) {
        float x0 = ends[0].x;
        float x1 = ends[1].x;
        boolean xInRange = (p.x > Math.min(x0, x1)+0.01) && (p.x < Math.max(x0, x1)-0.01);
        return xInRange;
    }

    /**
     * Computes the gradient of the line.
     *
     * @return The gradient of the line.
     */
    private float gradient() {
        //TODO: check x0 == x1
        if (ends[1].x < ends[0].x) {
            return (ends[0].y - ends[1].y)/(ends[0].x - ends[1].x);
        } else {
            return (ends[1].y - ends[0].y)/(ends[1].x - ends[0].x);
        }
    }
}
