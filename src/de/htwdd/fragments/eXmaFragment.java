package de.htwdd.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.htwdd.EventPopup;
import de.htwdd.R;
import de.htwdd.SpecialAdapter3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class eXmaFragment extends Fragment
{
    public String[] links;
    public String[] titles;
    public String[] location;
    public String[] shortinfo;
    public LinearLayout wait;
    public int dayid;


    public eXmaFragment(int i)
    {
        // TODO Auto-generated constructor stub
        dayid = i;
    }


    public eXmaFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.heute, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        Calendar today = Calendar.getInstance(Locale.GERMANY);
        Calendar nextDay = (Calendar) today.clone();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);

        Calendar nextnextDay = (Calendar) today.clone();
        nextnextDay.add(Calendar.DAY_OF_YEAR, 2);

        worker w = new worker();
        if (dayid == 0) w.execute(today);
        if (dayid == 1) w.execute(nextDay);
        if (dayid == 2) w.execute(nextnextDay);

        wait = (LinearLayout) getView().findViewById(R.id.waitheute);
        wait.setVisibility(View.VISIBLE);

    }


    private class worker extends AsyncTask<Calendar, Void, String>
    {
        @Override
        protected String doInBackground(Calendar... params)
        {


            String line;
            String line2 = "";
            try
            {

                // name="was" value=1

                URL url = new URL(
                        "http://www.exmatrikulationsamt.de/events");

                if (params[0] != null)
                {


                    int month = params[0].get(Calendar.MONTH) + 1;
                    int dom = params[0].get(Calendar.DAY_OF_MONTH);
                    int year = params[0].get(Calendar.YEAR);

                    url = new URL("http://www.exmatrikulationsamt.de/index.php?act=events&d=+" + dom + "&m=+" + month + "&j=" + year);
                }


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

            //	Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG).show();

            try
            {
                //	result= result.substring(result.indexOf("N/W</td></tr>"));
                //	result=result.substring(0, result.indexOf("N/W</td>"));


                String[] events = result.split("<td valign=\"top\"><a class=\"event\"");


                titles = new String[events.length - 1];
                //String[] desc= new String[events.length-1];
                location = new String[events.length - 1];
                links = new String[events.length - 1];
                shortinfo = new String[events.length - 1];


                for (int i = 1; i < events.length; i++)
                {


                    titles[i - 1] = events[i].substring(events[i].indexOf("anzeigen\">") + 10, events[i].indexOf("</a>"));
                    shortinfo[i - 1] = events[i].substring(events[i].indexOf("<i>") + 3, events[i].indexOf("</i>"));
                    links[i - 1] = events[i].substring(events[i].indexOf("href=\"") + 6, events[i].indexOf("\" title"));

                    location[i - 1] = events[i].substring(events[i].indexOf("Locationprofil anzeigen\">") + 25, events[i].indexOf("</a></b></div>"));


                }


                ListView v = (ListView) getView().findViewById(R.id.eventlist);


                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map = new HashMap<String, String>();

//	    		for (int i=0;i<pruefung.length;i++){
//	    			
//	    		if 	(pruefung[i]._art.length()>1){
//	    		map = new HashMap<String, String>();
//	            map.put("titel",	pruefung[i]._titel );
//	            map.put("art",	"Art:\t\t\t\t"+pruefung[i]._art );
//	            map.put("tag","Datum:\t\t"+pruefung[i]._tag );
//	            map.put("zeit","Uhrzeit:\t\t"+pruefung[i]._zeit );
//	            map.put("raum","Raum:\t\t"+pruefung[i]._raum );
//	            mylist.add(map);
//	    		}
//
//	    			

                for (int i = 0; i < titles.length; i++)
                {

                    map = new HashMap<String, String>();
                    map.put("titel", "" + Html.fromHtml(titles[i]));
                    map.put("art", "" + Html.fromHtml(location[i]));
                    map.put("tag", "" + Html.fromHtml(shortinfo[i]));
//	    			map.put("zeit",	eintritt[i] );

                    mylist.add(map);
                }


                SpecialAdapter3 mSchedule = new SpecialAdapter3(getActivity(), mylist, R.layout.notenrow4,
                        new String[]{"titel", "art", "tag"}, new int[]{R.id.pruftitel, R.id.art, R.id.pruftag});

                v.setAdapter(mSchedule);
                v.setDividerHeight(0);

                final int count = v.getCount() - 1;
//	        		
                v.setOnItemClickListener(new OnItemClickListener()
                                         {
                                             public void onItemClick(AdapterView<?> l, View view, int position, long id)
                                             {
                                                 int ID = l.getId();

                                                 //    String strID = new Integer(ID).toString();
                                                 //     Toast.makeText(context, ("ID:" + ID+"  POS:" + id), Toast.LENGTH_SHORT).show();
                                                 Intent nextScreen = new Intent(getActivity().getApplicationContext(), EventPopup.class);

                                                 nextScreen.putExtra("title", titles[position]);
                                                 nextScreen.putExtra("link", links[position]);
                                                 nextScreen.putExtra("location", location[position]);
                                                 nextScreen.putExtra("shortinfo", shortinfo[position]);

                                                 startActivity(nextScreen);

                                             }
                                         }
                );

                wait.setVisibility(View.GONE);


            } catch (Exception e)
            {
                //			 Toast.makeText(Pruefungen.this, e.toString(), Toast.LENGTH_SHORT).show();
            }


        }


    }

}
