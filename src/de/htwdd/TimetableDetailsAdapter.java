package de.htwdd;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import de.htwdd.types.Lesson;

public class TimetableDetailsAdapter extends BaseAdapter
{
    private ArrayList<Lesson> lessonArrayList;
    private LayoutInflater mLayoutInflater = null;
    private final String[] nameOfDays = DateFormatSymbols.getInstance().getWeekdays();

    public TimetableDetailsAdapter(Context context, ArrayList<Lesson> lessonArrayList)
    {
        this.lessonArrayList = lessonArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = mLayoutInflater.inflate(R.layout.activity_timetable_details_item, viewGroup, false);

        // Hole Lesson
        Lesson lesson = getItem(i);

        TextView textName = (TextView) view.findViewById(R.id.timetable_edit_LessonName);
        textName.setText("("+lesson.lessonTag+") "+lesson.name);

        // Zeige Art an
        String[] lessonType = view.getResources().getStringArray(R.array.lesson_type);

        TextView textType = (TextView) view.findViewById(R.id.timetable_edit_LessonType);
        textType.setText(lessonType[lesson.getTypeInt()] + " " + view.getResources().getString(R.string.timetable_details_by) + " " + (lesson.professor!=null?lesson.professor:""));

        // Zeige Raum an
        TextView textRoom = (TextView) view.findViewById(R.id.timetable_edit_LessonRoom);
        textRoom.setText(lesson.rooms);

        // Zeige Woche an
        String[] lessonWeek = view.getResources().getStringArray(R.array.lesson_week);

        TextView textWeek = (TextView) view.findViewById(R.id.timetable_edit_LessonWeek);
        textWeek.setText(lessonWeek[lesson.week]);

        // Zeige Tag an
        TextView textDay = (TextView) view.findViewById(R.id.timetable_edit_LessonDay);
        textDay.setText(nameOfDays[lesson.day+1]);

        // Zeige DS an
        String[] lessonDS = view.getResources().getStringArray(R.array.lesson_ds);

        TextView textDS = (TextView) view.findViewById(R.id.timetable_edit_LessonDS);
        textDS.setText(lessonDS[lesson.ds-1]);

        // Zeige KW
        TextView textOnlyKW = (TextView) view.findViewById(R.id.timetable_edit_LessonWeeksOnly);
        textOnlyKW.setText(lesson.weeksOnly);

        return view;
    }
}
