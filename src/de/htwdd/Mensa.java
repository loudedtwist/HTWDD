package de.htwdd;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwdd.types.Day;
import de.htwdd.types.TEssen;

public class Mensa {
    short MensaID = 9;
    public TEssen[] Food;

    //Standardkonstruktor
    public Mensa(){
    }

    public Mensa(short _MensaID)
    {
        MensaID=_MensaID;
    }

    // Lade Essen von der Mensa
    public void getDataCurrentDay(){
        String result;
        String token[];
        Pattern ID      = Pattern.compile(".*?details-(\\d*).html.*?");
        Pattern title   = Pattern.compile(".*?<title>(.*?)( \\((.*?)\\))?</title>.*?");
        Matcher matcher;

        try {
            HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=" + MensaID);
            result          = downloader.getString();
            token           = result.split("<item>");
            int AnzToken    = token.length;
            Food   = new TEssen[token.length-2];

            //Speichere einzelne Mahlzeiten in das Array
            for (int i = 2; i < AnzToken; i++)
            {
                Food[i-2]   = new TEssen();

                // Extrahiere die benötigten Informationen
                try {
                    //ID
                    matcher = ID.matcher(token[i]);
                    matcher.find();
                    Food[i-2].ID = Integer.parseInt(matcher.group(1));

                    //Title
                    matcher = title.matcher(token[i]);
                    matcher.find();
                    Food[i-2].Title = matcher.group(1);

                    //Preis
                    Food[i-2].Price = matcher.group(3);
                }
                catch (Exception e)
                {
                    Food[i-2].Title = "Fehler im Parser";
                }
            }
        }
        catch (Exception e)
        {
            Food = new TEssen[1];
            Food[0] = new TEssen();
            Food[0].Title = "Keine Internetverbindung!";
        }
    }

    /*
    Widget: Lädt die Mahlzeiten des übergebenen Tages herrunter
     */
    public void getDataDay(int day)
    {
        HTTPDownloader downloader;
        String result;
        String token[];
        Pattern title   = Pattern.compile(".*?<td class=\"text\">(.*?)</td>");
        Pattern price   = Pattern.compile(">(\\d?\\d,\\d\\d|ausverkauft| )");
        Matcher matcher;
        int AnzToken;


        try {

            if (day >= Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html?print=1");
            else
                downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse-w1.html?print=1");

            result      = downloader.getStringUTF8();
            token       = result.split("class=\"speiseplan\"");

            // Zähle Mahlzeiten die es an dem entsprechenden Tag gibt und lege Array an
            token       = token[day-1].split("<tr class=");
            AnzToken    = token.length-1;
            Food        = new TEssen[AnzToken];

            // Speichere einzelne Mahlzeiten in das Array
            for (int i = 1; i < AnzToken; i++)
            {
                Food[i-1] = new TEssen();

                try {
                    //Title
                    matcher = title.matcher(token[i]);
                    matcher.find();
                    Food[i-1].Title = matcher.group(1);

                    //Preis
                    matcher = price.matcher(token[i]);
                    matcher.find();
                    if (!matcher.group(1).equals("ausverkauft"))
                        Food[i-1].Price = matcher.group(1)+" €";
                    else
                        Food[i-1].Price = "ausverk";
                }
                catch (Exception e)
                {
                    Food[i-1].Title = "Fehler im Parser";
                }
            }
        }
        catch (Exception e)
        {
            Food = new TEssen[1];
            Food[0] = new TEssen();
            Food[0].Title = "Keine Internetverbindung!";
        }
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
            Food    = new TEssen[5];

            // Gehe Montag bis Freitag durch
            for (int i = 0; i < 5; i++)
            {
                Food[i] = new TEssen();
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
            Food = new TEssen[1];
            Food[0] = new TEssen();
            Food[0].Title = "Keine Internetverbindung!";
        }
    }

    // Lade Vorschaubilder zu den Mahlzeiten
    public void getThumbnail()
    {
        Calendar rightNow = Calendar.getInstance();

        for (TEssen i : Food)
        {
            String url = "http://bilderspeiseplan.studentenwerk-dresden.de/m" + MensaID + "/"+rightNow.get(Calendar.YEAR)+String.format("%02d",rightNow.get(Calendar.MONTH)+1)+"/thumbs/"+i.ID+".jpg";
            HTTPDownloader downloader = new HTTPDownloader(url);
            i.Thumbnail = downloader.getBitmap();
        }
    }

}
