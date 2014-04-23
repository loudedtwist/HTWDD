package com.skyworxx.htwdd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.skyworxx.htwdd.types.TEvent;

public class CareerArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private String[] titles;
    private TEvent[] events;
    ViewGroup parent;


    public CareerArrayAdapter(Context context, String[] titles, TEvent[] events)
    {
        super(context, R.layout.careerrow, titles);
        this.context = context;
        this.events = events;
        ;
        this.titles = titles;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.careerrow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView trainer_tv = (TextView) rowView.findViewById(R.id.trainer);
        TextView desc_tv = (TextView) rowView.findViewById(R.id.desc);
        TextView date_tv = (TextView) rowView.findViewById(R.id.date);
        LinearLayout careerln = (LinearLayout) rowView.findViewById(R.id.careerll);
        TextView raum_tv = (TextView) rowView.findViewById(R.id.raum);


        titles_tv.setText(titles[position]);


        if (events[position].trainer.length() != 0) trainer_tv.setText(events[position].trainer);
        if (events[position].datum.length() != 0) date_tv.setText(events[position].datum);
        if (events[position].raum.length() != 0) raum_tv.setText(events[position].raum);
        if (events[position].desc.length() != 0) desc_tv.setText(events[position].desc);

        this.parent = parent;


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
        {
            button = new Button(context, null, android.R.attr.buttonStyleSmall);

        }
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button.setText("mehr Informationen");

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        careerln.addView(button, lp);

        button.setOnClickListener(new MyOnClickListener(position));


        return rowView;
    }


    class MyOnClickListener implements OnClickListener
    {
        int pos;

        MyOnClickListener(int pos)
        {

            this.pos = pos;
        }

        public void onClick(View v)
        {
            try
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(events[pos].link));
                context.startActivity(i);
            } catch (Exception e)
            {
            }
            ;
        }
    }


}