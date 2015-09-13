package de.htwdd.classes;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.types.Lesson;


public class Timetable
{
    public ArrayList<Lesson> lessonArrayList = new ArrayList<Lesson>();
    private Context context;

    public static final Time time11 = Time.valueOf("07:30:00");
    public static final Time time12 = Time.valueOf("09:00:00");
    public static final Time time21 = Time.valueOf("09:20:00");
    public static final Time time22 = Time.valueOf("10:50:00");
    public static final Time time31 = Time.valueOf("11:10:00");
    public static final Time time32 = Time.valueOf("12:40:00");
    public static final Time time41 = Time.valueOf("13:10:00");
    public static final Time time42 = Time.valueOf("14:40:00");
    public static final Time time51 = Time.valueOf("15:00:00");
    public static final Time time52 = Time.valueOf("16:30:00");
    public static final Time time61 = Time.valueOf("16:50:00");
    public static final Time time62 = Time.valueOf("18:20:00");
    public static final Time time71 = Time.valueOf("18:30:00");
    public static final Time time72 = Time.valueOf("20:00:00");

    public Timetable(Context context)
    {
        this.context = context;
    }

    public int getTimtableStudent(String StgJhr, String Stg, String StgGrp)
    {
        return getTimetable(new HTTPDownloader("https://www2.htw-dresden.de/~app/API/GetTimetable.php?StgJhr=" + StgJhr + "&Stg=" + Stg + "&StgGrp=" + StgGrp));
    }

    public int getTimetableProf(String Prof)
    {
        return getTimetable(new HTTPDownloader("https://www2.htw-dresden.de/~app/API/GetTimetable.php?Prof="+Prof));
    }

    public int getTimetableRoom(String Room)
    {
        try {
            Room = URLEncoder.encode(Room, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return getTimetable(new HTTPDownloader("https://www2.htw-dresden.de/~app/API/GetTimetable.php?Room="+Room));
    }

    public void saveTimetableUser()
    {
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
        databaseHandlerTimetable.clearTable();
        databaseHandlerTimetable.saveTimetable(lessonArrayList);
        databaseHandlerTimetable.close();
    }

    private int getTimetable(HTTPDownloader downloader)
    {

        String response = downloader.getString();

        if (downloader.ResponseCode != 200)
            return downloader.ResponseCode;

        lessonArrayList.clear();

        try
        {
            JSONArray array = new JSONArray(response);
            int array_count = array.length();

            for (int i=0; i < array_count; i++)
            {
                JSONObject object   = array.getJSONObject(i);
                Lesson lesson       = new Lesson();
                lesson.lessonTag    = object.getString("lessonTag");
                lesson.name         = object.getString("name");
                lesson.type         = object.getString("type");
                lesson.week         = object.getInt("week");
                lesson.day          = object.getInt("day");
                lesson.beginTime    = Time.valueOf(object.getString("beginTime"));
                lesson.endTime      = Time.valueOf(object.getString("endTime"));
                lesson.professor    = object.getString("professor");
                lesson.weeksOnly    = object.getString("WeeksOnly");
                JSONArray roomArray = object.getJSONArray("Rooms");
                lesson.rooms        = "";
                int roomArrayCount  = roomArray.length();
                for (int j=0; j < roomArrayCount; j++)
                    lesson.rooms += roomArray.getString(j).replaceAll(" ","")+" ";

                // Bestimme Anfangs DS
                if (lesson.beginTime.before(time12))
                    lesson.ds = 1;
                else if (lesson.beginTime.before(time22))
                    lesson.ds = 2;
                else if (lesson.beginTime.before(time32))
                    lesson.ds = 3;
                else if (lesson.beginTime.before(time42))
                    lesson.ds = 4;
                else if (lesson.beginTime.before(time52))
                    lesson.ds = 5;
                else if (lesson.beginTime.before(time62))
                    lesson.ds = 6;
                else if (lesson.beginTime.before(time72))
                    lesson.ds = 7;

                // Stunde hinzufügen
                lessonArrayList.add(lesson);

                // Bestimme End DS und ggf neu eintragen
                switch (lesson.ds)
                {
                    case 1:
                        if (lesson.endTime.before(time21))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 2;
                        lessonArrayList.add(lesson);
                    case 2:
                        if (lesson.endTime.before(time31))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 3;
                        lessonArrayList.add(lesson);
                    case 3:
                        if (lesson.endTime.before(time41))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 4;
                        lessonArrayList.add(lesson);
                    case 4:
                        if (lesson.endTime.before(time51))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 5;
                        lessonArrayList.add(lesson);
                    case 5:
                        if (lesson.endTime.before(time61))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 6;
                        lessonArrayList.add(lesson);
                    case 6:
                        if (lesson.endTime.before(time71))
                            break;
                        lesson = lesson.clone();
                        lesson.ds = 7;
                        lessonArrayList.add(lesson);
                }
            }
        }
        catch (Exception e)
        {
            // Fehler beim Parsen zurückgeben
            return 999;
        }

        // Status 200 - Alles OK zurückgeben
        return downloader.ResponseCode;
    }

}
