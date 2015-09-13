package de.htwdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.classes.CONST;
import de.htwdd.classes.LessonSearch;
import de.htwdd.types.Lesson;
import de.htwdd.types.RoomTimetable;

/**
 * Adapter für die Belungsanzeige
 * @author Kay Förster
 */
public class RoomTimeTableAdapter extends BaseAdapter
{
    private ArrayList<RoomTimetable> arrayList;
    private LayoutInflater inflater;
    private Context context;
    private Calendar calendar;
    private String[] lessonDS;


    public RoomTimeTableAdapter(Context context, ArrayList<RoomTimetable> arrayList)
    {
        this.arrayList = arrayList;
        this.context   = context;
        this.inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.calendar  = GregorianCalendar.getInstance();
        lessonDS       = context.getResources().getStringArray(R.array.lesson_ds_timeOnly);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public RoomTimetable getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_room_timetable_item, viewGroup, false);

        // Hole Item
        final RoomTimetable roomTimetable = getItem(i);

        // Bestimme aktuelle DS
        int current_time = CONST.TimetableCalc.currentTime();
        int current_ds   = CONST.TimetableCalc.getCurrentDS(current_time);

        // Setze Title
        TextView title = (TextView)view.findViewById(R.id.fragment_room_timetable_titel);
        title.setText(roomTimetable.RoomName);

        // Setzte Tag
        if (roomTimetable.day != calendar.get(Calendar.DAY_OF_WEEK)-1)
        {
            final String[] nameOfDays = DateFormatSymbols.getInstance().getWeekdays();

            TextView overview_day = (TextView)view.findViewById(R.id.overview_lesson_day);
            overview_day.setText(nameOfDays[roomTimetable.day+1]);

            // Anzeige der aktuellen Stunde "ausschalten
            current_ds = 0;
        }

        // Daten für Stundenplan-Vorschau
        String[] values           = new String[7];
        LessonSearch lessonSearch = new LessonSearch();

        for (int x=1; x<8; x++)
        {
            ArrayList<Lesson> lessons = new ArrayList<Lesson>();

            for (Lesson lesson : roomTimetable.Timetable) {
                if (lesson.ds == x)
                    lessons.add(lesson);
            }

            // Suche nach passender Stunde
            int single = lessonSearch.searchLesson(lessons, calendar.get(Calendar.WEEK_OF_YEAR));

            switch (single)
            {
                case 0:
                    values[x-1] = "";
                    break;
                case 1:
                    values[x-1] = lessonSearch.lesson.lessonTag+ " ("+lessonSearch.lesson.type+")";
                    break;
                case 2:
                    values[x-1] = context.getResources().getString(R.string.timetable_moreLessons);
                    break;
            }
        }

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.overview_lessons_list);
        linearLayout.removeAllViews();

        int index = 0;
        for (String lessonD : lessonDS) {

            View sub_view = inflater.inflate(R.layout.fragment_timetable_busy_plan, viewGroup, false);

            // Hintergrund einfärben
            if (index% 2 == 0)
                sub_view.setBackgroundColor(context.getResources().getColor(R.color.whitegrey));
            else
                sub_view.setBackgroundColor(context.getResources().getColor(R.color.white));

            if (index == (current_ds - 1))
                sub_view.setBackgroundColor(context.getResources().getColor(R.color.hellblau));

            // Zeiten anzeigen
            TextView textDS = (TextView) sub_view.findViewById(R.id.timetable_busy_plan_ds);
            textDS.setText(lessonD);

            // Stunde anzeigen
            TextView textLesson = (TextView) sub_view.findViewById(R.id.timetable_busy_plan_lesson);
            textLesson.setText(values[index]);

            // Index erhöhen
            index++;

            // View zum LinearLayout hinzufügen
            linearLayout.addView(sub_view);
        }

        return view;
    }
}
