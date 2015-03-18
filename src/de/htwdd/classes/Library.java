package de.htwdd.classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwdd.types.Medium;

public class Library
{
    public ArrayList<Medium> search(String term, int Page)
    {
        ArrayList<Medium> arrayList = new ArrayList<Medium>();

        HTTPDownloader downloader = new HTTPDownloader("https://katalog.bib.htw-dresden.de/Search/Results?lookfor="+term+"&type=AllFields&view=rss&page="+Page);
        String result = downloader.getString();

        if (downloader.ResponseCode != 200)
            return null;

        Pattern item = Pattern.compile("<item>.*?Record/(\\d*)</link>.*?<title>(.*?)/?</title>.*?</item>");
        Matcher matcher = item.matcher(result);

        while (matcher.find())
        {
            Medium medium = new Medium();

            medium.ID   = matcher.group(1);
            medium.Title= matcher.group(2);
            arrayList.add(medium);
        }

        return arrayList;
    }

    static public Medium getMedium(String ID)
    {
        Medium medium = new Medium();

        // Lade Mediendetails über Export-Schnittstelle
        HTTPDownloader downloader = new HTTPDownloader("https://katalog.bib.htw-dresden.de/Record/"+ID+"/Export?style=EndNote");
        String result = downloader.getString();

        if (result==null)
            return null;

        // Teile String
        String row[] = result.split("%");
        Integer count = row.length;

        for (int i=1; i<count;i++)
        {
            switch (((int) row[i].charAt(0)))
            {
                // %0 Art des Mediums
                case 48:
                    medium.Medium = row[i].substring(2);
                    break;
                // %A Author
                case 65:
                    medium.Author = row[i].substring(2);
                    break;
                // %L Signatur
                case 76:
                    if (medium.Signatur == null)
                        medium.Signatur = row[i].substring(2);
                    else
                        medium.Signatur += ",\n"+row[i].substring(2);
                    break;
                // %T Titel des Mediums
                case 84:
                    medium.Title = row[i].substring(2);
                    break;
                // %Z Link zum Eintrag
                case 90:
                    medium.Link = row[i].substring(2);
                    break;
            }
        }

        // Lade Verfügbarkeit
        downloader = new HTTPDownloader("https://katalog.bib.htw-dresden.de/AJAX/JSON?method=getItemStatuses&id[]="+ID);
        result = downloader.getString();

        if (result==null)
            return medium;

        try
        {
            // Wandle result in ein JSON-Objekt um
            JSONObject data = new JSONObject(result);

            // Wandle das Feld data in ein JSON-Array
            JSONArray array = data.getJSONArray("data");

            // Vom Array das erste Objekt holen
            JSONObject data2= array.getJSONObject(0);

            // Verfügbarkeit zum Medium hinzufügen
            medium.Availability = data2.getString("availability_message");
        }
        catch (Exception e)
        {
            return medium;
        }

        return medium;
    }
}
