package de.htwdd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import de.htwdd.types.Lesson;

public class TimetableDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable_details);

        final Context context = getApplicationContext();

        // Hole Daten aus Bundle
        Bundle bundle = getIntent().getExtras();
        int ds = bundle.getInt("DS");
        int day = bundle.getInt("Day");
        int week = bundle.getInt("Week");

        // Lade Stunden aus DB
        DatabaseHandlerTimetable timetable = new DatabaseHandlerTimetable(context);
        ArrayList<Lesson> lessons = timetable.getDS(week, day, ds);
        timetable.close();

        // Keine Stunde vorhanden, zum Editieren wechseln
        if (lessons.size()==0)
        {
            Intent intent = new Intent(context, TimetableEditActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            finish();
        }

        // ListView setzen
        TimetableDetailsAdapter adapter = new TimetableDetailsAdapter(getApplicationContext(), lessons);
        ListView listView = (ListView) findViewById(R.id.timetable_edit_select_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = getIntent().getExtras();
                bundle.putInt("index", i);
                Intent intent = new Intent(context, TimetableEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                finish();
            }
        });

        // OnClick-Listener für Button
        Button button = (Button) findViewById(R.id.timetable_details_addLesson);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                bundle.putBoolean("new", true);
                Intent intent = new Intent(context, TimetableEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                finish();
            }
        });
    }
}
