package org.secuso.privacyfriendlycameraruler.database;

/**
 *
 * @author Karola Marky
 * @author Roberts Kolosovs
 * @version 20161223
 *
 * This class holds the predefined reference objects.
 *
 */

public class PredefinedReferences {

    private int PR_ID;
    private String PR_NAME;
    private ObjectType PR_TYPE;
    private float PR_SIZE;

    public PredefinedReferences() {    }


    /**
     * Always use this constructor to generate data with values.
     * @param PR_ID The primary key for the database (will be automatically set by the DB)
     * @param PR_NAME Human readable name of the reference object
     * @param PR_TYPE The type of the reference object (associated with shape)
     * @param PR_SIZE Size of the reference object (length, diameter, area)
     */
    public PredefinedReferences(int PR_ID, String PR_NAME, ObjectType PR_TYPE, float PR_SIZE) {

        this.PR_ID=PR_ID;
        this.PR_NAME=PR_NAME;
        this.PR_TYPE=PR_TYPE;
        this.PR_SIZE=PR_SIZE;
    }

    public ObjectType getPR_TYPE() {
        return PR_TYPE;
    }

    public void setPR_TYPE(ObjectType PR_TYPE) {
        this.PR_TYPE = PR_TYPE;
    }

    public float getPR_SIZE() {
        return PR_SIZE;
    }

    public void setPR_SIZE(float PR_SIZE) {
        this.PR_SIZE = PR_SIZE;
    }

    public int getPR_ID() {
        return PR_ID;
    }

    public void setPR_ID(int PR_ID) {
        this.PR_ID = PR_ID;
    }

    public String getPR_NAME() {
        return PR_NAME;
    }

    public void setPR_NAME(String PR_NAME) {
        this.PR_NAME = PR_NAME;
    }

}