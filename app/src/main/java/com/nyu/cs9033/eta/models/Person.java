package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

public class Person implements Parcelable {

	// Member fields should exist here, what else do you need for a person?
	// Please add additional fields
    public static final String TAG = Person.class.getSimpleName();
	private String mName;
    private String mPhone;
    private String mCurrentLocation;
	private long mTimeleft;
	private double mDistLeft;    /**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		public Person createFromParcel(Parcel p) {
			return new Person(p);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}

	};

	/**
	 * Create a Person model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 *
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Person(Parcel p) {

		// TODO - fill in here
		mName=p.readString();
        mPhone=p.readString();
        mCurrentLocation=p.readString();
	}

	/**
	 * Create a Person model object from arguments
	 *
	 * @param name Add arbitrary number of arguments to
	 * instantiate Person class based on member variables.
	 */
	public Person(String name, String phone, String currentLocation) {
		this.mCurrentLocation=currentLocation;
        this.mName=name;
        this.mPhone=phone;
		// TODO - fill in here, please note you must have more arguments here
	}

	public Person(String name, double distanceLeft, long timeLeft){
		this.mName = name;
		this.mDistLeft = distanceLeft;
		this.mTimeleft = timeLeft;
	}

	/**
	 * Serialize Person object by using writeToParcel.
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
		dest.writeString(mName);
        dest.writeString(mPhone);
        dest.writeString(mCurrentLocation);
		// TODO - fill in here
	}

	/**
	 * Feel free to add additional functions as necessary below.
	 */

	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
    public String getName() {
        // TODO Auto-generated method stub
        return mName;
    }
    public String getPhone(){
        return mPhone;
    }
    public String getLocation(){
        return mCurrentLocation;
    }

	public long getTime_left() {
		return mTimeleft;
	}

	public void setTime_left(int time_left) {
		this.mTimeleft = time_left;
	}

	public double getDistance_left() {
		return mDistLeft;
	}

}
