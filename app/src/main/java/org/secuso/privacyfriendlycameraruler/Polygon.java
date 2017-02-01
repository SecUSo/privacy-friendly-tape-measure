package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public abstract class Polygon extends Shape {

    public Point[] corners;

    public abstract float getArea();

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

    public boolean isSelfIntersecting() {
        boolean result = false;
        int length = corners.length;
        Line[] sides = new Line[length];
        for (int i = 0; i < length; i++) {
            sides[i] = new Line(corners[i], corners[i % length]);
        }

        int i = 0;
        int j = 0;
        while (i < length && !result) {
            while (j < length && !result) {
                if (i != j) {
                    Point intersection = sides[i].intersects(sides[j]);
                    if (intersection != null && sides[i].contains(intersection)){result = true;}
                }
                j++;
            }
            i++;
        }

        return result;
    }
}
