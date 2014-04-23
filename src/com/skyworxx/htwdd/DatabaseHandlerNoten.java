package com.skyworxx.htwdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skyworxx.htwdd.types.TNote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandlerNoten extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NotenManager2";

    // Contacts table name
    private static final String TABLE_NOTEN = "noten";

    // Contacts Table Columns names
    private static final String KEY_NR = "nr";
    private static final String KEY_TITEL = "titel";
    private static final String KEY_SEM = "sem";
    private static final String KEY_NOTE = "note";
    private static final String KEY_BESTANDEN = "bestanden";
    private static final String KEY_CREDITS = "credits";
    private static final String KEY_ECTS = "ects";
    private static final String KEY_VERMERK = "vermerk";
    private static final String KEY_VERSUCH = "versuch";
    private static final String KEY_DATUM = "datum";

    public DatabaseHandlerNoten(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_STUNDEN_TABLE = "CREATE TABLE " + TABLE_NOTEN + "("
                + KEY_NR + " TEXT," +
                KEY_TITEL + " TEXT," +
                KEY_SEM + " TEXT," +
                KEY_NOTE + " TEXT," +
                KEY_BESTANDEN + " TEXT," +
                KEY_CREDITS + " TEXT," +
                KEY_ECTS + " TEXT," +
                KEY_VERMERK + " TEXT," +
                KEY_VERSUCH + " TEXT," +
                KEY_DATUM + " TEXT" + ")";
        db.execSQL(CREATE_STUNDEN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTEN);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addNote(TNote note)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NR, note._Nr);
        values.put(KEY_TITEL, note._Titel);
        values.put(KEY_SEM, note._Sem);
        values.put(KEY_NOTE, note._Note);
        values.put(KEY_BESTANDEN, note._Bestanden);
        values.put(KEY_CREDITS, note._Credits);
        values.put(KEY_ECTS, note._ECTS);
        values.put(KEY_VERMERK, note._Vermerk);
        values.put(KEY_VERSUCH, note._Versuch);
        values.put(KEY_DATUM, note._Datum);


        // Inserting Row
        db.insert(TABLE_NOTEN, null, values);
        db.close(); // Closing database connection

    }

//	// Getting single contact
//	public Type_Stunde getNote(int id) {
//
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_NOTEN, new String[] { KEY_ID,
//				KEY_WOCHE, KEY_TAG, KEY_STUNDE, KEY_NAME, KEY_TYP, KEY_RAUM },
//				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
//				null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		Type_Stunde type_Stunde = new Type_Stunde(Integer.parseInt(cursor.getString(0)),
//				Integer.parseInt(cursor.getString(1)), cursor.getString(2),
//				Integer.parseInt(cursor.getString(3)), cursor.getString(4),
//				cursor.getString(5), cursor.getString(6));
//		// return contact
//		cursor.close();
//		db.close();
//		return type_Stunde;
//
//	}

    public void purge()
    {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_NOTEN, null, null);
        db.close();
    }

    public TNote getNote(String titel, String sem)
    {

        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN
                + " WHERE TITEL = '" + titel + "' AND SEM ='" + sem
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                db.close();
                return note;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        TNote note = new TNote();
        return note;

    }

    // Getting All Contacts
    public List<TNote> getAllNoten()
    {

        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                notenList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return notenList;

    }


    public List<TNote> getNeueNoten()
    {

        String semstring = " ";

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR) - 2000;

        if ((month >= 2) && (month <= 6)) semstring = "Wintersemester " + (year - 1) + "/" + year;
        if ((month >= 6) && (month <= 12)) semstring = "Sommersemester " + year;
        if ((month <= 1)) semstring = "Sommersemester " + (year - 1);


        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN + " WHERE " + KEY_SEM + " LIKE '" + semstring + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                notenList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return notenList;

    }


    public List<TNote> getNichtBestandenNoten()
    {

        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN + " WHERE " + KEY_BESTANDEN + " NOT LIKE 'bestanden'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                notenList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return notenList;

    }


    public List<TNote> getBestandenNoten()
    {

        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN + " WHERE " + KEY_BESTANDEN + " LIKE 'bestanden'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                notenList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return notenList;

    }


    public List<TNote> getSemNoten(String sem)
    {

        List<TNote> notenList = new ArrayList<TNote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTEN + " WHERE " + KEY_SEM + " LIKE '" + sem + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                TNote note = new TNote(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


                // Adding contact to list
                notenList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return notenList;

    }


    public String[] getAllSem()
    {

        List<String> notenList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT sem FROM " + TABLE_NOTEN + " group by sem";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {


                // Adding contact to list
                notenList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list


        String[] allsem = new String[notenList.size()];

        for (int i = 0; i < allsem.length; i++)
        {
            allsem[i] = notenList.get(i);

        }

        return allsem;

    }


    // Getting contacts Count
    public int getNotenCount()
    {

        String countQuery = "SELECT  * FROM " + TABLE_NOTEN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();

    }


    public int getSemesterCount()
    {

        String countQuery = "SELECT " + KEY_SEM + " * FROM " + TABLE_NOTEN + " group by " + KEY_SEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();

    }

//	// Updating single contact
//	public int updateNote(Type_Stunde type_Stunde) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_WOCHE, type_Stunde.getWoche());
//		values.put(KEY_TAG, type_Stunde.getTag());
//		values.put(KEY_STUNDE, type_Stunde.getStunde());
//		values.put(KEY_NAME, type_Stunde.getName());
//		values.put(KEY_TYP, type_Stunde.getTyp());
//		values.put(KEY_RAUM, type_Stunde.getRaum());
//
//		// updating row
//		return db.update(TABLE_NOTEN, values, KEY_ID + " = ?",
//				new String[] { String.valueOf(type_Stunde.getID()) });
//
//	}

    //
    // Deleting single contact
    public void deleteNote(TNote note)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTEN, KEY_NR + " = ?",
                new String[]{String.valueOf(note._Nr)});
        db.close();

        //
    }

    public void overwriteNote(TNote note)
    {


        String selectQuery = "DELETE  FROM " + TABLE_NOTEN
                + " WHERE TITEL = '" + note._Titel + "' AND SEM ='" + note._Sem
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);

        ContentValues values = new ContentValues();
        values.put(KEY_NR, note._Nr);
        values.put(KEY_TITEL, note._Titel);
        values.put(KEY_SEM, note._Sem);
        values.put(KEY_NOTE, note._Note);
        values.put(KEY_BESTANDEN, note._Bestanden);
        values.put(KEY_CREDITS, note._Credits);
        values.put(KEY_ECTS, note._ECTS);
        values.put(KEY_VERMERK, note._Vermerk);
        values.put(KEY_VERSUCH, note._Versuch);
        values.put(KEY_DATUM, note._Datum);

        // Inserting Row
        db.insert(TABLE_NOTEN, null, values);
        db.close(); // Closing database connection

    }

}