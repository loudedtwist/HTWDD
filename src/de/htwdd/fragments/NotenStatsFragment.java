package de.htwdd.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.htwdd.DatabaseHandlerNoten;
import de.htwdd.NotenStatsAdapter;
import de.htwdd.R;
import de.htwdd.Stats_element;
import de.htwdd.types.TNote;

import java.util.List;


public class NotenStatsFragment extends Fragment
{
    public SharedPreferences app_preferences;
    public DatabaseHandlerNoten db;
    public int count;


    public NotenStatsFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.noten, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        db = new DatabaseHandlerNoten(getActivity());
        app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());


        LinearLayout l = (LinearLayout) getView().findViewById(R.id.linearLayout2);
        l.setVisibility(View.VISIBLE);
        ExpandableListView v = (ExpandableListView) getView().findViewById(R.id.noten);
        v.setVisibility(View.GONE);

        getView().findViewById(R.id.wait).setVisibility(View.GONE);


        List<TNote> e = db.getAllNoten();


        TNote[] noten = new TNote[e.size()];
        for (int i = 0; i < e.size(); i++)
        {
            noten[i] = e.get(i);
        }

        double finalmark = 0;
        int validcount = 0;
        double mark = 0;
        double credits = 0;


        float totalworstmark = 0.0f;
        float totalbestmark = 5.0f;

        for (int i = 0; i < e.size(); i++)
        {
            try
            {
                double c = Float.parseFloat(noten[i]._Note.replace(',', '.'));
                double cr = Float.parseFloat(noten[i]._Credits);
                if (cr > 0)
                {
                    mark += (c * cr);
                    validcount++;
                    credits += cr;
                }
                totalworstmark = (float) Math.max(totalworstmark, c);
                totalbestmark = (float) Math.min(totalbestmark, c);

            } catch (Exception e2)
            {
            }
            ;

        }

        //if (validcount!=0)
        if (credits != 0)
            finalmark = mark / credits;
        finalmark = Math.round(finalmark * 100.0) / 100.0;

        //Toast.makeText(Noten.this,"Gesamtnote:"+finalmark  ,Toast.LENGTH_LONG).show();


        ListView v2 = (ListView) getView().findViewById(R.id.stats);

        int semesterCount = 0;


        String[] all_sem = db.getAllSem();


        Stats_element[] statsElements = new Stats_element[all_sem.length + 1];
        statsElements[0] = new Stats_element();

        statsElements[0].finalmark = (float) finalmark;
        statsElements[0].worstmark = totalworstmark;
        statsElements[0].bestmark = totalbestmark;
        statsElements[0].semester = "Studium";
        statsElements[0].validcount = validcount;
        statsElements[0].credits = (float) credits;

        for (int i = 1; i < all_sem.length + 1; i++)
        {

            statsElements[i] = new Stats_element();

            List<TNote> sem_noten = db.getSemNoten(all_sem[i - 1]);


            double sem_finalmark = 0;
            int sem_validcount = 0;
            double sem_mark = 0;
            double sem_credits = 0;

            float worstmark = 0.0f;
            float bestmark = 5.0f;

            for (int j = 0; j < sem_noten.size(); j++)
            {

                if (sem_noten.get(j) != null)
                {
                    try
                    {
                        double c = Float.parseFloat(sem_noten.get(j)._Note.replace(',', '.'));
                        String credits2 = sem_noten.get(j)._Credits;
                        if (credits2.length() == 0) credits2 = "0.0";
                        double cr = Float.parseFloat(credits2);
                        if (cr > 0)
                        {
                            sem_mark += (c * cr);
                            sem_validcount++;
                            sem_credits += cr;
                        }
                        worstmark = (float) Math.max(worstmark, c);
                        bestmark = (float) Math.min(bestmark, c);
                    } catch (Exception e2)
                    {
                    }
                }


            }

            if (credits != 0)
                sem_finalmark = sem_mark / sem_credits;
            sem_finalmark = Math.round(sem_finalmark * 100.0) / 100.0;

            statsElements[i].finalmark = (float) sem_finalmark;
            statsElements[i].worstmark = worstmark;
            statsElements[i].bestmark = bestmark;
            statsElements[i].semester = all_sem[i - 1];
            statsElements[i].validcount = sem_validcount;
            statsElements[i].credits = (float) sem_credits;


        }

        String titles[] = new String[statsElements.length];

        for (int i = 0; i < titles.length; i++)
        {
            titles[i] = statsElements[i].semester;
        }


        //Gesamtdurchschnitt und infos


        NotenStatsAdapter colorAdapter = new NotenStatsAdapter(getActivity(), titles, statsElements);
        v2.setAdapter(colorAdapter);
        v2.setDividerHeight(0);


    }


}

