
package com.nyu.cs9033.eta.models;

import java.text.ParseException;
import java.util.Locale;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Trip implements Parcelable {

    // Member fields should exist here, what else do you need for a trip?
    // Please add additional fields
    //private long trip_id;
    private String name;
    private Person[] invited_people;
    private String location_name;
    private String location_addr;
    private String location_lat;
    private String location_lng;
    private String day_of_trip;
    private String time_of_trip;
    private long trip_id;
    public static final String ACTIVE_TRIP = "curr_trip";
    public static final String SHARED_PREF = "com.nyu.cs9033.eta.sharedprefs";
    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel p) {
            return new Trip(p);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    /**
     * Create a Trip model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Trip(Parcel p) {
        trip_id = p.readLong();
        name = p.readString();
        final int invite_size = p.readInt();
        invited_people = new Person[invite_size];
        p.readTypedArray(invited_people, Person.CREATOR);
        location_name = p.readString();
        location_addr = p.readString();
        location_lat = p.readString();
        location_lng = p.readString();
        day_of_trip = p.readString();
        time_of_trip = p.readString();
    }

    /**
     * Create a Trip model object from arguments
     *
     * @param name  Add arbitrary number of arguments to
     * instantiate Trip class based on member variables.
     * @param time2
     * @throws ParseException
     */
    public Trip(long trip_id,String name, String l_name,String l_addr,
                String l_lat, String l_lng,String date, String time){

        this.trip_id = trip_id;
        this.name = name.toUpperCase(Locale.US);
        Log.d("title inside constructo",name);
        this.location_name = l_name;
        this.location_addr = l_addr;
        this.location_lat = l_lat;
        this.location_lng = l_lng;
        this.day_of_trip = date;
        this.time_of_trip = time;
        invited_people = new Person[1];
    }

    /**
     * Serialize Trip object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest Parcel object that gets written on
     * serialization. Use functions to write out the
     * object stored via your member variables.
     *
     * @param flags Additional flags about how the object
     * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     * In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.trip_id);
        dest.writeString(this.name);
        dest.writeInt(invited_people.length);
        dest.writeTypedArray(invited_people,0);
        dest.writeString(this.location_name);
        dest.writeString(this.location_addr);
        dest.writeString(this.location_lat);
        dest.writeString(this.location_lng);
        dest.writeString(this.day_of_trip);
        dest.writeString(this.time_of_trip);
    }

    /**
     * Feel free to add additional functions as necessary below.
     */
	/*
	 * Add People to the trip after it is created.
	 */
    public void addPeople(Person[] p){
        if(invited_people.length == 1 && invited_people[0] == null){
            invited_people = p;
        }
        else
        {
            Person[] temp_copy = new Person[invited_people.length+p.length];
            System.arraycopy(invited_people, 0, temp_copy, 0, invited_people.length);
            System.arraycopy(p, 0, temp_copy, invited_people.length,p.length);
            invited_people = temp_copy;
        }
    }
    public final String getName(){
        Log.d("title inside getName",name);

        return name;
    }
    public final long getTripID(){
        return trip_id;
    }
    public String getDate(){
        return day_of_trip;
    }
    public final String getTime(){
        return time_of_trip;
    }

    public final Person[] getPeople(){
        return invited_people;
    }

    public final String getLocation(){
        return location_name;
    }
    public final String getLocationAddr(){
        return location_addr;
    }
    public final String getLocationLat(){
        return location_lat;
    }
    public final String getLocationLng(){
        return location_lng;
    }

    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }
}
