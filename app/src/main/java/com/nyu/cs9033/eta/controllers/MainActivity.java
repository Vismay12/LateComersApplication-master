package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.JSONRequestObject;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.TripDbHelper;
import com.nyu.cs9033.eta.service.TripUpdaterService;
import android.app.Activity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int VIEW_TRIP_REQ_CODE = 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CREATE_TRIP_REQ_CODE = 1;
    Button createTripButton;
    ArrayList<String> mTripTitles;
    private ArrayList<Trip> mTrips;
    ArrayAdapter<String> adapter = null;
    TextView viewTripButton;
    private Trip activeTrip;
    private LinearLayout currentTripInfo;
    ListView tripTitlesView;
    TextView viewTripActivityView;
    TripDbHelper tdh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTrips = new ArrayList<Trip>();
        tdh = new TripDbHelper(getApplicationContext());
        mTrips = tdh.getAllTrips();
        mTripTitles = new ArrayList<String>();
        for (Trip t : mTrips) {
            mTripTitles.add(t.getName());
        }

        createTripButton = (Button) findViewById(R.id.createTripButton);

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

        viewTripActivityView = (TextView) findViewById(R.id.ViewTripActivityButton);
        viewTripActivityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage();
            }
        });
        SharedPreferences sp = getSharedPreferences(Trip.SHARED_PREF, Context.MODE_PRIVATE);
        if (sp.contains(Trip.ACTIVE_TRIP)) {
            long trip_id = sp.getLong(Trip.ACTIVE_TRIP, -1);
            for (Trip trip : mTrips) {
                if (trip != null) {
                    if (trip.getTripID() == trip_id) {
                        viewTripActivityView.setText(trip.getName());
                        currentTripInfo = (LinearLayout) findViewById(R.id.currentTripInfo);
                        activeTrip = trip;
                        getTripStatus(ConnectInternet.SERVERURI);
                        TripUpdaterService.wakeUpPolling(this, true);
                        break;
                    }
                }
            }
        } else {
            viewTripActivityView.setText("Select Trips Seen Below");

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
            req.putToJSON("trip_id", activeTrip.getTripID());

            TripStatus t = new TripStatus();
            t.execute(req);
        } else {
            Toast.makeText(this, "Internet Problem", Toast.LENGTH_LONG).show();
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
            startActivityForResult(intent, VIEW_TRIP_REQ_CODE);
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
         else if(requestCode == VIEW_TRIP_REQ_CODE) {
            Log.d(TAG, "inside onTrip Creativity viewTrip block");
            if(resultCode == RESULT_OK){
                TripUpdaterService.wakeUpPolling(this, true);
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
            ArrayList<Person> friends = ParseJSON.parseInfo(result);
            return friends;
        }

        @Override
        protected void onPostExecute(ArrayList<Person> pList) {
            if (pList == null) {
                Toast.makeText(MainActivity.this,"Looks like an error has occurred!", Toast.LENGTH_LONG).show();
            } else {
                LinearLayout additionalLayout = new LinearLayout(MainActivity.this);
                additionalLayout.setOrientation(LinearLayout.HORIZONTAL);
                additionalLayout.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

                TextView additionalTextView = new TextView(MainActivity.this);
                TextView additionalViewLocLat = new TextView(MainActivity.this);
                TextView friends = new TextView(MainActivity.this);
                friends.setLayoutParams(new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
                for (Person p : pList) {
                    friends.append(p.getName() + " " + p.getDistanceLeft() + " " + p.getTimeLeft() + "\r\n");
                }

                additionalTextView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                additionalTextView.setText(activeTrip.getDate() + " " + activeTrip.getTime());
                additionalTextView.setPadding(2, 2, 5, 2);
                additionalViewLocLat.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                additionalViewLocLat.setText(activeTrip.getLocation() + " " + activeTrip.getLocationAddr());
                additionalLayout.addView(additionalTextView);
                additionalLayout.addView(additionalViewLocLat);
                currentTripInfo.addView(additionalLayout);
                currentTripInfo.addView(friends);
                final Button endButton = new Button(MainActivity.this);
                endButton.setText("Present Sir");
                endButton.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSharedPreferences(Trip.SHARED_PREF, Context.MODE_PRIVATE).edit().remove(Trip.ACTIVE_TRIP).commit();
                        TripUpdaterService.wakeUpPolling(getApplicationContext(), false);
                        currentTripInfo.removeAllViews();
                        viewTripActivityView.setText("Trip tracking ended");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                                mTrips = tdh.getAllTrips();
                                for (Trip t : mTrips) {
                                    if(mTripTitles.contains(t)){
                                        Log.d(TAG,"already present");
                                    }else{
                                        mTripTitles.add(t.getName());
                                        Log.d("title", mTripTitles.get(0));
                                    }


                                }
                                adapter.notifyDataSetChanged();
                            }
//                        });

  //                  }
                });
                currentTripInfo.addView(endButton);


//                Intent mainActivityIntent = new Intent();
//                setResult(Activity.RESULT_OK, mainActivityIntent);
//                finish();
            }
        }
    }
}