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

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                WidgetTimetableSmallProvider.class);
        int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds)
        {
            // Create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.widget_vorschau
            );

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

            final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(
                    this.getApplicationContext());

            String daystring = "";
            String odaystring = "";
            Calendar calendar = Calendar.getInstance(Locale.GERMANY);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            int month = calendar.get(Calendar.MONTH);
            month++;
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            int timeinmin = hour * 60 + min;
            int aktstunde = 0;

            // timeinmin=800;
            // day=3;
            // week=49;
            // hour=11;

            if ((timeinmin >= 450) && (timeinmin < 560))
                aktstunde = 1;
            if ((timeinmin >= 560) && (timeinmin < 670))
                aktstunde = 2;
            if ((timeinmin >= 670) && (timeinmin < 790))
                aktstunde = 3;
            if ((timeinmin >= 790) && (timeinmin < 900))
                aktstunde = 4;
            if ((timeinmin >= 900) && (timeinmin < 1010))
                aktstunde = 5;
            if ((timeinmin >= 1010) && (timeinmin < 1110))
                aktstunde = 6;
            if ((timeinmin >= 1110) && (timeinmin < 1200))
                aktstunde = 7;
            if (timeinmin >= 1200)
                aktstunde = 8;
            // TextView tt2 = (TextView) findViewById(R.id.textView2);
            // TextView tt4 = (TextView) findViewById(R.id.textView4);
            // TextView tt6 = (TextView) findViewById(R.id.textView6);
            // TextView tt8 = (TextView) findViewById(R.id.textView8);
            // TextView tt9 = (TextView) findViewById(R.id.textView9);
            // TextView tt5 = (TextView) findViewById(R.id.textView5);
            // TextView tt7 = (TextView) findViewById(R.id.textView7);
            //
            // TextView tt12 = (TextView) findViewById(R.id.textView12);
            // TextView tt13 = (TextView) findViewById(R.id.textView13);
            //
            // TextView tttoday = (TextView) findViewById(R.id.textviewToday);

            int oweek = week;

            week = week % 2;

            if (week == 0)
                week = 2;

            String currentday = "heute";
            if ((hour > 17) && (day != 1) && (day != 7))
            {
                day++;
                currentday = "morgen";

            }

            if (day == 2)
            {
                odaystring = "Montag";
            }
            else if (day == 3)
            {
                odaystring = "Dienstag";
            }
            else if (day == 4)
            {
                odaystring = "Mittwoch";
            }
            else if (day == 5)
            {
                odaystring = "Donnerstag";
            }
            else if (day == 6)
            {
                odaystring = "Freitag";
            }
            else if (day == 7)
            {
                odaystring = "Samstag";
            }
            else if (day == 1)
            {
                odaystring = "Sonntag";
            }

            if ((day == 1) || (day == 7))
            {
                day = 2;

                currentday = "Montag";
                if (week == 1)
                    week = 2;
                else
                    week = 1;
            }

            if (day == 2)
            {
                daystring = "Montag";
            }
            else if (day == 3)
            {
                daystring = "Dienstag";
            }
            else if (day == 4)
            {
                daystring = "Mittwoch";
            }
            else if (day == 5)
            {
                daystring = "Donnerstag";
            }
            else if (day == 6)
            {
                daystring = "Freitag";
            }
            else if (day == 7)
            {
                daystring = "Samstag";
            }
            else if (day == 1)
            {
                daystring = "Sonntag";
            }

            Type_Stunde aktstundeOB = db.getStunde(odaystring, week, aktstunde);

            if (!aktstundeOB.getName().equals("(leer)"))
            {
                if (currentday.equals("heute"))
                    remoteViews.setTextViewText(
                            R.id.TextView03,
                            aktstundeOB.getTyp() + " - "
                                    + aktstundeOB.getRaum()
                    );

                // tt8.setText(aktstundeOB.getTyp()+" - "+aktstundeOB.getRaum());

                int maxtime = 0;

                if (aktstunde == 1)
                    maxtime = 540;
                if (aktstunde == 2)
                    maxtime = 650;
                if (aktstunde == 3)
                    maxtime = 760;
                if (aktstunde == 4)
                    maxtime = 880;
                if (aktstunde == 5)
                    maxtime = 990;
                if (aktstunde == 6)
                    maxtime = 1100;
                if (aktstunde == 7)
                    maxtime = 1200;

                int timeleft = maxtime - timeinmin;

                if (currentday.equals("heute"))
                    if (timeleft > 0)
                        remoteViews.setTextViewText(R.id.TextView04, "noch "
                                + timeleft + " Minuten");
                        // tt5.setText("noch "+timeleft+" Minuten");
                    else
                        remoteViews.setTextViewText(R.id.TextView04,
                                "Schluss seit " + -timeleft + " Min");
                // tt5.setText("Schluss seit "+-timeleft+" Min");

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
                if (currentday == "heute")
                    remoteViews.setTextViewText(R.id.TextView01, uhrzeit);
                // tt12.setText(uhrzeit);

                remoteViews.setTextViewText(R.id.TextView02,
                        aktstundeOB.getName());
                // tt4.setText(aktstundeOB.getName());
            }

            Type_Stunde upstundeOB = new Type_Stunde();
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

            if (!upstundeOB.getName().equals("(leer)"))
            {
                remoteViews.setTextViewText(R.id.textView4, upstundeOB.getTyp()
                        + " - " + upstundeOB.getRaum());
                // tt9.setText(upstundeOB.getTyp()+" - "+upstundeOB.getRaum());

                int mintime = 0;
                if (upstundeOB.getStunde() == 1)
                    mintime = 450;
                if (upstundeOB.getStunde() == 2)
                    mintime = 560;
                if (upstundeOB.getStunde() == 3)
                    mintime = 670;
                if (upstundeOB.getStunde() == 4)
                    mintime = 790;
                if (upstundeOB.getStunde() == 5)
                    mintime = 900;
                if (upstundeOB.getStunde() == 6)
                    mintime = 1010;
                if (upstundeOB.getStunde() == 7)
                    mintime = 1110;

                int timetogo = mintime - timeinmin;

                int timetogoinhours = timetogo / 60;
                int timetogoinmin = timetogo - (timetogoinhours * 60);

                if (currentday.equals("heute"))
                {
                    if (timetogo > 120)
                        remoteViews.setTextViewText(R.id.textView6, "in " + timetogoinhours + "h " + timetogoinmin + "min");
                    else
                        remoteViews.setTextViewText(R.id.textView6, "in " + timetogo + " Minuten");
                    remoteViews
                            .setViewVisibility(R.id.TextView02, View.VISIBLE);
                    remoteViews
                            .setViewVisibility(R.id.TextView03, View.VISIBLE);
                    remoteViews
                            .setViewVisibility(R.id.TextView04, View.VISIBLE);

                }
                // tt7.setText("in "+timetogo+" Minuten");
                else
                {
                    remoteViews.setTextViewText(R.id.TextView01,
                            "Heute keine Vorlesungen mehr.");
                    remoteViews.setTextViewText(R.id.textView1, currentday
                            + ":");
                    remoteViews.setViewVisibility(R.id.textView1, View.VISIBLE);

                    remoteViews.setViewVisibility(R.id.TextView02, View.GONE);
                    remoteViews.setViewVisibility(R.id.TextView03, View.GONE);
                    remoteViews.setViewVisibility(R.id.TextView04, View.GONE);

                }
                // tt7.setText("Montag");

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
                // tt13.setText(uhrzeit);
                remoteViews.setTextViewText(R.id.textView2,
                        upstundeOB.getName());
                // tt6.setText(upstundeOB.getName());

            }

            Intent intent2 = new Intent(this.getApplicationContext(),
                    ResponsiveUIActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(
                    this.getApplicationContext(), 0, intent2, 0);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button

            remoteViews.setOnClickPendingIntent(R.id.logoimage, pendingIntent2);

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    WidgetTimetableSmallProvider.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widgetcontainer,
                    pendingIntent);
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