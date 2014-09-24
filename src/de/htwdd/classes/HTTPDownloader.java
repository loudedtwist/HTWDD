package de.htwdd.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HTTPDownloader
{
    private String agent = "HTWDresden App";
    public String urlstring;
    public String urlParameters;


    public HTTPDownloader(String urlstring)
    {
        this.urlstring = urlstring;
    }


    public String getString()
    {
        String line, line2 = "";

        URL url;
        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agent);
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
        URL url;

        try
        {
            url = new URL(urlstring);

            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agent);
            conn.connect();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while ((line = rd.readLine()) != null)
                line2 += line;

            rd.close();

        } catch (Exception e)
        {
            return null;
        }

        return line2;
    }



    public String getStringWithPost()
    {

        String line, line2 = "";
        URL url;

        try
        {
            url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("User-Agent", agent);
            conn.connect();

            //Send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush ();
            wr.close ();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while ((line = rd.readLine()) != null)
                line2 += line;

            rd.close();

        } catch (Exception e)
        {
            return null;
        }

        return line2;
    }

    public Bitmap getBitmap()
    {
        try
        {
            URL myFileUrl   = new URL(urlstring);
            int code;

            HttpURLConnection connection = (HttpURLConnection) myFileUrl.openConnection();

            connection.addRequestProperty("User-Agent", agent);
            connection.connect();

            code = connection.getResponseCode();
            if (code != 404)
            {
                InputStream is = connection.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception e)
        {
        }

        return null;
    }
}