package de.htwdd;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.htwdd.R;

public class MenuArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final String[] values;
    private int menuheight;

    public MenuArrayAdapter(Context context, String[] values, int height)
    {
        super(context, R.layout.menurow, values);
        this.context = context;
        this.values = values;
        menuheight = height;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.menurow, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.pruftitel);
        textView.setText(values[position]);
        LinearLayout t = (LinearLayout) rowView.findViewById(R.id.menurow);
        textView.setTextColor(Color.WHITE);
        if (values[position].length() == 0)
            t.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, menuheight));

        ImageView image_iv = (ImageView) rowView.findViewById(R.id.image);
        switch (position)
        {
            case 1:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.home));
                break;
            case 3:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.calendar));
                break;
            case 4:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.round));
                break;
            case 5:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.certificate));
                break;
            case 6:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.spreadsheet));
                break;
            case 8:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.messenger));
                break;
            case 9:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.group));
                break;
            case 10:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.administrator));
                break;
            case 12:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.bookmark));
                break;
            case 13:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.gear));
                break;
            case 14:
                image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.info2));
                break;
        }

        // Change the icon for Windows and iPhone
        String s = values[position];

        if (position == 0 || position == 2 || position == 7 || position == 11)
        {
            t.setBackgroundColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

            textView.setPadding(10, 5, 10, 5);

            Typeface face = Typeface.createFromAsset(context.getAssets(), "robotothin.ttf");
            textView.setTypeface(face);
        }

        return rowView;
    }
}