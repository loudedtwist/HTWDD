package de.htwdd.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import de.htwdd.DatabaseHandlerTimetable;
import de.htwdd.R;
import de.htwdd.TimetableBusyPlan;
import de.htwdd.WizardWelcome;
import de.htwdd.classes.HTTPDownloader;
import de.htwdd.classes.LessonSearch;
import de.htwdd.classes.Mensa;
import de.htwdd.classes.Noten;
import de.htwdd.types.Lesson;
import de.htwdd.types.Meal;
import de.htwdd.types.News;
import de.htwdd.types.Stats;

public class CardFragment extends Fragment
{
    public PackageInfo info;
    SharedPreferences sharedPreferences;
    private View view;



    public CardFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.main, container, false);

        // Hilfsvariablen für Buttons
        Button button;
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout;


        // Willkomenskachel anzeigen?
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("first_run_bool", true))
        {
            // Anzeigen
            view.findViewById(R.id.willkommen_box).setVisibility(View.VISIBLE);

            // Onclick-Listener für Schliesen-Icon
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("first_run_bool", false);
                    editor.apply();
                    getActivity().findViewById(R.id.willkommen_box).setVisibility(View.GONE);
                }
            });

            // Button anzeigen
            if (currentAPIVersion >= 14)
            {
                button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
                button.setTextColor(getResources().getColor(R.color.maintextcolor2));
            }
            else
                button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            button.setText(R.string.overview_show_config);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent nextScreen = new Intent(getActivity().getApplicationContext(), WizardWelcome.class);
                    startActivity(nextScreen);
                    getActivity().finish();
                }
            });

            linearLayout = (LinearLayout) view.findViewById(R.id.willkommen);
            linearLayout.addView(button, layoutParams);
        }


        // Button für Stundenplan einfügen
        if (currentAPIVersion >= 14)
        {
            button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button.setTextColor(getResources().getColor(R.color.maintextcolor2));
        }
        else
            button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setText(R.string.overview_show_timetable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
                ra.switchContent(new Fragment(), 3);
            }
        });

        linearLayout = (LinearLayout) view.findViewById(R.id.stundenplan);
        linearLayout.addView(button, layoutParams);


        // Button für Mensa anzeigen
        if (currentAPIVersion >= 14)
        {
            button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button.setTextColor(getResources().getColor(R.color.maintextcolor2));
        }
        else
            button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setText(R.string.overview_show_mensa);
        button.setOnClickListener(new View.OnClickListener()
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

        linearLayout = (LinearLayout) view.findViewById(R.id.mensalayout);
        linearLayout.addView(button, layoutParams);


        // Button für Noten anzeigen
        if (currentAPIVersion >= 14)
        {
            button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            button.setTextColor(getResources().getColor(R.color.maintextcolor2));
        }
        else
            button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

        linearLayout = (LinearLayout) view.findViewById(R.id.Stats);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setText(R.string.overview_show_grades);
        button.setOnClickListener(new View.OnClickListener()
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

        linearLayout.addView(divider,layoutParams);
        linearLayout.addView(button, layoutParams);


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

        TextView semester = (TextView) view.findViewById(R.id.StatsSemester);
        semester.setText("Noten");
        semester.setBackgroundColor(getResources().getColor(R.color.faded_magenta));
        semester.setTextColor(getResources().getColor(R.color.white));
        semester.setTextSize(30);

        TextView textAverage  = (TextView) view.findViewById(R.id.StatsAverage);
        TextView textnote     = (TextView) view.findViewById(R.id.StatsNoten);
        TextView textcredits  = (TextView) view.findViewById(R.id.StatsCredits);
        TextView textnoteBest = (TextView) view.findViewById(R.id.StatsNoteBest);
        TextView textnoteWorst= (TextView) view.findViewById(R.id.StatsNoteWorst);

        textAverage.setText(String.format("Durchschnitt: %.2f",average));
        textAverage.setPadding(0,15,0,0);
        textnote.setText(count+" Noten");
        textnote.setPadding(0,15,0,0);
        textcredits.setText(credits+" Credits");
        textnoteBest.setText("beste Note: "+gradeBest);
        textnoteWorst.setText("schlechteste Note: "+gradeWorst);
        textnoteWorst.setPadding(0,0,0,15);


        // Update Message anzeigen
        if (sharedPreferences.getBoolean("AppUpdate", false))
            showUpdateMessage("");

        // Überprüfe auf neue App-Version
        CheckUpdate w1 = new CheckUpdate();
        w1.execute();

        // Lade aktuelle App-Nachrichten aus dem Web
        NewsWorker w2 = new NewsWorker();
        w2.execute();

        // Lade Mensa
        MensaWorker w3 = new MensaWorker();
        w3.execute();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        View view = getView();

        if (view==null)
            return;

        // Stundenplan Anbindung
        DatabaseHandlerTimetable databaseHandlerTimetable = new DatabaseHandlerTimetable(getActivity());

        // Typen
        String[] lessonType = view.getResources().getStringArray(R.array.lesson_type);

        // Stunde bestimmen
        Calendar calendar   = GregorianCalendar.getInstance();
        int current_time    = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        int week            = calendar.get(Calendar.WEEK_OF_YEAR);
        int current_ds = 0;

        if (current_time > LessonSearch.lessonEndTimes[7-1])
            current_ds=0;
        else if (current_time >= LessonSearch.lessonStartTimes[6])
            current_ds=7;
        else if (current_time >= LessonSearch.lessonStartTimes[5])
            current_ds=6;
        else if (current_time >= LessonSearch.lessonStartTimes[4])
            current_ds=5;
        else if (current_time >= LessonSearch.lessonStartTimes[3])
            current_ds=4;
        else if (current_time >= LessonSearch.lessonStartTimes[2])
            current_ds=3;
        else if (current_time >= LessonSearch.lessonStartTimes[1])
            current_ds=2;
        else if (current_time >= LessonSearch.lessonStartTimes[0])
            current_ds=1;

        // Aktuell Vorlesungszeit?
        if (current_ds != 0 && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
        {
            ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(week, calendar.get(Calendar.DAY_OF_WEEK)-1,current_ds);

            // Gibt es aktuell eine Stunde?
            if (lessons.size() != 0)
            {
                // Suche nach einer passenden Veranstaltung
                LessonSearch lessonSearch = new LessonSearch();
                int single = lessonSearch.searchLesson(lessons, week);

                TextView overview_lessons_current_tag = (TextView) view.findViewById(R.id.overview_lessons_current_tag);
                TextView overview_lessons_current_type = (TextView) view.findViewById(R.id.overview_lessons_current_type);

                // verbleibende Zeit anzeigen
                TextView overview_lessons_current_remaining = (TextView) view.findViewById(R.id.overview_lessons_current_remaining);
                int difference = current_time - LessonSearch.lessonEndTimes[current_ds-1];

                if (difference < 0)
                    overview_lessons_current_remaining.setText(String.format(getResources().getString(R.string.overview_lessons_remaining_end), -difference));
                else
                    overview_lessons_current_remaining.setText(String.format(getResources().getString(R.string.overview_lessons_remaining_final), difference));

                // Es gibt keine passende Veranstaltung die angezeigt werden kann
                switch (single)
                {
                    case 0:
                        overview_lessons_current_tag.setText("");
                        overview_lessons_current_remaining.setVisibility(View.GONE);
                        break;
                    case 1:
                        overview_lessons_current_tag.setText(lessonSearch.lesson.lessonTag);
                        overview_lessons_current_type.setText(lessonType[lessonSearch.lesson.getTypeInt()]+" - "+lessonSearch.lesson.rooms);
                        break;
                    case 2:
                        overview_lessons_current_tag.setText(R.string.timetable_moreLessons);
                        break;
                }
            }
        }

        // Stundenplanvorschau
        RelativeLayout overview_lessons_busy_plan = (RelativeLayout) view.findViewById(R.id.overview_lessons_busy_plan);

        // Nächste Stunde suchen
        LessonSearch lessonSearch = new LessonSearch();
        Calendar nextLesson = GregorianCalendar.getInstance();

        // Vorlesungszeit vorbei? Dann auf nächsten Tag springen
        if (current_time > LessonSearch.lessonEndTimes[7-1])
            nextLesson.add(Calendar.DAY_OF_YEAR, +1);

        int single;
        int ds = current_ds;

        do {
            // DS erhöhen
            if ((++ds)%7==0)
            {
                ds=1;
                nextLesson.add(Calendar.DAY_OF_MONTH,1);
            }

            // Lade Stunde aus DB
            ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(nextLesson.get(Calendar.WEEK_OF_YEAR), nextLesson.get(Calendar.DAY_OF_WEEK)-1,ds);

            // Suche nach passender Stunde
            single=lessonSearch.searchLesson(lessons, nextLesson.get(Calendar.WEEK_OF_YEAR));

            // Suche solange nach einer passenden Stunde bis eine Stunde gefunden wurde. Nach über zwei Tagen wird die Suche abgebrochen
        }while (single==0 && (nextLesson.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR)) < 2);

        if (single!=0)
        {
            // Stunden
            String[] lessonDS = getResources().getStringArray(R.array.lesson_ds_timeOnly);

            // Abstand berechnen und anzeigen
            TextView overview_lessons_next_remaining = (TextView) view.findViewById(R.id.overview_lessons_next_remaining);

            int difference = nextLesson.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);

            if (difference == 0)
                overview_lessons_next_remaining.setText(String.format(getResources().getString(R.string.overview_lessons_remaining_start), -(current_time - LessonSearch.lessonStartTimes[ds - 1])));
            else if (difference == 1)
            {
                overview_lessons_next_remaining.setText(getResources().getText(R.string.overview_tomorrow) + " " + lessonDS[ds - 1]);

                // Vorsschau setzen
                TextView overview_lessons_busy_plan_day = (TextView) view.findViewById(R.id.overview_lessons_busy_plan_day);
                overview_lessons_busy_plan_day.setText(R.string.overview_tomorrow);

                // DS nicht mehr anzeigen
                current_ds=99;
            }
            else
            {
                final String[] nameOfDays = DateFormatSymbols.getInstance().getWeekdays();
                overview_lessons_next_remaining.setText(nameOfDays[nextLesson.get(Calendar.DAY_OF_WEEK)]+" "+lessonDS[ds-1]);

                // Vorschau ausblenden
                overview_lessons_busy_plan.setVisibility(View.GONE);
            }

            // Name + Art anzeigen
            if (single==1)
            {
                TextView overview_lessons_next_tag = (TextView) view.findViewById(R.id.overview_lessons_next_tag);
                overview_lessons_next_tag.setText(lessonSearch.lesson.lessonTag);

                // Zeige Art an
                TextView overview_lessons_next_type = (TextView) view.findViewById(R.id.overview_lessons_next_type);
                overview_lessons_next_type.setText(lessonType[lessonSearch.lesson.getTypeInt()]+" - "+lessonSearch.lesson.rooms);
            }
            else if (single==2)
            {
                TextView overview_lessons_next_tag = (TextView) view.findViewById(R.id.overview_lessons_next_tag);
                overview_lessons_next_tag.setText(R.string.timetable_moreLessons);
            }
        }

        // Daten für Stundenplan-Vorschau
        String[] values = new String[7];
        for (int i=1;i<8;i++)
        {
            ArrayList<Lesson> lessons = databaseHandlerTimetable.getShortDS(nextLesson.get(Calendar.WEEK_OF_YEAR), nextLesson.get(Calendar.DAY_OF_WEEK)-1,i);

            // Suche nach passender Stunde
            single=lessonSearch.searchLesson(lessons, nextLesson.get(Calendar.WEEK_OF_YEAR));

            switch (single)
            {
                case 0:
                    values[i-1] = "";
                    break;
                case 1:
                    values[i-1] = lessonSearch.lesson.lessonTag+ " ("+lessonSearch.lesson.type+")";
                    break;
                case 2:
                    values[i-1] = getResources().getString(R.string.timetable_moreLessons);
                    break;
            }
        }

        // Datenbank schließen
        databaseHandlerTimetable.close();

        TimetableBusyPlan busyPlan = new TimetableBusyPlan(getActivity(), values, current_ds);

        ListView overview_lessons_list = (ListView) view.findViewById(R.id.overview_lessons_list);
        overview_lessons_list.setAdapter(busyPlan);
    }


    /**
     * Blendet die Kachel zur Information das ein Update verfügbar ist ein.
     *
     * @param AlternateUpdateMessage Anzeige eines alternativen Update-Nachricht
     */
    private void showUpdateMessage(String AlternateUpdateMessage)
    {
        // Layout der gesamten Update-Kachel
        LinearLayout linearLayoutMain = (LinearLayout) view.findViewById(R.id.UpdateMessage);;

        // Alternativen Text anzeigen
        if(!AlternateUpdateMessage.isEmpty())
        {
            TextView UpdateMessage = (TextView) view.findViewById(R.id.UpdateMessageText);
            UpdateMessage.setText(Html.fromHtml(AlternateUpdateMessage));
        }

        // Wenn Kachel schon angezeigt wird, muss kein Button mehr hinzugefügt werden
        if (linearLayoutMain.getVisibility() != View.VISIBLE)
        {
            // Button zum Updaten hinzufügen
            Button ButtonUpdate;
            if (Build.VERSION.SDK_INT >= 14)
            {
                ButtonUpdate = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
                ButtonUpdate.setTextColor(Color.parseColor("#33B5E5"));
            }
            else
                ButtonUpdate = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);

            ButtonUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            ButtonUpdate.setText("Download App");

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout04);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            ButtonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://htwdd.github.io/HTWDD-latest.apk"));
                    startActivity(browserIntent);
                }
            });

            // Button hinzufügen
            linearLayout.addView(ButtonUpdate, layoutParams);
        }

        // Schalte Kachel sichtbar
        linearLayoutMain.setVisibility(View.VISIBLE);
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
                JSONArray array = new JSONArray(downloader.getString());
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
                return new HTTPDownloader("https://htwdd.github.io/currentversion").getString().split(";");
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

            try
            {
                info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            } catch (NameNotFoundException e1)
            {
                return;
            }

            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (result != null && Integer.parseInt(result[0]) > info.versionCode)
            {
                edit.putBoolean("AppUpdate", true);
                showUpdateMessage(result[1]);
            }
            else
                edit.putBoolean("AppUpdate", false);

            edit.apply();
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