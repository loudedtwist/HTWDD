package de.htwdd.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.MyAdapter;
import de.htwdd.R;
import de.htwdd.Wizard1;
import de.htwdd.types.Type_Stunde;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StundenplanFragment extends Fragment
{
    public int fragmentwidth, fragmentheight;
    public int week_id;
    public ProgressBar progressbar;

    public StundenplanFragment(int fragmentwidth, int fragmentheight, int i)
    {
        this.fragmentheight = fragmentheight;
        this.fragmentwidth = fragmentwidth;
        week_id = i;
    }

    public StundenplanFragment()
    {
    }


    public StundenplanFragment(int week)
    {
        week_id = week;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.stundenplan, null);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //worker w = new worker();
        //w.execute();
        showweek(week_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);


        //worker w = new worker();
        //w.execute();
        //showweek(week_id);
    }


    public void showweek(int week)
    {
        final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(getActivity());

        List<Type_Stunde> list = db.getAllStunden();
        if (list.size() == 0)
        {
            week = 0;
        }

        if (week == 0)
        {
            getView().findViewById(R.id.grid).setVisibility(View.GONE);
            getView().findViewById(R.id.waitIndicator).setVisibility(View.GONE);

            //	 final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(getActivity());
            final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

	    		/*
	    		 * 
	    		 * String data = URLEncoder.encode("imm", "UTF-8")
							+ "="
							+ URLEncoder.encode(app_preferences.getString("im", "0"),"UTF-8")
							+ "&"
							+ URLEncoder.encode("stuga", "UTF-8")
							+ "="
							+ URLEncoder.encode(app_preferences.getString("stdg", "0"),"UTF-8")
							+ "&"
							+ URLEncoder.encode("grup", "UTF-8")
							+ "="
							+ URLEncoder.encode(app_preferences.getString("studgruppe", "0"),"UTF-8");
	    		 * 
	    		 * 
	    		 */
            if ((app_preferences.getString("prof_kennung", "").length() < 1) && (app_preferences.getString("im", "").length() < 1 || app_preferences.getString("stdg", "").length() < 1 || app_preferences.getString("studgruppe", "0").length() < 1))
            {

                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
                alertDialog2.setTitle("Fehlende Daten");
                alertDialog2.setMessage("Für den Stundenplan müssen IM-Jahr, Studiengangsnummer und Studiengruppennummer eingetragen werden.\n\nSoll der Konfigurations-Assistent gestartet werden?");
                alertDialog2.setIcon(R.drawable.ic_launcher);
                alertDialog2.setButton2("nein", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User clicked OK button
                        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
                        p.setVisibility(View.GONE);
                        getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
                        GridView gridview = (GridView) getView().findViewById(R.id.grid);
                        gridview.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog2.setButton("Assistenten starten", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent nextScreen = new Intent(getActivity(), Wizard1.class);
                        startActivity(nextScreen);
                        getActivity().finish();
                    }
                });
                alertDialog2.show();
            }
            else
            {
                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
                alertDialog2.setTitle("Stundenplan");
                alertDialog2.setMessage("Es wurde noch kein Stundenplan angelegt. Soll dieser nun von dem HTW Server geladen werden?");
                alertDialog2.setIcon(R.drawable.ic_launcher);
                alertDialog2.setButton2("nein", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User clicked OK button
                        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
                        p.setVisibility(View.GONE);
                        getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
                        GridView gridview = (GridView) getView().findViewById(R.id.grid);
                        gridview.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog2.setButton("ja", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        getView().findViewById(R.id.waitIndicator).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);


                        if (app_preferences.getString("prof_kennung", "").length() < 1)
                        {
                            worker w = new worker();
                            w.execute(db, app_preferences);
                        }
                        {
                            worker2 w2 = new worker2();
                            w2.execute(db, app_preferences);

                        }
                        Toast.makeText(getActivity(), "Stundenplan wird aktualisiert", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog2.show();


            }

            return;
        }


        //   try {

        GridView gridview = (GridView) getView().findViewById(R.id.grid);


        int width = fragmentwidth;
        int height = fragmentheight;
        int planweek = week % 2;
        if (planweek == 0) planweek = 2;

        gridview.setAdapter(new MyAdapter(getActivity(), planweek, width, height));
        gridview.setColumnWidth(width / 6);

        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

        p.setVisibility(View.GONE);
        getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);

        gridview.setVisibility(View.VISIBLE);
			
	        
	        /*}catch (Exception e){

		    	final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(getActivity());             
	    		final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());       		 
	    		worker w = new worker();
	    		w.execute(db, app_preferences);
	        }*/
    }


    public class worker extends AsyncTask<Object, Void, String>
    {
        @Override
        protected String doInBackground(Object... params)
        {
            DatabaseHandlerTimetable db = (DatabaseHandlerTimetable) params[0];
            SharedPreferences app_preferences = (SharedPreferences) params[1];

            float progress;
            onProgressUpdate(10);
            String line2;
            // Construct data
            try
            {
//					String data = URLEncoder.encode("matr", "UTF-8")
//							+ "="
//							+ URLEncoder.encode(
//									app_preferences.getString("matnr", "0"),
//									"UTF-8");

                String data = URLEncoder.encode("imm", "UTF-8")
                        + "="
                        + URLEncoder.encode(app_preferences.getString("im", "0"), "UTF-8")
                        + "&"
                        + URLEncoder.encode("stuga", "UTF-8")
                        + "="
                        + URLEncoder.encode(app_preferences.getString("stdg", "0"), "UTF-8")
                        + "&"
                        + URLEncoder.encode("grup", "UTF-8")
                        + "="
                        + URLEncoder.encode(app_preferences.getString("studgruppe", "0"), "UTF-8");

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
                onProgressUpdate(50);

                List<Type_Stunde> stunden = db.getAllStunden();

                try
                {
                    db.purge();
                } catch (Exception e)
                {
                }
                ;
                onProgressUpdate(60);
					/*for (Type_Stunde cn : stunden) {
						String log = "Deleting: " + cn.getID() + " ,Name: "
								+ cn.getName();
						// Writing Stunden to log
						Log.d("Deleting: ", log);

						db.deleteStunde(cn);
						
					}*/

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
                onProgressUpdate(70);
            }

            //
            // String tokens2[] =
            // line2.split("Nur die Pflichtfächer und die mit Häkchen");

            String zeiten[] = line2.split("</td>");

            String resultstring = "success";

            progress = 70;
            float maxprogressperslot = 30 / zeiten.length;

            for (int i = 0; i < zeiten.length; i++)
            {
                progress += maxprogressperslot;
                onProgressUpdate((int) progress);


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
                            resultstring = "Problem beim Erkennen der Stunden(namen), Arten oder Räume. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Fehlende Stunden oder falsche Daten können durch Antippen angepasst werden.";

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
                        ;
                        // System.out.println(Integer.toString(i));
                        //
                        // System.out.println(Integer.toString(Woche));
                        // System.out.println(Integer.toString(Stunde));
                        // System.out.println(kname2 );
                        // System.out.println(ktyp );
                        // System.out.println(kraum);
                        // System.out.println("");
                        Log.d("Insert: ", "Inserting " + kname2 + " " + Woche + " " + Tag + " " + Stunde);
                        try
                        {
                            db.overwriteStunde(new Type_Stunde(Woche, Tag, Stunde,
                                    kname2, ktyp, kraum));
                        } catch (Exception e)
                        {
                            resultstring = "Problem beim Erkennen der Stunde/Wochentag. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Bei fehlenden Stunden oder falschen Daten k�nnen diese durch Antippen angepasst werden.";

                        }
                        ;
                    }
                }
            }

            return resultstring;
        }

        protected void onProgressUpdate(Integer... values)
        {
            // TODO Auto-generated method stub
            progressbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equals("success"))
            {
                Toast.makeText(getActivity(),
                        "Stundenplan neu eingelesen!", Toast.LENGTH_LONG)
                        .show();
            }
            else
            {
                Toast.makeText(getActivity(),
                        "Aktualisieres des Stundenplans fehlgeschlagen.", Toast.LENGTH_LONG)
                        .show();
            }

            ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
            ra.switchContent(new Fragment(), 3);

            int week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

            showweek(week);
        }
    }


    public class worker2 extends AsyncTask<Object, Void, String>
    {
        @Override
        protected String doInBackground(Object... params)
        {
            DatabaseHandlerTimetable db = (DatabaseHandlerTimetable) params[0];
            SharedPreferences app_preferences = (SharedPreferences) params[1];

            float progress;
            onProgressUpdate(10);
            String line2;
            // Construct data
            try
            {
//						String data = URLEncoder.encode("matr", "UTF-8")
//								+ "="
//								+ URLEncoder.encode(
//										app_preferences.getString("matnr", "0"),
//										"UTF-8");


                String data = URLEncoder.encode("unix", "UTF-8")
                        + "="
                        + URLEncoder.encode(app_preferences.getString("prof_kennung", "0"), "UTF-8");

                // Send data
                URL url = new URL(
                        "http://www2.htw-dresden.de/~rawa/cgi-bin/auf/raiplan.php");
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
                onProgressUpdate(50);

                List<Type_Stunde> stunden = db.getAllStunden();

                try
                {
                    db.purge();
                } catch (Exception e)
                {
                }
                onProgressUpdate(60);
						/*for (Type_Stunde cn : stunden) {
							String log = "Deleting: " + cn.getID() + " ,Name: "
									+ cn.getName();
							// Writing Stunden to log
							Log.d("Deleting: ", log);

							db.deleteStunde(cn);
							
						}*/

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
                onProgressUpdate(70);
            }


            //
            // String tokens2[] =
            // line2.split("Nur die Pflichtfächer und die mit Häkchen");

            String zeiten[] = line2.split("</td>");

            String resultstring = "success";

            progress = 70;
            float maxprogressperslot = 30 / zeiten.length;


            for (int i = 0; i < zeiten.length; i++)
            {
                progress += maxprogressperslot;
                onProgressUpdate((int) progress);


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
                            resultstring = "Problem beim Erkennen der Stunden(namen), Arten oder Räume. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Fehlende Stunden oder falsche Daten können durch Antippen angepasst werden.";

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
                            resultstring = "Problem beim Erkennen der Stunde/Wochentag. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Bei fehlenden Stunden oder falschen Daten k�nnen diese durch Antippen angepasst werden.";

                        }
                        ;
                        // System.out.println(Integer.toString(i));
                        //
                        // System.out.println(Integer.toString(Woche));
                        // System.out.println(Integer.toString(Stunde));
                        // System.out.println(kname2 );
                        // System.out.println(ktyp );
                        // System.out.println(kraum);
                        // System.out.println("");
                        Log.d("Insert: ", "Inserting " + kname2 + " " + Woche + " " + Tag + " " + Stunde);
                        try
                        {
                            db.overwriteStunde(new Type_Stunde(Woche, Tag, Stunde,
                                    kname2, ktyp, kraum));
                        } catch (Exception e)
                        {
                            resultstring = "Problem beim Erkennen der Stunde/Wochentag. Es wurden Stunden in den Plan eingetragen, die Korrektheit kann jedoch nicht garantiert werden. Bei fehlenden Stunden oder falschen Daten können diese durch Antippen angepasst werden.";

                        }

                    }
                }
            }
            return resultstring;
        }

        protected void onProgressUpdate(Integer... values)
        {
            // TODO Auto-generated method stub
            progressbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equals("success"))
            {
                Toast.makeText(getActivity(),
                        "Stundenplan neu eingelesen!", Toast.LENGTH_LONG)
                        .show();
            }
            else
            {
                Toast.makeText(getActivity(),
                        "Aktualisieres des Stundenplans fehlgeschlagen.", Toast.LENGTH_LONG)
                        .show();
            }


            ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
            ra.switchContent(new Fragment(), 3);

            int week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

            showweek(week);
        }
    }
}