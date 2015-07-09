package de.htwdd.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.R;
import de.htwdd.TimetableAdapter;
import de.htwdd.TimetableDetailsActivity;
import de.htwdd.TimetableEditActivity;
import de.htwdd.WizardWelcome;
import de.htwdd.classes.HTTPDownloader;
import de.htwdd.classes.Timetable;
import de.htwdd.types.Lesson;


public class TimetableFragment extends Fragment
{
    private String StgJhr;
    private String Stg;
    private String StgGrp;
    private String Prof;
    private int week;
    private SharedPreferences sharedPreferences;
    private Context context;
    // Liste mit allen Stunden der aktuellen Woche
    private ArrayList<Lesson> lessons_week = new ArrayList<Lesson>();

    public TimetableAdapter timetableAdapter;


    public TimetableFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = inflater.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        week = getArguments().getInt("week", new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        // Hole Stunden aus DB
        loadLessons();

        // Setze Adapter
        timetableAdapter = new TimetableAdapter(getActivity(), lessons_week, week);

        GridView gridView = (GridView) view.findViewById(R.id.Timetable);
        gridView.setAdapter(timetableAdapter);

        // Sollen Einträge aktuallisiert werden?
        if (getArguments() != null && getArguments().getBoolean("Update"))
                loadTimetable();
        else
        {
            //Einträge in der Datenbank vorhanden, wenn nein Vorschlagen zu updaten
            DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
            if (databaseHandlerTimetable.countDS() == 0)
                loadTimetable();
            databaseHandlerTimetable.close();
        }

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("DS", i/7);
                bundle.putInt("Day", i%7);
                bundle.putInt("Week", week);
                Intent intent = new Intent(context, TimetableEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("DS", i/7);
                bundle.putInt("Day", i%7);
                bundle.putInt("Week", week);
                Intent intent = new Intent(context, TimetableDetailsActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        loadLessons();
        timetableAdapter.notifyDataSetChanged();
    }


    /**
     * Lädt die Stunden der aktuellen Woche {@see week} aus der Datenbank und speichert sie in einer
     * Liste {@see lesson_week}
     */
    void loadLessons()
    {
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
        lessons_week.clear();
        lessons_week.addAll(databaseHandlerTimetable.getShortWeek(week));
        databaseHandlerTimetable.close();
    }


    private void loadTimetable()
    {
        int modus;
        StgJhr  = sharedPreferences.getString("im", "");
        Stg     = sharedPreferences.getString("stdg", "");
        StgGrp  = sharedPreferences.getString("studgruppe", "");
        Prof    = sharedPreferences.getString("prof_kennung", "");

        // Überprüfe Daten
        if ((StgJhr.length() < 2 || Stg.length() != 3 || StgGrp.length() == 0) && (Prof.length() == 0))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.timetable_noData)
                    .setTitle(R.string.noData)
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton(R.string.startAssistent, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent nextScreen = new Intent(getActivity(), WizardWelcome.class);
                            startActivity(nextScreen);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Toast.makeText(context,R.string.timetable_updade_error,Toast.LENGTH_LONG).show();
                        }
                    }).show();

            return;
        }

        // Auswahl was geladen werden soll
        if (!(StgJhr.length() < 2 || Stg.length() != 3 || StgGrp.length() == 0))
            modus = 1;
        else modus = 2;

        // Überprüfe Internet-Verbindung
        if (!HTTPDownloader.CheckInternet(context))
        {
            Toast.makeText(context, R.string.app_no_internet,Toast.LENGTH_LONG).show();
            return;
        }

        WorkerGetTimeTable getTimeTable = new WorkerGetTimeTable();
        getTimeTable.execute(modus);
    }


    private class WorkerGetTimeTable extends AsyncTask<Integer, Void, Timetable>
    {
        private int ResponseCode;

        @Override
        protected Timetable doInBackground(Integer... integers)
        {
            Timetable timetable = new Timetable(context);

            switch (integers[0])
            {
                case 1:
                    ResponseCode = timetable.getTimtableStudent(StgJhr, Stg, StgGrp);
                    break;
                case 2:
                    ResponseCode = timetable.getTimetableProf(Prof);
                    break;
            }

            // Speichern des Stundenplans
            if (ResponseCode == 200)
                timetable.saveTimetableUser();

            return timetable;
        }

        @Override
        protected void onPostExecute(Timetable timetable)
        {
            if (!isAdded())
                return;

            switch (ResponseCode)
            {
                case 200:
                    Toast.makeText(context,R.string.timetable_updade_success, Toast.LENGTH_SHORT).show();
                    break;
                case 999:
                    Toast.makeText(context,R.string.pars_error, Toast.LENGTH_LONG);
                    break;
                default:
                    Toast.makeText(context,R.string.app_internet_error,Toast.LENGTH_LONG).show();
                    break;
            }

            // Adapter über neue Daten informieren
            timetableAdapter.notifyDataSetChanged();
        }
    }
}
