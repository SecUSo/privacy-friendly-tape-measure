package org.secuso.privacyfriendlycameraruler.cameraruler;

import static java.lang.Float.NaN;

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
        float m1 = this.gradient(); //compute line gradients
        float m2 = other.gradient();

        if (Float.isNaN(m1)){ //handle lines with infinite gradient
            if (Float.isNaN(m2)) { //parallel lines with infinite gradient
                if (this.ends[0].x == other.ends[0].x) {return this.ends[0];}
                else {return null;}
            } else { //this line has infinite gradient but other not
                float b2 = other.ends[0].y - other.ends[0].x * m2;
                return new Point(this.ends[0].x, this.ends[0].x*m2+b2);
            }
        } else if (Float.isNaN(m2)) { //other has infinite gradient but this not
            float b1 = this.ends[0].y - this.ends[0].x * m1;
            return new Point(other.ends[0].x, other.ends[0].x*m1+b1);
        }

        float b1 = this.ends[0].y - this.ends[0].x * m1; //compute line zero values
        float b2 = other.ends[0].y - other.ends[0].x * m2;

        if (Math.abs(m1) != Math.abs(m2)) { //find intersection point
            float intersectX = (b2 - b1)/(m1 - m2);
            float intersectY = intersectX * m1 + b1;
            return new Point(intersectX, intersectY);
        } else { //handle parallel lines
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
        if (ends[1].x == ends[0].x) {
            return NaN;
        } else if (ends[1].x < ends[0].x) {
            return (ends[0].y - ends[1].y)/(ends[0].x - ends[1].x);
        } else {
            return (ends[1].y - ends[0].y)/(ends[1].x - ends[0].x);
        }
    }
}
