package de.htwdd.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
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

import de.htwdd.BelegungsAdapter;
import de.htwdd.DatabaseHandlerNoten;
import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.HTTPDownloader;
import de.htwdd.R;
import de.htwdd.WizardWelcome;
import de.htwdd.types.TEssen;
import de.htwdd.types.TNote;
import de.htwdd.types.Type_Stunde;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CardFragment extends Fragment
{
    public CardFragment()
    {
    }

    public PackageInfo info;

    public void clearApplicationData()
    {
        File cache = getActivity().getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists())
        {
            String[] children = appDir.list();
            for (String s : children)
            {
                if (!s.equals("lib"))
                {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.main, container, false);

        // NowLayout now= new NowLayout(getActivity());

        //  return now;


        //TextView tv = new TextView(getActivity());
        //tv.setText("My first fragment");

        //return tv;

    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());


        if (app_preferences.getBoolean("first_run_bool", true) == false)
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
                editor.commit();
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
        int month = calendar.get(Calendar.MONTH);
        month++;
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
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


        int oweek = week;

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
        LinearLayout ln5 = (LinearLayout) getActivity().findViewById(R.id.noten);
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


        ln5.addView(button5, lp5);


        //Noten--------------------

        DatabaseHandlerNoten db = new DatabaseHandlerNoten(getActivity());

        List<TNote> e = db.getAllNoten();

        TNote[] noten = new TNote[e.size()];
        for (int i = 0; i < e.size(); i++)
        {
            noten[i] = e.get(i);
        }

        double finalmark = 0;
        int validcount = 0;
        double mark = 0;
        double credits = 0;


        float totalworstmark = 0.0f;
        float totalbestmark = 5.0f;

        for (int i = 0; i < e.size(); i++)
        {
            try
            {
                double c = Float.parseFloat(noten[i]._Note.replace(',', '.'));
                double cr = Float.parseFloat(noten[i]._Credits);
                if (cr > 0)
                {
                    mark += (c * cr);
                    validcount++;
                    credits += cr;
                }
                totalworstmark = (float) Math.max(totalworstmark, c);
                totalbestmark = (float) Math.min(totalbestmark, c);

            } catch (Exception e2)
            {
            }
        }

        //if (validcount!=0)
        if (credits != 0)
            finalmark = mark / credits;
        finalmark = Math.round(finalmark * 100.0) / 100.0;

        ((TextView) (getActivity().findViewById(R.id.finalmark))).setText("Durchschnitt: " + finalmark);
        ((TextView) (getActivity().findViewById(R.id.count))).setText(validcount + " Noten (mit Credits)");
        ((TextView) (getActivity().findViewById(R.id.credits))).setText(credits + " Credits");
        ((TextView) (getActivity().findViewById(R.id.best))).setText("beste Note " + totalbestmark);
        ((TextView) (getActivity().findViewById(R.id.worst))).setText("schlechteste Note " + totalworstmark);

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


    public class News
    {
        String title, desc, author;
        Bitmap bitmap;
        int id;
        Date startdate, enddate;
        String url;
    }


    private class NewsWorker extends AsyncTask<Calendar, Void, News[]>
    {
        @Override
        protected News[] doInBackground(Calendar... params)
        {
            try {
                HTTPDownloader downloader = new HTTPDownloader("https://htwdd.github.io/news");

                String result = downloader.getString();

                String[] items = result.split("<item>");

                News news[] = new News[items.length];

                for (int i = 0; i < items.length; i++)
                {
                    String[] items2 = items[i].split("<br>");
                    if (items2.length < 2) break;

                    news[i] = new News();
                    news[i].id = Integer.parseInt(items2[0]);
                    news[i].author = items2[6];

                    HTTPDownloader imageloader = new HTTPDownloader("https://htwdd.github.io/images/" + items2[3]);

                    news[i].bitmap = imageloader.getNormalBitmap();

                    //add http// to url if not present
                    if (!items2[4].startsWith("http://") && !items2[4].startsWith("https://"))
                        items2[4] = "http://" + items2[4];

                    news[i].title = items2[1];
                    news[i].desc = items2[2];
                    news[i].url = items2[4];
                }

                return news;
            }
            catch (Exception e)
            {
                return  null;
            }
        }

        @Override
        protected void onPostExecute(News[] result)
        {
            News news = null;

            if (result.length == 1)
                news = result[0];
            else if (result.length > 1)
            {
                int randomnumber = (int) ((Math.random() * (result.length)));
                news = result[randomnumber];
            }

            try
            {
                if (news == null)
                {
                    // Blende Kachel aus
                    LinearLayout ln = (LinearLayout) getActivity().findViewById(R.id.aktuellbox);
                    ln.setVisibility(View.GONE);
                }
                else
                {
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

            } catch (Exception e)
            {
            }
        }
    }


    private class CheckUpdate extends AsyncTask<Calendar, Void, String[]>
    {
        @Override
        protected String[] doInBackground(Calendar... params)
        {
            try
            {
                HTTPDownloader downloader = new HTTPDownloader("https://htwdd.github.io/currentversion");

                String result = downloader.getString();

                String[] items = result.split(";");

                return items;

            } catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            if (result != null)
            {
                try
                {
                    if (Integer.parseInt(result[0]) > info.versionCode)
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
                } catch (Exception e2)
                {
                }
            }
        }
    }


    private class MensaWorker extends AsyncTask<Calendar, Void, TEssen[]>
    {
        @Override
        protected TEssen[] doInBackground(Calendar... params)
        {
            int index;

            try
            {
                HTTPDownloader downloader = new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=9");

                String result   = downloader.getString();
                String tokens[] = result.split("<title>");
                TEssen[] essen  = new TEssen[tokens.length - 2];

                for (int i = 0; i < essen.length; i++)
                {
                    essen[i] = new TEssen();
                    try
                    {
                        // Bestimme Index wo Titel endet (ohne Preis)
                        index = tokens[i + 2].indexOf("(");
                        if(index == -1)
                            index = tokens[i + 2].indexOf("</title>");

                        // Extrahiere die benötigten Informationen
                        essen[i].setTitle(tokens[i + 2].substring(0, index));
                        essen[i].setSonst(tokens[i + 2].substring(tokens[i + 2].indexOf("<description>") + 13, tokens[i + 2].indexOf("</description>")));
                        essen[i].setID(Integer.parseInt(tokens[i + 2].substring(tokens[i + 2].indexOf("details-") + 8, tokens[i + 2].indexOf(".html"))));
                    } catch (Exception e)
                    {
                        essen[i].setTitle("Fehler im Parser");
                    }
                }
                return essen;

            } catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(TEssen[] essen)
        {
            try
            {
                // check if the fragment is currently added to its activity
                // otherwise getActivity will throw an exception
                if(isAdded())
                {
                    if (essen.length < 1)
                    {
                        essen = new TEssen[1];
                        essen[0] = new TEssen();
                        essen[0].setTitle("Kein Angebot an diesem Tag.");
                    }

                    String titles[] = new String[essen.length];

                    String mensa = "";

                    for (int i = 0; i < titles.length; i++)
                    {
                        if (i < titles.length - 1)
                            mensa += (essen[i].getTitle() + "\n\n");
                        else
                            mensa += (essen[i].getTitle());
                    }

                    TextView mensatext = (TextView) getActivity().findViewById(R.id.mensatext);

                    mensatext.setText(mensa);
                    if (mensa.contains("UnkownHost"))
                        mensatext.setText("Verbindung zum Mensa-Server nicht möglich.");
                }

            } catch (Exception e)
            {
                return;
            }
        }
    }
}