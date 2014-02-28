package com.androidapp.hup.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidapp.hup.Dto.Event;

public class DB {
	public static final String EVENT_NAME = "event_name";
	public static final String EVENT_ACTION = "action";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	private static final String DATABASE_NAME = "HYP";
	private static final String TABLE_NAME = "EVENTS";

	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase database;

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, 3);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + EVENT_NAME
					+ " VARCHAR PRIMARY KEY," + EVENT_ACTION
					+ " VARCHAR NOT NULL, " + LATITUDE + " VARCHAR NOT NULL ,"
					+ LONGITUDE + " VARCHAR NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	public DB(Context context) {
		this.context = context;
	}

	public DB open() throws SQLException {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long createEvent(Event event) {
		ContentValues content = new ContentValues();
		content.put(EVENT_NAME, event.getEventName());
		content.put(EVENT_ACTION, event.getEventAction());
		content.put(LATITUDE, event.getLatitude());
		content.put(LONGITUDE, event.getLongtiude());

		return database.insert(TABLE_NAME, null, content);
	}

	public long updateEvent(Event event) {
		ContentValues content = new ContentValues();
		content.put(EVENT_NAME, event.getEventName());
		content.put(EVENT_ACTION, event.getEventAction());
		content.put(LATITUDE, event.getLatitude());
		content.put(LONGITUDE, event.getLongtiude());

		return database.update(TABLE_NAME, content, EVENT_NAME + "=?",
				new String[] { event.getEventName() });
	}

	public Map<String, Event> getAllEvents() {
		String[] columns = new String[] { EVENT_NAME, EVENT_ACTION, LATITUDE,
				LONGITUDE };
		Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
				null, null);
		Map<String, Event> events = new HashMap<String, Event>();

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Event event = new Event();
			event.setEventName(cursor.getString(0));
			event.setEventAction(cursor.getString(1));
			event.setLatitude(cursor.getString(2));
			event.setLongtiude(cursor.getString(3));
			events.put(event.getEventName(), event);
		}
		return events;
	}

	public Event getEvent(String eventName) {
		String[] columns = new String[] { EVENT_NAME, EVENT_ACTION, LATITUDE,
				LONGITUDE };
		Cursor cursor = database.query(true, TABLE_NAME, columns, EVENT_NAME
				+ "=?", new String[] { eventName }, null, null, null, null);
		Event event = null;
		if (cursor != null) {
			cursor.moveToFirst();
			event = new Event();
			event.setEventName(cursor.getString(0));
			event.setEventAction(cursor.getString(1));
			event.setLatitude(cursor.getString(2));
			event.setLongtiude(cursor.getString(3));
		}

		return event;
	}

	public boolean delteEvent(String eventName) {
		return database.delete(TABLE_NAME, EVENT_NAME + "=?",
				new String[] { eventName }) > 0;
	}
}
