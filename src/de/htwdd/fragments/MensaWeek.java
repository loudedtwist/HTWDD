package de.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.htwdd.Mensa;
import de.htwdd.MensaArrayAdapter;
import de.htwdd.R;
import de.htwdd.types.TEssen;


public class MensaWeek extends ListFragment
{

    public MensaWeek()
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
        super.onActivityCreated(savedInstanceState);

        worker w = new worker();
        w.execute();
    }


    private class worker extends AsyncTask<Void, Void, TEssen[]>
    {
        @Override
        protected TEssen[] doInBackground(Void... params)
        {
            Mensa myMensa = new Mensa();
            myMensa.getDataWeek();

            return myMensa.Food;
        }

        @Override
        protected void onPostExecute(TEssen[] essen)
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
                    essen = new TEssen[1];
                    essen[0] = new TEssen();
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
