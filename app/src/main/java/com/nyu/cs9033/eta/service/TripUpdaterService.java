package com.nyu.cs9033.eta.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.nyu.cs9033.eta.controllers.ConnectInternet;
import com.nyu.cs9033.eta.models.JSONRequestObject;

import org.json.JSONException;
import org.json.JSONObject;


public class TripUpdaterService extends IntentService {

	private static final String TAG = TripUpdaterService.class.getSimpleName();
	private static final int POLL_INTERVAL = 1000 * 60;
	private PBG pollingBackgroundThread;
	double latitude;
	double longitude;
    public static boolean ALIVE = false;
    
	public TripUpdaterService() {
		super(TAG);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "Started Service");
		ALIVE = true;	       
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		if(pollingBackgroundThread == null) {
	           pollingBackgroundThread = new PBG();
		}
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		 final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}

			@Override
			public void onStatusChanged(String s, int i, Bundle bundle) {

			}

			@Override
			public void onProviderEnabled(String s) {

			}

			@Override
			public void onProviderDisabled(String s) {

			}
		};

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 30, 0, locationListener);
		if(!pollingBackgroundThread.isAlive())
			pollingBackgroundThread.start();
	}


	private class PBG extends Thread {
		@Override
		public void run() {
			super.run();
			JSONRequestObject p = new JSONRequestObject();
			LocationListener gpsLocationListener;

			Log.i(TAG, "Background Thread Entry");

			p.setMethod("PUT");
			p.setUri(ConnectInternet.SERVERURI);
			p.putToJSON("command", "UPDATE_LOCATION");
			p.putToJSON("latitude", latitude);
			p.putToJSON("longitude", longitude);
			p.putToJSON("datetime", System.currentTimeMillis());

			Log.v(TAG, p.getjsonObject().toString());
			String connectData = ConnectInternet.getData(p);
			
			if(connectData == null){
				Log.d(TAG,"connectData is null");
 				return;
			}
			JSONObject jsObj;
			try {
				jsObj = new JSONObject(connectData);

			if(jsObj.getInt("response_code")!=0){
				Log.v(TAG,"response code not null");
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.i(TAG,"Background Thread exit");
		}
	}
	public static void wakeUpPolling(Context context, boolean alive){
		Intent i = new Intent(context, TripUpdaterService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		AlarmManager alarmManager = (AlarmManager)
				context.getSystemService(Context.ALARM_SERVICE);
		if(alive) {
			alarmManager.setRepeating(AlarmManager.RTC,
					System.currentTimeMillis(), POLL_INTERVAL, pi);
		} else {
			alarmManager.cancel(pi);
			pi.cancel();
		}
	}
}
