package de.htwdd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import de.htwdd.fragments.TimetableEditFragment;
import de.htwdd.fragments.TimetableEditSelectFragment;
import de.htwdd.types.Lesson;


public class TimetableEditActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frame);

        Context context = getApplicationContext();

        // Hole Daten aus Bundle
        Bundle bundle = getIntent().getExtras();
        int ds = bundle.getInt("DS");
        int day = bundle.getInt("Day");
        int week = bundle.getInt("Week");

        // Lade Stunden aus DB
        DatabaseHandlerTimetable timetable = new DatabaseHandlerTimetable(context);
        ArrayList<Lesson> lessons = timetable.getDS(week, day, ds);
        timetable.close();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        if (lessons.size() <= 1)
        {
            bundle.putInt("Index", 0);
            fragment = new TimetableEditFragment();
        }
        else fragment = new TimetableEditSelectFragment();

        fragment.setArguments(bundle);

        // Bei Orientation change Fragment nicht neu einfügen
        if (savedInstanceState == null)
            fragmentManager.beginTransaction().replace(R.id.menu_frame,fragment).commit();
    }

}
