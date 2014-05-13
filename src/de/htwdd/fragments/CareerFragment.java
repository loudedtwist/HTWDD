package de.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.htwdd.CareerArrayAdapter;
import de.htwdd.HTTPDownloader;
import de.htwdd.R;
import de.htwdd.types.TEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CareerFragment extends Fragment
{
    public String[] links;
    public String[] titles;
    public String[] location;
    public String[] shortinfo;
    public LinearLayout wait;
    public int mode;

    public CareerFragment()
    {
    }

    public CareerFragment(int i)
    {
        // TODO Auto-generated constructor stub
        mode = i;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mode != 2)
            return inflater.inflate(R.layout.career, null);
        else
            return inflater.inflate(R.layout.career_beratung, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (mode != 2)
        {
            worker w = new worker();
            w.execute(mode);

            wait = (LinearLayout) getView().findViewById(R.id.waitheute);
            wait.setVisibility(View.VISIBLE);
        }
    }

    private class worker extends AsyncTask<Object, Integer, TEvent[]>
    {
        @Override
        protected TEvent[] doInBackground(Object... params)
        {
            HTTPDownloader downloader = null;

            try {
                if (mode == 0)
                    downloader = new HTTPDownloader("http://www.htw-dresden.de/fileadmin/userfiles/wiwi/Career_Service/Dateien_fuer_HTWDD_App/Termine_fuer_HTWDD_App_Veranstaltungen.csv");
                else if (mode == 1)
                    downloader = new HTTPDownloader("http://www.htw-dresden.de/fileadmin/userfiles/wiwi/Career_Service/Dateien_fuer_HTWDD_App/Termine_fuer_HTWDD_App_Workshops.csv");

                List<String> result = downloader.getCSV();
                String[] events_s   = new String[result.size()];
                TEvent[] events     = new TEvent[events_s.length];

                for (int i = 0; i < events_s.length; i++)
                {
                    events_s[i] = result.get(i);

                    String details = events_s[i];

                    String[] details2 = details.split(";");
                    events[i] = new TEvent();

                    if (details2.length > 0)
                        events[i].title = details2[0];

                    if (details2.length == 7)
                    {

                        if (details2.length > 2)
                            events[i].trainer = details2[1];
                        else
                            events[i].trainer = "";

                        if (details2.length > 3)
                            events[i].datum = "Datum: " + details2[2] + " - Uhrzeit: " + details2[3];
                        else
                            events[i].datum = "";

                        if (details2.length > 4)
                            events[i].raum = details2[4];
                        else
                            events[i].raum = "";

                        if (details2.length > 5)
                            events[i].desc = details2[5];
                        else
                            events[i].desc = "";

                        if (details2.length > 6)
                            events[i].link = details2[6];
                        else
                            events[i].link = "";
                    }
                    else
                    {
                        events[i].trainer = details2[1];

                        if (details2.length > 2)
                            events[i].datum = "Datum: " + details2[2] + " - Uhrzeit: " + details2[3];
                        else
                            events[i].datum = "";

                        //if (details2.length>3)
                        //	events[i].raum=details2[3];
                        //	else
                        events[i].raum = "";

                        if (details2.length > 4)
                            events[i].desc = details2[4];
                        else
                            events[i].desc = "";

                        if (details2.length > 5)
                            events[i].link = details2[5];
                        else
                            events[i].link = "";
                    }
                }
                return events;
            }
            catch (Exception e)
            {
            }
            return  null;
        }

        protected void onProgressUpdate(Integer... progress)
        {
        }


        @Override
        protected void onPostExecute(TEvent[] events)
        {
            try
            {
                ListView l = (ListView) getView().findViewById(R.id.eventlist);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                TEvent[] events_new = new TEvent[events.length - 1];

                for (int i = 0; i < events_new.length; i++)
                {
                    events_new[i] = events[i + 1];
                }

                List list2 = new ArrayList();

                for (int i = 0; i < events_new.length; i++)
                {
                    try
                    {
                        String datestring = events_new[i].datum;
                        datestring = datestring.substring(7, datestring.indexOf("-") - 1);
                        if (datestring.contains("/"))
                            datestring = datestring.substring(datestring.indexOf("/") + 1);
                        if (datestring.length() == 10)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            Date date = sdf.parse(datestring);
                            if (date.after(new Date()))
                                list2.add(events_new[i]);
                        }
                        else
                            list2.add(events_new[i]);
                    } catch (Exception e)
                    {
                    }
                }

                if (list2.size() == 0)
                {
                    TEvent event = new TEvent();
                    event.title = "aktuell kein Angebot";
                    event.desc = "";
                    event.raum = "";
                    event.zeit = "";
                    event.trainer = "";
                    event.datum = "";
                    list2.add(event);
                }

                TEvent[] pruned_events = new TEvent[list2.size()];

                for (int i = 0; i < list2.size(); i++)
                    pruned_events[i] = (TEvent) list2.get(i);

                String titles[] = new String[pruned_events.length];

                for (int i = 0; i < pruned_events.length; i++)
                    titles[i] = pruned_events[i].title;

                wait.setVisibility(View.GONE);

                CareerArrayAdapter colorAdapter = new CareerArrayAdapter(getActivity(), titles, pruned_events);
                l.setAdapter(colorAdapter);
            } catch (Exception e)
            {
            }
        }
    }
}