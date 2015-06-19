package de.htwdd;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwdd.types.Lesson;

public class TimetableEditSelectAdapter extends BaseAdapter
{
    private ArrayList<Lesson> lessonArrayList;
    private LayoutInflater mLayoutInflater = null;

    public TimetableEditSelectAdapter(Context context, ArrayList<Lesson> lessonArrayList)
    {
        this.lessonArrayList = lessonArrayList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lessonArrayList.size();
    }

    @Override
    public Lesson getItem(int i) {
        return lessonArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            view = mLayoutInflater.inflate(R.layout.fragment_timetable_edit_select_item, viewGroup, false);

        // Hole Lesson
        Lesson lesson = getItem(i);

        // Zeige Namen und Tag an
        TextView textName = (TextView) view.findViewById(R.id.timetable_edit_LessonName);
        textName.setText("("+lesson.lessonTag+") "+lesson.name);

        // Zeige Art an
        String[] lessonType = view.getResources().getStringArray(R.array.lesson_type);

        TextView textType = (TextView) view.findViewById(R.id.timetable_edit_LessonType);
        textType.setText(lessonType[lesson.getTypeInt()]);

        // Zeige Wochen an
        TextView textWeeks = (TextView) view.findViewById(R.id.timetable_edit_LessonWeeksOnly);
        textWeeks.setText(view.getResources().getText(R.string.timetable_edit_LessonWeeksOnly)+": "+lesson.weeksOnly);

        // zeige Raum an
        TextView textRoom = (TextView) view.findViewById(R.id.timetable_edit_LessonRoom);
        textRoom.setText(view.getResources().getText(R.string.lesson_Room)+": "+lesson.rooms);

        return view;
    }
}
