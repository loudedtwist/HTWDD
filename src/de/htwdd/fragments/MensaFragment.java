package de.htwdd.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import de.htwdd.HTTPDownloader;
import de.htwdd.MensaArrayAdapter;
import de.htwdd.R;
import de.htwdd.types.TEssen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MensaFragment extends ListFragment
{

    int mensa_id = 9;
    public SharedPreferences app_preferences;
    ArrayList mensen;
    public ProgressBar progressbar;

    public MensaFragment(int mensa_id)
    {
        mensen = null;
        this.mensa_id = mensa_id;
    }

    public MensaFragment(ArrayList mensen)
    {
        mensa_id = -1;
        this.mensen = mensen;
    }

    public MensaFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list2, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);

        worker w = new worker();
        w.execute();
    }

    private class worker extends AsyncTask<Calendar, Void, TEssen[]>
    {
        @Override
        protected TEssen[] doInBackground(Calendar... params)
        {
            int progress = 0;
            int index;
            int a = 0;
            String result;

            try {
                HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=" + mensa_id);
                result = downloader.getString();

                String tokens[] = result.split("<title>");

                while (tokens.length < 3 && a <= 4)
                {
                    result = downloader.getString();
                    tokens = result.split("<title>");
                    a++;
                }

                if (tokens.length < 3)
                    return null;

                TEssen[] essen = new TEssen[tokens.length - 2];

                int maxprogressperfood = 100 / essen.length;

                for (int i = 0; i < essen.length; i++)
                {
                    essen[i] = new TEssen();
                    progress += maxprogressperfood;
                    onProgressUpdate(progress);

                    // Bestimme Index wo Titel endet (ohne Preis)
                    index = tokens[i + 2].indexOf("(");
                    if(index == -1)
                        index = tokens[i + 2].indexOf("</title>");

                    // Extrahiere die benötigten Informationen
                    essen[i].setTitle(tokens[i + 2].substring(0, index));
                    essen[i].setSonst(tokens[i + 2].substring(tokens[i + 2].indexOf("<description>") + 13, tokens[i + 2].indexOf("</description>")));
                    essen[i].setID(Integer.parseInt(tokens[i + 2].substring(tokens[i + 2].indexOf("details-") + 8, tokens[i + 2].indexOf(".html"))));

                    Calendar calendar = Calendar.getInstance(Locale.GERMANY);
                    final int month = calendar.get(Calendar.MONTH) + 1;
                    final int year = calendar.get(Calendar.YEAR);

                    String monthstring = String.valueOf(month);
                    if (month < 10) monthstring = "0" + String.valueOf(month);

                    String thumbs = "/";

                    int thumbmode = app_preferences.getInt("thumbnail", 2);

                    if (thumbmode == 1) thumbs = "/thumbs/";

                    Bitmap image;
                    String url =
                            "http://bilderspeiseplan.studentenwerk-dresden.de/m" + mensa_id + "/"
                                    + String.valueOf(year)
                                    + monthstring + thumbs + essen[i].getID()
                                    + ".jpg";

                    if (thumbmode != 0)
                    {
                        HTTPDownloader imagedownloader = new HTTPDownloader(url);

                        essen[i].setBild(imagedownloader.getBitmap(essen[i].getID()));
                    }
                }

                return essen;
            }
            catch (Exception e)
            {
            }

            return  null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            // TODO Auto-generated method stub
            progressbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(TEssen[] essen)
        {
            try
            {
                ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

                p.setVisibility(View.GONE);

                ListView l = (ListView) getView().findViewById(android.R.id.list);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                String titles[];

                if (essen != null)
                    if (essen.length < 1)
                    {
                        essen = new TEssen[1];
                        essen[0] = new TEssen();
                        essen[0].setTitle("Kein Angebot an diesem Tag.");
                        essen[0]._comments = "";
                    }
                else if (essen == null)
                {
                    essen = new TEssen[1];
                    essen[0] = new TEssen();
                    essen[0].setTitle("Kein Angebot an diesem Tag.");
                    essen[0]._comments = "";
                }

                titles = new String[essen.length];

                for (int i = 0; i < titles.length; i++)
                {
                    titles[i] = essen[i].getTitle();
                }

                MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(), titles, essen);
                setListAdapter(colorAdapter);
            } catch (Exception e)
            {

                essen = new TEssen[1];
                essen[0] = new TEssen();
                essen[0].setTitle("Fehler aufgetreten. Versuche es später erneut.");
                essen[0]._comments = "";
                String titles[] = new String[essen.length];

                for (int i = 0; i < titles.length; i++)
                {
                    titles[i] = essen[i].getTitle();
                }

                try
                {
                    MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(), titles, essen);
                    setListAdapter(colorAdapter);
                } catch (Exception e2)
                {
                }
            }
        }
    }
}