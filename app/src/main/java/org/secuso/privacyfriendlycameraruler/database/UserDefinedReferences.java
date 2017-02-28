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
 *
 * @author Karola Marky
 * @author Roberts Kolosovs
 * @version 20161223
 *
 * This class holds the user defined reference objects.
 *
 */

public class UserDefinedReferences {

    private int UDR_ID;
    private String UDR_NAME;
    private String UDR_SHAPE;
    private float UDR_SIZE;
    private boolean UDR_ACTIVE;

    public UserDefinedReferences() {
        this.UDR_NAME="";
        this.UDR_SHAPE="";
        this.UDR_SIZE=0;
        this.UDR_ACTIVE=false;
    }

    public UserDefinedReferences(int UDR_ID) {
        this.UDR_ID=UDR_ID;
        this.UDR_NAME="";
        this.UDR_SHAPE="";
        this.UDR_SIZE=0;
        this.UDR_ACTIVE=false;
    }

    public UserDefinedReferences(int UDR_ID, String name, String shape, float size) {

        this.UDR_ID=UDR_ID;
        this.UDR_NAME=name;
        this.UDR_SHAPE=shape;
        this.UDR_SIZE=size;
        this.UDR_ACTIVE=true;
    }

    /**
     * All variables need getters and setters. because the variables are private.
     */

    public float getUDR_SIZE() {
        return UDR_SIZE;
    }

    public void setUDR_SIZE(float UDR_SIZE) {
        this.UDR_SIZE = UDR_SIZE;
    }

    public boolean getUDR_ACTIVE() {
        return UDR_ACTIVE;
    }

    public void setUDR_ACTIVE(boolean UDR_ACTIVE) {
        this.UDR_ACTIVE = UDR_ACTIVE;
    }

    public String getUDR_SHAPE() {
        return UDR_SHAPE;
    }

    public void setUDR_SHAPE(String UDR_SHAPE) {
        this.UDR_SHAPE = UDR_SHAPE;
    }

    public int getUDR_ID() {
        return UDR_ID;
    }

    public void setUDR_ID(int UDR_ID) {
        this.UDR_ID = UDR_ID;
    }

    public String getUDR_NAME() {
        return UDR_NAME;
    }

    public void setUDR_NAME(String UDR_NAME) {
        this.UDR_NAME = UDR_NAME;
    }

}