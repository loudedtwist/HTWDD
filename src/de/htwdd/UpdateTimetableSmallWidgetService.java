package de.htwdd;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import de.htwdd.R;

import de.htwdd.fragments.ResponsiveUIActivity;
import de.htwdd.types.Type_Stunde;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class UpdateTimetableSmallWidgetService extends Service
{
    @Override
    public void onStart(Intent intent, int startId)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        ComponentName thisWidget = new ComponentName(getApplicationContext(), WidgetTimetableSmallProvider.class);
        int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds)
        {
            // Create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_vorschau);

            // Reset all textfields
            remoteViews.setTextViewText(R.id.TextView01, "");
            remoteViews.setTextViewText(R.id.TextView02, "");
            remoteViews.setTextViewText(R.id.TextView03, "");
            remoteViews.setTextViewText(R.id.TextView04, "");
            remoteViews.setTextViewText(R.id.update, "");
            remoteViews.setTextViewText(R.id.textView2, "");
            remoteViews.setTextViewText(R.id.textView4, "");
            remoteViews.setTextViewText(R.id.textView6, "");
            remoteViews.setViewVisibility(R.id.textView1, View.GONE);

            final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(this.getApplicationContext());

            String daystring    = "";
            String odaystring   = "";
            Calendar calendar   = Calendar.getInstance(Locale.GERMANY);
            int day             = calendar.get(Calendar.DAY_OF_WEEK);
            int week            = calendar.get(Calendar.WEEK_OF_YEAR);
            int hour            = calendar.get(Calendar.HOUR_OF_DAY);
            int min             = calendar.get(Calendar.MINUTE);
            int timeinmin       = hour * 60 + min;
            int aktstunde       = 0;
            int oweek           = week;


            // Bestimme aktuelle Stunde
            if ((timeinmin >= 450) && (timeinmin < 560))
                aktstunde = 1;
            else if ((timeinmin >= 560) && (timeinmin < 670))
                aktstunde = 2;
            else if ((timeinmin >= 670) && (timeinmin < 790))
                aktstunde = 3;
            else if ((timeinmin >= 790) && (timeinmin < 900))
                aktstunde = 4;
            else if ((timeinmin >= 900) && (timeinmin < 1010))
                aktstunde = 5;
            else if ((timeinmin >= 1010) && (timeinmin < 1110))
                aktstunde = 6;
            else if ((timeinmin >= 1110) && (timeinmin < 1200))
                aktstunde = 7;
            else if (timeinmin >= 1200)
                aktstunde = 8;

            // Bestimme aktuelle Woche
            week = week % 2;

            if (week == 0)
                week = 2;

            String currentday = "heute";
            if ((hour > 17) && (day != 1) && (day != 7))
            {
                day++;
                currentday = "morgen";
            }

            switch (day)
            {
                case 1:
                    daystring  = "Montag";
                    odaystring = "Sonntag";
                    break;
                case 2:
                    daystring = odaystring = "Montag";
                    break;
                case 3:
                    daystring = odaystring = "Dienstag";
                    break;
                case 4:
                    daystring = odaystring = "Mittwoch";
                    break;
                case 5:
                    daystring = odaystring = "Donnerstag";
                    break;
                case 6:
                    daystring = odaystring = "Freitag";
                    break;
                case 7:
                    daystring  = "Montag";
                    odaystring = "Samstag";
                    break;
            }

            // Am Wochenende Plan für nächste Woche anzeigen
            if ((day == 1) || (day == 7))
            {
                currentday = "Montag";
                if (week == 1)
                    week = 2;
                else
                    week = 1;
            }

            Type_Stunde aktstundeOB = db.getStunde(odaystring, week, aktstunde);

            // Aktuelle Stunde anzeigen
            if (!aktstundeOB.getName().equals("(leer)"))
            {
                int maxtime = 0;
                switch (aktstunde)
                {
                    case 1:
                        maxtime = 540;
                        break;
                    case 2:
                        maxtime = 650;
                        break;
                    case 3:
                        maxtime = 760;
                        break;
                    case 4:
                        maxtime = 880;
                        break;
                    case 5:
                        maxtime = 990;
                        break;
                    case 6:
                        maxtime = 1100;
                        break;
                    case 7:
                        maxtime = 1200;
                        break;
                }

                String uhrzeit = "";
                switch (aktstundeOB.getStunde())
                {
                    case 1:
                        uhrzeit = "07:30 - 09:00";
                        break;
                    case 2:
                        uhrzeit = "09:20 - 10:50";
                        break;
                    case 3:
                        uhrzeit = "11:10 - 12:40";
                        break;
                    case 4:
                        uhrzeit = "13:10 - 14:40";
                        break;
                    case 5:
                        uhrzeit = "15:00 - 16:30";
                        break;
                    case 6:
                        uhrzeit = "16:50 - 18:20";
                        break;
                    case 7:
                        uhrzeit = "18:30 - 20:00";
                        break;
                }

                int timeleft = maxtime - timeinmin;

                if (currentday.equals("heute"))
                {
                    remoteViews.setTextViewText(R.id.TextView01, uhrzeit);
                    remoteViews.setTextViewText(R.id.TextView03, aktstundeOB.getTyp() +" - "+ aktstundeOB.getRaum());

                    if (timeleft > 0)
                        remoteViews.setTextViewText(R.id.TextView04, "noch " + timeleft + " Minuten");
                    else
                        remoteViews.setTextViewText(R.id.TextView04, "Schluss seit " + -timeleft + " Min");
                }

                remoteViews.setTextViewText(R.id.TextView02, aktstundeOB.getName());
            }

            Type_Stunde upstundeOB;

            int a = 1;
            if (!currentday.equals("heute"))
            {
                aktstunde = 1;
                a = 0;
            }

            do
            {
                upstundeOB = db.getStunde(daystring, week, aktstunde + a);

                a++;
                if ((aktstunde + a > 8))
                    break;
            } while (upstundeOB.getName().equals("(leer)"));

            // Nächste Stunde anzeigen
            if (!upstundeOB.getName().equals("(leer)"))
            {
                remoteViews.setTextViewText(R.id.textView4, upstundeOB.getTyp()+ " - " + upstundeOB.getRaum());

                int mintime = 0;
                switch (upstundeOB.getStunde())
                {
                    case 1:
                        mintime = 450;
                        break;
                    case 2:
                        mintime = 560;
                        break;
                    case 3:
                        mintime = 670;
                        break;
                    case 4:
                        mintime = 790;
                        break;
                    case 5:
                        mintime = 900;
                        break;
                    case 6:
                        mintime = 1010;
                        break;
                    case 7:
                        mintime = 1110;
                        break;
                }

                int timetogo        = mintime - timeinmin;
                int timetogoinhours = timetogo / 60;
                int timetogoinmin   = timetogo - (timetogoinhours * 60);

                if (currentday.equals("heute"))
                {
                    if (timetogo > 120)
                        remoteViews.setTextViewText(R.id.textView6, "in " + timetogoinhours + "h " + timetogoinmin + "min");
                    else
                        remoteViews.setTextViewText(R.id.textView6, "in " + timetogo + " Minuten");

                    remoteViews.setViewVisibility(R.id.TextView02, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.TextView03, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.TextView04, View.VISIBLE);
                }
                else
                {
                    remoteViews.setTextViewText(R.id.TextView01,"Heute keine Vorlesungen mehr.");
                    remoteViews.setTextViewText(R.id.textView1, currentday+ ":");
                    remoteViews.setViewVisibility(R.id.textView1, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.TextView02, View.GONE);
                    remoteViews.setViewVisibility(R.id.TextView03, View.GONE);
                    remoteViews.setViewVisibility(R.id.TextView04, View.GONE);
                }

                String uhrzeit = "";
                switch (upstundeOB.getStunde())
                {
                    case 1:
                        uhrzeit = "7:30 - 9:00";
                        break;
                    case 2:
                        uhrzeit = "9:20 - 10:50";
                        break;
                    case 3:
                        uhrzeit = "11:10 - 12:40";
                        break;
                    case 4:
                        uhrzeit = "13:10 - 14:40";
                        break;
                    case 5:
                        uhrzeit = "15:00 - 16:30";
                        break;
                    case 6:
                        uhrzeit = "16:50 - 18:20";
                        break;
                    case 7:
                        uhrzeit = "18:30 - 20:00";
                        break;
                }
                remoteViews.setTextViewText(R.id.update, uhrzeit);
                remoteViews.setTextViewText(R.id.textView2, upstundeOB.getName());
            }

            Intent intent2 = new Intent(this.getApplicationContext(), ResponsiveUIActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(this.getApplicationContext(), 0, intent2, 0);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button

            remoteViews.setOnClickPendingIntent(R.id.logoimage, pendingIntent2);

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(), WidgetTimetableSmallProvider.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widgetcontainer, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();

        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}