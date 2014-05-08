package de.htwdd;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import de.htwdd.R;

import de.htwdd.fragments.ResponsiveUIActivity;
import de.htwdd.types.Type_Stunde;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BelegungsOverviewAdapter extends ArrayAdapter<String>
{
    private final Context context;

    private String[] titles;
    private Type_Stunde[] stunden;
    ViewGroup parent;


    public BelegungsOverviewAdapter(Context context, String[] titles)
    {
        super(context, R.layout.belegungsoverviewrow, titles);
        this.context = context;

        this.titles = titles;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.belegungsoverviewrow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);


        this.parent = parent;


        titles_tv.setText(titles[position]);

        DatabaseHandlerRoomTimetable db = new DatabaseHandlerRoomTimetable(context);


        String odaystring = "";
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        if (week % 2 == 0) week = 2;
        if (week % 2 == 1) week = 1;

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


        List<Type_Stunde> list2 = db.getStundenTag(odaystring, week, titles[position]);

        Type_Stunde[] stunden = new Type_Stunde[7];


        for (int a = 0; a < stunden.length; a++)
        {
            stunden[a] = new Type_Stunde();
        }

        for (int a = 1; a <= stunden.length; a++)
        {
            for (int i = 0; i < list2.size(); i++)
            {
                if (a == list2.get(i).getStunde())
                    stunden[a - 1] = list2.get(i);
            }
        }

        ListView l = (ListView) rowView.findViewById(R.id.belegungslist);
        l.setVisibility(View.VISIBLE);
        l.setDividerHeight(0);

        String titles2[] = {"07:30 - 09:00", "09:20 - 10:50", "11:10 - 12:40", "13:10 - 14:40", "15:00 - 16:30", "16:50 - 18:20", "18:30 - 20:00"};

        if (stunden.length > 0)
        {
            BelegungsAdapter colorAdapter = new BelegungsAdapter(context, titles2, stunden);
            l.setAdapter(colorAdapter);
        }


        // web_tv.loadData(essen[position], "text/html", "utf-8");


        Button button;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            //?android:attr/borderlessButtonStyle

            button = new Button(context, null, android.R.attr.borderlessButtonStyle);
            //	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
            button.setTextColor(Color.parseColor("#33B5E5"));

        }
        else
            button = new Button(context, null, android.R.attr.buttonStyleSmall);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button.setText("Wochenbelegung anzeigen");
        LinearLayout ln = (LinearLayout) rowView.findViewById(R.id.menurow);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button.setOnClickListener(new MyOnClickListener(titles[position]));

        ln.addView(button, lp);


        return rowView;
    }


    class MyOnClickListener implements OnClickListener
    {
        private String raum;

        public MyOnClickListener(String titles)
        {
            this.raum = titles;
        }

        public void onClick(View v)
        {
            // Preform a function based on the position


            if (context instanceof ResponsiveUIActivity)
            {
                ResponsiveUIActivity ra = (ResponsiveUIActivity) context;
                ra.switchtoRoom(raum);
            }


            //	 Toast.makeText(context,"Woche"+week+" - Tag:"+tag+" - Stunde:"+stunde, Toast.LENGTH_SHORT).show();
        }
    }


}