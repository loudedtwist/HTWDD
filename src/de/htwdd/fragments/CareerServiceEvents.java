package de.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.htwdd.CareerServiceArrayAdapter;
import de.htwdd.classes.HTWCalendar;
import de.htwdd.R;
import de.htwdd.types.Event;


public class CareerServiceEvents extends Fragment
{
    LinearLayout waitIndicator;
    int mode;

    public CareerServiceEvents()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mode = getArguments().getInt("mode",0);
        if (mode != 2)
            return inflater.inflate(R.layout.career_service_events, null);
        else
            return inflater.inflate(R.layout.career_service_beratung, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (mode != 2)
        {
            worker w = new worker();
            w.execute();

            // Zeige Progessbar an
            waitIndicator = (LinearLayout) getView().findViewById(R.id.ProgressBar);
            waitIndicator.setVisibility(View.VISIBLE);
        }
    }

    private class worker extends AsyncTask<Event, Integer, Event[]>
    {
        @Override
        protected Event[] doInBackground(Event... params)
        {
            long time = System.currentTimeMillis();

            HTWCalendar calendar = new HTWCalendar();
            return calendar.getEvents((short)1,time, time+30*24*60*60*1000);
        }

         @Override
        protected void onPostExecute(Event[] events)
        {
            try
            {
                // Progessbar unsichtbar machen
                waitIndicator.setVisibility(View.GONE);

                // Liste sichtbar machen
                ListView l = (ListView) getView().findViewById(R.id.eventlist);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                if (events.length < 1)
                {
                    events = new Event[1];
                    events[0] = new Event();
                    events[0].Title = "Aktuell keine Angebote";
                }

                CareerServiceArrayAdapter colorAdapter = new CareerServiceArrayAdapter(getActivity(), events);
                l.setAdapter(colorAdapter);
            } catch (Exception e)
            {
            }
        }
    }
}