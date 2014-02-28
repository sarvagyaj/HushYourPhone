package com.androidapp.hup.db;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConnection {

	public static final String PROFILE_NAME = "profile_name";
	public static final String PROFILE_TYPE = "profile_type";
	public static final String WIFI_NAME = "wifi_name";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "HYP";
	private static final String TABLE_NAME = "PROFILES";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" + PROFILE_NAME
			+ " VARCHAR not null UNIQUE, " + PROFILE_TYPE
			+ " VARCHAR not null, " + WIFI_NAME + " VARCHAR not null UNIQUE);";

	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public DBConnection(Context ctx) {
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub ---

			db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub ---
			Log.w(TAG, "Upgrading Database from verison " + oldVersion + "to "
					+ newVersion + ",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	// ---- open the Database -----
	public DBConnection open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	// ---close the Database---
	public void close() {
		dbHelper.close();
	}

	// ----- insert records to the database -----
	public long insertRecord(String pName, String pType, String wName) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PROFILE_NAME, pName);
		initialValues.put(PROFILE_TYPE, pType);
		initialValues.put(WIFI_NAME, wName);
		return db.insert(TABLE_NAME, null, initialValues);
	}

	// ---deletes a particular record---

	public boolean deleteRecord(String pName) {
		return db.delete(TABLE_NAME, PROFILE_NAME + "='" + pName + "'",
				null) > 0;
	}

	// --- Retrieve all records ---
	public Cursor getAllRecords() {
		return db.query(TABLE_NAME, new String[] { PROFILE_NAME,
				PROFILE_TYPE, WIFI_NAME }, null, null, null, null, null);
	}

	public Cursor getRecord(String pName) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_NAME, new String[] {
				PROFILE_NAME, PROFILE_TYPE, WIFI_NAME }, PROFILE_NAME + "="
				+ pName, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateRecord(String pName, String pType, String wName) {
		ContentValues args = new ContentValues();
		args.put(PROFILE_NAME, pName);
		args.put(PROFILE_TYPE, pType);
		args.put(WIFI_NAME, wName);
		return db.update(TABLE_NAME, args, PROFILE_NAME + "='" + pName
				+ "'", null) > 0;
	}
}
