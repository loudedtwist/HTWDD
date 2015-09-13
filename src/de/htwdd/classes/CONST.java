package de.htwdd.classes;

import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Klasse welche alle Konstanten in der App beinhaltet
 */
public final class CONST
{
    // Datenbank
    public static final String TYPE_TEXT       = " TEXT";
    public static final String TYPE_FLOAT      = " REAL";
    public static final String TYPE_INT        = " INTEGER";
    public static final String TYPE_TIME       = " TIME";
    public static final String COMMA_SEP       = ",";

    public static class DataBaseTimetableEntry implements BaseColumns
    {
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
    }


    public static final class DataBaseRoomTimetableEntry extends DataBaseTimetableEntry
    {
        public static final String TABLE_NAME = "roomTimetable";
        public static final String COLUMN_NAME_ROOMS      = "room";
    }

    public static int db_week(final int week)
    {
        return week%2 == 0?2:week%2;
    }


    // Stundenplan
    public static class TimetableCalc
    {
        /**
         * Liefert die DS zur übergebene Zeit
         *
         * @param currentTime aktuelle Zeit, in Minuten seit Mitternacht
         * @return Aktuelle Stunde oder 0 falls auserhalb der Unterrichtszeiten
         */
        public static int getCurrentDS(int currentTime)
        {
            if (currentTime > LessonSearch.lessonEndTimes[7-1])
                 return 0;
            else if (currentTime >= LessonSearch.lessonStartTimes[6])
                return 7;
            else if (currentTime >= LessonSearch.lessonStartTimes[5])
                return 6;
            else if (currentTime >= LessonSearch.lessonStartTimes[4])
                return 5;
            else if (currentTime >= LessonSearch.lessonStartTimes[3])
                return 4;
            else if (currentTime >= LessonSearch.lessonStartTimes[2])
                return 3;
            else if (currentTime >= LessonSearch.lessonStartTimes[1])
                return 2;
            else if (currentTime >= LessonSearch.lessonStartTimes[0])
                return 1;

            return 0;
        }

        public static int currentTime()
        {
            Calendar calendar   = GregorianCalendar.getInstance();
            return calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        }
    }

}
