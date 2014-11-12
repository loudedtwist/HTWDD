package de.htwdd.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDownloader
{
    private String agent = "HTWDresden App";
    public String urlstring;
    public String urlParameters;
    public int ResponseCode;


    public HTTPDownloader(String urlstring)
    {
        this.urlstring = urlstring;
    }

    public String getString()
    {
        return getString("iso-8859-15");
    }

    public String getStringUTF8()
    {
        return getString("UTF-8");
    }

    public String getStringWithPost()
    {
        String tmp;
        StringBuilder result = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(urlstring);

            // create a urlconnection object
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

            // Get the response code
            ResponseCode = conn.getResponseCode();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while ((tmp = rd.readLine()) != null)
                result.append(tmp);

            rd.close();
            conn.disconnect();

        } catch (Exception e)
        {
            ResponseCode = 999;
            return null;
        }

        return result.toString();
    }

    public Bitmap getBitmap()
    {
        try
        {
            // create a url object
            URL url = new URL(urlstring);

            // create a urlconnection object
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.addRequestProperty("User-Agent", agent);
            conn.connect();

            ResponseCode = conn.getResponseCode();

            if (ResponseCode == 200)
            {
                InputStream is = conn.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception e)
        {
            ResponseCode = 999;
        }

        return null;
    }

    private String getString(String Encoding)
    {
        String tmp;
        StringBuilder result = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(urlstring);

            // create a urlconnection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Referer", urlstring);
            conn.addRequestProperty("User-Agent", agent);
            conn.connect();

            ResponseCode = conn.getResponseCode();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Encoding));

            while ((tmp = rd.readLine()) != null)
                result.append(tmp);

            rd.close();
            conn.disconnect();

        } catch (Exception e)
        {
            ResponseCode = 999;
            return null;
        }

        return result.toString();
    }
}