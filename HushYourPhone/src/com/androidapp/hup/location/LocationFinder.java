package com.androidapp.hup.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.androidapp.hup.services.VolumeService;

public class LocationFinder implements LocationListener {

	Context ctx;

	public LocationFinder(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public void onLocationChanged(Location location) {

	/*	if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();

			Editor editor = ctx.getSharedPreferences("c2dmPref",
					Context.MODE_PRIVATE).edit();
			editor.putString("lat", new Double(lat).toString());
			editor.putString("lng", new Double(lng).toString());
			editor.commit();
		}*/

		Intent serviceIntent = new Intent(ctx, VolumeService.class);
		ctx.startService(serviceIntent);

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
