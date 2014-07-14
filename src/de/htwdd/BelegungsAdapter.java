package de.htwdd;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.htwdd.types.Type_Stunde;

import java.util.Calendar;
import java.util.Locale;

public class BelegungsAdapter extends ArrayAdapter<String>
{
    private final Context context;

    private String[] titles;
    private Type_Stunde[] stunden;
    ViewGroup parent;


    public BelegungsAdapter(Context context, String[] titles, Type_Stunde[] essen)
    {
        super(context, R.layout.mensa, titles);
        this.context = context;
        this.stunden = essen;
        this.titles = titles;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.belegungsrow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);

        LinearLayout lin = (LinearLayout) rowView.findViewById(R.id.menurow);

        if (position % 2 == 0)
            lin.setBackgroundColor(Color.parseColor("#eeeeee"));

        this.parent = parent;


        titles_tv.setText(titles[position]);
        if (stunden[position].getName() == null)
            prices_tv.setText("frei");
        else if (stunden[position].getName().equals("(leer)"))
            prices_tv.setText("");

        else
            prices_tv.setText(stunden[position].getName() + "\t\t" + stunden[position].getTyp());
        // web_tv.loadData(essen[position], "text/html", "utf-8");


        Calendar calendar = Calendar.getInstance(Locale.GERMANY);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int timeinmin = hour * 60 + min;
        int aktstunde = 0;


        if ((timeinmin >= 450) && (timeinmin < 560)) aktstunde = 0;
        if ((timeinmin >= 560) && (timeinmin < 670)) aktstunde = 1;
        if ((timeinmin >= 670) && (timeinmin < 790)) aktstunde = 2;
        if ((timeinmin >= 790) && (timeinmin < 900)) aktstunde = 3;
        if ((timeinmin >= 900) && (timeinmin < 1010)) aktstunde = 4;
        if ((timeinmin >= 1010) && (timeinmin < 1110)) aktstunde = 5;
        if ((timeinmin >= 1110) && (timeinmin < 1200)) aktstunde = 6;
        if (timeinmin >= 1200) aktstunde = 7;

        if (aktstunde == position)
            lin.setBackgroundColor(Color.parseColor("#aae8ff"));


        return rowView;
    }


}