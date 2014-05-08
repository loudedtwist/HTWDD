package de.htwdd;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import de.htwdd.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class EventPopup extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.eventpopup);

        if (getIntent().hasExtra("title")) this.setTitle(getIntent().getStringExtra("title"));

        TextView t1 = (TextView) findViewById(R.id.textView2);
        TextView t2 = (TextView) findViewById(R.id.textView3);

        if (getIntent().hasExtra("location"))
            t1.setText(Html.fromHtml(getIntent().getStringExtra("location")));
        if (getIntent().hasExtra("shortinfo"))
            t2.setText(Html.fromHtml(getIntent().getStringExtra("shortinfo")));


        if (getIntent().hasExtra("link"))
        {


            worker w = new worker();
            w.execute(getIntent().getStringExtra("link"));

        }


        //target="_blank"><img src="

    }

    private class imageworker extends AsyncTask<String, Integer, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... params)
        {
            Bitmap image = null;
            try
            {

                URL url = new URL(params[0]);
                HttpURLConnection conn2 = (HttpURLConnection) url
                        .openConnection();
                conn2.setDoInput(true);

                conn2.connect();
                int code = conn2.getResponseCode();
                if (code != 404)
                {

                    InputStream is = conn2.getInputStream();
                    image = BitmapFactory.decodeStream(is);


                }
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return image;

        }


        @Override
        protected void onPostExecute(Bitmap image)
        {
            ImageView previewThumbnail = (ImageView) findViewById(R.id.essenpreview);
            if (image != null) previewThumbnail.setImageBitmap(image);

        }

    }


    private class worker extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params)
        {


            String line;
            String line2 = "";
            try
            {

                // name="was" value=1


                URL url = new URL(params[0]);
                URLConnection conn = (java.net.HttpURLConnection) url.openConnection();


                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "ISO-8859-1"));

                while (!(line2.contains("</html>")))
                {
                    line = rd.readLine();
                    line2 += line;
                }
                // wr.close();
                rd.close();


            } catch (Exception e)
            {

                return "\n\nDer interne Fehler war: " + e.toString();
            }


            return line2;

        }


        @Override
        protected void onPostExecute(String result)
        {

            String link = result.substring(result.indexOf("<!--einfo-->"));
            link = link.substring(link.indexOf("src=\"") + 5);

            link = link.substring(0, link.indexOf("\""));

            imageworker w = new imageworker();
            w.execute(link);

            result = result.substring(result.indexOf("<!--sinfo-->") + 12, result.indexOf("<!--einfo-->"));

            TextView t = (TextView) findViewById(R.id.textView1);
            WebView web = (WebView) findViewById(R.id.webView1);

            String summary = "<?xml version='1.0' encoding='utf-8'?><html><body>" + result + " </body></html>";
//	           String uri = Uri.encode(summary);
//	           web.loadData(uri, "text/html", "utf-8");

            web.getSettings().setDefaultTextEncodingName("utf-8");
            web.loadDataWithBaseURL(null, summary, "text/html", "utf-8", null);

            web.setBackgroundColor(Color.TRANSPARENT);
            //	t.setText(Html.fromHtml(result));
            t.setText("Diese Informationen wurden von eXmatrikulationsamt.de bereitgestellt.");


        }

    }


}