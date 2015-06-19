package de.htwdd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.R;
import de.htwdd.TimetableEditSelectAdapter;
import de.htwdd.types.Lesson;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableEditSelectFragment extends Fragment
{
    private int week;
    private int day;
    private int ds;

    public TimetableEditSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            week    = getArguments().getInt("Week");
            day     = getArguments().getInt("Day");
            ds      = getArguments().getInt("DS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_edit_select, container, false);

        // Lade Stunden
        DatabaseHandlerTimetable timetable = new DatabaseHandlerTimetable(getActivity());
        ArrayList<Lesson> lessonArrayList = timetable.getDS(week, day, ds);

        TimetableEditSelectAdapter adapter = new TimetableEditSelectAdapter(getActivity(), lessonArrayList);

        // ListView setzen
        ListView listView = (ListView) view.findViewById(R.id.timetable_edit_select_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                Bundle bundleFragement = new Bundle();
                bundleFragement.putInt("Week", week);
                bundleFragement.putInt("Day", day);
                bundleFragement.putInt("DS", ds);
                bundleFragement.putInt("Index", i);

                Fragment fragment = new TimetableEditFragment();
                fragment.setArguments(bundleFragement);

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.menu_frame, fragment).commit();
            }
        });

        return view;
    }
}
