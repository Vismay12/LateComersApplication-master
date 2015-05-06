package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.JSONRequestObject;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.TripDbHelper;
import com.nyu.cs9033.eta.service.PollTripService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int VIEWTRIPREQCODE = 2;
    private static final String TAG = "MainActivity";
    private static final int CREATE_TRIP_REQ_CODE = 1;
    Button createTripButton;
    ArrayList<String> mTripTitles;
    private ArrayList<Trip> mTrips;
    ArrayAdapter<String> adapter = null;
    TextView viewTripButton;
    private Trip currTrip;
    private LinearLayout tripDetails;
    ListView tripTitlesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTrips = new ArrayList<Trip>();
        TripDbHelper tdh = new TripDbHelper(getApplicationContext());
        mTrips = tdh.getAllTrips();
        mTripTitles = new ArrayList<String>();
        for (Trip t : mTrips) {
            mTripTitles.add(t.getName());
        }

        createTripButton = (Button) findViewById(R.id.createTripButton);
        viewTripButton = (TextView) findViewById(R.id.ViewTripActivityButton);
        viewTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage();
            }
        });
        mTripTitles = new ArrayList<String>();
        createTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateTripActivity();
            }
        });
        // TODO - fill in here
        tripTitlesView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTripTitles);
        tripTitlesView.setAdapter(adapter);
        tripTitlesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startViewTripActivity(mTrips.get(i));
            }
        });

//Current Trip

        TextView tv = (TextView) findViewById(R.id.ViewTripActivityButton);
        SharedPreferences sp = getSharedPreferences(Trip.SHARED_PREF, Context.MODE_PRIVATE);
        if (sp.contains(Trip.PREF_CURRENT_TRIP)) {
            long trip_id = sp.getLong(Trip.PREF_CURRENT_TRIP, -1);
            for (Trip t : mTrips) {
                if (t != null) {
                    if (t.getID() == trip_id) {
                        tv.setText(t.getName());
                        tripDetails = (LinearLayout) findViewById(R.id.currentTripInfo);
                        currTrip = t;
                        getTripStatus(ConnectInternet.SERVERURI);
                        PollTripService.setServiceAlarm(this, true);
                        break;
                    }
                }
            }
        } else {
            tv.setText("No active Trips!!");
        }

    }

    void getTripStatus(String uri) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            JSONRequestObject req = new JSONRequestObject();
            req.setMethod("POST");
            req.setUri(uri);
            req.putToJSON("command", "TRIP_STATUS");
            req.putToJSON("trip_id", currTrip.getID());

            TripStatus t = new TripStatus();
            t.execute(req);
        } else {
            Toast.makeText(this, "Internet Problem", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void errorMessage() {
        Toast t = Toast.makeText(getApplicationContext(), "Create Activity and select from list below", Toast.LENGTH_LONG);
        t.show();

    }

    /**
     * This method should start the
     * Activity responsible for creating
     * a Trip.
     */
    public void startCreateTripActivity() {

        // TODO - fill in here
        Intent createTripIntent = new Intent(this, CreateTripActivity.class);
        startActivityForResult(createTripIntent, CREATE_TRIP_REQ_CODE);
    }

    /**
     * This method should start the
     * Activity responsible for viewing
     * a Trip.
     */

    public void startViewTripActivity(Trip trip) {

        // TODO - fill in here
        if (trip != null) {
            Intent intent = new Intent(this, ViewTripActivity.class);
            intent.putExtra("trip", trip);
            startActivityForResult(intent, VIEWTRIPREQCODE);
        }
    }

    /**
     * Receive result from CreateTripActivity here.
     * Can be used to save instance of Trip object
     * which can be viewed in the ViewTripActivity.
     * <p/>
     * Note: This method will be called when a Trip
     * object is returned to the main activity.
     * Remember that the Trip will not be returned as
     * a Trip object; it will be in the persisted
     * Parcelable form. The actual Trip object should
     * be created and saved in a variable for future
     * use, i.e. to view the trip.
     * //
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO - fill in here
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"reqcode and resultcode"+requestCode+""+resultCode);
        if (requestCode == CREATE_TRIP_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                Bundle bundle = data.getExtras();
//                Trip trip = bundle.getParcelable("trip");
//
//                mTripTitles.add(trip.getTripTitle());
//                mTrips.add(trip);
//                adapter.notifyDataSetChanged();
//
//            } else {
//                Toast.makeText(this, "Trip Cancelled", Toast.LENGTH_LONG);
//            }
//        }
                Log.d(TAG, "inside onTrip Creativity createView block");
                tripTitlesView.setVisibility(View.VISIBLE);
                TripDbHelper tdh = new TripDbHelper(getApplicationContext());
                mTrips = tdh.getAllTrips();
                mTripTitles.clear();
                for (Trip t : mTrips) {
                    mTripTitles.add(t.getName());
                    Log.d("title", mTripTitles.get(0));

                }
                adapter.notifyDataSetChanged();

            }
        }
         else if(requestCode == VIEWTRIPREQCODE) {
            Log.d(TAG, "inside onTrip Creativity viewTrip block");
            if(resultCode == RESULT_OK){
                PollTripService.setServiceAlarm(this, true);
            }

        }else{
            (Toast.makeText(this, "Trip Cancelled", Toast.LENGTH_LONG)).show();

        }
    }

    private class TripStatus extends AsyncTask<JSONRequestObject, Void, ArrayList<Person>> {

        @Override
        protected ArrayList<Person> doInBackground(JSONRequestObject... params) {
            Log.d(TAG, "doInBackground");
            String result = ConnectInternet.getData(params[0]);
            Log.d(TAG, result);
            ArrayList<Person> pList = ParseJSON.parseInfo(result);
            return pList;
        }

        @SuppressLint("InlinedApi")
        @Override
        protected void onPostExecute(ArrayList<Person> pList) {
            if (pList == null) {
                Toast.makeText(MainActivity.this,
                        "Looks like an error has occurred!", Toast.LENGTH_LONG)
                        .show();
            } else {
                LinearLayout ll = new LinearLayout(MainActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

                TextView tv1 = new TextView(MainActivity.this);
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                tv1.setText(currTrip.getDate() + " " + currTrip.getTime());
                tv1.setPadding(1, 1, 5, 1);

                TextView tv2 = new TextView(MainActivity.this);
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                tv2.setText(currTrip.getLocation() + " " + currTrip.getLocationAddr());

                ll.addView(tv1);
                ll.addView(tv2);

                TextView peopleDet = new TextView(MainActivity.this);
                peopleDet.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
                for (Person p : pList) {
                    peopleDet.append(p.getName() + " " + p.getDistance_left() + " "
                            + p.getTime_left() + "\r\n");
                }

                final Button btn = new Button(MainActivity.this);
                btn.setText("Reached!!");
                btn.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSharedPreferences(Trip.SHARED_PREF, Context.MODE_PRIVATE)
                                .edit()
                                .remove(Trip.PREF_CURRENT_TRIP)
                                .commit();

                        PollTripService.setServiceAlarm(getApplicationContext(), false);

                        tripDetails.removeAllViews();

                    }
                });

                tripDetails.addView(ll);
                tripDetails.addView(peopleDet);
                tripDetails.addView(btn);
//                Intent mainActivityIntent = new Intent();
//                setResult(Activity.RESULT_OK, mainActivityIntent);
//                finish();
            }
        }
    }
}