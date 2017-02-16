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