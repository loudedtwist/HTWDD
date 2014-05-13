package de.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;

import de.htwdd.HTTPDownloader;
import de.htwdd.R;


public class MentoringFragment extends Fragment
{
    public String[] links;
    public String[] titles;
    public String[] location;
    public LinearLayout wait;
    public int mode;
    WebView web;

    public MentoringFragment()
    {
    }

    public MentoringFragment(int i)
    {
        // TODO Auto-generated constructor stub
        mode = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.mentoring, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        web = (WebView) getView().findViewById(R.id.webView1);
        worker w = new worker();
        w.execute(mode);

        wait = (LinearLayout) getView().findViewById(R.id.waitheute);
        wait.setVisibility(View.VISIBLE);
    }


    private class worker extends AsyncTask<Object, Integer, String>
    {
        @Override
        protected String doInBackground(Object... params)
        {
            HTTPDownloader downloader = null;

            try {
                if (mode == 0)
                    downloader = new HTTPDownloader("http://www2.htw-dresden.de/~mumm/mentoring2/mentoren/");
                else if (mode == 1)
                    downloader = new HTTPDownloader("http://www2.htw-dresden.de/~mumm/");
                else if (mode == 2)
                    downloader = new HTTPDownloader("http://www2.htw-dresden.de/~mumm/uber-uns/kontakt/");

                String result = downloader.getStringUTF8();
                result = result.replace("https", "http");
                result = result.substring(result.indexOf("<div id=\"content\">"), result.indexOf("<!-- #content -->"));

                return result;
            } catch (Exception e)
            {
            }

            return  null;
        }

        @Override
        protected void onPostExecute(String html)
        {
            try
            {
                final String mimeType = "text/html";
                final String encoding = "UTF-8";

                String styles = "<html><head>" +
                        " <link rel=\"stylesheet\" href=\"http://www2.htw-dresden.de/~mumm/wp-content/plugins/buddypress/bp-themes/bp-htw/style.css\" type=\"text/css\" media=\"screen\" />" +
                        "<link rel='stylesheet' id='NextGEN-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nextgen-gallery/css/nggallery.css?ver=1.0.0' type='text/css' media='screen' />" +
                        "<link rel='stylesheet' id='shutter-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nextgen-gallery/shutter/shutter-reloaded.css?ver=1.3.4' type='text/css' media='screen' />" +
                        "<link rel='stylesheet' id='jq_ui_css-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/jquery-ui-1.8.13.custom.css?ver=1.8.13' type='text/css' media='all' />" +
                        "<link rel='stylesheet' id='categories-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/cat_colors.css?ver=0.9.9.2' type='text/css' media='all' />" +
                        "<link rel='stylesheet' id='custom-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/custom.css?ver=0.9.9.2' type='text/css' media='all' />" +
                        "<link rel='stylesheet' id='lightboxStyle-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/lightbox-plus/css/shadowed/colorbox.css?ver=2.0.2' type='text/css' media='screen' />" +
                        "<link rel='stylesheet' id='galleryview-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nggGalleryview/galleryview.css?ver=1.0.1' type='text/css' media='screen' />" +
                        "</head><body>";

                wait = (LinearLayout) getView().findViewById(R.id.waitheute);
                wait.setVisibility(View.GONE);

                web.loadDataWithBaseURL("", styles + html + "</body></html>", mimeType, encoding, "");
                web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

            } catch (Exception e)
            {
            }
        }
    }
}