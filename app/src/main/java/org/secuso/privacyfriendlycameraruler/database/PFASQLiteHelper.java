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
     * Adds a single objectType to our Table
     * As no ID is provided and OT_ID is autoincremented the last
     * available key of the table is taken and incremented by 1
     *
     * @param objectType data that will be added
     */
    public void addObjectType(ObjectType objectType) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OT_NAME, objectType.getOT_NAME());
        values.put(OT_SHAPE, objectType.getOT_SHAPE());

        database.insert(TABLE_TYPE, null, values);
        database.close();
    }

    /**
     * Adds a single predefinedReference to our Table
     * As no ID is provided and OT_ID is autoincremented the last
     * available key of the table is taken and incremented by 1
     *
     * @param predefRef data that will be added
     */
    public void addPredefinedReference(PredefinedReferences predefRef) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PR_NAME, predefRef.getPR_NAME());
        values.put(PR_TYPE_ID, predefRef.getPR_TYPE().getOT_ID());
        values.put(PR_SIZE, predefRef.getPR_SIZE());

        database.insert(TABLE_PREDEFINED, null, values);
        database.close();
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
     * This method gets a single objectType entry based on its ID
     *
     * @param id of the objectType that is requested, could be get by the get-method
     * @return the objectType that is requested.
     */
    public ObjectType getObjectType(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("DATABASE", Integer.toString(id));

        Cursor cursor = database.query(TABLE_TYPE, new String[]{OT_ID,
                        OT_NAME, OT_SHAPE}, OT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        ObjectType objectType = new ObjectType();

        if (cursor != null && cursor.moveToFirst()) {
            objectType.setOT_ID(Integer.parseInt(cursor.getString(0)));
            objectType.setOT_NAME(cursor.getString(1));
            objectType.setOT_SHAPE(cursor.getString(2));

            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");

            cursor.close();
        }

        return objectType;
    }

    /**
     * This method gets a single predefinedReference entry based on its ID
     *
     * @param id of the predefinedReference that is requested, could be get by the get-method
     * @return the predefinedReference that is requested.
     */
    public PredefinedReferences getPredefRef(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.d("DATABASE", Integer.toString(id));

        Cursor cursor = database.rawQuery(
                "SELECT " + PR_ID +
                        ", " + PR_NAME +
                        ", " + PR_TYPE_ID +
                        ", " + OT_ID +
                        ", " + OT_NAME +
                        ", " + OT_SHAPE +
                        ", " + PR_SIZE +
                        " FROM " + TABLE_PREDEFINED + " INNER JOIN " + TABLE_TYPE +
                        " ON " + TABLE_PREDEFINED + "." + PR_TYPE_ID + " = " + TABLE_TYPE + "." + OT_ID +
                        " WHERE " + PR_ID + " = " + id
                , new String[]{});

        PredefinedReferences predefRef = new PredefinedReferences();

        if (cursor != null && cursor.moveToFirst()) {
            predefRef.setPR_ID(Integer.parseInt(cursor.getString(0)));
            predefRef.setPR_NAME(cursor.getString(1));

            ObjectType ot = new ObjectType();
            ot.setOT_ID(Integer.parseInt(cursor.getString(2)));
            ot.setOT_NAME(cursor.getString(3));
            ot.setOT_SHAPE(cursor.getString(4));

            predefRef.setPR_TYPE(ot);
            predefRef.setPR_SIZE(Float.parseFloat(cursor.getString(5)));

            Log.d("DATABASE", "Read " + cursor.getString(1) + " from DB");

            cursor.close();
        }
        return predefRef;
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
     * This method returns all objectType from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @return A list of all available objectType in the Database
     */
    public List<ObjectType> getAllObjectTypes() {
        List<ObjectType> objectTypeList = new ArrayList<ObjectType>();

        String selectQuery = "SELECT  * FROM " + TABLE_TYPE;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        ObjectType objectType = null;

        if (cursor.moveToFirst()) {
            do {
                objectType = new ObjectType();
                objectType.setOT_ID(Integer.parseInt(cursor.getString(0)));
                objectType.setOT_NAME(cursor.getString(1));
                objectType.setOT_SHAPE(cursor.getString(2));
                objectTypeList.add(objectType);
            } while (cursor.moveToNext());
        }

        return objectTypeList;
    }

    /**
     * This method returns all userDefinedReferences from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @return A list of all available userDefinedReferences in the Database
     */
    public List<UserDefinedReferences> getAllUDefRef() {
        List<UserDefinedReferences> uDefRefList = new ArrayList<UserDefinedReferences>();

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

    /**
     * This method returns all active userDefinedReferences from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @return A list of all active userDefinedReferences in the Database
     */
    public List<UserDefinedReferences> getAllActiveUDefRef() {
        List<UserDefinedReferences> uDefRefList = new ArrayList<UserDefinedReferences>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERDEFINED + " WHERE " + UDR_ACTIVE + " = 1";

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

    /**
     * This method returns all predefinedReferences with a certain type from the DB as a list
     * This could be used for instance to fill a recyclerView
     *
     * @param type the name of type to be filtered for
     * @return A list of all userDefinedReferences with a certain type in the Database
     */
    public List<PredefinedReferences> getAllPredRefOfType(String type) {
        List<PredefinedReferences> predefRefList = new ArrayList<PredefinedReferences>();

        String selectQuery = "SELECT " + PR_ID +
                ", " + PR_NAME +
                ", " + PR_TYPE_ID +
                ", " + OT_ID +
                ", " + OT_NAME +
                ", " + OT_SHAPE +
                ", " + PR_SIZE +
                " FROM " + TABLE_PREDEFINED + " INNER JOIN " + TABLE_TYPE +
                " ON " + TABLE_PREDEFINED + "." + PR_TYPE_ID + " = " + TABLE_TYPE + "." + OT_ID +
                " WHERE " + OT_NAME + " = " + type;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        PredefinedReferences predefRef = null;

        if (cursor.moveToFirst()) {
            do {
                predefRef = new PredefinedReferences();
                predefRef.setPR_ID(Integer.parseInt(cursor.getString(0)));
                predefRef.setPR_NAME(cursor.getString(1));

                ObjectType ot = new ObjectType();
                ot.setOT_ID(Integer.parseInt(cursor.getString(2)));
                ot.setOT_NAME(cursor.getString(3));
                ot.setOT_SHAPE(cursor.getString(4));

                predefRef.setPR_TYPE(ot);
                predefRef.setPR_SIZE(Float.parseFloat(cursor.getString(5)));
                predefRefList.add(predefRef);
            } while (cursor.moveToNext());
        }

        return predefRefList;
    }
}