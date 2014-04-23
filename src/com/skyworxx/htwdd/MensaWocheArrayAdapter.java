package com.skyworxx.htwdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MensaWocheArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;

    private String[] titles;
    private String[] essen;
    ViewGroup parent;


    public MensaWocheArrayAdapter(Context context, String[] titles, String[] essen)
    {
        super(context, R.layout.mensarow, titles);
        this.context = context;
        this.essen = essen;
        this.titles = titles;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mensawocherow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);

        this.parent = parent;

        try
        {

            titles_tv.setText(titles[position]);
            prices_tv.setText(essen[position]);
            // web_tv.loadData(essen[position], "text/html", "utf-8");
        } catch (Exception e)
        {
        }


        return rowView;
    }


}