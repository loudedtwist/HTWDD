package de.htwdd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.R;
import de.htwdd.types.Lesson;


public class TimetableEditFragment extends Fragment
{
    private int week;
    private int day;
    private int ds;
    private int index;
    private int internID;
    private boolean createNew;
    View view;

    public TimetableEditFragment()
    {
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
            index   = getArguments().getInt("Index");
            createNew= getArguments().getBoolean("new", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_timetable_edit, container, false);

        // Lade Stunde
        DatabaseHandlerTimetable timetable = new DatabaseHandlerTimetable(getActivity());
        ArrayList<Lesson> lessonArrayList = timetable.getDS(week, day, ds);
        final Lesson lesson;

        // OnClick-Listener zum Speichern
        Button editSave = (Button) view.findViewById(R.id.timetable_edit_LessonSave);
        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveLesson())
                {
                    Toast.makeText(getActivity(), R.string.timetable_edit_LessonSaveSuccess, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                else Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });


        if(lessonArrayList.size()==0 || createNew)
        {
            // Wähle Woche aus
            Spinner editWeek = (Spinner) view.findViewById(R.id.timetable_edit_LessonWeek);
            editWeek.setSelection(week%2 == 0?2:week%2);

            // Wähle Tag aus
            Spinner editDay = (Spinner) view.findViewById(R.id.timetable_edit_LessonDay);
            editDay.setSelection(day-1);

            // Wähle DS aus
            Spinner editDS = (Spinner) view.findViewById(R.id.timetable_edit_LessonDS);
            editDS.setSelection(ds-1);

            // Deaktiviere Löschen Button
            Button editDelete = (Button) view.findViewById(R.id.timetable_edit_LessonDelete);
            editDelete.setEnabled(false);

            return view;
        }
        else
            lesson = lessonArrayList.get(index);

        internID = lesson.internID;

        // Setze Vorlesung
        EditText editVorlesung = (EditText) view.findViewById(R.id.timetable_edit_LessonName);
        editVorlesung.setText(lesson.name);

        // Setze Tag
        EditText editTag = (EditText) view.findViewById(R.id.timetable_edit_LessonTag);
        editTag.setText(lesson.lessonTag);

        // Wähle Art aus
        Spinner editType = (Spinner) view.findViewById(R.id.timetable_edit_LessonType);
        editType.setSelection(lesson.getTypeInt());

        // Setze Raum
        EditText editRoom = (EditText) view.findViewById(R.id.timetable_edit_LessonRoom);
        editRoom.setText(lesson.rooms);


        // Wähle Woche aus
        Spinner editWeek = (Spinner) view.findViewById(R.id.timetable_edit_LessonWeek);
        editWeek.setSelection(lesson.week);

        // Wähle Tag aus
        Spinner editDay = (Spinner) view.findViewById(R.id.timetable_edit_LessonDay);
        editDay.setSelection(lesson.day-1);

        // Wähle DS aus
        Spinner editDS = (Spinner) view.findViewById(R.id.timetable_edit_LessonDS);
        editDS.setSelection(lesson.ds-1);

        // Setze WeeksOnly
        EditText editWeeksOnly = (EditText) view.findViewById(R.id.timetable_edit_LessonWeeksOnly);
        editWeeksOnly.setText(lesson.weeksOnly);

        // Setze Onclick-Listener für Löschen-Button
        Button editDelete = (Button) view.findViewById(R.id.timetable_edit_LessonDelete);
        editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(getActivity());
                if (databaseHandlerTimetable.deleteLesson(lesson.internID))
                {
                    Toast.makeText(getActivity(), R.string.timetable_edit_LessonDeleteSuccess, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                else Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private boolean saveLesson()
    {
        Lesson lesson = new Lesson();

        lesson.internID = internID;

        // Setze Vorlesung
        EditText editVorlesung = (EditText) view.findViewById(R.id.timetable_edit_LessonName);
        lesson.name = editVorlesung.getText().toString();

        // Setze Tag
        EditText editTag = (EditText) view.findViewById(R.id.timetable_edit_LessonTag);
        lesson.lessonTag = editTag.getText().toString();

        // Wähle Art aus
        Spinner editType = (Spinner) view.findViewById(R.id.timetable_edit_LessonType);
        lesson.setTypeInt(editType.getSelectedItemPosition());

        // Setze Raum
        EditText editRoom = (EditText) view.findViewById(R.id.timetable_edit_LessonRoom);
        lesson.rooms = editRoom.getText().toString();

        // Wähle Woche aus
        Spinner editWeek = (Spinner) view.findViewById(R.id.timetable_edit_LessonWeek);
        lesson.week = editWeek.getSelectedItemPosition();

        // Wähle Tag aus
        Spinner editDay = (Spinner) view.findViewById(R.id.timetable_edit_LessonDay);
        lesson.day = editDay.getSelectedItemPosition()+1;

        // Wähle DS aus
        Spinner editDS = (Spinner) view.findViewById(R.id.timetable_edit_LessonDS);
        lesson.ds = editDS.getSelectedItemPosition()+1;

        // Setze WeeksOnly
        EditText editWeeksOnly = (EditText) view.findViewById(R.id.timetable_edit_LessonWeeksOnly);
        lesson.weeksOnly = editWeeksOnly.getText().toString();

        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(getActivity());
        return databaseHandlerTimetable.updateLesson(lesson);
    }
}