package de.htwdd.classes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwdd.types.Day;
import de.htwdd.types.Meal;

public class Mensa {
    short MensaID = 9;
    public Meal[] Food;

    //Standardkonstruktor
    public Mensa(){
    }

    public Mensa(short _MensaID)
    {
        MensaID=_MensaID;
    }

    // Lade Essen von der Mensa
    public void getDataCurrentDay()
    {
        ArrayList<Meal> food = new ArrayList<Meal>();
        Pattern pattern = Pattern.compile(".*?<item>.*?<title>(.*?)( \\((.*?)\\))?</title>.*?details-(\\d*).html</link>.*?</item>");
        Matcher matcher;

        HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=" + MensaID);
        String result = downloader.getString();

        // Prüfe ob Downloader ein Ergebnis zurückgibt
        if (result == null)
        {
            Food = new Meal[1];
            Food[0] = new Meal();
            Food[0].Title = "Keine Internetverbindung!";
            return;
        }

        matcher = pattern.matcher(result);
        while (matcher.find())
        {
            Meal meal   = new Meal();

            try
            {
                meal.Title  = matcher.group(1);
                meal.Price  = matcher.group(3);
                meal.ID     = Integer.parseInt(matcher.group(4));
            }
            catch (Exception e)
            {
                meal.Title  = "Fehler im Parser";
            }

            food.add(meal);
        }
        Food = food.toArray(new Meal[food.size()]);
    }


    /**
     * Speichert das Essen von dem entsprechendem Tag der übergebenen Woche in der Klassenvariable Food.
     * Es kann dabei nur das Essen von der aktuellen oder der nächste Woche geparst werden
     *
     * @param day Wochentag von welchem das Essen geparst werden soll
     * @param week Kalenderwoche von welcher das Essen geparst werden soll (nur aktuelle oder nächste Woche möglich)
     */
    public void getDataDay(int day, int week)
    {
        HTTPDownloader downloader;
        ArrayList<Meal> arrayList = new ArrayList<Meal>();
        Pattern pattern = Pattern.compile(".*?<td class=\"text\">(.*?)</td>.*?>(\\d?\\d,\\d\\d|ausverkauft| )");

        if (week <= Calendar.getInstance(Locale.GERMANY).get(Calendar.WEEK_OF_YEAR))
            downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html?print=1");
        else
            downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse-w1.html?print=1");

        String result = downloader.getStringUTF8();

        // Prüfe ob Downloader ein Ergebnis zurückgibt
        if (result == null)
        {
            Food = new Meal[1];
            Food[0] = new Meal();
            Food[0].Title = "Keine Internetverbindung!";
            return;
        }

        // Teile Speiseplan in einzelne Tage und übergebe entsprechenden Tag an Matcher
        String token[]  = result.split("class=\"speiseplan\"");
        Matcher matcher = pattern.matcher(token[day-1]);

        // Suche die einzelnen Speisen
        while (matcher.find())
        {
            Meal meal = new Meal();

            try
            {
                meal.Title = matcher.group(1);

                if (!matcher.group(2).equals("ausverkauft"))
                    meal.Price = matcher.group(2)+" €";
                else
                    meal.Price = "aus.";
            }
            catch (Exception e)
            {
                meal.Title = "Fehler im Parser";
            }

            arrayList.add(meal);
        }

        Food = arrayList.toArray(new Meal[arrayList.size()]);
    }

    public void getDataWeek()
    {
        String result;
        String token[];
        Pattern title   = Pattern.compile(".*?<td class=\"text\">(.*?)</td>.*?");
        Matcher matcher;

        try {
            HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html?print=1");
            result  = downloader.getStringUTF8();
            token   = result.split("class=\"speiseplan\"");
            Food    = new Meal[5];

            // Gehe Montag bis Freitag durch
            for (int i = 0; i < 5; i++)
            {
                Food[i] = new Meal();
                Food[i].Price = "";
                Food[i].Title = Day.values()[i].toString();

                // Extrahiere die benötigten Informationen
                try {
                    // Title
                    matcher = title.matcher(token[i+1]);
                    while (matcher.find())
                    {
                        Food[i].Price += matcher.group(1)+"\n\n";
                    }
                }
                catch (Exception e)
                {
                    Food[i].Title = "Fehler im Parser";
                }
            }
        }
        catch (Exception e)
        {
            Food = new Meal[1];
            Food[0] = new Meal();
            Food[0].Title = "Keine Internetverbindung!";
        }
    }

    // Lade Vorschaubilder zu den Mahlzeiten
    public void getThumbnail()
    {
        Calendar rightNow = Calendar.getInstance();

        for (Meal i : Food)
        {
            String url = "http://bilderspeiseplan.studentenwerk-dresden.de/m" + MensaID + "/"+rightNow.get(Calendar.YEAR)+String.format("%02d",rightNow.get(Calendar.MONTH)+1)+"/thumbs/"+i.ID+".jpg";
            HTTPDownloader downloader = new HTTPDownloader(url);
            i.Thumbnail = downloader.getBitmap();
        }
    }
}