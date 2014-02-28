package com.androidapp.hup.services;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;

import com.androidapp.hup.Dto.Event;
import com.androidapp.hup.db.DB;

public class VolumeService extends Service {

	private final IBinder binder = new LocalBinder();
	private AudioManager audioManager;
	private DB database;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		database = new DB(this);
		database.open();

		audioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		doChanges();

		return START_STICKY;

	}

	private void doChanges() {
		// LocationFinder find = new LocationFinder(this);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 0,
		// find);
		Location lastKnownLocation = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double lat = 0.0;
		double lng = 0.0;
		if (lastKnownLocation != null) {
			lat = lastKnownLocation.getLatitude();
			lng = lastKnownLocation.getLongitude();
		}

		Map<String, Event> events = database.getAllEvents();
		database.close();

		Set<Entry<String, Event>> eventSet = events.entrySet();
		Iterator<Entry<String, Event>> iterator = eventSet.iterator();

		while (iterator.hasNext()) {
			checkRules(iterator.next(), lat, lng);

		}

	}

	private void checkRules(Entry<String, Event> entry, double currentLatitude,
			double currentLongitude) {
		Event event = entry.getValue();
		double latitude = Double.parseDouble(event.getLatitude());
		double longitude = Double.parseDouble(event.getLongtiude());

		if (GetDistanceBetweenPoints(currentLatitude, currentLongitude,
				latitude, longitude) < 10) {
			if (event.getEventAction().equals("vibrate")) {
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			} else if (event.getEventAction().equals("ring")) {
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			} else {
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
		}

	}

	public class LocalBinder extends Binder {
		VolumeService getService() {
			return VolumeService.this;
		}
	}

	public static double GetDistanceBetweenPoints(double sourceLatitude,
			double sourceLongitude, double destLatitude, double destLongitude) {

		double theta = sourceLongitude - destLongitude;
		double distance = Math.sin(DegToRad(sourceLatitude))
				* Math.sin(DegToRad(destLatitude))
				+ Math.cos(DegToRad(sourceLatitude))
				* Math.cos(DegToRad(destLatitude)) * Math.cos(DegToRad(theta));
		distance = Math.acos(distance);
		distance = RadToDeg(distance);
		distance = distance * 60 * 1.1515;
		System.out.println("Distance" + distance);
		return (distance);
	}

	public static double DegToRad(double degrees) {
		return (degrees * Math.PI / 180.0);
	}

	public static double RadToDeg(double radians) {
		return (radians / Math.PI * 180.0);
	}

}
