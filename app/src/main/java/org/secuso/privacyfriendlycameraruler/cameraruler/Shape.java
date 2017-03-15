/**
 * Privacy Friendly Camera Ruler is licensed under the GPLv3. Copyright (C) 2016 Roberts Kolosovs
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 * <p>
 * The icons used in the nagivation drawer are licensed under the CC BY 2.5.
 * In addition to them the app uses icons from Google Design Material Icons licensed under Apache
 * License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the
 * header in the navigation drawer) copyright Technische Universtität Darmstadt (2016).
 */

package org.secuso.privacyfriendlycameraruler.cameraruler;

import android.graphics.Matrix;

/**
 * Abstract superclass for all shapes to be drawn onto the canvas in the CameraRulerView.
 *
 * @author Roberts Kolosovs
 * Created by rkolosovs on 23.01.17.
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

    public abstract void move(float x, float y);

    public abstract void endMove();

//    public abstract void zoom(float scale, float x, float y);

    public abstract void zoom(Matrix m);
}
