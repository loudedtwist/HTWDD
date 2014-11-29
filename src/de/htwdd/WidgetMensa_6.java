package de.htwdd;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.htwdd.classes.Mensa;
import de.htwdd.types.Day;
import de.htwdd.types.Meal;


/**
 * Implementation of App Widget functionality.
 */
public class WidgetMensa_6 extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_mensa_6);

        // There may be multiple widgets active, so update all of them
        for (int ID: appWidgetIds)
        {
            //Create a new intent that will target this class
            Intent intent = new Intent(context, WidgetMensa_6.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ID);
            intent.setAction("Update");

            // Create a new PendingIntent which will be run whenever the widget is clicked
            // This PendingIntent will run the intent we just createt before this
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.WidgetMensa, pi);

            // Manually run the AsyncTask to initially
            new MensaWorker().execute(context);

            //Update the Widget
            appWidgetManager.updateAppWidget(ID, remoteViews);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        if (intent.getAction().equals("Update"))
        {
            new MensaWorker().execute(context);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private class MensaWorker extends AsyncTask<Context, Void, Meal[]>
    {
        Context context;
        RemoteViews views;
        Calendar calendar       = Calendar.getInstance(Locale.GERMANY);
        int hour                = calendar.get(Calendar.HOUR_OF_DAY);
        int day                 = calendar.get(Calendar.DAY_OF_WEEK);
        int week                = calendar.get(Calendar.WEEK_OF_YEAR);

        @Override
        protected Meal[] doInBackground(Context... params)
        {
            context = params[0];

            // Am Freitag nach 15 Uhr, Samstag, Sonntag auf Montag springen
            if (day == 7 || day == 1 || (day == 6 && hour >= 15))
            {
                day = 2;
                week++;
            }
            // Nach 15 Uhr das Essen von morgen anzeigen, außer Freitag / Samstag / Sonntag
            else if (hour >= 15)
                day++;

            // Essen laden
            Mensa mensa = new Mensa();
            mensa.getDataDay(day,week);
            return mensa.Food;
        }

        @Override
        protected void onPostExecute(Meal[] essen)
        {
            int resID_Title;
            int resID_Price;
            int count_essen;

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            views               = new RemoteViews(context.getPackageName(),R.layout.widget_mensa_6);
            Resources ressource = context.getResources();
            String packageName  = context.getPackageName();

            // Lösche alle Felder
            for (int i = 1; i < 7; i++)
            {
                views.setTextViewText(ressource.getIdentifier("Food_"+i,"id", packageName), "");
                views.setTextViewText(ressource.getIdentifier("Price_" + i, "id", packageName), "");
            }

            // Anzahl Essen die angezeigt werden
            if (essen.length < 6+1)
                count_essen = essen.length+1;
            else
                count_essen = 6+1;

            // Zeige Essen an
            for (int i = 1; i < count_essen; i++)
            {
                resID_Title = ressource.getIdentifier("Food_"+i,"id", packageName);
                resID_Price = ressource.getIdentifier("Price_" + i, "id", packageName);

                views.setTextViewText(resID_Title, essen[i-1].Title);
                views.setTextViewText(resID_Price, essen[i-1].Price);
            }

            // Tag von dem der Speiseplan angezeigt wird
            views.setTextViewText(R.id.Menu_from, "Speiseplan von " + Day.values()[day-2]);

            // Setze aktuellen Stand
            views.setTextViewText(R.id.Update, "Stand: " + (new SimpleDateFormat("HH:mm")).format(calendar.getTime()) + " Uhr, " + (new SimpleDateFormat("dd.MM.")).format(calendar.getTime()));

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(new ComponentName(context, WidgetMensa_6.class), views);
        }
    }
}