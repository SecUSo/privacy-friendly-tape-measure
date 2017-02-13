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
    private int TYPE_ID;
    private float PR_SIZE;

    public PredefinedReferences() {    }


    /**
     * Always use this constructor to generate data with values.
     * @param PR_ID The primary key for the database (will be automatically set by the DB)
     * @param PR_NAME Human readable name of the reference object
     * @param TYPE_ID Foreign key to a type
     * @param PR_SIZE Size of the reference object (length, diameter, area)
     */
    public PredefinedReferences(int PR_ID, String PR_NAME, int TYPE_ID, float PR_SIZE) {

        this.PR_ID=PR_ID;
        this.PR_NAME=PR_NAME;
        this.TYPE_ID=TYPE_ID;
        this.PR_SIZE=PR_SIZE;
    }

    public int getTYPE_ID() {
        return TYPE_ID;
    }

    public void setTYPE_ID(int TYPE_ID) {
        this.TYPE_ID = TYPE_ID;
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