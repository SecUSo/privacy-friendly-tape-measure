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

    //Names of table in the database
    private static final String TABLE_PREDEFINED = "PREDEFINED_REFS";
    private static final String TABLE_TYPE = "OBJECT_TYPES";
    private static final String TABLE_USERDEFINED = "USER_DEFINED_REFS";

    //Names of columns of the table PREDEFINED_REFS
    private static final String PR_ID = "pr_id";
    private static final String PR_NAME = "pr_name";
    private static final String PR_TYPE_ID = "pr_type_id";
    private static final String PR_SIZE = "pr_size";

    //Names of columns of the table OBJECT_TYPES
    private static final String OT_ID = "ot_id";
    private static final String OT_NAME = "ot_name";
    private static final String OT_SHAPE = "ot_shape";

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

        String CREATE_PREDEFINED_TABLE = "CREATE TABLE " + TABLE_PREDEFINED +
                "(" +
                PR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PR_NAME + " TEXT NOT NULL," +
                PR_TYPE_ID + " INTEGER NOT NULL," +
                PR_SIZE + "REAL NOT NULL," +
                " FOREIGN KEY (" + PR_TYPE_ID + ") REFERENCES " + TABLE_TYPE + "(" + OT_ID + "));";

        String CREATE_TYPES_TABLE = "CREATE TABLE " + TABLE_TYPE +
                "(" +
                OT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OT_NAME + " TEXT NOT NULL," +
                OT_SHAPE + " TEXT NOT NULL);";

        String CREATE_USERDEFINED_TABLE = "CREATE TABLE " + TABLE_USERDEFINED +
                "(" +
                UDR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UDR_NAME + " TEXT NOT NULL," +
                UDR_SHAPE + " TEXT NOT NULL," +
                UDR_SIZE + " REAL NOT NULL," +
                UDR_ACTIVE + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TYPES_TABLE);
        sqLiteDatabase.execSQL(CREATE_PREDEFINED_TABLE);
        sqLiteDatabase.execSQL(CREATE_USERDEFINED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PREDEFINED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDEFINED);

        onCreate(sqLiteDatabase);
    }


    /**
     * Adds a single sampleData to our Table
     * As no ID is provided and KEY_ID is autoincremented (see line 50)
     * the last available key of the table is taken and incremented by 1
     *
     * @param sampleData data that will be added
     */
//    public void addSampleData(PFASampleDataType sampleData) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        //To adjust this class for your own data, please add your values here.
//        ContentValues values = new ContentValues();
//        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
//        values.put(KEY_USERNAME, sampleData.getUSERNAME());
//        values.put(KEY_LENGTH, sampleData.getLENGTH());
//
//        database.insert(TABLE_SAMPLEDATA, null, values);
//        database.close();
//    }

    /**
     * Adds a single sampleData to our Table
     * This method can be used for re-insertion for example an undo-action
     * Therefore, the key of the sampleData will also be written into the database
     *
     * @param sampleData data that will be added
     *                   Only use this for undo options and re-insertions
     */
//    public void addSampleDataWithID(PFASampleDataType sampleData) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        //To adjust this class for your own data, please add your values here.
//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, sampleData.getID());
//        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
//        values.put(KEY_USERNAME, sampleData.getUSERNAME());
//        values.put(KEY_LENGTH, sampleData.getLENGTH());
//
//        database.insert(TABLE_SAMPLEDATA, null, values);
//
//        //always close the database after insertion
//        database.close();
//    }


    /**
     * This method gets a single sampleData entry based on its ID
     *
     * @param id of the sampleData that is requested, could be get by the get-method
     * @return the sampleData that is requested.
     */
//    public PFASampleDataType getSampleData(int id) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        Log.d("DATABASE", Integer.toString(id));
//
//        Cursor cursor = database.query(TABLE_SAMPLEDATA, new String[]{KEY_ID,
//                        KEY_DOMAIN, KEY_USERNAME, KEY_LENGTH}, KEY_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        PFASampleDataType sampleData = new PFASampleDataType();
//
//        if (cursor != null && cursor.moveToFirst()) {
//            sampleData.setID(Integer.parseInt(cursor.getString(0)));
//            sampleData.setDOMAIN(cursor.getString(1));
//            sampleData.setUSERNAME(cursor.getString(2));
//            sampleData.setLENGTH(Integer.parseInt(cursor.getString(3)));
//
//            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");
//
//            cursor.close();
//        }
//
//        return sampleData;
//
//    }

    /**
     * This method returns all data from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @return A list of all available sampleData in the Database
     */
//    public List<PFASampleDataType> getAllSampleData() {
//        List<PFASampleDataType> sampleDataList = new ArrayList<PFASampleDataType>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;
//
//        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//
//        PFASampleDataType sampleData = null;
//
//        if (cursor.moveToFirst()) {
//            do {
//                //To adjust this class for your own data, please add your values here.
//                //be careful to use the right get-method to get the data from the cursor
//                sampleData = new PFASampleDataType();
//                sampleData.setID(Integer.parseInt(cursor.getString(0)));
//                sampleData.setDOMAIN(cursor.getString(1));
//                sampleData.setUSERNAME(cursor.getString(2));
//                sampleData.setLENGTH(Integer.parseInt(cursor.getString(3)));
//
//                sampleDataList.add(sampleData);
//            } while (cursor.moveToNext());
//        }
//
//        return sampleDataList;
//    }

    /**
     * Updates a database entry.
     *
     * @param sampleData
     * @return actually makes the update
     */
//    public int updateSampleData(PFASampleDataType sampleData) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        //To adjust this class for your own data, please add your values here.
//        ContentValues values = new ContentValues();
//        values.put(KEY_DOMAIN, sampleData.getDOMAIN());
//        values.put(KEY_USERNAME, sampleData.getUSERNAME());
//        values.put(KEY_LENGTH, sampleData.getLENGTH());
//
//        return database.update(TABLE_SAMPLEDATA, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(sampleData.getID())});
//    }

    /**
     * Deletes sampleData from the DB
     * This method takes the sampleData and extracts its key to build the delete-query
     *
     * @param sampleData that will be deleted
     */
//    public void deleteSampleData(PFASampleDataType sampleData) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        database.delete(TABLE_SAMPLEDATA, KEY_ID + " = ?",
//                new String[]{Integer.toString(sampleData.getID())});
//        //always close the DB after deletion of single entries
//        database.close();
//    }

    /**
     * deletes all sampleData from the table.
     * This could be used in case of a reset of the app.
     */
//    public void deleteAllSampleData() {
//        SQLiteDatabase database = this.getWritableDatabase();
//        database.execSQL("delete from " + TABLE_SAMPLEDATA);
//    }

}