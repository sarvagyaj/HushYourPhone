package com.hyp.maps;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.androidapp.hup.Dto.Event;
import com.androidapp.hup.db.DB;

public class AddEvent extends Activity implements OnClickListener {

	private Button save;
	private Button reset;
	private Button currentLocation;
	private Button locationFromMap;
	private DB db;
	private EditText latitude;
	private EditText longitude;
	private RadioButton silent;
	private RadioButton ring;
	private RadioButton vibrate;
	private EditText eventName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		db = new DB(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);

		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		reset = (Button) findViewById(R.id.reset);
		reset.setOnClickListener(this);

		currentLocation = (Button) findViewById(R.id.currentLocation);
		currentLocation.setOnClickListener(this);

		locationFromMap = (Button) findViewById(R.id.pickLocation);
		locationFromMap.setOnClickListener(this);

		latitude = (EditText) findViewById(R.id.latitude);
		longitude = (EditText) findViewById(R.id.longitude);
		eventName = (EditText) findViewById(R.id.newEvent);

		silent = (RadioButton) findViewById(R.id.silent);
		vibrate = (RadioButton) findViewById(R.id.vibrate);
		ring = (RadioButton) findViewById(R.id.ring);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.save:
			save();
			break;

		case R.id.reset:
			reset();
			break;

		case R.id.currentLocation:
			setCurrentLocation();
			break;

		case R.id.pickLocation:
			Intent intent = new Intent(getBaseContext(), Map.class);
			startActivity(intent);
			break;

		}

	}

	private void setCurrentLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = null;
		List<String> providers = lm.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */

		for (int i = providers.size() - 1; i >= 0; i--) {
			lastKnownLocation = lm.getLastKnownLocation(providers.get(i));
			if (lastKnownLocation != null) {

				latitude.setText(String.valueOf(lastKnownLocation.getLatitude()));
				longitude.setText(String.valueOf(lastKnownLocation
						.getLongitude()));

				break;
			}
		}
	}

	private void reset() {
		eventName.setText("");
		latitude.setText("");
		longitude.setText("");
		silent.setSelected(true);
		vibrate.setSelected(false);
		ring.setSelected(false);
	}

	private void save() {

		if (eventName == null || eventName.getText() == null
				|| eventName.getText().toString().trim().equals("")) {
			return;
		}

		if (latitude == null || latitude.getText() == null
				|| latitude.getText().toString().trim().equals("")) {
			return;
		}

		if (longitude == null || longitude.getText() == null
				|| longitude.getText().toString().trim().equals("")) {
			return;
		}

		Event event = new Event();
		event.setEventName(eventName.getText().toString());
		event.setLatitude(latitude.getText().toString());
		event.setLongtiude(longitude.getText().toString());
		if (silent.isChecked()) {
			event.setEventAction("silent");
		} else if (vibrate.isChecked()) {
			event.setEventAction("vibrate");
		} else {
			event.setEventAction("ring");
		}
		try {
			db.open();
		} catch (SQLException se) {
			System.out.println("SQL exception in opening db");
			return;
		}
		db.createEvent(event);
		db.close();
		Intent mainIntent = new Intent(getBaseContext(), Main.class);
		startActivity(mainIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
		latitude.setText(String.valueOf(getIntent().getDoubleExtra("latitude",
				0.0)));
		longitude.setText(String.valueOf(getIntent().getDoubleExtra(
				"longitude", 0.0)));
	}
}
