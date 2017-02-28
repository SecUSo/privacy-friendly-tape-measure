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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karola Marky
 * @author Roberts Kolosovs
 * @version 20161223
 *          Structure based on http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 *          accessed at 16th June 2016
 *          <p>
 *          This class defines the structure of our database.
 */

public class PFASQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "PF_CAMERA_RULER_DB";

    //Name of table in the database
    private static final String TABLE_USERDEFINED = "USER_DEFINED_REFS";

    //Names of columns of the table USER_DEFINED_REFS
    private static final String UDR_ID = "udr_id";
    private static final String UDR_NAME = "udr_name";
    private static final String UDR_SHAPE = "udr_shape";
    private static final String UDR_SIZE = "udr_size";
    private static final String UDR_ACTIVE = "udr_active";

    public PFASQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_USERDEFINED_TABLE = "CREATE TABLE " + TABLE_USERDEFINED +
                "(" +
                UDR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UDR_NAME + " TEXT NOT NULL," +
                UDR_SHAPE + " TEXT NOT NULL," +
                UDR_SIZE + " REAL NOT NULL," +
                UDR_ACTIVE + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_USERDEFINED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDEFINED);

        onCreate(sqLiteDatabase);
    }

    /**
     * Adds a single userDefinedReference to our Table
     * As no ID is provided and OT_ID is autoincremented the last
     * available key of the table is taken and incremented by 1
     *
     * @param userDefRef data that will be added
     */
    public void addUserDefinedRef(UserDefinedReferences userDefRef) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UDR_NAME, userDefRef.getUDR_NAME());
        values.put(UDR_SHAPE, userDefRef.getUDR_SHAPE());
        values.put(UDR_SIZE, userDefRef.getUDR_SIZE());
        values.put(UDR_ACTIVE, userDefRef.getUDR_ACTIVE());

        database.insert(TABLE_USERDEFINED, null, values);
        database.close();
    }

    /**
     * This method gets a single userDefinedReference entry based on its ID
     *
     * @param id of the userDefinedReference that is requested, could be get by the get-method
     * @return the userDefinedReference that is requested.
     */
    public UserDefinedReferences getUserDefRef(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("DATABASE", Integer.toString(id));

        Cursor cursor = database.query(TABLE_USERDEFINED, new String[]{UDR_ID,
                        UDR_NAME, UDR_SHAPE, UDR_SIZE, UDR_ACTIVE}, UDR_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        UserDefinedReferences userDefRef = new UserDefinedReferences();

        if (cursor != null && cursor.moveToFirst()) {
            userDefRef.setUDR_ID(Integer.parseInt(cursor.getString(0)));
            userDefRef.setUDR_NAME(cursor.getString(1));
            userDefRef.setUDR_SHAPE(cursor.getString(2));
            userDefRef.setUDR_SIZE(Float.parseFloat(cursor.getString(3)));
            userDefRef.setUDR_ACTIVE(Integer.parseInt(cursor.getString(4)) == 1);

            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");

            cursor.close();
        }
        return userDefRef;
    }

    /**
     * Updates a database entry.
     *
     * @param userDefRef
     * @return actually makes the update
     */
    public int updateUserDefRef(UserDefinedReferences userDefRef) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(UDR_NAME, userDefRef.getUDR_NAME());
        values.put(UDR_SHAPE, userDefRef.getUDR_SHAPE());
        values.put(UDR_SIZE, userDefRef.getUDR_SIZE());
        values.put(UDR_ACTIVE, userDefRef.getUDR_ACTIVE());

        return database.update(TABLE_USERDEFINED, values, UDR_ID + " = ?",
                new String[]{String.valueOf(userDefRef.getUDR_ID())});
    }

    /**
     * This method returns all userDefinedReferences from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @return A list of all available userDefinedReferences in the Database
     */
    public ArrayList<UserDefinedReferences> getAllUDefRef() {
        ArrayList<UserDefinedReferences> uDefRefList = new ArrayList<UserDefinedReferences>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERDEFINED;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        UserDefinedReferences uDefRef = null;

        if (cursor.moveToFirst()) {
            do {
                uDefRef = new UserDefinedReferences();
                uDefRef.setUDR_ID(Integer.parseInt(cursor.getString(0)));
                uDefRef.setUDR_NAME(cursor.getString(1));
                uDefRef.setUDR_SHAPE(cursor.getString(2));
                uDefRef.setUDR_SIZE(Float.parseFloat(cursor.getString(3)));
                uDefRef.setUDR_ACTIVE(Integer.parseInt(cursor.getString(4)) == 1);
                uDefRefList.add(uDefRef);
            } while (cursor.moveToNext());
        }

        return uDefRefList;
    }
}