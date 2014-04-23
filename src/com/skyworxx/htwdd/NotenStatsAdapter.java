package com.skyworxx.htwdd;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotenStatsAdapter extends ArrayAdapter<String>
{
    private final Context context;

    private String[] titles;
    private Stats_element[] stats;
    ViewGroup parent;


    public NotenStatsAdapter(Context context, String[] titles, Stats_element[] stats)
    {
        super(context, R.layout.statsrow, titles);
        this.context = context;
        this.stats = stats;
        this.titles = titles;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.statsrow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView final_tv = (TextView) rowView.findViewById(R.id.finalmark);
        TextView count_tv = (TextView) rowView.findViewById(R.id.count);
        TextView credits_tv = (TextView) rowView.findViewById(R.id.credits);
        TextView best_tv = (TextView) rowView.findViewById(R.id.best);
        TextView worst_tv = (TextView) rowView.findViewById(R.id.worst);


        titles[position] = titles[position].replace("Wintersemester", "WS");
        titles[position] = titles[position].replace("Sommersemester", "SS");
        titles_tv.setText(titles[position]);
        final_tv.setText("Durchschnitt: " + stats[position].finalmark);

        if (stats[position].finalmark < 2.0)
            final_tv.setTextColor(Color.parseColor("#46d400"));

        if ((stats[position].finalmark >= 2.0) && (stats[position].finalmark <= 3.3))
            final_tv.setTextColor(Color.parseColor("#e8a400"));

        if ((stats[position].finalmark > 3.3))
            final_tv.setTextColor(Color.parseColor("#e83c00"));

        count_tv.setText("" + stats[position].validcount + " Noten");
        credits_tv.setText("" + stats[position].credits + " Credits");
        best_tv.setText("beste Note: " + stats[position].bestmark);
        worst_tv.setText("schlechteste Note: " + stats[position].worstmark);
        //prices_tv.setText(essen[position]);
        // web_tv.loadData(essen[position], "text/html", "utf-8");


        return rowView;
    }


}