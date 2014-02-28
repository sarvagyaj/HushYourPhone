package com.hyp.maps;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Activity implements OnClickListener,
		OnMapClickListener, OnMapLongClickListener {

	private GoogleMap googlemap;
	private TextView mTapTextView;
	private MarkerOptions markerOptions;
	private Button locationButton;
	private LatLng location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Log.i("maps", "maps starting now");

		markerOptions = new MarkerOptions();
		mTapTextView = (TextView) findViewById(R.id.tap_text);
		locationButton = (Button) findViewById(R.id.useThisLocation);

		locationButton.setOnClickListener(this);
		setupMapifNeeded();
	}

	private void setupMapifNeeded() {
		if (googlemap == null) {
			Log.i("maps", "maps null rite now");
			googlemap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map1)).getMap();
			if (googlemap != null) {
				setupMap();
			}
		}
	}

	private void setupMap() {
		googlemap.setMyLocationEnabled(true);
		double longi, lati;

		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String serviceProvider = locManager.getBestProvider(criteria, true);
		Location currentLocation = locManager
				.getLastKnownLocation(serviceProvider);
		googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		longi = currentLocation.getLongitude();
		lati = currentLocation.getLatitude();
		LatLng latlong = new LatLng(lati, longi);

		// Setting the position for the marker
		markerOptions.position(latlong);
		markerOptions.title(latlong.latitude + " : " + latlong.longitude);

		googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlong));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(15));

		googlemap.setOnMapClickListener(this);
		googlemap.setOnMapLongClickListener(this);
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// mTapTextView.setText("long pressed, point=" + point);

	}

	@Override
	public void onMapClick(LatLng point) {
		location = point;

		// Setting the marker
		markerOptions.position(point);
		markerOptions.title(point.latitude + " : " + point.longitude);

		// Clears the previously touched position
		googlemap.clear();

		// Animating to the touched position
		googlemap.animateCamera(CameraUpdateFactory.newLatLng(point));

		// Placing a marker on the touched position
		googlemap.addMarker(markerOptions);
		// mTapTextView.setText("tapped, point=" + point);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(Map.this, AddEvent.class);
		intent.putExtra("latitude", location.latitude);
		intent.putExtra("longitude", location.longitude);
		startActivity(intent);

	}

}
