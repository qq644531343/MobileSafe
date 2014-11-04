package com.itheima.mobilesafe.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSService extends Service {

	private LocationManager lm;
	private MyLocationListener myListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);

		myListener = new MyLocationListener();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(provider, 0, 0, myListener);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		lm.removeUpdates(myListener);
		myListener = null;
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			
			String longitude = "j:" +  location.getLongitude();
			String latitude =  "w:" + location.getLatitude();
			String accuracy ="a: " +  location.getAccuracy();
			
			//标准gps转火星坐标
			InputStream is;
			try {
				is = getAssets().open("axisoffset.dat");
				ModifyOffset offset = ModifyOffset.getInstance(is);
				PointDouble pointDouble = offset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
				longitude = "j: " +pointDouble.x + "\n";
				latitude = "w: " + pointDouble.y + "\n";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("locationService", "location:" + longitude + "  " + latitude + " acc " + accuracy );
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude + latitude +accuracy);
			editor.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

}
