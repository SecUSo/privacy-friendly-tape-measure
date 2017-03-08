/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 * <p>
 * The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 * In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 * License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 * header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.cameraruler;

/**
 * Class representing polygons with various number of sides. Consists of an array of points.
 * Contains a helper method for sorting three numbers and a method for checking if the represented
 * shape self-intersects.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
 */

public abstract class Polygon extends Shape {
    private Point[] oldCorners = null;

    public Point[] corners;

    /**
     * Computes the area of the polygon. Only works properly if the shape doesn't self-intersect.
     * @return The area of the polygon in pixel squared.
     */
    public abstract float getArea();

    /**
     * Sorts an array of three doubles from biggest to smallest.
     * @param a a double
     * @param b another double
     * @param c yet another double
     * @return Array sorted in descending order.
     */
    protected double[] sort3(double a, double b, double c) {
        double[] res = new double[3];
        if (a < b) {
            if (a < c) {
                res[0] = Math.max(b, c);
                res[1] = Math.min(b, c);
                res[2] = a;
            } else { // c < a < b
                res[0] = b;
                res[1] = a;
                res[2] = c;
            }
        } else { // b < a
            if (b < c) {
                res[0] = Math.max(a, c);
                res[1] = Math.min(a, c);
                res[2] = b;
            } else { // c < b < a
                res[0] = a;
                res[1] = b;
                res[2] = c;
            }
        }
        return res;
    }

    /**
     * Tests if a polygon self-intersects (at least two of its sides intersect).
     * @return true if the figure self-intersects.
     */
    public boolean isSelfIntersecting() {
        boolean result = false;
        int length = corners.length;
        Line[] sides = new Line[length];
        for (int i = 0; i < length; i++) {
            sides[i] = new Line(corners[i], corners[(i + 1) % length]);
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    Line l1 = sides[i];
                    Line l2 = sides[j];
                    Point intersection = l1.intersects(l2);
                    if (intersection != null && l1.contains(intersection) &&
                            l2.contains(intersection)) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void move(float x, float y) {
        if (oldCorners == null) {
            oldCorners = new Point[corners.length];
            for (int i = 0; i < corners.length; i++) {
                oldCorners[i] = new Point(corners[i]);
            }
        }
        for (int i = 0; i < corners.length; i++) {
            corners[i].x = oldCorners[i].x+x;
            corners[i].y = oldCorners[i].y+y;
        }
    }

    @Override
    public void endMove() {
        oldCorners = null;
    }
}
