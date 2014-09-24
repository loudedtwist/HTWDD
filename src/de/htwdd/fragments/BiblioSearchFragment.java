package de.htwdd.fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import de.htwdd.BiblioSearchArrayAdapter;
import de.htwdd.classes.HTTPDownloader;
import de.htwdd.R;
import de.htwdd.types.TBuchSuche;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BiblioSearchFragment extends Fragment
{
    public SharedPreferences app_preferences;
    public int currentim;
    public static String ids[];
    public static int anzv[];
    public String token;
    public static int globalposition;
    public String mid;
    public String bib;
    public String searchstring;

    public BiblioSearchFragment()
    {

    }

    public static Date parsedate(String in)
    {

        in = in.trim();
        in = in.replaceAll("Jan", "1");
        in = in.replaceAll("Feb", "2");
        in = in.replaceAll("Mär", "3");
        in = in.replaceAll("Apr", "4");
        in = in.replaceAll("Mai", "5");
        in = in.replaceAll("Jun", "6");
        in = in.replaceAll("Jul", "7");
        in = in.replaceAll("Sep", "8");
        in = in.replaceAll("Aug", "9");
        in = in.replaceAll("Okt", "10");
        in = in.replaceAll("Nov", "11");
        in = in.replaceAll("Dez", "12");

        DateFormat formatter;
        Date date = new Date();
        formatter = new SimpleDateFormat("dd M yyyy");
        try
        {
            date = (Date) formatter.parse(in);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.bibliosearchlist, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        bib = app_preferences.getString("bib", "0");
        if ((bib.contains("s")) || (bib.contains("S"))) bib = bib.substring(1);


        Button button = (Button) getActivity().findViewById(R.id.button1);
        EditText edittext = (EditText) getActivity().findViewById(R.id.editText1);
        // edittext.clearFocus();
        // edittext.requestFocus();
         /*
		 edittext.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
		 edittext.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
		 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
		 
		 
		 InputMethodManager imm = (InputMethodManager)
				    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

				if (imm != null){
				    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				}
		 */
        button.setOnClickListener(new MyOnClickListener());

        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
        p.setVisibility(View.GONE);
        ListView l = (ListView) getView().findViewById(R.id.biblioListView);
        l.setVisibility(View.GONE);


    }

    class MyOnClickListener implements OnClickListener
    {

        public void onClick(View v)
        {
            EditText edittext = (EditText) getActivity().findViewById(R.id.editText1);

            ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
            p.setVisibility(View.VISIBLE);
            ListView l = (ListView) getView().findViewById(R.id.biblioListView);
            l.setVisibility(View.GONE);

            worker w = new worker();
            w.execute(edittext.getText().toString());


        }
    }


    private class worker extends AsyncTask<String, Void, TBuchSuche[]>
    {
        @Override
        protected TBuchSuche[] doInBackground(String... params)
        {


            String line2 = "";
            String line = "";
            URL url;
            String line3 = "";
            String line4 = "";

            TBuchSuche[] buecher = null;
            try
            {

                //-------get Token------------------------

                url = new URL("http://bsv2.bib.htw-dresden.de/libero/WebOpac.cls");
                URLConnection conn = (java.net.HttpURLConnection) url.openConnection();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                while (!(line2.contains("</html>")))
                {
                    line = rd.readLine();
                    line2 += line;
                }
                // wr.close();
                rd.close();

                line2 = line2.split("Benutzerfunktionen")[3];
                line2 = line2.split("Mein Konto")[0];
                line2 = line2.substring(line2.indexOf("href=\"") + 6, line2.indexOf("\" title="));
                token = line2.substring(line2.indexOf("TOKEN=") + 6);
                token = token.substring(0, token.indexOf("&amp;"));


                //-------Connect to get books------------


                String data = URLEncoder.encode("MGWCHD", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");

                data += "&" + URLEncoder.encode("TOKEN", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                data += "&" + URLEncoder.encode("TOKENX", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                data += "&" + URLEncoder.encode("DATA", "UTF-8") + "=" + URLEncoder.encode("HTW", "UTF-8");
                data += "&" + URLEncoder.encode("usercode", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
                data += "&" + URLEncoder.encode("VERSION", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8");

                data += "&" + URLEncoder.encode("TERM_1", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

                data += "&" + URLEncoder.encode("USE_1", "UTF-8") + "=" + URLEncoder.encode("ku", "UTF-8");
                data += "&" + URLEncoder.encode("PrefSel", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("LIMLOC", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
                data += "&" + URLEncoder.encode("PSIZE", "UTF-8") + "=" + URLEncoder.encode("200", "UTF-8");
                data += "&" + URLEncoder.encode("YEARFROM", "UTF-8") + "=" + URLEncoder.encode("JJJJ", "UTF-8");
                data += "&" + URLEncoder.encode("YEARTO", "UTF-8") + "=" + URLEncoder.encode("JJJJ", "UTF-8");


                data += "&" + URLEncoder.encode("SimpSearch", "UTF-8") + "=" + URLEncoder.encode("SIMPLESEARCH", "UTF-8");


                data += "&" + URLEncoder.encode("TOKENZ", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

                data += "&" + URLEncoder.encode("ACTION", "UTF-8") + "=" + URLEncoder.encode("SUCHEN", "UTF-8");


                // Send data
                url = new URL("http://bsv2.bib.htw-dresden.de/libero/WebOpac.cls");
                conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the response
                rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));


                while (!(line4.contains("</html>")))
                {
                    line4 = rd.readLine();
                    line3 += line4;
                }
                // wr.close();
                rd.close();


            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (line3.contains("Ihre Suche brachte keine Treffer")) return null;

            String[] rawtokens = line3.split("<!-- Start Display -->");


            buecher = new TBuchSuche[rawtokens.length - 1];

            for (int i = 0; i < rawtokens.length - 1; i++)
            {


                buecher[i] = new TBuchSuche();
                try
                {
                    String title = rawtokens[i + 1].substring(rawtokens[i + 1].indexOf("<img alt=\"") + 10, rawtokens[i + 1].indexOf("\" src='"));
                    buecher[i]._titel = title;
                } catch (Exception e)
                {
                    buecher[i]._titel = "";
                }

                try
                {
                    String picstring = rawtokens[i + 1].substring(rawtokens[i + 1].indexOf("src='") + 5);
                    picstring = picstring.substring(0, picstring.indexOf("'"));

                    HTTPDownloader imagedownloader = new HTTPDownloader(picstring);

                    buecher[i]._pic = imagedownloader.getBitmap();
                } catch (Exception e)
                {
                    buecher[i]._pic = null;
                }

                try
                {
                    String authorstring = rawtokens[i + 1].substring(rawtokens[i + 1].indexOf("<span class='ShortTAut'><a tabindex=" + 36));
                    authorstring = authorstring.substring(authorstring.indexOf(">"), authorstring.indexOf("<"));

                    buecher[i]._verfasser = authorstring;
                } catch (Exception e)
                {
                    buecher[i]._verfasser = "";
                }

                try
                {
                    String yearstring = rawtokens[i + 1].substring(rawtokens[i + 1].indexOf("<span class='ShortTPub'>" + 24));
                    yearstring = yearstring.substring(0, yearstring.indexOf("<"));
                    buecher[i]._year = yearstring;
                } catch (Exception e)
                {
                    buecher[i]._year = "";
                }

                String infostring = rawtokens[i + 1].substring(rawtokens[i + 1].indexOf("<td headers=\"th7\">") + 18);
                infostring = infostring.substring(0, infostring.indexOf("</td>"));
                buecher[i]._info = infostring;

            }


            return buecher;
        }

        @Override
        protected void onPostExecute(TBuchSuche[] buecher)
        {

//				TextView titeltext=  (TextView) findViewById(R.id.titeltext);    	
//		    	titeltext.setText("Ausgeliehene Bücher von "+app_preferences.getString("bib", "0")+":");
//				 


            try
            {
                ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

                p.setVisibility(View.GONE);

                ListView l = (ListView) getView().findViewById(R.id.biblioListView);


                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                String titles[] = new String[buecher.length];

                for (int i = 0; i < buecher.length; i++)
                {

                    titles[i] = buecher[i]._titel;

                }


                BiblioSearchArrayAdapter colorAdapter = new BiblioSearchArrayAdapter(getActivity(), titles, buecher);
                l.setAdapter(colorAdapter);
            } catch (Exception e)
            {


            }


        }
    }


}
