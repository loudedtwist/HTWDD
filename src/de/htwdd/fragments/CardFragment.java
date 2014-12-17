package de.htwdd.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.htwdd.BelegungsAdapter;
import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.R;
import de.htwdd.WizardWelcome;
import de.htwdd.classes.HTTPDownloader;
import de.htwdd.classes.Mensa;
import de.htwdd.classes.Noten;
import de.htwdd.types.Meal;
import de.htwdd.types.News;
import de.htwdd.types.Stats;
import de.htwdd.types.Type_Stunde;

public class CardFragment extends Fragment
{
    public PackageInfo info;

    public CardFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        if (!app_preferences.getBoolean("first_run_bool", true))
        {
            getActivity().findViewById(R.id.willkommen_box).setVisibility(View.GONE);
        }
        ImageView iv1 = (ImageView) getActivity().findViewById(R.id.imageView1);
        iv1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                SharedPreferences app_preferences1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = app_preferences1.edit();
                editor.putBoolean("first_run_bool", false);
                editor.apply();
                getActivity().findViewById(R.id.willkommen_box).setVisibility(View.GONE);
            }
        });


        PackageManager manager = this.getActivity().getPackageManager();

        try
        {
            info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
        } catch (NameNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(getActivity());

        String daystring = "";
        String odaystring = "";
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int timeinmin = hour * 60 + min;
        int aktstunde = 0;


        if ((timeinmin >= 450) && (timeinmin < 560)) aktstunde = 1;
        if ((timeinmin >= 560) && (timeinmin < 670)) aktstunde = 2;
        if ((timeinmin >= 670) && (timeinmin < 790)) aktstunde = 3;
        if ((timeinmin >= 790) && (timeinmin < 900)) aktstunde = 4;
        if ((timeinmin >= 900) && (timeinmin < 1010)) aktstunde = 5;
        if ((timeinmin >= 1010) && (timeinmin < 1110)) aktstunde = 6;
        if ((timeinmin >= 1110) && (timeinmin < 1200)) aktstunde = 7;
        if (timeinmin >= 1200) aktstunde = 8;

        TextView tt4 = (TextView) getView().findViewById(R.id.textView4);
        TextView tt6 = (TextView) getView().findViewById(R.id.textView6);
        TextView tt8 = (TextView) getView().findViewById(R.id.textView8);
        TextView tt9 = (TextView) getView().findViewById(R.id.textView9);
        TextView tt5 = (TextView) getView().findViewById(R.id.textView5);
        TextView tt7 = (TextView) getView().findViewById(R.id.textView7);

        TextView tt12 = (TextView) getView().findViewById(R.id.textView12);
        TextView tt13 = (TextView) getView().findViewById(R.id.textView13);


        week = week % 2;


        if (week == 0) week = 2;
        int dayoffset = 0;

        String currentday = "heute";


        if (day == 2)
        {
            odaystring = "Montag";
        }
        else if (day == 3)
        {
            odaystring = "Dienstag";
        }
        else if (day == 4)
        {
            odaystring = "Mittwoch";
        }
        else if (day == 5)
        {
            odaystring = "Donnerstag";
        }
        else if (day == 6)
        {
            odaystring = "Freitag";
        }
        else if (day == 7)
        {
            odaystring = "Samstag";
        }
        else if (day == 1)
        {
            odaystring = "Sonntag";
        }

        //This will cause problems at the end of the month!
        //tttoday.setText("Heute: "+odaystring+", "+	Integer.toString(calendar.get(Calendar.DATE)+dayoffset)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR)+" (KW "+oweek+ ")");

        if ((hour > 17) && (day != 1) && (day != 7))
        {
            day++;
            currentday = "morgen";
            dayoffset = 1;
        }

        if ((day == 1) || (day == 7))
        {
            day = 2;
//
            currentday = "Montag";
            if (week == 1) week = 2;
            else
                week = 1;
        }

        //	tt2.setText("Stundenübersicht von "+currentday);


        if (day == 2)
        {
            daystring = "Montag";
        }
        else if (day == 3)
        {
            daystring = "Dienstag";
        }
        else if (day == 4)
        {
            daystring = "Mittwoch";
        }
        else if (day == 5)
        {
            daystring = "Donnerstag";
        }
        else if (day == 6)
        {
            daystring = "Freitag";
        }
        else if (day == 7)
        {
            daystring = "Samstag";
        }
        else if (day == 1)
        {
            daystring = "Sonntag";
        }


        de.htwdd.types.Type_Stunde aktstundeOB = db.getStunde(odaystring, week, aktstunde);


        if (!aktstundeOB.getName().equals("(leer)"))
        {
            if (currentday.equals("heute"))
                tt8.setText(aktstundeOB.getTyp() + " - " + aktstundeOB.getRaum());

            int maxtime = 0;

            if (aktstunde == 1) maxtime = 540;
            if (aktstunde == 2) maxtime = 650;
            if (aktstunde == 3) maxtime = 760;
            if (aktstunde == 4) maxtime = 880;
            if (aktstunde == 5) maxtime = 990;
            if (aktstunde == 6) maxtime = 1100;
            if (aktstunde == 7) maxtime = 1200;

            int timeleft = maxtime - timeinmin;

            if (currentday.equals("heute"))
                if (timeleft > 0)
                    tt5.setText("noch " + timeleft + " Minuten");
                else
                    tt5.setText("Schluss seit " + -timeleft + " Min");


            String uhrzeit = "";
            switch (aktstundeOB.getStunde())
            {
                case 1:
                    uhrzeit = "07:30 - 09:00";
                    break;
                case 2:
                    uhrzeit = "09:20 - 10:50";
                    break;
                case 3:
                    uhrzeit = "11:10 - 12:40";
                    break;
                case 4:
                    uhrzeit = "13:10 - 14:40";
                    break;
                case 5:
                    uhrzeit = "15:00 - 16:30";
                    break;
                case 6:
                    uhrzeit = "16:50 - 18:20";
                    break;
                case 7:
                    uhrzeit = "18:30 - 20:00";
                    break;
            }
            if (currentday.equals("heute"))
                tt12.setText(uhrzeit);


            tt4.setText(aktstundeOB.getName());
        }


        de.htwdd.types.Type_Stunde upstundeOB = new de.htwdd.types.Type_Stunde();
        int a = 1;
        if (!currentday.equals("heute"))
        {
            aktstunde = 1;
            a = 0;
        }

        do
        {
            upstundeOB = db.getStunde(daystring, week, aktstunde + a);

            a++;
            if ((aktstunde + a > 8)) break;
        } while (upstundeOB.getName().equals("(leer)"));

        if (!upstundeOB.getName().equals("(leer)"))
        {

            tt9.setText(upstundeOB.getTyp() + " - " + upstundeOB.getRaum());


            int mintime = 0;
            if (upstundeOB.getStunde() == 1) mintime = 450;
            if (upstundeOB.getStunde() == 2) mintime = 560;
            if (upstundeOB.getStunde() == 3) mintime = 670;
            if (upstundeOB.getStunde() == 4) mintime = 790;
            if (upstundeOB.getStunde() == 5) mintime = 900;
            if (upstundeOB.getStunde() == 6) mintime = 1010;
            if (upstundeOB.getStunde() == 7) mintime = 1110;


            int timetogo = mintime - timeinmin;
            int timetogoinhours = timetogo / 60;
            int timetogoinmin = timetogo - (timetogoinhours * 60);

            if (currentday.equals("heute"))
            {
                if (timetogo > 120)
                    tt7.setText("in " + timetogoinhours + "h " + timetogoinmin + "min");
                else
                    tt7.setText("in " + timetogo + " Minuten");
            }
            else
                tt7.setText(currentday);

            String uhrzeit = "";
            switch (upstundeOB.getStunde())
            {
                case 1:
                    uhrzeit = "7:30 - 9:00";
                    break;
                case 2:
                    uhrzeit = "9:20 - 10:50";
                    break;
                case 3:
                    uhrzeit = "11:10 - 12:40";
                    break;
                case 4:
                    uhrzeit = "13:10 - 14:40";
                    break;
                case 5:
                    uhrzeit = "15:00 - 16:30";
                    break;
                case 6:
                    uhrzeit = "16:50 - 18:20";
                    break;
                case 7:
                    uhrzeit = "18:30 - 20:00";
                    break;
            }

            tt13.setText(uhrzeit);

            tt6.setText(upstundeOB.getName());
        }

        try
        {
            List<Type_Stunde> list2 = db.getStundenTag(odaystring, week);

            Type_Stunde[] stunden = new Type_Stunde[7];

            if (list2.size() != 0)
            {
                for (int ab = 0; ab < stunden.length; ab++)
                {
                    stunden[ab] = list2.get(ab);
                }

//		for (int ab=1;ab<=stunden.length;a++)
//			for (int i=0;i<list2.size();i++)
//				if (ab==list2.get(i).getStunde())
//					stunden[ab-1]=list2.get(i);

                ListView l = (ListView) getActivity().findViewById(R.id.belegungslist);
                l.setVisibility(View.VISIBLE);
                l.setDividerHeight(0);

                String titles2[] = {"07:30 - 09:00", "09:20 - 10:50", "11:10 - 12:40", "13:10 - 14:40", "15:00 - 16:30", "16:50 - 18:20", "18:30 - 20:00"};

                if (stunden.length > 0)
                {
                    BelegungsAdapter colorAdapter = new BelegungsAdapter(getActivity(), titles2, stunden);
                    l.setAdapter(colorAdapter);
                }
            }

        } catch (Exception e)
        {
            Toast.makeText(getActivity(), "Konnte Stundenplan nicht anzeigen.", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

			/*if (app_preferences.getString("wizardrun", "no").equals("no"))
			{
				Intent nextScreen = new Intent(getActivity().getApplicationContext(), Wizard1.class);
	            startActivity(nextScreen);

			}*/

        Button button;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button.setText("Stundenplan anzeigen");
        LinearLayout ln = (LinearLayout) getActivity().findViewById(R.id.stundenplan);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (getActivity() instanceof ResponsiveUIActivity)
                {
                    ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                    ra.switchContent(new Fragment(), 3);
                }
            }
        });


        ln.addView(button, lp);

        Button button2;
        int currentapiVersion2 = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion2 >= 14)
        {
            button2 = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button2.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button2 = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button2.setText("Mensa anzeigen");
        LinearLayout ln2 = (LinearLayout) getActivity().findViewById(R.id.mensalayout);
        LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (getActivity() instanceof ResponsiveUIActivity)
                {
                    ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                    ra.switchContent(new Fragment(), 4);
                }
            }
        });

        ln2.addView(button2, lp2);


        Button button3;

        if (currentapiVersion >= 14)
        {
            button3 = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button3.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button3 = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button3.setText("Konfigurations-Assistent starten");
        LinearLayout ln3 = (LinearLayout) getActivity().findViewById(R.id.willkommen);
        LayoutParams lp3 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(getActivity().getApplicationContext(), WizardWelcome.class);
                startActivity(nextScreen);
                getActivity().finish();
            }
        });


        ln3.addView(button3, lp3);

        Button button4;

        if (currentapiVersion >= 14)
        {
            button4 = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button4.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button4 = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button4.setText("Career Service öffnen");
        LinearLayout ln4 = (LinearLayout) getActivity().findViewById(R.id.career);
        LayoutParams lp4 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (getActivity() instanceof ResponsiveUIActivity)
                {
                    ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                    ra.switchContent(null, 7);
                }
            }
        });


        ln4.addView(button4, lp4);




        Button button5;

        if (currentapiVersion >= 14)
        {
            button5 = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button5.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button5 = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button5.setText("Noten anzeigen");
        LinearLayout ln5 = (LinearLayout) getActivity().findViewById(R.id.Stats);
        LayoutParams lp5 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (getActivity() instanceof ResponsiveUIActivity)
                {
                    ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                    ra.switchContent(null, 5);
                }
            }
        });

        View divider = new View(getActivity());
        divider.setBackgroundColor(getResources().getColor(R.color.appbg));
        divider.setMinimumHeight(2);

        ln5.addView(divider,lp5);
        ln5.addView(button5, lp5);


        // Zeige Noten Statistik an
        Noten noten = new Noten(getActivity());
        Stats[] statses = noten.getStats();
        float average=0.0f, gradeBest= 0.0f, gradeWorst= 0.0f, credits=0.0f;
        int count = 0;
        if (statses.length != 0)
        {
            average = statses[0].Average;
            credits = statses[0].Credits;
            gradeBest = statses[0].GradeBest;
            gradeWorst = statses[0].GradeWorst;
            count = statses[0].GradeCount;
        }

        TextView semester = (TextView) getActivity().findViewById(R.id.StatsSemester);
        semester.setText("Noten");
        semester.setBackgroundColor(getResources().getColor(R.color.faded_magenta));
        semester.setTextColor(getResources().getColor(R.color.white));
        semester.setTextSize(30);

        TextView textAverage  = (TextView) getActivity().findViewById(R.id.StatsAverage);
        TextView textnote     = (TextView) getActivity().findViewById(R.id.StatsNoten);
        TextView textcredits  = (TextView) getActivity().findViewById(R.id.StatsCredits);
        TextView textnoteBest = (TextView) getActivity().findViewById(R.id.StatsNoteBest);
        TextView textnoteWorst= (TextView) getActivity().findViewById(R.id.StatsNoteWorst);

        textAverage.setText(String.format("Durchschnitt: %.2f",average));
        textAverage.setPadding(0,15,0,0);
        textnote.setText(count+" Noten");
        textnote.setPadding(0,15,0,0);
        textcredits.setText(credits+" Credits");
        textnoteBest.setText("beste Note: "+gradeBest);
        textnoteWorst.setText("schlechteste Note: "+gradeWorst);
        textnoteWorst.setPadding(0,0,0,15);

        // Überprüfe auf neue App-Version
        CheckUpdate w1 = new CheckUpdate();
        w1.execute();

        // Lade aktuelle App-Nachrichten aus dem Web
        NewsWorker w2 = new NewsWorker();
        w2.execute();

        // Lade Mensa
        MensaWorker w3 = new MensaWorker();
        w3.execute();
    }

    private class NewsWorker extends AsyncTask<Calendar, Void, News[]>
    {
        @Override
        protected News[] doInBackground(Calendar... params)
        {
            JSONObject object;
            ArrayList<News> arrayList = new ArrayList<News>();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            try {
                HTTPDownloader downloader = new HTTPDownloader("https://htwdd.github.io/news.json");

                JSONArray array = new JSONArray(downloader.getStringUTF8());
                int count = array.length();

                for (int i=0; i<count; i++)
                {
                    object = array.getJSONObject(i);

                    startDate.setTime(format.parse(object.getString("StartDate")));
                    endDate.setTime(format.parse(object.getString("EndDate")));

                    if (startDate.after(today) || endDate.before(today))
                        continue;

                    News news   = new News();
                    news.title  = object.getString("Title");
                    news.desc   = object.getString("Desc");
                    news.author = object.getString("Author");
                    news.url    = object.getString("URL");

                    if (object.getString("Image") != null)
                    {
                        HTTPDownloader imageloader = new HTTPDownloader("https://htwdd.github.io/images/" + object.getString("Image"));
                        news.bitmap = imageloader.getBitmap();
                    }

                    arrayList.add(news);
                }

                return arrayList.toArray(new News[array.length()]);
            }
            catch (Exception e)
            {
                return  null;
            }
        }

        @Override
        protected void onPostExecute(News[] result)
        {
            Random random = new Random();
            News news = null;

            if (!isAdded())
                return;

            if (result != null)
                news = result[(random.nextInt(result.length))];

            if (news == null)
            {
                // Blende Kachel aus
                LinearLayout ln = (LinearLayout) getActivity().findViewById(R.id.aktuellbox);
                ln.setVisibility(View.GONE);
                return;
            }

            TextView NewsHeader = (TextView) getActivity().findViewById(R.id.aktuellheader);
            NewsHeader.setText(Html.fromHtml(news.author));

            TextView NewsTitel = (TextView) getActivity().findViewById(R.id.aktuelltitel);
            NewsTitel.setText(Html.fromHtml(news.title));
            NewsTitel.setVisibility(View.VISIBLE);

            TextView NewsDesc = (TextView) getActivity().findViewById(R.id.aktuelldesc);
            NewsDesc.setText(Html.fromHtml(news.desc));

            ImageView aktimage = (ImageView) getActivity().findViewById(R.id.aktuellimage);
            aktimage.setImageBitmap(news.bitmap);
            if (news.bitmap != null) aktimage.setVisibility(View.VISIBLE);

            final String urlstring = news.url;

            Button ButtonNews;

            if (android.os.Build.VERSION.SDK_INT >= 14)
            {
                ButtonNews = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
                ButtonNews.setTextColor(Color.parseColor("#33B5E5"));
            }
            else
                ButtonNews = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

            ButtonNews.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            ButtonNews.setText("Website öffnen");

            LinearLayout ln6 = (LinearLayout) getActivity().findViewById(R.id.htwaktuell);
            LayoutParams lp6 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            ButtonNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlstring));
                    startActivity(browserIntent);
                }
            });

            ln6.addView(ButtonNews, lp6);
        }
    }


    private class CheckUpdate extends AsyncTask<Void, Void, String[]>
    {
        @Override
        protected String[] doInBackground(Void... params)
        {
            try
            {
                return new HTTPDownloader("https://htwdd.github.io/currentversion").getStringUTF8().split(";");
            }
            catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            if (!isAdded())
                return;

            if (result != null && Integer.parseInt(result[0]) > info.versionCode)
            {
                // Schalte Kachel sichtbar
                LinearLayout ln = (LinearLayout) getActivity().findViewById(R.id.UpdateMessage);
                ln.setVisibility(View.VISIBLE);

                // Alternativen Text anzeigen
                if(!result[1].isEmpty())
                {
                    TextView UpdateMessage = (TextView) getActivity().findViewById(R.id.UpdateMessageText);
                    UpdateMessage.setText(Html.fromHtml(result[1]));
                }

                Button ButtonUpdate;

                if (android.os.Build.VERSION.SDK_INT >= 14)
                {
                    ButtonUpdate = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
                    ButtonUpdate.setTextColor(Color.parseColor("#33B5E5"));
                }
                else
                    ButtonUpdate = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

                ButtonUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                ButtonUpdate.setText("Download App");
                LinearLayout ln6 = (LinearLayout) getActivity().findViewById(R.id.LinearLayout04);
                LayoutParams lp6 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                ButtonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://htwdd.github.io/HTWDD-latest.apk"));
                        startActivity(browserIntent);
                    }
                });

                ln6.addView(ButtonUpdate, lp6);
            }
        }
    }


    private class MensaWorker extends AsyncTask<Void, Void, Meal[]>
    {
        @Override
        protected Meal[] doInBackground(Void... params)
        {
            // Lade Mensa
            Mensa myMensa = new Mensa();
            myMensa.getDataCurrentDay();

            return myMensa.Food;
        }

        @Override
        protected void onPostExecute(Meal[] essen)
        {
            try
            {
                // check if the fragment is currently added to its activity
                // otherwise getActivity will throw an exception
                if(!isAdded())
                    return;

                String mensa = "Heute kein Angebot";

                if (essen.length > 0)
                {
                    // Alle Mahlzeiten (Titel) in einen String verketten
                    mensa = "";
                    for (int i = 0; i < essen.length; i++)
                        if (i < essen.length - 1)
                            mensa += essen[i].Title + "\n\n";
                        else
                            mensa += essen[i].Title;
                }

                TextView mensatext = (TextView) getActivity().findViewById(R.id.mensatext);
                mensatext.setText(mensa);
            } catch (Exception e)
            {
            }
        }
    }
}