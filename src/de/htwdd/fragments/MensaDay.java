package de.htwdd.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import de.htwdd.classes.Mensa;
import de.htwdd.MensaArrayAdapter;
import de.htwdd.R;
import de.htwdd.types.Meal;


public class MensaDay extends ListFragment
{
    short mensa_id = 9;
    public SharedPreferences app_preferences;
    public ProgressBar progressbar;

    public MensaDay()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list2, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        if (getArguments() != null)
            mensa_id = getArguments().getShort("MensaID",(short)9);

        super.onActivityCreated(savedInstanceState);

        worker w = new worker();
        w.execute();
    }

    private class worker extends AsyncTask<Void, Void, Meal[]>
    {
        @Override
        protected Meal[] doInBackground(Void... params)
        {
            Mensa myMensa = new Mensa(mensa_id);
            myMensa.getDataCurrentDay();
            myMensa.getThumbnail();

            return myMensa.Food;
        }

        @Override
        protected void onPostExecute(Meal[] essen)
        {
            try
            {
                // Progessbar unsichtbar machen
                getView().findViewById(R.id.waitIndicator).setVisibility(View.GONE);

                // Liste sichtbar machen
                ListView l = (ListView) getView().findViewById(android.R.id.list);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                if (essen.length < 1)
                {
                    essen = new Meal[1];
                    essen[0] = new Meal();
                    essen[0].Title = "Heute kein Angebot";
                }

                MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(), essen);
                setListAdapter(colorAdapter);
            } catch (Exception e)
            {
            }
        }
    }
}