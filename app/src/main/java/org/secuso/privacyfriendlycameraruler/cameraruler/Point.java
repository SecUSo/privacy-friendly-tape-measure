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

/**
 * Class representing a point in a cartesian coordinate system. Consists of a x and a y coordinate.
 * Contains a method for computing the distance from it to another point.

 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
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
