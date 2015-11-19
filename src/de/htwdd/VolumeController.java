package de.htwdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.classes.CONST;
import de.htwdd.types.Lesson;

public class VolumeController {
    private AudioManager amanager;
    private Context context;


    public VolumeController(Context context){
        this.context=context;
        amanager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getVolumeChangedStatus() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                VolumeControllerService.PREFERENCE_FILE_VOLUME_CTRL,
                Context.MODE_PRIVATE
        );

        int mode = sharedPref.getInt(
                VolumeControllerService.PREFERENCE_MODE,
                VolumeControllerService.PREFERENCE_MODE_CHANGED_SILENT
        );

        return mode;
    }

    public void setVolumeChangedStatus(final int volumeMode) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                VolumeControllerService.PREFERENCE_FILE_VOLUME_CTRL,
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();

        //set control "bit" as false -> sound was turned off by our app.
        // We don't wont to  turn sound on , if it was not changed by us
        sharedPrefEditor.putInt(
                VolumeControllerService.PREFERENCE_MODE,
                volumeMode
        );
        sharedPrefEditor.apply();
    }

    public void turnSoundOff(){
        // Stundenplan Anbindung
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
        // Stunde bestimmen
        Calendar calendar   = GregorianCalendar.getInstance();
        int current_time    = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);//in minutes
        int week            = calendar.get(Calendar.WEEK_OF_YEAR);
        int current_ds = 0;

        current_ds= CONST.TimetableCalc.getCurrentDS(current_time);

        // Aktuell Vorlesungszeit?
        if (current_ds == 0 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Log.i("FROM turnSoundOff ", "Aktuell keine Vorlesungszeit");
            return;
        }

        ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(
                week,
                calendar.get(Calendar.DAY_OF_WEEK) - 1,
                current_ds
        );

        // Gibt es aktuell eine Lehrveranstaltung?
        if (lessons.size() == 0) {
            Log.i("FROM turnSoundOff ", "Es gibt aktuell keine Lehrveranstaltung");
            return;
        }

        int mode = amanager.getRingerMode();
        Log.i("CURRENT MODE", " " + mode + ": " + AudioManager.RINGER_MODE_SILENT + " - silent: (nothing to do , exit funktion) , 2 - normal: change to silent");
        //if we dont changed the audio mode and it is in silent mode,
        //volumeStatus stays in Normal mode, so we don't turn it at the end of class
        if (mode == AudioManager.RINGER_MODE_SILENT) return;
        //SAVE, DID WE CHANGE VOLUME MODE OR NOT
        setVolumeChangedStatus(VolumeControllerService.PREFERENCE_MODE_CHANGED_SILENT);
        Log.i("FROM turnSoundOff", "DID: ON -> OFF: silence mode");
        amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

    }

    public void turnSoundOn() {
        //LOAD, DID WE CHANGE VOLUME MODE OR NOT
        int mode = getVolumeChangedStatus();
        Log.i("EDITED BY US?", Integer.toString(mode));
        //if we didn't change the audio mode, we don't have to change it back to normal
        if(mode == VolumeControllerService.PREFERENCE_MODE_DEFAULT_NORMAL) return;
        //control bit set to default value (FROM 1:SILENT MODE TO -> 0: NORMAL MODE).
        setVolumeChangedStatus(VolumeControllerService.PREFERENCE_MODE_DEFAULT_NORMAL);
        Log.i("FROM turnSoundOn", "DID: OFF -> ON: IN NORMAL MODE");
        amanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }


}
