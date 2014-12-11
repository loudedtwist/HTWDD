package de.htwdd.classes;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import de.htwdd.DatabaseHandlerNoten;
import de.htwdd.types.Grade;
import de.htwdd.types.Stats;


public class Noten
{
    private String sNummer;
    private String RZLogin;
    private DatabaseHandlerNoten mDbHelper;
    private Context context;
    public Grade[] noten;

    public Noten(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sNummer = sharedPreferences.getString("bib", "");
        RZLogin = sharedPreferences.getString("RZLogin", "");
        mDbHelper = new DatabaseHandlerNoten(context);
        this.context = context;
    }

    public int getNotenHIS()
    {
        noten = new Grade[0];
        JSONObject object;
        ArrayList<Grade> arrayList = new ArrayList<Grade>();
        Grade grade;

        // Lade Studiengänge des Studenten
        HTTPDownloader downloader = new HTTPDownloader("https://wwwqis.htw-dresden.de/appservice/getcourses");
        downloader.urlParameters  = "sNummer=s" + sNummer + "&RZLogin=" + RZLogin;
        downloader.context =context;

        String response = downloader.getStringWithPost();

        if (downloader.ResponseCode != 200)
            return downloader.ResponseCode;

        try
        {
            JSONArray arrayCourses = new JSONArray(response);
            int countCourses = arrayCourses.length();

            for (int i = 0; i < countCourses; i++)
            {
                // Hole JSON-Objekt
                object = arrayCourses.getJSONObject(i);

                // Lade die Noten je nach Studiengang
                downloader = new HTTPDownloader("https://wwwqis.htw-dresden.de/appservice/getgrades");
                downloader.context = context;
                downloader.urlParameters  = "sNummer=s" + sNummer + "&RZLogin=" + RZLogin + "&AbschlNr=" + object.getString("AbschlNr") + "&StgNr=" + object.getString("StgNr") + "&POVersion=" + object.getString("POVersion");

                response = downloader.getStringWithPost();


                // Überprüfe HTTP- ResonseCde
                if (downloader.ResponseCode != 200)
                    return downloader.ResponseCode;

                JSONArray array = new JSONArray(response);
                int countGrade  = array.length();

                for (int x = 0; x < countGrade; x++)
                {
                    // Hole JSON-Objekt
                    object = array.getJSONObject(x);

                    // Noten anlegen
                    grade =  new Grade();
                    grade.Modul     = object.getString("PrTxt");
                    grade.Note      = Float.parseFloat(object.getString("PrNote"))/100;
                    grade.Vermerk   = object.getString("Vermerk");
                    grade.Status    = object.getString("Status");
                    grade.Credits   = Float.parseFloat(object.getString("EctsCredits"));
                    grade.Versuch   = Short.parseShort(object.getString("Versuch"));
                    grade.Semester  = object.getInt("Semester");
                    grade.Kennzeichen= object.getString("PrForm");

                    arrayList.add(grade);
                }

                Collections.reverse(arrayList);
                noten = arrayList.toArray(new Grade[arrayList.size()]);
            }
        }
        catch (Exception e)
        {
            return 999;
        }

        return downloader.ResponseCode;
    }

    public void getNotenLocal()
    {
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                DatabaseHandlerNoten.COLUMN_NAME_MODUL,
                DatabaseHandlerNoten.COLUMN_NAME_Note,
                DatabaseHandlerNoten.COLUMN_NAME_VERMERK,
                DatabaseHandlerNoten.COLUMN_NAME_STATUS,
                DatabaseHandlerNoten.COLUMN_NAME_CREDITS,
                DatabaseHandlerNoten.COLUMN_NAME_VERSUCH,
                DatabaseHandlerNoten.COLUMN_NAME_SEMESTER,
                DatabaseHandlerNoten.COLUMN_NAME_KENNZEICHEN
        };

        Cursor cursor = db.query(DatabaseHandlerNoten.TABLE_NAME, projection, null, null,null,null,null);

        if (cursor.moveToFirst())
        {
            ArrayList<Grade> arrayList = new ArrayList<Grade>();

            do
            {
                Grade grade = new Grade();
                grade.Modul = cursor.getString(0);
                grade.Note  = Float.parseFloat(cursor.getString(1));
                grade.Vermerk = cursor.getString(2);
                grade.Status = cursor.getString(3);
                grade.Credits = Float.parseFloat(cursor.getString(4));
                grade.Versuch = Short.parseShort(cursor.getString(5));
                grade.Semester = cursor.getInt(6);
                grade.Kennzeichen = cursor.getString(7);

                arrayList.add(grade);
            }while (cursor.moveToNext());

            noten = arrayList.toArray(new Grade[arrayList.size()]);
        }
        else
            noten = new Grade[0];

        db.close();
    }

    public void saveNotenLocal()
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        mDbHelper.clearTable(db);

        for (Grade note : noten)
        {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseHandlerNoten.COLUMN_NAME_MODUL, note.Modul);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_Note, note.Note);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_VERMERK, note.Vermerk);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_STATUS, note.Status);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_CREDITS, note.Credits);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_VERSUCH, note.Versuch);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_SEMESTER, note.Semester);
            values.put(DatabaseHandlerNoten.COLUMN_NAME_KENNZEICHEN, note.Kennzeichen);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(DatabaseHandlerNoten.TABLE_NAME, null, values);
        }
        db.close();
    }

    public Stats[] getStats()
    {
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ArrayList<Stats> arrayList = new ArrayList<Stats>();

        Cursor cursor = db.rawQuery("SELECT Noten.Semester, MAX(Note), MIN(Note), AnzahlNoten,SUM(Credits),SUM(Note*Credits)" +
                " FROM Noten" +
                " JOIN (SELECT Semester, Count(Credits) AS AnzahlNoten FROM Noten WHERE Credits != 0.0 GROUP BY Semester) AS UA" +
                " ON UA.Semester == Noten.Semester" +
                " GROUP BY Noten.Semester", null);

        if (cursor.moveToFirst())
        {
            Stats total = new Stats();
            total.Semester = "Studium";
            total.GradeBest = 5.0f;
            total.GradeWorst = 1.0f;

            do
            {
                Stats stats         = new Stats();
                stats.Semester      = getSemester(Integer.parseInt(cursor.getString(0)));
                stats.GradeWorst    = cursor.getFloat(1);
                stats.GradeBest     = cursor.getFloat(2);
                stats.GradeCount    = cursor.getInt(3);
                stats.Credits       = cursor.getFloat(4);
                stats.Average       = cursor.getFloat(5)/stats.Credits;

                total.GradeBest     = Math.min(total.GradeBest,cursor.getFloat(2));
                total.GradeWorst    = Math.max(total.GradeWorst,cursor.getFloat(1));
                total.Credits       += cursor.getFloat(4);
                total.GradeCount    += cursor.getInt(3);
                total.Average       += cursor.getFloat(5);

                arrayList.add(stats);

            }while (cursor.moveToNext());

            total.Average = total.Average / total.Credits;

            arrayList.add(0, total);

            db.close();

            return arrayList.toArray(new Stats[arrayList.size()]);
        }

        db.close();
        return new Stats[0];
    }

    public static String getSemester(Integer Semester)
    {
        Semester-=20000;
        if (Semester%2 == 1)
            return "Sommersemester " + Semester / 10;
        else
            return "Wintersemester " + Semester / 10 + " / " + ((Semester / 10)+1);
    }
}