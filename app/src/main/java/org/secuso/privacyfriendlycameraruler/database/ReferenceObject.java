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

package org.secuso.privacyfriendlycameraruler.database;

/**
 * Class representing a predefined reference object. What is meant by size depends on the shape
 * (contained in the object type). Size of a line is its length. A circles size is its diameter.
 * A polygons size is its area.
 *
 * @author Roberts Kolosovs
 * Created by roberts on 14.02.17.
 */

public class ReferenceObject {

    public int nameId;
    public ObjectType type;
    public float size;

    ReferenceObject(int name, ObjectType type, float size) {
        this.nameId = name;
        this.type = type;
        this.size = size;
    }
}
