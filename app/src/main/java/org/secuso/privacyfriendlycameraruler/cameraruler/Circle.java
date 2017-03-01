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

import static org.secuso.privacyfriendlycameraruler.cameraruler.CameraRulerView.TOUCHPOINT_RADIUS;

/**
 * A class representing a circular shape. Consists of a center point, a radius length and
 * a point for the thouchpoint for radius adjustment to be drawn on.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
 */

public class Circle extends Shape {
    public Point center = new Point(0, 0);

    public float radius = 0;

    public Point radiusTouchPoint = new Point(0, 0);

    public Circle(Point center, float radius) {
        this.center = center;
        this.radius = radius;
        radiusTouchPoint = new Point(center.x+radius+TOUCHPOINT_RADIUS, center.y);
    }

    /**
     * Computes the area of the circle.
     * @return The circle's area.
     */
    public float getArea() {
        return (float) (Math.PI * Math.pow(radius, 2));
    }
}
