package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Triangle extends Polygon {
    public Point[] corners = {new Point(0, 0), new Point(0, 0), new Point(0, 0)};

    public float getArea() {
        // Using the formula of Heron A=sqrt[s*(s-a)*(s-b)*(s-c)]
        // with s=0.5*(a+b+c) where a,b,c are the lengths of the sides.
        // The numerically stable version is used.
        double a = corners[0].dist(corners[1]);
        double b = corners[2].dist(corners[1]);
        double c = corners[0].dist(corners[2]);

        double[] lengths = sort3(a, b, c);
        a = lengths[0];
        b = lengths[1];
        c = lengths[2];

        return (float) (0.25 * Math.sqrt((a+(b+c))*(c-(a-b))*(c+(a-b))*(a+(b-c))));
    }
}