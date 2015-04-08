package de.htwdd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import de.htwdd.types.Meal;

public class MensaArrayAdapter extends ArrayAdapter<Meal>
{
    private final Context context;
    private Meal[] essen;

    public MensaArrayAdapter(Context context, Meal[] essen)
    {
        super(context, R.layout.mensa, essen);
        this.context = context;
        this.essen = essen;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.mensa, parent, false);

            // Setup the ViewHolder
            viewHolder          = new ViewHolder();
            viewHolder.titel    = (TextView) convertView.findViewById(R.id.titel);
            viewHolder.preis    = (TextView) convertView.findViewById(R.id.preis);
            viewHolder.image    = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.separator= convertView.findViewById(R.id.separator);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.menurow);
            convertView.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) convertView.getTag();

        if (essen[position].ID != 0)
        {
            // Füge Button "Weiter Informationen" hinzu
            Button button;
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                button = new Button(context, null, android.R.attr.borderlessButtonStyle);
                button.setTextColor(Color.parseColor("#33B5E5"));
            } else
                button = new Button(context, null, android.R.attr.buttonStyleSmall);

            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            button.setText(R.string.mensa_more_information);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.studentenwerk-dresden.de/mensen/speiseplan/details-" + essen[position].ID + ".html?pni=1"));
                    context.startActivity(browserIntent);
                }
            });

            button.setTag("Button");
            viewHolder.linearLayout.removeView(viewHolder.linearLayout.findViewWithTag("Button"));
            viewHolder.linearLayout.addView(button, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            viewHolder.separator.setVisibility(View.VISIBLE);
        }
        else
            viewHolder.separator.setVisibility(View.GONE);

        // Ordne den Elementen ihre Daten zu
        if (essen[position].Thumbnail != null)
            viewHolder.image.setImageBitmap(essen[position].Thumbnail);
        else
            viewHolder.image.setVisibility(View.GONE);

        viewHolder.titel.setText(essen[position].Title);
        viewHolder.preis.setText(essen[position].Price);

        return convertView;
    }

    private static class ViewHolder
    {
        public TextView titel;
        public TextView preis;
        public ImageView image;
        public View separator;
        public LinearLayout linearLayout;
    }
}