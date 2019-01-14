package com.saskpolytech.cst142;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


/**
 * This class will be used to access the database where SavedLocation objects will be stored
 * @author Dylan Sies (cst142)
 */
public class LocationDBHelper extends SQLiteOpenHelper
{

    //Static attributes that pertain to the database and the first table.
    private static final String DB_NAME = "coordinates.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_NAME = "Location";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public SQLiteDatabase sqlDB;


    //Constructor
    public LocationDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * Open the database connection
     */
    public void open()
    {
        sqlDB = this.getWritableDatabase();
    }


    /**
     * Close the database connection
     */
    public void close()
    {
        sqlDB.close();
    }


    /**
     * Called when the database file does not exist by getWriteableDatabase.
     * Create the table with specified name and fields
     * @param db - Database which the table will be created on
     */
    public void onCreate(SQLiteDatabase db)
    {
        String sqlCreate = "CREATE TABLE " + TABLE_NAME + " (" + ID + " integer primary key autoincrement, " +
                            NAME + " text not null, " +
                            LATITUDE + " text not null, " +
                            LONGITUDE + " text not null);";

        //Execute the sql statement creating the table
        db.execSQL(sqlCreate);

    }


    /**
     * Called if the version of the database changes, it will delete the location table and call
     * onCreate which will create it again.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Delete the location table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    /**
     * This method will create a location object in the database.
     * @param location The location object that will be created based on its parameters in the database
     * @return The id the the object was given when it was created in the database
     */
    public long createLocation(SavedLocation location)
    {
        //Add the location objects parameters to cvs for the insert statement
        ContentValues cvs = new ContentValues();

        cvs.put(NAME, location.sName);
        cvs.put(LATITUDE, location.sLatitude);
        cvs.put(LONGITUDE, location.sLongitude);

        long autoid = sqlDB.insert(TABLE_NAME, null, cvs);

        //Set the objects id to the returned id
        location.id = autoid;

        return autoid;
    }


    /**
     * This method will update an existing Savedlocation object in the database
     * @param location The location object that will be updated
     * @return True if the item was updated, false otherwise
     */
    public boolean updateLocation(SavedLocation location)
    {
        //Check to see if the purchase is already saved in the database
        if (location.id < 0)
        {
            return false;
        }
        else
        {
            //Create content values list for the update statement
            ContentValues cvs = new ContentValues();

            cvs.put(NAME, location.sName);
            cvs.put(LATITUDE, location.sLatitude);
            cvs.put(LONGITUDE, location.sLongitude);

            return sqlDB.update(TABLE_NAME, cvs, ID + " = " + location.id, null) > 0;
        }
    }


    /**
     * This method will delete a SavedLocation in the database
     * @param location The location object that will be deleted
     * @return True if the item was deleted, false otherwise.
     */
    public boolean deleteLocation(SavedLocation location)
    {
        return sqlDB.delete(TABLE_NAME, ID + " = " + location.id, null) > 0;
    }


    /**
     * This method will get every single SavedLocation from the database. It will extract the data
     * from the cursor and convert it to SavedLocation objects.
     * @return An array containing all SavedLocation objects in the database.
     */
    public ArrayList<SavedLocation> getAllLocations()
    {
        //Arraylist of SavedLocation objects to be returned
        ArrayList<SavedLocation> aReturn = new ArrayList<>();


        //String array of columns to return
        String[] sFields = new String[] {ID, NAME, LATITUDE, LONGITUDE};

        Cursor cursor = sqlDB.query(TABLE_NAME, sFields, null, null, null, null, null);

        //Go through the cursor and create SavedLocation objects and add them to the arraylist if there is any
        if (cursor != null)
        {
            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
                aReturn.add(new SavedLocation(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                cursor.moveToNext();
            }

            //Free the cursor
            cursor.close();
        }

        return aReturn;
    }


    public Cursor getAllLocationsAsCursor()
    {
        //Array of all columsn to return
        String[] sFields = new String[] {ID, NAME, LATITUDE, LONGITUDE};

        //Execute the sql and return the cursor
        return sqlDB.query(TABLE_NAME,sFields,null,null,null,null,null);
    }


    /**
     * This method will find and return a specific SavedLocation object from the database based
     * on the passed in ID
     * @param id The id to look for in the database
     * @return The SavedLocation object with the specified id
     */
    public SavedLocation getLocation(long id)
    {
        //Saved Location object to be returned
        SavedLocation obReturn = null;

        //String array of columns to return
        String[] sFields = new String[] {ID, NAME, LATITUDE, LONGITUDE};

        Cursor cursor = sqlDB.query(TABLE_NAME, sFields, ID + " = " + id, null, null, null, null);

        //If an object is found create and return a SavedLocation
        if (cursor != null)
        {
            cursor.moveToFirst();
            obReturn = new SavedLocation(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Free the cursor
            cursor.close();
        }

        return obReturn;
    }


}
