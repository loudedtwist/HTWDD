package com.skyworxx.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.R;


public class MensaWocheFragment extends ListFragment
{

    public MensaWocheFragment()
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
        getActivity().findViewById(R.id.progressBar1).setVisibility(View.GONE);
        worker w = new worker();
        w.execute();

    }


    private class worker extends AsyncTask<Object, Integer, String[]>
    {


        @Override
        protected String[] doInBackground(Object... params)
        {
            try
            {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

                HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html");

                //	HTTPDownloader downloader=new HTTPDownloader("http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html");

                String result = downloader.getsafeString();

                result = result.substring(result.indexOf("<table class="));
                result = result.substring(0, result.indexOf("<div id=\"footer\""));

                String[] essen = new String[5];

                essen[0] = result.substring(result.indexOf("Montag"), result.indexOf("Dienstag"));
                essen[1] = result.substring(result.indexOf("Dienstag"), result.indexOf("Mittwoch"));
                essen[2] = result.substring(result.indexOf("Mittwoch"), result.indexOf("Donnerstag"));
                essen[3] = result.substring(result.indexOf("Donnerstag"), result.indexOf("Freitag"));
                essen[4] = result.substring(result.indexOf("Freitag"), result.indexOf("Samstag"));

                String[] tokensMontag = essen[0].split("<td class=\"text\">");
                String[] tokensDienstag = essen[1].split("<td class=\"text\">");
                String[] tokensMittwoch = essen[2].split("<td class=\"text\">");
                String[] tokensDonnerstag = essen[3].split("<td class=\"text\">");
                String[] tokensFreitag = essen[4].split("<td class=\"text\">");

                String montag = "";
                for (int i = 1; i < tokensMontag.length; i++)
                {
                    tokensMontag[i] = tokensMontag[i].substring(tokensMontag[i].indexOf(">") + 1, tokensMontag[i].indexOf("</a>"));
                    if (tokensMontag[i].contains(">"))
                        tokensMontag[i] = tokensMontag[i].substring(tokensMontag[i].lastIndexOf(">") + 2);

                    montag += tokensMontag[i] + System.getProperty("line.separator") + System.getProperty("line.separator");
                }

                String dienstag = "";
                for (int i = 1; i < tokensDienstag.length; i++)
                {
                    tokensDienstag[i] = tokensDienstag[i].substring(tokensDienstag[i].indexOf(">") + 1, tokensDienstag[i].indexOf("</a>"));
                    if (tokensDienstag[i].contains(">"))
                        tokensDienstag[i] = tokensDienstag[i].substring(tokensDienstag[i].lastIndexOf(">") + 2);

                    dienstag += tokensDienstag[i] + System.getProperty("line.separator") + System.getProperty("line.separator");
                }

                String mittwoch = "";
                for (int i = 1; i < tokensMittwoch.length; i++)
                {
                    tokensMittwoch[i] = tokensMittwoch[i].substring(tokensMittwoch[i].indexOf(">") + 1, tokensMittwoch[i].indexOf("</a>"));
                    if (tokensMittwoch[i].contains(">"))
                        tokensMittwoch[i] = tokensMittwoch[i].substring(tokensMittwoch[i].lastIndexOf(">") + 2);

                    mittwoch += tokensMittwoch[i] + System.getProperty("line.separator") + System.getProperty("line.separator");
                }

                String donnerstag = "";
                for (int i = 1; i < tokensDonnerstag.length; i++)
                {
                    tokensDonnerstag[i] = tokensDonnerstag[i].substring(tokensDonnerstag[i].indexOf(">") + 1, tokensDonnerstag[i].indexOf("</a>"));
                    if (tokensDonnerstag[i].contains(">"))
                        tokensDonnerstag[i] = tokensDonnerstag[i].substring(tokensDonnerstag[i].lastIndexOf(">") + 2);
                    donnerstag += tokensDonnerstag[i] + System.getProperty("line.separator") + System.getProperty("line.separator");
                }

                String freitag = "";
                for (int i = 1; i < tokensFreitag.length; i++)
                {
                    tokensFreitag[i] = tokensFreitag[i].substring(tokensFreitag[i].indexOf(">") + 1, tokensFreitag[i].indexOf("</a>"));
                    if (tokensFreitag[i].contains(">"))
                        tokensFreitag[i] = tokensFreitag[i].substring(tokensFreitag[i].lastIndexOf(">") + 2);
                    freitag += tokensFreitag[i] + System.getProperty("line.separator") + System.getProperty("line.separator");
                }

                if (montag.length() < 1) montag = "Kein Angebot an diesem Tag";
                if (dienstag.length() < 1) dienstag = "Kein Angebot an diesem Tag";
                if (mittwoch.length() < 1) mittwoch = "Kein Angebot an diesem Tag";
                if (donnerstag.length() < 1) donnerstag = "Kein Angebot an diesem Tag";
                if (freitag.length() < 1) freitag = "Kein Angebot an diesem Tag";

                essen[0] = montag;
                essen[1] = dienstag;
                essen[2] = mittwoch;
                essen[3] = donnerstag;
                essen[4] = freitag;

                return essen;
            } catch (Exception e)
            {

                return null;

            }
        }

        protected void onProgressUpdate(Integer... progress)
        {


        }


        @Override
        protected void onPostExecute(String[] essen)
        {
            try
            {
                ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

                p.setVisibility(View.GONE);

                ListView l = (ListView) getView().findViewById(android.R.id.list);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                String titles[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};


                MensaWocheArrayAdapter colorAdapter = new MensaWocheArrayAdapter(getActivity(), titles, essen);
                setListAdapter(colorAdapter);
            } catch (Exception e)
            {


            }
        }

    }

}
