package org.secuso.privacyfriendlycameraruler.cameraruler;

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

    /**
     * Computes the distance from this point to another point.
     * @param other Point to compute the distance to.
     * @return The distance between this and other.
     */
    public float dist(Point other) {
        return (float) Math.sqrt(
                Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
