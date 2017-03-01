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

import static java.lang.Float.NaN;

/**
 * A class representing a linear shape. Consists of two end points. Contains methods for computing
 * its own gradient, intersection point with other line and if a point lies on this line.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
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
        boolean xInRange;
        if (x0 == x1) {
            // Special case for a vertical line. Precise x value check and additional y value check.
            float y0 = ends[0].y;
            float y1 = ends[1].y;
            xInRange = (x0 == p.x && p.y >= Math.min(y0, y1) && p.y <= Math.max(y0, y1));
        } else {
            // Add a gap of .01 pixel while checking for lines sharing a corner not to intersect by default.
            xInRange = (p.x > Math.min(x0, x1) + 0.01) && (p.x < Math.max(x0, x1) - 0.01);
        }
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
