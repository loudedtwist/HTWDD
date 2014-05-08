package de.htwdd.fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import de.htwdd.BelegungsOverviewAdapter;
import de.htwdd.DatabaseHandlerRoomTimetable;
import de.htwdd.R;
import de.htwdd.types.Type_Stunde;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;


public class BelegungsFragment extends Fragment
{
    String raum;

    public BelegungsFragment(String raum)
    {
        // TODO Auto-generated constructor stub
        this.raum = raum;
    }

    public BelegungsFragment()
    {
        this.raum = "0";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.belegungs, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        DatabaseHandlerRoomTimetable db = new DatabaseHandlerRoomTimetable(getActivity());
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!raum.equals("0"))
        {
            int count = db.getRaumCount(raum);

            if (count == 0)
            {
                worker w = new worker();
                w.execute(db, app_preferences, raum);
            }
            else
            {
                try
                {
                    ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
                    TextView t = (TextView) getView().findViewById(R.id.textView2);
                    t.setVisibility(View.GONE);
                    p.setVisibility(View.GONE);

//   			List<Type_Stunde> list2=db.getStundenTag("Montag", 1, raum);
//   			
//   			Type_Stunde[] stunden= new Type_Stunde[7];
//   			
//   			
//   			for (int a=0;a<stunden.length;a++)
//   				stunden[a]=new Type_Stunde();
//   			
//   			for (int a=1;a<=stunden.length;a++)
//   				for (int i=0;i<list2.size();i++)
//   					if (a==list2.get(i).getStunde())
//   						stunden[a-1]=list2.get(i);
//   			
//   			ListView l= (ListView) getView().findViewById(R.id.belegungslist);
//   			l.setVisibility(View.VISIBLE);
//   			l.setDividerHeight(0);
//   			
//   			 String titles[]={"7:30","9:20","11:10","13:10","15:00","16:50","18:30"};
//   			
//   		if (stunden.length>0){
//   				BelegungsAdapter colorAdapter = new BelegungsAdapter(getActivity(),titles, stunden);
//   				l.setAdapter(colorAdapter);
//   		}
                } catch (Exception e)
                {

                    int a = 0;

                }
            }
        }
        else
        {
            ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

            p.setVisibility(View.GONE);
            TextView t = (TextView) getView().findViewById(R.id.textView2);
            t.setVisibility(View.GONE);
            ListView l = (ListView) getView().findViewById(R.id.belegungslist);
            l.setVisibility(View.VISIBLE);
            l.setDividerHeight(0);

            List<Type_Stunde> list2 = db.getDiffRaum();

            if (list2.size() > 0)
            {

                final String[] titles = new String[list2.size()];

                for (int i = 0; i < titles.length; i++)
                {
                    titles[i] = list2.get(i).getRaum();
                }

                // final String titles[]={"7:30","9:20","11:10","13:10","15:00","16:50","18:30"};

                BelegungsOverviewAdapter colorAdapter = new BelegungsOverviewAdapter(getActivity(), titles);
                l.setAdapter(colorAdapter);

                l.setOnItemClickListener(new OnItemClickListener()
                                         {


                                             @Override
                                             public void onItemClick(AdapterView<?> arg0, View arg1,
                                                                     int arg2, long arg3)
                                             {
                                                 // TODO Auto-generated method stub

                                                 Toast.makeText(getActivity(), titles[arg2], Toast.LENGTH_LONG).show();

                                                 ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                                                 ra.showRaum(titles[arg2]);
                                             }
                                         }
                );


            }
            else
                Toast.makeText(getActivity(), "Füge Räume hinzu indem Du auf das + Symbol drückst", Toast.LENGTH_LONG).show();
        }
    }


    public class worker extends AsyncTask<Object, Void, String>
    {
        @Override
        protected String doInBackground(Object... params)
        {
            DatabaseHandlerRoomTimetable db = (DatabaseHandlerRoomTimetable) params[0];
            SharedPreferences app_preferences = (SharedPreferences) params[1];

            String line2;
            // Construct data
            try
            {
//						String data = URLEncoder.encode("matr", "UTF-8")
//								+ "="
//								+ URLEncoder.encode(
//										app_preferences.getString("matnr", "0"),
//										"UTF-8");


                String data = URLEncoder.encode("matr", "UTF-8")
                        + "="
                        + URLEncoder.encode((String) params[2]);

                // Send data
                URL url = new URL("http://www2.htw-dresden.de/~rawa/cgi-bin/auf/raiplan.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line;
                line2 = "";
                while ((line = rd.readLine()) != null)
                {
                    line2 += line;
                }
                // wr.close();
                rd.close();

                String tokens[] = line2.split("Stundenplan");
                line2 = tokens[2];

            } catch (Exception e)
            {
                return "Netzwerkfehler: " + e.toString() + " - Keine Internetverbindung?";
            }

            if (line2.length() > 100)
            {
                List<Type_Stunde> stunden = db.getAllStunden();

//						for (Type_Stunde cn : stunden) {
//							String log = "Deleting: " + cn.getID() + " ,Name: "
//									+ cn.getName();
//							// Writing Stunden to log
//							Log.d("Deleting: ", log);
//
//							db.deleteStunde(cn);
//
//						}

                // add completely empty timetable

                for (int w = 1; w <= 2; w++)
                {
                    for (int i = 1; i <= 7; i++)
                    {
                        Log.d("Deleting: ", "empty");
                        db.addContact(new Type_Stunde(w, "Montag", i, "(leer)", "", ""));
                        db.addContact(new Type_Stunde(w, "Dienstag", i, "(leer)", "", ""));
                        db.addContact(new Type_Stunde(w, "Mittwoch", i, "(leer)", "", ""));
                        db.addContact(new Type_Stunde(w, "Donnerstag", i, "(leer)", "",
                                ""));
                        db.addContact(new Type_Stunde(w, "Freitag", i, "(leer)", "", ""));
                    }
                }
            }


            //
            // String tokens2[] =
            // line2.split("Nur die Pflichtfächer und die mit Häkchen");

            String zeiten[] = line2.split("</td>");

            String resultstring = "success";

            for (int i = 0; i < zeiten.length; i++)
            {
                if ((i > 8) && (i != 14) && (i != 20) && (i != 26)
                        && (i != 32) && (i != 38) && (i != 44) && (i != 50)
                        && (i != 51) && (i != 52) && (i != 53) && (i != 54)
                        && (i != 55) && (i != 56) && (i != 62)
                        && (i != 68) && (i != 74) && (i != 80) && (i != 86) && (i != 92)
                        && (i < 98))
                {
                    String kname;
                    kname = zeiten[i].substring(10);
                    if (kname.length() > 0 && !kname.contains("Gremien"))
                    {
                        String ktyp = "";
                        int Stunde = 0;
                        String kname2 = "";
                        String kraum = "";
                        int Woche = 0;
                        String Tag = "";

                        try
                        {
                            ktyp = kname.substring(kname.indexOf(" ") + 1,
                                    kname.indexOf("<br>") - 1);

                            if (ktyp.contains("V"))
                                ktyp = "Vorlesung";
                            else if (ktyp.contains("Pr"))
                                ktyp = "Praktikum";
                            else
                                ktyp = "Übung";


                            kname2 = kname.substring(0, kname.indexOf(" "));

                            kraum = kname.substring(kname.indexOf("<br>") + 4,
                                    kname.indexOf(" - "));


                            if (i < 51)
                                Woche = 1;
                            if (i > 56)
                                Woche = 2;
                        } catch (Exception e)
                        {
                            resultstring = "Problem beim Erkennen der Stunden(namen), Arten oder Räume. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Bei fehlenden Stunden oder falschen Daten können diese durch Antippen angepasst werden.";
                        }

                        try
                        {
                            if ((i > 8 && i < 14) || (i > 56 && i < 62))
                                Stunde = 1;
                            if ((i > 14 && i < 20) || (i > 62 && i < 68))
                                Stunde = 2;
                            if ((i > 20 && i < 26) || (i > 68 && i < 74))
                                Stunde = 3;
                            if ((i > 26 && i < 32) || (i > 74 && i < 80))
                                Stunde = 4;
                            if ((i > 32 && i < 38) || (i > 80 && i < 86))
                                Stunde = 5;
                            if ((i > 38 && i < 44) || (i > 86 && i < 92))
                                Stunde = 6;
                            if ((i > 44 && i <= 50) || (i > 92 && i <= 97))
                                Stunde = 7;

                            if ((i == 9) || (i == 15) || (i == 21) || (i == 27)
                                    || (i == 33) || (i == 39) || (i == 45)
                                    || (i == 57) || (i == 63) || (i == 69)
                                    || (i == 75) || (i == 81) || (i == 87) || (i == 93))
                                Tag = "Montag";
                            if ((i == 10) || (i == 16) || (i == 22) || (i == 28)
                                    || (i == 34) || (i == 40) || (i == 46)
                                    || (i == 58) || (i == 64) || (i == 70)
                                    || (i == 76) || (i == 82) || (i == 88) || (i == 94))
                                Tag = "Dienstag";
                            if ((i == 11) || (i == 17) || (i == 23) || (i == 29)
                                    || (i == 35) || (i == 41) || (i == 47)
                                    || (i == 59) || (i == 65) || (i == 71)
                                    || (i == 77) || (i == 83) || (i == 89) || (i == 95))
                                Tag = "Mittwoch";
                            if ((i == 12) || (i == 18) || (i == 24)
                                    || (i == 30) || (i == 36) || (i == 42)
                                    || (i == 48) || (i == 60) || (i == 66)
                                    || (i == 72) || (i == 78) || (i == 84)
                                    || (i == 90) || (i == 96))
                                Tag = "Donnerstag";
                            if ((i == 13) || (i == 19) || (i == 25)
                                    || (i == 31) || (i == 37) || (i == 43)
                                    || (i == 49) || (i == 61) || (i == 67)
                                    || (i == 73) || (i == 79) || (i == 85)
                                    || (i == 91) || (i == 97))
                                Tag = "Freitag";
                        } catch (Exception e)
                        {
                            resultstring = "Problem beim Erkennen der Stunde/Wochentag. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Bei fehlenden Stunden oder falschen Daten können diese durch Antippen angepasst werden.";
                        }

//                        Log.d("Insert: ", "Inserting " + kname2 + " " + Woche + " " + Tag + " " + Stunde);
//									db.addContact(new Type_Stunde(Woche, Tag, Stunde,
//											kname2, ktyp, kraum));
                        db.overwriteStunde(new Type_Stunde(Woche, Tag, Stunde, kname2, ktyp, kraum));
                    }
                }
            }

            return resultstring;
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                if (result.equals("success"))
                {
                    Toast.makeText(getActivity(),
                            "Belegungsplan neu eingelesen!", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    Toast.makeText(getActivity(),
                            "Aktualisieres des Belegungsplans nicht möglich.", Toast.LENGTH_LONG)
                            .show();
                }

                if (getActivity() instanceof ResponsiveUIActivity)
                {
                    ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                    ra.switchContent(new Fragment(), 6);
                }

            } catch (Exception e)
            {
            }
        }
    }
}