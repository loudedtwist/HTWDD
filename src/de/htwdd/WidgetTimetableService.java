package de.htwdd;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.classes.LessonSearch;
import de.htwdd.fragments.ResponsiveUIActivity;
import de.htwdd.types.Lesson;

public class WidgetTimetableService extends Service
{

    public static final String UPDATE = "update";

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("Service", "Service beendet");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        Log.i("Service", "onStartCommand: update "+appWidgetId);
        updateAppWidget(getApplicationContext(), appWidgetManager, appWidgetId);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_timetable);

        //views.setTextViewText(R.id.overview_lessons_current_remaining, "TEST");
        // Stundenplan Anbindung
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);

        // Typen
        String[] lessonType = context.getResources().getStringArray(R.array.lesson_type);

        // Stunde bestimmen
        Calendar calendar   = GregorianCalendar.getInstance();
        int current_time    = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        int week            = calendar.get(Calendar.WEEK_OF_YEAR);
        int current_ds = 0;

        if (current_time > LessonSearch.lessonEndTimes[7-1])
            current_ds=0;
        else if (current_time >= LessonSearch.lessonStartTimes[6])
            current_ds=7;
        else if (current_time >= LessonSearch.lessonStartTimes[5])
            current_ds=6;
        else if (current_time >= LessonSearch.lessonStartTimes[4])
            current_ds=5;
        else if (current_time >= LessonSearch.lessonStartTimes[3])
            current_ds=4;
        else if (current_time >= LessonSearch.lessonStartTimes[2])
            current_ds=3;
        else if (current_time >= LessonSearch.lessonStartTimes[1])
            current_ds=2;
        else if (current_time >= LessonSearch.lessonStartTimes[0])
            current_ds=1;

        // Aktuell Vorlesungszeit?
        if (current_ds != 0 && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
        {
            ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(week, calendar.get(Calendar.DAY_OF_WEEK)-1,current_ds);

            // Gibt es aktuell eine Stunde?
            if (lessons.size() != 0)
            {
                // Suche nach einer passenden Veranstaltung
                LessonSearch lessonSearch = new LessonSearch();
                int single = lessonSearch.searchLesson(lessons, week);

                // verbeleibende Zeit anzeigen
                int difference = current_time - LessonSearch.lessonEndTimes[current_ds-1];

                if (difference < 0)
                    views.setTextViewText(R.id.overview_lessons_current_remaining, String.format(context.getResources().getString(R.string.overview_lessons_remaining_end), -difference));
                else
                    views.setTextViewText(R.id.overview_lessons_current_remaining, String.format(context.getResources().getString(R.string.overview_lessons_remaining_final), difference));

                // Es gibt keine passende Veranstaltung die angezeigt werden kann
                switch (single)
                {
                    case 0:
                        views.setTextViewText(R.id.overview_lessons_current_tag, "");
                        views.setTextViewText(R.id.overview_lessons_current_remaining, "");
                        break;
                    case 1:
                        views.setTextViewText(R.id.overview_lessons_current_tag, lessonSearch.lesson.lessonTag);
                        views.setTextViewText(R.id.overview_lessons_current_type, lessonType[lessonSearch.lesson.getTypeInt()] + " - " + lessonSearch.lesson.rooms);
                        break;
                    case 2:
                        views.setTextViewText(R.id.overview_lessons_current_tag, context.getResources().getString(R.string.timetable_moreLessons));
                        break;
                }
            }
       }


        // Nächste Stunde suchen
        LessonSearch lessonSearch = new LessonSearch();
        Calendar nextLesson = GregorianCalendar.getInstance();
        int single;
        int ds = current_ds;

        do {
            // DS erhöhen
            if ((++ds)%7==0)
            {
                ds=1;
                nextLesson.add(Calendar.DAY_OF_MONTH,1);
            }

            // Lade Stunde aus DB
            ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(nextLesson.get(Calendar.WEEK_OF_YEAR), nextLesson.get(Calendar.DAY_OF_WEEK)-1,ds);

            // Suche nach passender Stunde
            single=lessonSearch.searchLesson(lessons, nextLesson.get(Calendar.WEEK_OF_YEAR));

            // Suche solange nach einer passenden Stunde bis eine Stunde gefunden wurde. Nach über zwei Tagen wird die Suche abgebrochen
        }while (single==0 && (nextLesson.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR)) < 2);

        if (single!=0)
        {
            // Stunden
            String[] lessonDS = context.getResources().getStringArray(R.array.lesson_ds_timeOnly);

            int difference = nextLesson.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);

            if (difference == 0)
                views.setTextViewText(R.id.overview_lessons_next_remaining, String.format(context.getResources().getString(R.string.overview_lessons_remaining_start), -(current_time - LessonSearch.lessonStartTimes[ds - 1])));
            else if (difference == 1)
                views.setTextViewText(R.id.overview_lessons_next_remaining, context.getResources().getText(R.string.overview_tomorrow) + " " + lessonDS[ds - 1]);
            else
            {
                final String[] nameOfDays = DateFormatSymbols.getInstance().getWeekdays();
                views.setTextViewText(R.id.overview_lessons_next_remaining, nameOfDays[nextLesson.get(Calendar.DAY_OF_WEEK)]+" "+lessonDS[ds-1]);
            }

            // Name + Art anzeigen
            if (single==1)
            {
                views.setTextViewText(R.id.overview_lessons_next_tag, lessonSearch.lesson.lessonTag);

                // Zeige Art an
                views.setTextViewText(R.id.overview_lessons_next_type, lessonType[lessonSearch.lesson.getTypeInt()]+" - "+lessonSearch.lesson.rooms);
            }
            else if (single==2)
                views.setTextViewText(R.id.overview_lessons_next_tag, context.getResources().getText(R.string.timetable_moreLessons));
        }

        // Datenbank schließen
        databaseHandlerTimetable.close();

        // OnClick-Listener zum direkten starten der App
        Intent intent = new Intent(context, ResponsiveUIActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        views.setOnClickPendingIntent(R.id.widget_timetable_appLogo, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
