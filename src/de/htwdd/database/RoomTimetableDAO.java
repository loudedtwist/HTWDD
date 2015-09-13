package de.htwdd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import de.htwdd.classes.CONST;
import de.htwdd.types.Lesson;
import de.htwdd.types.RoomTimetable;

/**
 * DAO für den Raumplan
 * @author Kay Förster
 */
public class RoomTimetableDAO
{
    private RoomTimetableDatabaseManager roomTimetableDatabaseManager;

    public RoomTimetableDAO(Context context)
    {
        roomTimetableDatabaseManager = new RoomTimetableDatabaseManager(context);
    }

    public ArrayList<RoomTimetable> getOverview(final int day, final int week)
    {
        ArrayList<RoomTimetable> roomTimetables = new ArrayList<RoomTimetable>();

        // Projections
        String[] projection_rooms = {CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS};
        String[] projection_lesson = {
                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_LESSONTAG,
                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DS,
                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DAY,
                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_TYP,
                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEKSONLY};

        // Datenbank öffnen
        SQLiteDatabase sqLiteDatabase = roomTimetableDatabaseManager.getReadableDatabase();

        // Hole alle Räume aus der DB
        Cursor cursor_rooms = sqLiteDatabase.query(CONST.DataBaseRoomTimetableEntry.TABLE_NAME, projection_rooms, null, null, CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS, null, null);
        if (cursor_rooms.moveToFirst())
        {
            do {
                RoomTimetable roomTimetable = new RoomTimetable();
                roomTimetable.RoomName  = cursor_rooms.getString(0);
                roomTimetable.day       = day;
                roomTimetable.Timetable = new ArrayList<Lesson>();

                Cursor cursor = sqLiteDatabase.query(CONST.DataBaseRoomTimetableEntry.TABLE_NAME,
                        projection_lesson,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS + " = ? AND " +
                                CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DAY + " = ? AND " +
                                "(" + CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK + " = ? OR " + CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK + " = 0)",
                        new String[] { cursor_rooms.getString(0), String.valueOf(day), String.valueOf(CONST.db_week(week))},
                        null,
                        null,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DS + " DESC");

                if (cursor.moveToFirst())
                {
                    do {
                        Lesson lesson       = new Lesson();
                        lesson.lessonTag    = cursor.getString(0);
                        lesson.ds           = cursor.getInt(1);
                        lesson.day          = cursor.getInt(2);
                        lesson.type         = cursor.getString(3);
                        lesson.weeksOnly    = cursor.getString(4);

                        // Zur Liste hinzufügen
                        roomTimetable.Timetable.add(lesson);
                    }
                    while (cursor.moveToNext());
                }

                // Cursor für einzelne Stunden schliessen
                cursor.close();

                // Füge Raum zur Liste hinzu
                roomTimetables.add(roomTimetable);
            }
            while (cursor_rooms.moveToNext());
        }

        cursor_rooms.close();
        sqLiteDatabase.close();

        return roomTimetables;
    }

    public void add(RoomTimetable roomTimetable)
    {
        SQLiteDatabase sqLiteDatabase = roomTimetableDatabaseManager.getWritableDatabase();

        // Starte Transaction
        sqLiteDatabase.beginTransaction();

        for (Lesson lesson: roomTimetable.Timetable)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_LESSONTAG, lesson.lessonTag);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_NAME, lesson.name);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_TYP, lesson.type);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK, lesson.week);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DAY, lesson.day);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DS, lesson.ds);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_BEGINTIME, lesson.beginTime.toString());
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ENDTIME, lesson.endTime.toString());
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_PROFESSOR, lesson.professor);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEKSONLY, lesson.weeksOnly);
            contentValues.put(CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS, roomTimetable.RoomName);

            sqLiteDatabase.insert(CONST.DataBaseRoomTimetableEntry.TABLE_NAME, null, contentValues);
        }

        // Beende Transaction
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    public void removeRoom(String room)
    {
        SQLiteDatabase sqLiteDatabase = roomTimetableDatabaseManager.getWritableDatabase();
        sqLiteDatabase.delete(CONST.DataBaseRoomTimetableEntry.TABLE_NAME, CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS + " == '" + room + "'", null);
        sqLiteDatabase.close();
    }

    public ArrayList<Lesson> loadWeek(String room, int week)
    {
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();

        SQLiteDatabase sqLiteDatabase = roomTimetableDatabaseManager.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                CONST.DataBaseRoomTimetableEntry.TABLE_NAME,
                new String[] {
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DAY,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_DS,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_LESSONTAG,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_TYP,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEKSONLY,
                        CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS
                },
                "(" + CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK + "= ? OR " + CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_WEEK + " = 0) " +
                        "AND " + CONST.DataBaseRoomTimetableEntry.COLUMN_NAME_ROOMS + " = ? ",
                new String[] {String.valueOf(CONST.db_week(week)), room},
                null, null, null
        );

        if (cursor.moveToFirst())
        {
            do
            {
                Lesson lesson = new Lesson();

                lesson.day          = cursor.getInt(0);
                lesson.ds           = cursor.getInt(1);
                lesson.lessonTag    = cursor.getString(2);
                lesson.type         = cursor.getString(3);
                lesson.weeksOnly    = cursor.getString(4);
                lesson.rooms        = cursor.getString(5);
                lessons.add(lesson);

            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return lessons;
    }
}
