package de.htwdd;

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

import java.text.SimpleDateFormat;

import de.htwdd.types.Event;

public class CareerServiceArrayAdapter extends ArrayAdapter<Event>
{
    final Context context;
    Event[] events;
    ViewGroup parent;


    public CareerServiceArrayAdapter(Context context, Event[] events)
    {
        super(context, R.layout.fragment_career_item_detail, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView             = inflater.inflate(R.layout.fragment_career_item_detail, parent, false);
        TextView layout_title   = (TextView) convertView.findViewById(R.id.titel);
        TextView layout_desc    = (TextView) convertView.findViewById(R.id.desc);
        TextView layout_date    = (TextView) convertView.findViewById(R.id.date);
        LinearLayout careerln   = (LinearLayout) convertView.findViewById(R.id.careerll);

        // Setze Texte
        layout_title.setText(events[position].Title);
        layout_desc.setText(events[position].desc);
        if (events[position].datum != null)
            layout_date.setText(new SimpleDateFormat("dd.MM.yyyy kk:mm 'Uhr'").format(events[position].datum));
        this.parent = parent;

        // Button mit Url anzeigen
        if (events[position].url.length()> 0)
        {
            Button button;
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= 14)
            {
                button = new Button(context, null, android.R.attr.borderlessButtonStyle);
                button.setTextColor(Color.parseColor("#33B5E5"));
            }
            else
                button = new Button(context, null, android.R.attr.buttonStyleSmall);

            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            button.setText("mehr Informationen");

            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            careerln.addView(button, lp);

            button.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(events[position].url));
                    context.startActivity(i);
                }
            });
        }
        else
            convertView.findViewById(R.id.separator).setVisibility(View.GONE);

        return convertView;
    }
}