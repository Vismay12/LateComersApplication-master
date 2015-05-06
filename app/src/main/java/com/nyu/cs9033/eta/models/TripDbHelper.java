package com.nyu.cs9033.eta.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Vismay on 4/8/2015.
 */
public class TripDbHelper extends SQLiteOpenHelper implements BaseColumns {

private static final String DATABASE_NAME = "trip_db";
    private static final int DATABASE_VERSION = 2;

    //Tables for storing data
    private static final String TABLE_TRIP = "trip";
    private static final String TABLE_LOCATION = "location";
    private static final String TABLE_PERSON = "person";
    private static final String TABLE_TRIP_PERSON = "trip_person";

    //Column names for trip table
    private static final String COLUMN_TRIP_NAME = "name";
    private static final String COLUMN_TRIP_DATE = "date";
    private static final String COLUMN_TRIP_TIME = "time";
    private static final String COLUMN_TRIP_LOCATION = "location_id";

    //Column names for location table
    private static final String COLUMN_LOCATION_NAME = "name";
    private static final String COLUMN_LOCATION_ADDR = "address";
    private static final String COLUMN_LOCATION_LAT = "latitude";
    private static final String COLUMN_LOCATION_LNG = "longitude";

    //Column names for person table
    private static final String COLUMN_PERSON_NAME = "name";
    private static final String COLUMN_PERSON_NUMBER = "phone_no";
    private static final String COLUMN_PERSON_ID = "contact_id";
    private static final String COLUMN_PERSON_LOCATION = "location_id";
    private static final String COLUMN_TRIP_ID = "trip_id";
    private static final String TAG = "TripDatabaseHelper";


    public TripDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnTypes = new ArrayList<String>();


        columnNames.add(COLUMN_TRIP_ID);
        columnNames.add(COLUMN_TRIP_NAME);
        columnNames.add(COLUMN_TRIP_DATE);
        columnNames.add(COLUMN_TRIP_TIME);
        columnNames.add(COLUMN_TRIP_LOCATION);

        columnTypes.add("INTEGER PRIMARY KEY");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("INTEGER");

