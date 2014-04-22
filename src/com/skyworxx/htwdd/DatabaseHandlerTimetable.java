package com.skyworxx.htwdd;

import java.util.ArrayList;
import java.util.List;

import com.skyworxx.htwdd.types.Type_Stunde;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTimetable extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "StundeManager2";

	// Contacts table name
	private static final String TABLE_STUNDEN = "stunden";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_WOCHE = "woche";
	private static final String KEY_TAG = "tag";
	private static final String KEY_STUNDE = "stunde";
	private static final String KEY_NAME = "name";
	private static final String KEY_TYP = "typ";
	private static final String KEY_RAUM = "raum";

	public DatabaseHandlerTimetable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_STUNDEN_TABLE = "CREATE TABLE " + TABLE_STUNDEN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_WOCHE + " INTEGER,"
				+ KEY_TAG + " TEXT," + KEY_STUNDE + " INTEGER," + KEY_NAME
				+ " TEXT," + KEY_TYP + " TEXT," + KEY_RAUM + " TEXT" + ")";
		db.execSQL(CREATE_STUNDEN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUNDEN);

		// Create tables again
		onCreate(db);
	}

	// Adding new contact
	public void addContact(Type_Stunde type_Stunde) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WOCHE, type_Stunde.getWoche());
		values.put(KEY_TAG, type_Stunde.getTag());
		values.put(KEY_STUNDE, type_Stunde.getStunde());
		values.put(KEY_NAME, type_Stunde.getName());
		values.put(KEY_TYP, type_Stunde.getTyp());
		values.put(KEY_RAUM, type_Stunde.getRaum());

		Log.d("Inserting",
				type_Stunde.getStunde() + " " + type_Stunde.getWoche() + " "
						+ type_Stunde.getName() + " " + type_Stunde.getTyp() + " "
						+ type_Stunde.getTyp() + " ");
		// Inserting Row
		db.insert(TABLE_STUNDEN, null, values);
		db.close(); // Closing database connection

	}

	// Getting single contact
	public Type_Stunde getContact(int id) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STUNDEN, new String[] { KEY_ID,
				KEY_WOCHE, KEY_TAG, KEY_STUNDE, KEY_NAME, KEY_TYP, KEY_RAUM },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Type_Stunde type_Stunde = new Type_Stunde(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2),
				Integer.parseInt(cursor.getString(3)), cursor.getString(4),
				cursor.getString(5), cursor.getString(6));
		// return contact
		cursor.close();
		db.close();
		return type_Stunde;

	}
	
	public void purge() {

		
		SQLiteDatabase db = this.getWritableDatabase();

		
		
			db.delete(TABLE_STUNDEN, null, null);
			db.close();
		}

	public Type_Stunde getStunde(String tag, int woche, int stundee) {

		List<Type_Stunde> stundenList = new ArrayList<Type_Stunde>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_STUNDEN
				+ " WHERE TAG = '" + tag + "' AND WOCHE =" + woche
				+ " AND STUNDE =" + stundee + "  GROUP BY STUNDE ORDER BY STUNDE";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Type_Stunde type_Stunde = new Type_Stunde();
				type_Stunde.setID(Integer.parseInt(cursor.getString(0)));
				type_Stunde.setWoche(Integer.parseInt(cursor.getString(1)));
				type_Stunde.setTag(cursor.getString(2));
				type_Stunde.setStunde(Integer.parseInt(cursor.getString(3)));
				type_Stunde.setName(cursor.getString(4));
				type_Stunde.setTyp(cursor.getString(5));
				type_Stunde.setRaum(cursor.getString(6));

				// Adding contact to list
				db.close(); 
				return type_Stunde;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return contact list
		Type_Stunde stunde2 = new Type_Stunde();
		stunde2.setName("(leer)");
		return stunde2;

	}

	// Getting All Contacts
	public List<Type_Stunde> getAllStunden() {

		List<Type_Stunde> stundenList = new ArrayList<Type_Stunde>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_STUNDEN;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Type_Stunde type_Stunde = new Type_Stunde();
				type_Stunde.setID(Integer.parseInt(cursor.getString(0)));
				type_Stunde.setWoche(Integer.parseInt(cursor.getString(1)));
				type_Stunde.setTag(cursor.getString(2));
				type_Stunde.setStunde(Integer.parseInt(cursor.getString(3)));
				type_Stunde.setName(cursor.getString(4));
				type_Stunde.setTyp(cursor.getString(5));
				type_Stunde.setRaum(cursor.getString(6));

				// Adding contact to list
				stundenList.add(type_Stunde);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return contact list
		return stundenList;

	}

	public List<Type_Stunde> getStundenTag(String tag, int woche) {

		List<Type_Stunde> stundenList = new ArrayList<Type_Stunde>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_STUNDEN
				+ " WHERE TAG = '" + tag + "' AND WOCHE = "
				+ Integer.toString(woche) + "  GROUP BY STUNDE ORDER BY STUNDE";
		Log.d("QUERY", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Type_Stunde type_Stunde = new Type_Stunde();
				type_Stunde.setID(Integer.parseInt(cursor.getString(0)));
				type_Stunde.setWoche(Integer.parseInt(cursor.getString(1)));
				type_Stunde.setTag(cursor.getString(2));
				type_Stunde.setStunde(Integer.parseInt(cursor.getString(3)));
				type_Stunde.setName(cursor.getString(4));
				type_Stunde.setTyp(cursor.getString(5));
				type_Stunde.setRaum(cursor.getString(6));

				// Adding contact to list
				stundenList.add(type_Stunde);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return contact list
		return stundenList;

	}

	// Getting contacts Count
	public int getStundeCount() {
	
		String countQuery = "SELECT  * FROM " + TABLE_STUNDEN;
		SQLiteDatabase db = this.getWritableDatabase();
	
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();

	}

	// Updating single contact
	public int updateStunde(Type_Stunde type_Stunde) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WOCHE, type_Stunde.getWoche());
		values.put(KEY_TAG, type_Stunde.getTag());
		values.put(KEY_STUNDE, type_Stunde.getStunde());
		values.put(KEY_NAME, type_Stunde.getName());
		values.put(KEY_TYP, type_Stunde.getTyp());
		values.put(KEY_RAUM, type_Stunde.getRaum());
		
		// updating row
		int code= db.update(TABLE_STUNDEN, values, KEY_ID + " = ?",
				new String[] { String.valueOf(type_Stunde.getID()) });
		db.close();
		return code;

	}

	// Deleting single contact
	public void deleteStunde(Type_Stunde type_Stunde) {
		try{
			Log.d("Deleting",
					type_Stunde.getStunde() + " " + type_Stunde.getWoche() + " "
							+ type_Stunde.getName() + " " + type_Stunde.getTyp() + " "
							+ type_Stunde.getTyp() + " ");
	
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_STUNDEN, KEY_ID + " = ?",
					new String[] { String.valueOf(type_Stunde.getID()) });
			db.close();
		}catch (Exception e){
		
		}
		//
	}

	public void overwriteStunde(Type_Stunde type_Stunde) {

		String selectQuery = "Delete FROM " + TABLE_STUNDEN + " WHERE TAG = '"
				+ type_Stunde.getTag() + "' AND WOCHE = " + type_Stunde.getWoche()
				+ " AND STUNDE = " + type_Stunde.getStunde();

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(selectQuery);

		ContentValues values = new ContentValues();
		values.put(KEY_WOCHE, type_Stunde.getWoche());
		values.put(KEY_TAG, type_Stunde.getTag());
		values.put(KEY_STUNDE, type_Stunde.getStunde());
		values.put(KEY_NAME, type_Stunde.getName());
		values.put(KEY_TYP, type_Stunde.getTyp());
		values.put(KEY_RAUM, type_Stunde.getRaum());

		// Inserting Row
		db.insert(TABLE_STUNDEN, null, values);
		db.close(); // Closing database connection

	}

}