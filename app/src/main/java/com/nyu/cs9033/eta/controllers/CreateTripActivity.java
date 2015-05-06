package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.JSONRequestObject;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.TripDbHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CreateTripActivity extends Activity {
    private String _l_name,_l_address,_l_lat,_l_lng;
    private  EditText sec=null;
    private long mTrip_id;
    private static final int REQUEST_LOCATION = 2;
    private static final String AUTH_URI="com.example.nyu.hw3api";
    Uri uri = Uri.parse("location://"+AUTH_URI);
    private static final String TAG = CreateTripActivity.class.getSimpleName();
    private static final int CONTACT_REQ = 1;
    private EditText mTripTitle;
    private ArrayList<Person> mFriends;
    private EditText mPhone;
    private EditText mPersonName;
    private EditText mCurrentLocation;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    //private DatePicker mPickDate;
    private String mTime;
    private String mDate;
    private EditText mLocation;
    private String location;
    public static final String SERVER_URI = "http://cs9033-homework.appspot.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip);
        final Button mAddPeopleButton;
        Button mCreateTripButton;
        Button mResetButton;

        mLocation=(EditText)findViewById(R.id.tripLocation);

        mFriends = new ArrayList<Person>();

        mTripTitle = (EditText) findViewById(R.id.tripTitle);
        mDatePicker = (DatePicker) findViewById(R.id.tripDate);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mCurrentLocation = (EditText) findViewById(R.id.currentLocation);
        mPhone = (EditText) findViewById(R.id.phone);
        mPersonName = (EditText) findViewById(R.id.personName);
        mAddPeopleButton = (Button) findViewById(R.id.addFriendButton);

        mAddPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAddPeopleButtonPressed();
//                String name = mPersonName.getText().toString();
//                String phone = mPhone.getText().toString();
//                String curloc = mCurrentLocation.getText().toString();
//                Log.v(TAG, name);
//                Log.v(TAG, phone);
//                Log.v(TAG, curloc);
//                mFriends.add(new Person(name, phone, curloc));
//                mPersonName.setText("");
//                mPhone.setText("");
//                mCurrentLocation.setText("");
            }
        });
        // TODO - fill in here
        mCreateTripButton = (Button) findViewById(R.id.createTripButton);
        mCreateTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTrip();
            }
        });
        mResetButton = (Button) findViewById(R.id.resetTrip);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTrip();
            }
        });
        mTimePicker.setIs24HourView(true);
        Button searchButton = (Button)findViewById(R.id.searchButton);
        sec = (EditText)findViewById(R.id.secString);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String first=mLocation.getText().toString();
                final String second = sec.getText().toString();
                if(first.equals("")||second.equals("")){
                    Toast t= Toast.makeText(getApplicationContext(),"Please enter both the strings to search loc",Toast.LENGTH_LONG);
                    t.show();
                    Log.d("error","error");
                }else{
                    startSupportingApk(first,second);
                    Log.d("error", "noerror"+first+" "+second);

                }
            }
        });
    }
    public void startSupportingApk(String firstString,String secString ){
        //I am passing the search string as it is received from the EditText Box, i would like to parse the search string
        //in case needed if i know what exactly the HW3api is looking for.
        //Please allow me to complete this task next time coz i have to give my pc to repair

        String searchString=parseSearchQuery(firstString,secString);
        //String searchString=editTextSelected;
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.putExtra("searchVal",searchString);
        startActivityForResult(intent, REQUEST_LOCATION);
    }
    private String parseSearchQuery(String firstString,String secString) {
        return (firstString+"::"+secString);
    }
    private void mAddPeopleButtonPressed() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),CONTACT_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            if(uri!=null){
                String personName = retrieveContactName(uri);
                mPersonName.setText(personName);
                String phone = retrieveContactPhone(uri);
                mPhone.setText(phone);
                mFriends.add(new Person(personName, phone, null));
            }

        }else if (resultCode == 1 && requestCode == REQUEST_LOCATION){
            Bundle b = data.getExtras();
            ArrayList<String> result = b.getStringArrayList("retVal");
            _l_name = result.get(0);
            ((EditText)findViewById(R.id.tripLocation)).setText(_l_name);
            _l_address = result.get(1);
            _l_lat= result.get(2);
            _l_lng = result.get(3);
            ((EditText)findViewById(R.id.tripAddress)).setText(_l_address);
            ((EditText)findViewById(R.id.tripLocationLatitude)).setText(_l_lat);
            ((EditText)findViewById(R.id.tripLocationLongitude)).setText(_l_lng);
            sec.setText("");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String retrieveContactName(Uri uri) {
        Log.v("name","Inside getContactName");
        String contactName = null;
        Log.v("name","Inside getContactName"+" "+contactName);
        Cursor cursor = getContentResolver().query(uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null, null, null);
        if(cursor.moveToFirst()){
            contactName = cursor.getString(0);
            Log.v("name","Inside moveTofist"+" "+contactName);
        }
        Log.v("name","before cursor.close()"+" "+contactName);
        cursor.close();

        Log.d("name", "Retrieved Contact Info" + contactName);
        Log.d("name", "Retrieved Contact Info" + contactName);

        return contactName;
    }
    private String retrieveContactPhone(Uri uri) {

        String contactNumber = null;
        String contactID=null;
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }

    /**
     * This method should be used to
     * instantiate a Trip model object.
     *
     * @return The Trip as represented
     * by the View.
     */
    public Trip createTrip() {

        // TODO - fill in here

        mTime = mTimePicker.getCurrentHour() + ":" + mTimePicker.getCurrentMinute();
        mDate = mDatePicker.getDayOfMonth() + ":" + mDatePicker.getMonth() + ":" + mDatePicker.getYear();
        String title = mTripTitle.getText().toString();
        location=mLocation.getText().toString();
        Trip trip = new Trip(mTrip_id, title ,location,_l_address,_l_lat,_l_lng, mDate, mTime);
        trip.addPeople(mFriends.toArray(new Person[mFriends.size()]));
        TripDbHelper th = new TripDbHelper(this);
        th.insertTrip(trip);
        tripOnServer(SERVER_URI);
        saveTrip(trip);

        return null;
    }

    private void tripOnServer(String serverUri) {
            ConnectivityManager c = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = c.getActiveNetworkInfo();
            if( info!=null && info.isConnectedOrConnecting()){
                JSONRequestObject jsonRequest = new JSONRequestObject();
                jsonRequest.setMethod("POST");
                jsonRequest.setUri(serverUri);
                JSONArray locationDetails = null;
                try {
                    locationDetails  = new JSONArray(new Object[]{location,_l_address,_l_lat,_l_lng});
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonRequest.putToJSON("command", "CREATE_TRIP");
                jsonRequest.putToJSON("location", locationDetails);
                try {
                    long unixTime = System.currentTimeMillis() / 1000L;
                    Log.d(TAG, "Time in create trip "+unixTime);
                    jsonRequest.putToJSON("datetime", unixTime);

                    ArrayList<String> friends = new ArrayList<String>();
                    for (Person friend : mFriends) {
                        friends.add(friend.getName());
                    }
                    Log.d(TAG, "Insert names");
                    jsonRequest.putToJSON("people", new JSONArray(friends.toArray()));
                }
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                returnTripIdAsyncTask ct = new returnTripIdAsyncTask();
                ct.execute(jsonRequest);
            }

            else{
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();

            }

        }


    /**
     * For HW2 you should treat this method as a
     * way of sending the Trip data back to the
     * main Activity.
     * <p/>
     * Note: If you call finish() here the Activity
     * will end and pass an Intent back to the
     * previous Activity using setResult().
     *
     * @return whether the Trip was successfully
     * saved.
     */
    public boolean saveTrip(Trip trip) {

        // TODO - fill in here
        if (trip != null) {
            Intent mainActivityIntent = new Intent();
            mainActivityIntent.putExtra("trip", trip);
            setResult(Activity.RESULT_OK, mainActivityIntent);
            finish();
        }
        return false;
    }

    /**
     * This method should be used when a
     * user wants to cancel the creation of
     * a Trip.
     * <p/>
     * Note: You most likely want to call this
     * if your activity dies during the process
     * of a trip creation or if a cancel/back
     * button event occurs. Should return to
     * the previous activity without a result
     * using finish() and setResult().
     */
    public void cancelTrip() {
        // TODO - fill in here
        //setResult(Activity.RESULT_CANCELED);
        mPersonName.setText("");
        mPhone.setText("");
        mFriends.clear();
        mCurrentLocation.setText("");
        mTripTitle.setText("");
        Toast t=Toast.makeText(this,"All Details of this trip have been discarded",Toast.LENGTH_LONG);
        t.show();
    }


    private class returnTripIdAsyncTask extends AsyncTask<JSONRequestObject, Void, Long> {

        @Override
        protected Long doInBackground(JSONRequestObject... params) {
            Log.d(TAG,"doInBackground");
            String result = ConnectInternet.getData(params[0]);
            Log.v(TAG,result);
            long tripId = ParseJSON.parseId(result);
            return tripId;
        }
        @Override
        protected void onPostExecute(Long result) {
            if(result == 0 || result == -1){
                Toast.makeText(CreateTripActivity.this,"check your code", Toast.LENGTH_LONG)
                        .show();
            }
            else
            {
                Log.d(TAG,"TripID "+ result);
                mTrip_id = result;
            }
        }
    }

}
