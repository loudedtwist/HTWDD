package de.htwdd.classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import de.htwdd.types.Event;

public class HTWCalendar {
    static String url = "https://www.htw-dresden.de/vtms/service/events";

    public Event[] getEvents(short organizer, long startTime, long endTime)
    {
        try {
            JSONObject object;
            Event tmp;
            Date date;

            Date currentDate            = new Date();
            ArrayList<Event> arrayList = new ArrayList<Event>();
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
                tmp = new Event();
                tmp.datum = date;
                tmp.Title = object.getString("title");
                tmp.desc  = object.getString("title_ext");
                tmp.url  = object.getString("url");
                arrayList.add(tmp);
            }

            //Vektor umdrehen (jüngste Ereigenisse als erstes)
            Collections.reverse(arrayList);

            return arrayList.toArray(new Event[arrayList.size()]);
        }
        catch (Exception e)
        {
            Event[] events = new Event[1];
            events[0] = new Event();
            events[0].Title = "Keine Internetverbindung!";
            return  events;
        }
    }
}
