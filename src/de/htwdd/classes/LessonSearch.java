package de.htwdd.classes;

import java.util.ArrayList;
import java.util.Arrays;

import de.htwdd.types.Lesson;

public class LessonSearch
{
    public final static int lessonEndTimes[]   = {9*60,10*60+50,12*60+40,14*60+40,16*60+30,18*60+20,20*60};
    public final static int lessonStartTimes[] = {7*60+30,9*60+20,11*60+10,13*60+10,15*60,16*60+50,18*60+30};

    public Lesson lesson=null;


    /**
     *
     * @param lessons Liste der zu überprüfenden Stunden

     * @param week KW in der die zu suchende Veranstaltung stattfindet
     * @return 0= keine passende Stunden gefunden, 1=eine Stunden gefunden, 2=mehrere Stunden gefunden
     */
    public int searchLesson(ArrayList<Lesson> lessons, int week)
    {
        int single=0;

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

        return single;
    }

}
