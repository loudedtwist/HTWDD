package de.htwdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import de.htwdd.classes.CONST;
import de.htwdd.types.Lesson;

public class DatabaseHandlerTimetable extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION    = 2;
    private static final String DATABASE_NAME   = "TimetableUser.db";
    private static final String TYPE_TEXT       = " TEXT";
    private static final String TYPE_FLOAT      = " REAL";
    private static final String TYPE_INT        = " INTEGER";
    private static final String TYPE_TIME       = " TIME";
    private static final String COMMA_SEP       = ",";

    public static final String TABLE_NAME       = "TimetableUser";
    public static final String COLUMN_NAME_INTERNID = "internID";
    public static final String COLUMN_NAME_LESSONTAG = "lessonTag";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_TYP  = "typ";
    public static final String COLUMN_NAME_WEEK = "week";
    public static final String COLUMN_NAME_DAY  = "day";
    public static final String COLUMN_NAME_DS   = "ds";
    public static final String COLUMN_NAME_BEGINTIME  = "beginTime";
    public static final String COLUMN_NAME_ENDTIME    = "endTime";
    public static final String COLUMN_NAME_PROFESSOR  = "professor";
    public static final String COLUMN_NAME_WEEKSONLY  = "WeeksOnly";
    public static final String COLUMN_NAME_ROOMS      = "rooms";

    public DatabaseHandlerTimetable(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_INTERNID + TYPE_INT +" PRIMARY KEY"+ COMMA_SEP +
                COLUMN_NAME_LESSONTAG + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_NAME + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_TYP + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_WEEK + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_DAY + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_DS + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_BEGINTIME+ TYPE_TIME + COMMA_SEP +
                COLUMN_NAME_ENDTIME+ TYPE_TIME + COMMA_SEP +
                COLUMN_NAME_PROFESSOR + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_WEEKSONLY + TYPE_TEXT + COMMA_SEP +
                COLUMN_NAME_ROOMS + TYPE_TEXT +
                " )");
        sqLiteDatabase.execSQL("CREATE INDEX IndexAll ON "+TABLE_NAME+"("+COLUMN_NAME_WEEK+COMMA_SEP+COLUMN_NAME_DS+COMMA_SEP+COLUMN_NAME_DAY+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void clearTable()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+TABLE_NAME);
        sqLiteDatabase.close();
    }

    public void saveTimetable(ArrayList<Lesson> lessonArrayList)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for (Lesson lesson : lessonArrayList)
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_LESSONTAG, lesson.lessonTag);
            values.put(COLUMN_NAME_NAME, lesson.name);
            values.put(COLUMN_NAME_TYP, lesson.type);
            values.put(COLUMN_NAME_WEEK, lesson.week);
            values.put(COLUMN_NAME_DAY, lesson.day);
            values.put(COLUMN_NAME_DS, lesson.ds);
            values.put(COLUMN_NAME_BEGINTIME, lesson.beginTime.toString());
            values.put(COLUMN_NAME_ENDTIME, lesson.endTime.toString());
            values.put(COLUMN_NAME_PROFESSOR, lesson.professor);
            values.put(COLUMN_NAME_WEEKSONLY, lesson.weeksOnly);
            values.put(COLUMN_NAME_ROOMS, lesson.rooms);

            sqLiteDatabase.insert(TABLE_NAME,null,values);
        }
        sqLiteDatabase.close();
    }

    /**
     * Löscht die übergebene Lesson
     * @param lessonID ID der Lesson welche gelöscht werden soll
     * @return Ergebniss
     */
    public boolean deleteLesson(int lessonID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_NAME_INTERNID + "=" + lessonID, null) > 0;
    }

    /**
     * Ändert eine übergeben Stunde (wenn Lesson.internID gesetzt ist) oder fügt eine neue Stunde ein
     *
     * @param lesson Objekt-Eigenschaftem die geändert / gespeichert werden.
     * @return true wenn Datensatz geändert / hinzugefügt wurde, sonst false
     */
    public boolean updateLesson(Lesson lesson)
    {
        long count;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, lesson.name);
        values.put(COLUMN_NAME_LESSONTAG, lesson.lessonTag);
        values.put(COLUMN_NAME_TYP, lesson.type);
        values.put(COLUMN_NAME_ROOMS, lesson.rooms);
        values.put(COLUMN_NAME_WEEK, lesson.week);
        values.put(COLUMN_NAME_DAY, lesson.day);
        values.put(COLUMN_NAME_DS, lesson.ds);
        values.put(COLUMN_NAME_WEEKSONLY, lesson.weeksOnly);

        if (lesson.internID == 0)
            count = sqLiteDatabase.insert(TABLE_NAME, null, values);
        else count = sqLiteDatabase.update(TABLE_NAME,values, COLUMN_NAME_INTERNID+"=="+lesson.internID, null);

        sqLiteDatabase.close();

        return count > 0;
    }

    /**
     * Gibt die passenden Stunden komplett aus der Datenbank zurück
     *
     * @param week Kalenderwoche
     * @param  day Tag der Woche
     * @param  ds  Doppelstunde
     * @return ArrayList von Lessons
     */
    public ArrayList<Lesson> getDS(int week, int day, int ds)
    {
        ArrayList<Lesson> lessons       = new ArrayList<Lesson>();
        int week_db                     = week%2 == 0?2:week%2;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "+COLUMN_NAME_INTERNID + COMMA_SEP
                + COLUMN_NAME_LESSONTAG + COMMA_SEP
                + COLUMN_NAME_NAME + COMMA_SEP
                + COLUMN_NAME_TYP + COMMA_SEP
                + COLUMN_NAME_WEEK + COMMA_SEP
                + COLUMN_NAME_DAY + COMMA_SEP
                + COLUMN_NAME_DS + COMMA_SEP
                + COLUMN_NAME_PROFESSOR + COMMA_SEP
                + COLUMN_NAME_WEEKSONLY + COMMA_SEP
                + COLUMN_NAME_ROOMS +
                " FROM " + TABLE_NAME +
                " WHERE ("+ COLUMN_NAME_WEEK+"="+week_db+" OR "+ COLUMN_NAME_WEEK+"=0) AND "+COLUMN_NAME_DAY+"="+day+" AND "+COLUMN_NAME_DS+"="+ds,null);

        if (cursor.moveToFirst())
        {
            do
            {
                Lesson lesson = new Lesson();
                lesson.internID     = cursor.getInt(0);
                lesson.lessonTag    = cursor.getString(1);
                lesson.name         = cursor.getString(2);
                lesson.type         = cursor.getString(3);
                lesson.week         = cursor.getInt(4);
                lesson.day          = cursor.getInt(5);
                lesson.ds          = cursor.getInt(6);
                lesson.professor    = cursor.getString(7);
                lesson.weeksOnly    = cursor.getString(8);
                lesson.rooms        = cursor.getString(9);
                lessons.add(lesson);

            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        //sqLiteDatabase.close();
        return lessons;
    }


    /**
     * Gibt alle Lessons(wichtigste Parameter) einer gegebenen Zeit zurück
     * @param week Kalenderwoche des Jahres
     * @param day Tag der Woche
     * @param ds DS des Tages
     * @return ArrayList von Lessons
     */
    public ArrayList<Lesson> getShortDS(int week, int day, int ds)
    {
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "+ COLUMN_NAME_LESSONTAG + COMMA_SEP + COLUMN_NAME_TYP + COMMA_SEP + COLUMN_NAME_WEEKSONLY + COMMA_SEP + COLUMN_NAME_ROOMS +
                " FROM " + TABLE_NAME +
                " WHERE ("+ COLUMN_NAME_WEEK+"="+ CONST.db_week(week)+" OR "+ COLUMN_NAME_WEEK+"=0) AND "+COLUMN_NAME_DAY+"="+day+" AND "+COLUMN_NAME_DS+"="+ds,null);

        if (cursor.moveToFirst())
        {
            do
            {
                Lesson lesson = new Lesson();

                lesson.lessonTag    = cursor.getString(0);
                lesson.type         = cursor.getString(1);
                lesson.weeksOnly    = cursor.getString(2);
                lesson.rooms        = cursor.getString(3);
                lessons.add(lesson);

            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        //sqLiteDatabase.close();
        return lessons;
    }

    /**
     * Gibt alle Lessons(wichtigste Parameter) einer gegebenen Woche zurück
     * @param week Kalenderwoche des Jahres
     * @return ArrayList von Lessons
     */
    public ArrayList<Lesson> getShortWeek(int week)
    {
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "
                + COLUMN_NAME_DAY + COMMA_SEP
                + COLUMN_NAME_DS + COMMA_SEP
                + COLUMN_NAME_LESSONTAG + COMMA_SEP
                + COLUMN_NAME_TYP + COMMA_SEP
                + COLUMN_NAME_WEEKSONLY + COMMA_SEP
                + COLUMN_NAME_ROOMS +
                " FROM " + TABLE_NAME +
                " WHERE ("+ COLUMN_NAME_WEEK+"=" + CONST.db_week(week) + " OR "+ COLUMN_NAME_WEEK+"=0)", null);

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

    /**
     * Anzahl der DS in der Tabelle
     * @return Anzahl der DS in der Tabelle {@see DATABASE_NAME}
     */
    public long countDS()
    {
        return DatabaseUtils.queryNumEntries(getReadableDatabase(), TABLE_NAME);
    }
}