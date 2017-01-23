package org.secuso.privacyfriendlycameraruler;

/**
 * Created by roberts on 23.01.17.
 */

public class Point {
    public float x;

    public float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float dist(Point other) {
        return (float) Math.sqrt(
                Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
