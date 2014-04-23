package com.skyworxx.htwdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skyworxx.htwdd.types.TEssen;

public class SmallMensaArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private String[] prices;
    private String[] titles;
    private TEssen[] essen;
    ViewGroup parent;

    SharedPreferences app_preferences;


    public SmallMensaArrayAdapter(Context context, String[] titles, TEssen[] essen)
    {
        super(context, R.layout.belegungsrow, titles);
        this.context = context;
        this.essen = essen;
        this.titles = titles;

        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.belegungsrow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);


        this.parent = parent;

        titles_tv.setText(titles[position]);
        //   prices_tv.setText(essen[position].getSonst());


        //    prices_tv.setText(prices[position]);
        // textView.setTextColor(Color.WHITE);

        // textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
        // Change the icon for Windows and iPhone
        //  String s = prices[position];

        // t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, menuheight));


        return rowView;
    }


}