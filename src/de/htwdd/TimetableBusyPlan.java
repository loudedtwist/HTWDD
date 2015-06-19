package de.htwdd;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimetableBusyPlan extends ArrayAdapter<String>
{
    private Context context;
    private LayoutInflater inflater;
    private String[] lessonDS;
    private int currentDS;
    private String[] lessons;

    public TimetableBusyPlan(Context context, String[] objects, int currentDS)
    {
        super(context, R.layout.fragment_timetable_busy_plan, objects);

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentDS = currentDS;
        this.lessons = objects;

        // Stunden
        lessonDS = context.getResources().getStringArray(R.array.lesson_ds_timeOnly);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_timetable_busy_plan, parent, false);

        // Hintergrund einfärben
        if (position%2==0)
            convertView.setBackgroundColor(context.getResources().getColor(R.color.whitegrey));
        else
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        if (position==(currentDS-1))
            convertView.setBackgroundColor(context.getResources().getColor(R.color.hellblau));

        // Zeiten anzeigen
        TextView textDS = (TextView) convertView.findViewById(R.id.timetable_busy_plan_ds);
        textDS.setText(lessonDS[position]);

        // Stunde anzeigen
        TextView textLesson = (TextView) convertView.findViewById(R.id.timetable_busy_plan_lesson);
        textLesson.setText(lessons[position]);

        return convertView;
    }
}
