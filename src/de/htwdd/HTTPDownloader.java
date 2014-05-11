package de.htwdd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HTTPDownloader
{

    public String urlstring;

    public String[] agents = {"Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.21pre) Gecko K-Meleon/1.7.0",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; Avant Browser; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)",
            "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
            "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; en-US)",
            "Mozilla/5.0 (Windows NT 6.1; rv:6.0) Gecko/20110814 Firefox/6.0",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.3) Gecko/20100401 Mozilla/5.0 (X11; U; Linux i686; it-IT; rv:1.9.0.2) Gecko/2008092313 Ubuntu/9.25 (jaunty) Firefox/3.8",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; tr-TR) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27",
            "Mozilla/5.0 (Windows NT 6.2; rv:9.0.1) Gecko/20100101 Firefox/9.0.1", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/18.6.872.0 Safari/535.2 UNTRUSTED/1.0 3gpp-gba UNTRUSTED/1.0"};


    public HTTPDownloader(String urlstring)
    {
        this.urlstring = urlstring;
    }


    public String getString()
    {
        String line, line2 = "";
        int rnd = new Random().nextInt(agents.length);

        URL url;
        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agents[rnd]);
            conn.connect();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-15"));

            while ((line = rd.readLine()) != null)
                line2 += line;

            rd.close();

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return (e.toString());
        }

        return line2;
    }

    public String getStringUTF8()
    {
        String line, line2 = "";
        int rnd = new Random().nextInt(agents.length);
        URL url;

        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agents[rnd]);
            conn.connect();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while ((line = rd.readLine()) != null)
                line2 += line;

            rd.close();

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return (e.toString());
        }

        return line2;
    }

    public List<String> getCSV()
    {
        List<String> list   = new ArrayList<String>();
        int          rnd    = new Random().nextInt(agents.length);
        String       line   = "";
        URL          url;

        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agents[rnd]);
            conn.connect();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-15"));

            while ((line = rd.readLine()) != null)
                list.add(line);

            rd.close();

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return list;
        }

        return list;
    }

    public String getsafeString()
    {
        String  line, line2 = "";
        int     rnd = new Random().nextInt(agents.length);
        URL     url;

        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agents[rnd]);
            conn.connect();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = rd.readLine()) != null)
                line2 += line;

            rd.close();

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return (e.toString());
        }

        return line2;
    }

    public Bitmap getNormalBitmap()
    {
        try
        {
            int code;
            URL myFileUrl = new URL(urlstring);

            HttpURLConnection conn2 = (HttpURLConnection) myFileUrl.openConnection();

            conn2.connect();

            code = conn2.getResponseCode();
            if (code != 404)
            {
                InputStream is = conn2.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception t)
        {
        }

        return null;
    }

    public Bitmap getBitmap(int sid)
    {
        try
        {
            URL myFileUrl   = new URL(urlstring);
            int rnd         = new Random().nextInt(agents.length);
            int code;

            HttpURLConnection conn2 = (HttpURLConnection) myFileUrl.openConnection();

            conn2.setDoInput(true);
            conn2.addRequestProperty("Referer", "http://www.studentenwerk-dresden.de/mensen/speiseplan/details-" + sid + ".html");
            conn2.addRequestProperty("User-Agent", agents[rnd]);
            conn2.connect();

            code = conn2.getResponseCode();
            if (code != 404)
            {
                InputStream is = conn2.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception t)
        {
        }

        return null;
    }
}