        createTable(TABLE_TRIP, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();

        columnNames.add(COLUMN_PERSON_ID);
        columnNames.add(COLUMN_PERSON_NAME);
        columnNames.add(COLUMN_PERSON_NUMBER);
        columnNames.add(COLUMN_PERSON_LOCATION);

        columnTypes.add("INTEGER PRIMARY KEY");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("TEXT");
        columnTypes.add("TEXT");

        createTable(TABLE_PERSON, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();

        columnNames.add(_ID);
        columnNames.add(COLUMN_LOCATION_NAME);
        columnNames.add(COLUMN_LOCATION_ADDR);
        columnNames.add(COLUMN_LOCATION_LAT);
        columnNames.add(COLUMN_LOCATION_LNG);

        columnTypes.add("INTEGER PRIMARY KEY");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("TEXT");
        columnTypes.add("TEXT");
        columnTypes.add("TEXT");

        createTable(TABLE_LOCATION, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();

        columnNames.add(COLUMN_PERSON_ID);
        columnNames.add(COLUMN_TRIP_ID);
        columnTypes.add("INTEGER");
        columnTypes.add("INTEGER");

        createTable(TABLE_TRIP_PERSON, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();
    }

    private void createTable(String tableName, ArrayList<String> columnNames,
                             ArrayList<String> columnTypes, SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+ tableName + "(");
        for(Iterator<String> i = columnNames.iterator(), j = columnTypes.iterator();
            i.hasNext() && j.hasNext();){
            sb.append(i.next()+ " " + j.next()+",");
        }
        if(sb.length()>0 && sb.charAt(sb.length()-1)==','){
            sb.deleteCharAt(sb.length() -1);
        }
        sb.append(")");
        try{
            db.execSQL(sb.toString());
        }
        catch(SQLException e){
            Log.e(tableName,e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_PERSON);

        // create tables again
        onCreate(db);
    }

    public long insertTrip(Trip trip) {
        Person[] invited = trip.getPeople();
        ArrayList<Long> pId = new ArrayList<Long>();
        long location_id = insertLocation(trip.getLocation(),
                trip.getLocationAddr(),
                trip.getLocationLat(),
                trip.getLocationLng());
        Log.d(TAG,"inserted location "+location_id);
        for(Person p : invited){
            pId.add(insertPerson(p));
         //   Log.d(TAG,"inserted Person "+p.getContactId());
        }
       // long trip_id = trip.getTrip_id();

        ContentValues cv = new ContentValues();
      //  cv.put(COLUMN_TRIP_ID, trip_id);
        cv.put(COLUMN_TRIP_NAME, trip.getName());
        cv.put(COLUMN_TRIP_DATE, trip.getDate().toString());
        cv.put(COLUMN_TRIP_TIME, trip.getTime().toString());
        cv.put(COLUMN_TRIP_LOCATION, location_id);
        // return id of new trip
        long trip_id=getWritableDatabase().insert(TABLE_TRIP, null, cv);
        cv.clear();
        for(long i:pId){
            cv.put(COLUMN_PERSON_ID, i);
            cv.put(COLUMN_TRIP_ID, trip_id);
            getWritableDatabase().insert(TABLE_TRIP_PERSON, null, cv);
            cv.clear();
        }
        Log.d(TAG,"Trip is inserted");
        return trip_id;
    }

    public long insertLocation(String name, String addr, String lat,String lng ) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_NAME, name);
        cv.put(COLUMN_LOCATION_ADDR, addr);
        cv.put(COLUMN_LOCATION_LAT, lat);
        cv.put(COLUMN_LOCATION_LNG, lng);
        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }
    public long insertPerson(Person p){
        ContentValues cv = new ContentValues();
      //  cv.put(COLUMN_PERSON_ID, p.getContactId());
        cv.put(COLUMN_PERSON_NAME, p.getName());
        cv.put(COLUMN_PERSON_NUMBER, p.getPhone());
        cv.put(COLUMN_PERSON_LOCATION, p.getLocation());

        return getWritableDatabase().insertWithOnConflict(TABLE_PERSON,
                null, cv,
                SQLiteDatabase.CONFLICT_IGNORE);
    }
    @SuppressLint("UseSparseArrays")
    public ArrayList<Trip> getAllTrips(){
        ArrayList<Trip> allTrips = new ArrayList<Trip>();
        ArrayList<Long> tripIds = new ArrayList<Long>();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(TABLE_TRIP + " join "+TABLE_LOCATION +" on "+TABLE_TRIP+"."+COLUMN_TRIP_LOCATION
                + " = "+ TABLE_LOCATION+"."+_ID);
        Cursor c = qb.query(getReadableDatabase(),
                new String[]{TABLE_TRIP+"."+COLUMN_TRIP_ID,TABLE_TRIP+"."+COLUMN_TRIP_NAME,
                        COLUMN_TRIP_DATE,COLUMN_TRIP_TIME, TABLE_LOCATION+"."+COLUMN_LOCATION_NAME,
                        COLUMN_LOCATION_ADDR,COLUMN_LOCATION_LAT,COLUMN_LOCATION_LNG},
                null,null,null,null,"date("+COLUMN_TRIP_DATE+") DESC");
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            Trip t = null;
            t = new Trip(c.getLong(0),c.getString(1),c.getString(4),c.getString(5),
                    c.getString(6),c.getString(7),c.getString(2),c.getString(3));
            allTrips.add(t);
            tripIds.add(c.getLong(0));
        }
        c.close();
        qb.setTables(TABLE_TRIP_PERSON+" join "+TABLE_PERSON+" on "+TABLE_TRIP_PERSON+"."+COLUMN_PERSON_ID
                + " = "+ TABLE_PERSON+"."+COLUMN_PERSON_ID);
        for(int i = 0; i<tripIds.size(); i++){
            long id = tripIds.get(i);
            c = qb.query(getReadableDatabase(), new String[]{
                    TABLE_PERSON+"."+COLUMN_PERSON_ID ,COLUMN_PERSON_NAME,COLUMN_PERSON_LOCATION,
                    COLUMN_PERSON_NUMBER}, COLUMN_TRIP_ID+" = ?", new String[]{id+""}, null, null, null);
            ArrayList<Person> people = new ArrayList<Person>();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                Person p = new Person(c.getString(1),c.getString(3),c.getString(2));
                people.add(p);
            }
            c.close();
            allTrips.get(i).addPeople(people.toArray(new Person[people.size()]));
            people.clear();
        }
        return allTrips;
    }}
