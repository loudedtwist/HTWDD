package de.htwdd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import de.htwdd.NotenStatsAdapter;
import de.htwdd.R;
import de.htwdd.classes.Noten;
import de.htwdd.types.Stats;

public class NotenStatsFragment extends Fragment {

    NotenStatsAdapter mAdapter;
    Stats[] statses;

    public NotenStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_noten_stats, container, false);

        // Get the ListView
        ListView listView = (ListView) view.findViewById(R.id.NotenStatsList);

        // Get the Data
        Noten noten =  new Noten(getActivity());
        statses = noten.getStats();

        mAdapter = new NotenStatsAdapter(getActivity(),statses);
        listView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Hinweis falls keine Noten vorhanden
        if (statses.length == 0)
        {
            LinearLayout hinweis = (LinearLayout) getActivity().findViewById(R.id.NotenStatsHinweis);
            hinweis.setVisibility(View.VISIBLE);
            TextView message = (TextView) getActivity().findViewById(R.id.NotenStatsHinweisText);
            message.setText("Keine Noten vorhanden!");
        }
    }
}