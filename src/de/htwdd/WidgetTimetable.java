package de.htwdd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetTimetable extends AppWidgetProvider
{
    private PendingIntent pending = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.i("Widget", "onUpdate");

        Intent intent = new Intent(context.getApplicationContext(), WidgetTimetableService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        if (pending == null)
            pending = PendingIntent.getService(context, 0, intent, 0);

        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),1000*60, pending);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);

        Log.i("Widget", "onDeleted");

        // Lösche appWidgetIds aus intent
        ComponentName name = new ComponentName(context, WidgetTimetable.class);
        int [] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);

        Intent intent = new Intent(context.getApplicationContext(), WidgetTimetableService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),1000*60, pending);

        Log.i("Widget", "onDeleted"+ Arrays.toString(ids));
    }

    @Override
    public void onDisabled(Context context)
    {
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        m.cancel(pending);
    }
}

