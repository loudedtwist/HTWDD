package de.htwdd.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.R;
import de.htwdd.RoomTimeTableAdapter;
import de.htwdd.RoomTimetableDetailsActivity;
import de.htwdd.classes.HTTPDownloader;
import de.htwdd.classes.Timetable;
import de.htwdd.database.RoomTimetableDAO;
import de.htwdd.types.RoomTimetable;


public class RoomTimetableFragment extends Fragment
{
    private View view;
    private ArrayList<RoomTimetable> roomTimetables;
    private RoomTimeTableAdapter adapter;
    private RoomTimetableDAO dao;

    public RoomTimetableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_room_timetable, container, false);

        // ListView holen
        ListView listView = (ListView) view.findViewById(R.id.room_timetable_listview);

        // Footer View setzen
        View listView_footer = inflater.inflate(R.layout.fragment_room_timetable_footer, listView, false);
        listView.addFooterView(listView_footer, null, false);

        // Erstelle Liste mit Räumen für Adapter
        roomTimetables  = new ArrayList<RoomTimetable>();
        adapter         = new RoomTimeTableAdapter(getActivity(), roomTimetables);
        dao             = new RoomTimetableDAO(getActivity());

        // Lade Daten
        loadData();

        // Wurde nach einen neuem Raum gesucht?
        if (getArguments() != null && getArguments().getString("add_room") != null) {
            String getRoom = getArguments().getString("add_room");
            LoadRoom loadRoom = new LoadRoom();
            loadRoom.execute(getRoom);
        }

        // ListView Adapter setzen
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("room", roomTimetables.get(i).RoomName);
                Intent intent = new Intent(getActivity(), RoomTimetableDetailsActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        // Löschen, aktualisieren über Context-Menu
        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_room_timetable, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String room = roomTimetables.get(info.position).RoomName;

        switch (item.getItemId())
        {
            case R.id.room_timetable_delete:
                // Lösche Raum aus DB
                RoomTimetableDAO dao = new RoomTimetableDAO(getActivity());
                dao.removeRoom(room);
                loadData();
                return true;
            case R.id.room_timetable_update:
                LoadRoom loadRoom = new LoadRoom();
                loadRoom.execute(room);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadData()
    {
        // Kalender zum bestimmen welcher Plan angezeigt wird
        Calendar calendar = GregorianCalendar.getInstance();

        // Wenn Sonntag ist, auf Plan für Montag springen
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        int day  = calendar.get(Calendar.DAY_OF_WEEK);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        // Lade Stundepläne aus DB
        roomTimetables.clear();
        roomTimetables.addAll(dao.getOverview(day - 1, week));

        // Hinweis ein / ausblenden
        TextView textView = (TextView) view.findViewById(R.id.room_timetable_note);
        ListView listView = (ListView) view.findViewById(R.id.room_timetable_listview);

        if (roomTimetables.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else {
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


    private class LoadRoom extends AsyncTask<String, Void, Timetable>
    {
        private Context context = getActivity();
        private ProgressBar progressBar;
        private ListView listView;
        private TextView textView;
        private int responseCode;

        @Override
        protected void onPreExecute()
        {
            // Überprüfe Internetverbindung
            if (!HTTPDownloader.CheckInternet(context))
            {
                Toast.makeText(context, R.string.app_no_internet, Toast.LENGTH_LONG).show();
                this.cancel(true);
            }

            // ListView ausblenden
            listView = (ListView) view.findViewById(R.id.room_timetable_listview);
            listView.setVisibility(View.GONE);

            // Hinweise ausblenden
            textView = (TextView) view.findViewById(R.id.room_timetable_note);
            textView.setVisibility(View.GONE);

            // Progressbar anzeigen
            progressBar = (ProgressBar) view.findViewById(R.id.room_timetable_progressbar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Timetable doInBackground(String... strings)
        {
            // Lade Stundenplan für Raum
            Timetable timetable = new Timetable(context);
            responseCode = timetable.getTimetableRoom(strings[0]);

            // Alles OK? - Dann Stundenplan in DB speichern
            if (responseCode == 200 && timetable.lessonArrayList.size() != 0)
            {
                RoomTimetable roomTimetable = new RoomTimetable();
                roomTimetable.RoomName = strings[0].toUpperCase();
                roomTimetable.Timetable = timetable.lessonArrayList;
                // Lösche ggf alten Plan
                dao.removeRoom(roomTimetable.RoomName);
                // Neuen Plan speichern
                dao.add(roomTimetable);
            }

            return timetable;
        }

        @Override
        protected void onPostExecute(Timetable timetable)
        {
            if (!isAdded())
                return;

            // Auswertung anzeigen
            if (responseCode != 200)
                Toast.makeText(context, R.string.room_timetable_updade_error, Toast.LENGTH_SHORT).show();
            else if (timetable.lessonArrayList.size() == 0)
                Toast.makeText(context, R.string.room_timetable_updade_notFound, Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(context, R.string.room_timetable_updade_success, Toast.LENGTH_SHORT).show();
            }

            // Daten neuladen
            loadData();

            // Progressbar ausblenden
            progressBar.setVisibility(View.GONE);

            // ListView anzeigen?
            if (roomTimetables.size() != 0)
                listView.setVisibility(View.VISIBLE);
            else textView.setVisibility(View.VISIBLE);
        }
    }
}
