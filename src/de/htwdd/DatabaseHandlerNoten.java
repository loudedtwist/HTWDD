package de.htwdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerNoten extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Noten.db";
    public static final String TABLE_NAME = "Noten";
    public static final String COLUMN_NAME_MODUL = "modul";
    public static final String COLUMN_NAME_Note = "note";
    public static final String COLUMN_NAME_VERMERK = "vermerk";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_CREDITS = "credits";
    public static final String COLUMN_NAME_VERSUCH = "versuch";
    public static final String COLUMN_NAME_SEMESTER = "semester";
    public static final String COLUMN_NAME_KENNZEICHEN = "kennzeichen";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_FLOAT = " REAL";
    private static final String TYPE_INT = " INTEGER";
    private static final String COMMA_SEP = ",";


    public DatabaseHandlerNoten(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_MODUL + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_Note + TYPE_FLOAT + COMMA_SEP +
                COLUMN_NAME_VERMERK + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_STATUS + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_CREDITS+ TYPE_FLOAT + COMMA_SEP +
                COLUMN_NAME_VERSUCH+ TYPE_INT + COMMA_SEP +
                COLUMN_NAME_SEMESTER + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_KENNZEICHEN + TYPE_TEXT +
                " )");
        sqLiteDatabase.execSQL("CREATE INDEX IndexSemester ON "+TABLE_NAME+"("+COLUMN_NAME_SEMESTER+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void clearTable(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("DELETE FROM "+TABLE_NAME);
    }
}