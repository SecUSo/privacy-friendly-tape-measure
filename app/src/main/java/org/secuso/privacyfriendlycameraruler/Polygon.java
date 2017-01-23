package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public abstract class Polygon extends Shape {

    public abstract float getArea();

    protected double[] sort3(double a, double b, double c){
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
}
