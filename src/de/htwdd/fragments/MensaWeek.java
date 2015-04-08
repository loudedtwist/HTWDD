package de.htwdd.fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.htwdd.classes.Mensa;
import de.htwdd.MensaArrayAdapter;
import de.htwdd.R;
import de.htwdd.types.Meal;


public class MensaWeek extends ListFragment
{

    public MensaWeek()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        worker w = new worker();
        w.execute();
    }


    private class worker extends AsyncTask<Void, Void, Meal[]>
    {
        @Override
        protected Meal[] doInBackground(Void... params)
        {
            Mensa myMensa = new Mensa();
            myMensa.getDataWeek();

            return myMensa.Food;
        }

        @Override
        protected void onPostExecute(Meal[] essen)
        {
            if (!isAdded())
                return;

            Activity activity = getActivity();

            // Progessbar unsichtbar machen
            activity.findViewById(R.id.waitIndicator).setVisibility(View.GONE);

            // Liste sichtbar machen
            ListView listView = getListView();
            listView.setVisibility(View.VISIBLE);
            listView.setDividerHeight(0);

            // Kein Essen vorhanden
            if (essen.length < 1)
            {
                essen = new Meal[1];
                essen[0] = new Meal();
                essen[0].Title = "Heute kein Angebot";
            }

            MensaArrayAdapter colorAdapter = new MensaArrayAdapter(activity, essen);
            setListAdapter(colorAdapter);
        }
    }
}
