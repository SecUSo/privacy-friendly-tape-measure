package org.secuso.privacyfriendlycameraruler.database;

/**
 *
 * @author Karola Marky
 * @version 20161223
 *
 * This class holds the types of predefined reference objects along with the corresponding shape.
 *
 */

public class ObjectType {

    private int OT_ID;
    private String OT_NAME;
    private String OT_SHAPE;

    public ObjectType() {    }


    /**
     * Always use this constructor to generate data with values.
     * @param OT_ID The primary key for the database (will be automatically set by the DB)
     * @param OT_NAME Human readable name of the category
     * @param OT_SHAPE Shape associated with the object type (circle for coins, tetragon for banknotes and paper)
     */
    public ObjectType(int OT_ID, String OT_NAME, String OT_SHAPE) {

        this.OT_ID=OT_ID;
        this.OT_NAME=OT_NAME;
        this.OT_SHAPE=OT_SHAPE;
    }

    public String getOT_SHAPE() {
        return OT_SHAPE;
    }

    public void setOT_SHAPE(String OT_SHAPE) {
        this.OT_SHAPE = OT_SHAPE;
    }

    public int getOT_ID() {
        return OT_ID;
    }

    public void setOT_ID(int OT_ID) {
        this.OT_ID = OT_ID;
    }

    public String getOT_NAME() {
        return OT_NAME;
    }

    public void setOT_NAME(String OT_NAME) {
        this.OT_NAME = OT_NAME;
    }

}