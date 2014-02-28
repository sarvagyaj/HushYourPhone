package com.hyp.maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidapp.hup.Dto.Event;
import com.androidapp.hup.db.DB;
import com.androidapp.hup.location.LocationFinder;
import com.androidapp.hup.services.VolumeService;

public class Main extends Activity {

	private LinearLayout mainLayout;
	private Button addEvent;
	private DB db;
	private View event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LocationFinder find = new LocationFinder(this);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000 , 0 , find);
		
		startService(new Intent(getBaseContext(), VolumeService.class));
		
		db = new DB(this);
		try {
			db.open();
		}catch(SQLException se) {
			
		}
		mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
		addEvent = (Button)findViewById(R.id.addEvents);
		addEvent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent addEvent  = new Intent().setClass(getBaseContext(), AddEvent.class);
				startActivity(addEvent);
			}
		});
		
		Map<String, Event> events = db.getAllEvents();
		showAllEvents(events);
		db.close();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void showAllEvents(Map<String, Event> events) {
		
		
		Set<Entry<String,Event>>eventSet = events.entrySet();
		Iterator<Entry<String,Event>> iterator = eventSet.iterator();
		
		while(iterator.hasNext()) {
			display(iterator.next());
			
		}
		
	}
	
	private void display(Entry<String,Event> entry) {
		LayoutInflater inflator = getLayoutInflater();
		event = inflator.inflate(R.layout.event, null);
		
		TextView eventName = (TextView)event.findViewById(R.id.event);
		eventName.setText(entry.getKey());
		eventName.setVisibility(View.VISIBLE);
		
		Button editButton = (Button)event.findViewById(R.id.editEvents);
		editButton.setVisibility(View.VISIBLE);
		
		Button deleteButton = (Button)event.findViewById(R.id.deleteEvents);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				RelativeLayout eventDetails = (RelativeLayout)view.getParent();
				TextView eventView = (TextView)eventDetails.findViewById(R.id.event);
				String eventName = eventView.getText().toString();
				db.open();
				db.delteEvent(eventName);
				db.close();
				startActivity(getIntent());
				
			}
		});
		deleteButton.setVisibility(View.VISIBLE);
		
		mainLayout.addView(event);
	}

}
