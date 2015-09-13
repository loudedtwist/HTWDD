package de.htwdd.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.htwdd.classes.CONST;

public class RoomTimetableDatabaseManager extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME   = "Timetable.db";
    private static final int DATABASE_VERSION   = 2;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CONST.DataBaseRoomTimetableEntry.TABLE_NAME + " (" +
                    CONST.DataBaseRoomTimetableEntry._ID + CONST.TYPE_INT +" PRIMARY KEY"+ CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_LESSONTAG + CONST.TYPE_TEXT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_NAME + CONST.TYPE_TEXT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_TYP + CONST.TYPE_TEXT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK + CONST.TYPE_INT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DAY + CONST.TYPE_INT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DS + CONST.TYPE_INT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_BEGINTIME+ CONST.TYPE_TIME + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ENDTIME+ CONST.TYPE_TIME + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_PROFESSOR + CONST.TYPE_TEXT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEKSONLY + CONST.TYPE_TEXT + CONST.COMMA_SEP +
                    CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS + CONST.TYPE_TEXT +
                    " )";

    public RoomTimetableDatabaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + CONST.DataBaseRoomTimetableEntry.TABLE_NAME);
        onCreate(db);
    }
}