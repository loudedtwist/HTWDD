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
    ViewGroup parent;

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
        View rowView = inflater.inflate(R.layout.mensa, parent, false);
        TextView layout_Title   = (TextView) rowView.findViewById(R.id.titel);
        TextView layout_Preis   = (TextView) rowView.findViewById(R.id.preis);
        ImageView layout_Image  = (ImageView) rowView.findViewById(R.id.image);
        this.parent = parent;

        if (essen[position].ID != 0) {
            // Füge Button "Weiter Informationen" hinzu
            Button button;
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                button = new Button(context, null, android.R.attr.borderlessButtonStyle);
                button.setTextColor(Color.parseColor("#33B5E5"));
            } else
                button = new Button(context, null, android.R.attr.buttonStyleSmall);

            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            button.setText("mehr Informationen");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.studentenwerk-dresden.de/mensen/speiseplan/details-" + essen[position].ID + ".html?pni=1"));
                    context.startActivity(browserIntent);
                }
            });

            LinearLayout ln = (LinearLayout) rowView.findViewById(R.id.menurow);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            ln.addView(button, lp);
        }
        else
            rowView.findViewById(R.id.separator).setVisibility(View.GONE);

        // Ordne den Elementen ihre Daten zu
        if (essen[position].Thumbnail != null)
            layout_Image.setImageBitmap(essen[position].Thumbnail);
        else
            layout_Image.setVisibility(View.GONE);

        layout_Title.setText(essen[position].Title);
        layout_Preis.setText(essen[position].Price);

        return rowView;
    }
}