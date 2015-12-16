package de.htwdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.classes.CONST;
import de.htwdd.types.Lesson;

public class VolumeController {
    private AudioManager amanager;
    private Context context;

    public VolumeController(Context context) {
        this.context = context;
        amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getVolumeChangedStatus() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                VolumeControllerService.PREFERENCE_FILE_VOLUME_CTRL,
                Context.MODE_PRIVATE
        );

        return sharedPref.getInt(
                VolumeControllerService.PREFERENCE_MODE,
                VolumeControllerService.PREFERENCE_MODE_CHANGED_SILENT
        );
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

    /**
     * Wenn aktuell eine Vorlesung gibt, wird das Handy stummgeschaltet
     */
    public void turnSoundOff() {
        // Stundenplan Anbindung
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(context);
        // Stunde bestimmen
        Calendar calendar = GregorianCalendar.getInstance();
        int current_time = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int current_ds = CONST.TimetableCalc.getCurrentDS(current_time);

        // Aktuell Vorlesungszeit?, wenn nein return
        if (current_ds == 0 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            return;

        ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(
                week,
                calendar.get(Calendar.DAY_OF_WEEK) - 1,
                current_ds
        );

        // Gibt es aktuell eine Lehrveranstaltung?, wenn nein return
        if (lessons.size() == 0)
            return;

        int mode = amanager.getRingerMode();

        //if we dont changed the audio mode and it is in silent mode,
        //volumeStatus stays in Normal mode, so we don't turn it at the end of class
        if (mode == AudioManager.RINGER_MODE_SILENT)
            return;

        // Setze Audio-Ausgabe auf Silent
        setVolumeChangedStatus(VolumeControllerService.PREFERENCE_MODE_CHANGED_SILENT);

        // Setze Benachrichtungsmodus
        amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /**
     * Schaltet das Handy wieder in den Normalen-Modus
     */
    public void turnSoundOn() {
        //LOAD, DID WE CHANGE VOLUME MODE OR NOT
        int mode = getVolumeChangedStatus();

        //if we didn't change the audio mode, we don't have to change it back to normal
        if (mode == VolumeControllerService.PREFERENCE_MODE_DEFAULT_NORMAL)
            return;

        //control bit set to default value (FROM 1:SILENT MODE TO -> 0: NORMAL MODE).
        setVolumeChangedStatus(VolumeControllerService.PREFERENCE_MODE_DEFAULT_NORMAL);

        // Stellt den normalen Benachrichtungsmodus wieder ein.
        amanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
