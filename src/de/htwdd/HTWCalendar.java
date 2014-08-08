package de.htwdd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import de.htwdd.types.TEvent;

public class HTWCalendar {
    static String url = "https://www.htw-dresden.de/vtms/service/events";

    public TEvent[] getEvents(short organizer, long startTime, long endTime)
    {
        try {
            JSONObject object;
            TEvent tmp;
            Date date;

            Date currentDate            = new Date();
            Vector<TEvent> vector       = new Vector<TEvent>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            // Daten von HTW laden
            HTTPDownloader downloader   = new HTTPDownloader(url+"?organizer="+organizer);
            downloader.urlParameters    = "start="+startTime+"&end="+endTime;
            // Result in JSON-Array speichern
            JSONArray array             = new JSONArray(downloader.getStringWithPost());
            int count                   = array.length();

            for (int i=0; i<count; i++)
            {
                // Hole JSON-Objekt
                object  = array.getJSONObject(i);

                // Hole Start-Datum aus dem JSON-Objekt
                date    = dateFormat.parse(object.getString("start"));

                // Liegt das Start-Datum vor dem heutigen Datum -> abbrechen
                if (currentDate.after(date))
                    break;

                // Event anlegen und Werte speichern
                tmp = new TEvent();
                tmp.datum = date;
                tmp.Title = object.getString("title");
                tmp.desc  = object.getString("title_ext");
                tmp.url  = object.getString("url");
                vector.add(tmp);
            }

            //Vektor umdrehen (jüngste Ereigenisse als erstes)
            Collections.reverse(vector);

            return vector.toArray(new TEvent[vector.size()]);
        }
        catch (Exception e)
        {
            TEvent[] events = new TEvent[1];
            events[0] = new TEvent();
            events[0].Title = "Keine Internetverbindung!";
            return  events;
        }
    }
}
