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
 * A class representing a four sided polygon. Implements the area computation method.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
 */

public class Tetragon extends Polygon {

    public Tetragon(Point p0, Point p1, Point p2, Point p3) {
        corners = new Point[4];
        corners[0] = p0;
        corners[1] = p1;
        corners[2] = p2;
        corners[3] = p3;
    }

    public float getArea() {
        // partition the tetragon into two triangles
        double a1 = corners[0].dist(corners[1]);
        double b1 = corners[0].dist(corners[3]);
        double c = corners[3].dist(corners[1]);
        double a2 = corners[1].dist(corners[2]);
        double b2 = corners[2].dist(corners[3]);

        double[] lengths1 = sort3(a1, b1, c);
        double[] lengths2 = sort3(a2, b2, c);

        a1 = lengths1[0];
        b1 = lengths1[1];
        double c1 = lengths1[2];
        a2 = lengths2[0];
        b2 = lengths2[1];
        double c2 = lengths2[2];

        // compute the area of both triangles via Heron's formula and add them together
        return (float) (0.25 * (Math.sqrt((a1+(b1+c1))*(c1-(a1-b1))*(c1+(a1-b1))*(a1+(b1-c1))) +
                Math.sqrt((a2+(b2+c2))*(c2-(a2-b2))*(c2+(a2-b2))*(a2+(b2-c2)))));
    }
}
