package org.secuso.privacyfriendlycameraruler.cameraruler;

/**
 * Created by roberts on 23.01.17.
 * Abstract superclass for all shapes to be drawn onto the canvas in the CameraRulerView.
 */

public abstract class Shape {

    //inactive Shape's properties can't be changed and touch points aren't drawn
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }
}
