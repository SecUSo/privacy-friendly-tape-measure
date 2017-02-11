package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public abstract class Polygon extends Shape {

    public Point[] corners;

    /**
     * Computes the area of the polygon.
     * @return The polygon's area.
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
            sides[i] = new Line(corners[i], corners[(i+1)%length]);
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    Line l1 = sides[i];
                    Line l2 = sides[j];
                    Point intersection = l1.intersects(l2);
                    if (intersection != null && l1.contains(intersection) &&
                            l2.contains(intersection)){result = true;}
                }
            }
        }

        return result;
    }
}
