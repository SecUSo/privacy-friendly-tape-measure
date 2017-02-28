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
 * Created by roberts on 23.01.17.
 */

public class Triangle extends Polygon {

    public Triangle(Point p0, Point p1, Point p2) {
        corners = new Point[3];
        corners[0] = p0;
        corners[1] = p1;
        corners[2] = p2;
    }

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

    @Override
    public boolean isSelfIntersecting() {
        return false; //a triangle can't self-intersect
    }
}
