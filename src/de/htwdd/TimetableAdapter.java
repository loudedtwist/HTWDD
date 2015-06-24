package de.htwdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

import de.htwdd.types.Lesson;


public class TimetableAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private final String[] nameOfDays = DateFormatSymbols.getInstance().getShortWeekdays();

    public int week = 1;

    public TimetableAdapter(Context context)
    {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return 56;
    }

    @Override
    public ArrayList<Lesson> getItem(int i)
    {
        int DS  = i/7;
        int Day = i%7;

        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
        ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(week, Day, DS);
        databaseHandlerTimetable.close();
        return lessons;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ArrayList<Lesson> lessons;

        if (view == null)
            view = this.inflater.inflate(R.layout.fragment_timetable_item, viewGroup, false);

        // Standardgröße einer Zeile
        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 180));

        LinearLayout layout     = (LinearLayout) view.findViewById(R.id.TimetableLayout);
        TextView textViewTag    = (TextView) view.findViewById(R.id.TimetableTag);
        TextView textViewType   = (TextView) view.findViewById(R.id.TimetableType);
        TextView textViewRoom   = (TextView) view.findViewById(R.id.TimetableRoom);
        TextView textViewMore   = (TextView) view.findViewById(R.id.TimetableMoreLessons);
        TextView textViewKW   = (TextView) view.findViewById(R.id.TimetableOnlyKW);

        textViewTag.setVisibility(View.GONE);
        textViewRoom.setVisibility(View.GONE);
        textViewMore.setVisibility(View.GONE);
        textViewKW.setVisibility(View.GONE);

        switch (i)
        {
            case 0:
                textViewType.setText(null);
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 50));
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                textViewType.setText(nameOfDays[i+1]);
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 50));
                break;
            case 7:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_1);
                textViewType.setHeight(180);
                break;
            case 14:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_2);
                textViewType.setHeight(180);
                break;
            case 21:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_3);
                textViewType.setHeight(180);
                break;
            case 28:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_4);
                textViewType.setHeight(180);
                break;
            case 35:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_5);
                textViewType.setHeight(180);
                break;
            case 42:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_6);
                textViewType.setHeight(180);
                break;
            case 49:
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                textViewType.setText(R.string.timetable_DS_7);
                textViewType.setHeight(180);
                break;
            default:
                textViewTag.setVisibility(View.VISIBLE);
                textViewRoom.setVisibility(View.VISIBLE);

                Lesson lesson = null;
                lessons = getItem(i);

                // Nur eine Stunde für dieser DS vorhanden
                if (lessons.size() == 1)
                {
                    lesson = lessons.get(0);

                    textViewTag.setText(lesson.lessonTag);
                    textViewRoom.setText(lesson.rooms);

                    if (!lesson.weeksOnly.isEmpty()) {
                        textViewKW.setVisibility(View.VISIBLE);
                    }
                }
                // mehrere Stunden für dieser DS vorhanden
                else if (lessons.size() > 1)
                {
                    int single=0;
                    textViewMore.setVisibility(View.VISIBLE);

                    // Suche nach einer passenden Veranstaltung
                    for (Lesson tmp : lessons)
                    {
                        // Es ist keine spezielle KW gesetzt, d.h. die Veranstaltung ist immer
                        if (tmp.weeksOnly.isEmpty())
                        {
                            single++;

                            if (single==1)
                                lesson = tmp;
                            else
                                // Zweite Veranstallung gefunden, die "immer" ist
                                break;
                        }

                        // Es sind spezielle KW gestzt, suche aktuelle zum anzeigen
                        String[] lessonWeek = tmp.weeksOnly.split(";");

                        // Aktuelle Woche enthalten?
                        if (Arrays.asList(lessonWeek).contains(week+""))
                        {
                            single++;

                            if (single==1)
                                lesson = tmp;
                            else
                                // Zweite Veranstallung gefunden, die "immer" ist
                                break;
                        }
                    }

                    // Es gibt keine passende Veranstaltung die angezeigt werden kann
                    if (single!=1)
                    {
                        textViewRoom.setVisibility(View.GONE);
                        textViewType.setText(R.string.timetable_moreLessons);
                        break;
                    }

                    // Doch eine Veranstalltung gefunden
                    textViewTag.setText(lesson.lessonTag);
                    textViewRoom.setText(lesson.rooms);
                }
                // Keine Stunde in dieser DS
                else
                {
                    view.setBackgroundColor(context.getResources().getColor(R.color.faded_grey));
                    textViewTag.setText(null);
                    textViewType.setText(null);
                    textViewRoom.setText(null);
                    break;
                }

                // Zeige Art an
                String[] lessonType = view.getResources().getStringArray(R.array.lesson_type);

                // Setze Hintergrundfarbe
                switch (lesson.getTypeInt())
                {
                    case 0:
                        layout.setBackgroundColor(context.getResources().getColor(R.color.faded_blue));
                        textViewType.setText(lessonType[0]);
                        break;
                    case 1:
                        layout.setBackgroundColor(context.getResources().getColor(R.color.faded_orange));
                        textViewType.setText(lessonType[1]);
                        break;
                    case 2:
                        layout.setBackgroundColor(context.getResources().getColor(R.color.faded_green));
                        textViewType.setText(lessonType[2]);
                        break;
                    default:
                        layout.setBackgroundColor(context.getResources().getColor(R.color.faded_magenta));
                        textViewType.setText(lessonType[3]);

                }
        }
        return view;
    }
}