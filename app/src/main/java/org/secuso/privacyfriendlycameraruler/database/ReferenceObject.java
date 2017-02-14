package org.secuso.privacyfriendlycameraruler.database;

/**
 * Created by roberts on 14.02.17.
 */

public class ReferenceObject {

    public String name;
    public ObjectType type;
    public float size;

    public ReferenceObject(String name, ObjectType type, float size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }
}
