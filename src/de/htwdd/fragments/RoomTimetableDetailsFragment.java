package de.htwdd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.R;
import de.htwdd.TimetableAdapter;
import de.htwdd.database.RoomTimetableDAO;
import de.htwdd.types.Lesson;


public class RoomTimetableDetailsFragment extends Fragment
{
    public TimetableAdapter timetableAdapter;

    public RoomTimetableDetailsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        Bundle bundle = getArguments();
        if (bundle == null)
            return view;

        int week    = bundle.getInt("week", new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));
        String room = bundle.getString("room");

        // Hole Stunden aus DB
        RoomTimetableDAO dao = new RoomTimetableDAO(getActivity());
        ArrayList<Lesson> lessons_week = dao.loadWeek(room, week);

        // Überprüfe ob Stunden gefunden wurden
        if (lessons_week == null)
            return view;

        // Setze Adapter
        timetableAdapter = new TimetableAdapter(getActivity(), lessons_week, week);

        GridView gridView = (GridView) view.findViewById(R.id.Timetable);
        gridView.setAdapter(timetableAdapter);

        return view;
    }
}
