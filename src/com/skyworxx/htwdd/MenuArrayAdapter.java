package com.skyworxx.htwdd;


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
        // ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        LinearLayout t = (LinearLayout) rowView.findViewById(R.id.menurow);
        textView.setTextColor(Color.WHITE);
        if (values[position].length() == 0)
            t.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, menuheight));

        ImageView image_iv = (ImageView) rowView.findViewById(R.id.image);
        if (position == 1)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.home));
        if (position == 4)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.calendar));
        if (position == 5)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.round));
        if (position == 6)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.certificate));
        if (position == 7)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.spreadsheet));
        if (position == 10)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.messenger));
        if (position == 8)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.emoticongrin));
        if (position == 12)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.administrator));
        if (position == 13)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.bookmark));
        if (position == 11)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.group));
        if (position == 15)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.gear));
        if (position == 2)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.heart));
        if (position == 16)
            image_iv.setImageDrawable(rowView.getResources().getDrawable(R.drawable.info2));

        // textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
        // Change the icon for Windows and iPhone
        String s = values[position];

        // t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, menuheight));

        //   if (position==5) textView.setText(values[position]+Html.fromHtml("<sup><small>BETA</small></sup>"));
        //  if (position==6) textView.setText(values[position]+Html.fromHtml("<sup><small>BETA</small></sup>"));
        //  if (position==12) textView.setText(values[position]+Html.fromHtml("<sup><small>BETA</small></sup>"));


        if (position == 0 || position == 3 || position == 9 || position == 14)
        {
            t.setBackgroundColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

            textView.setPadding(10, 5, 10, 5);


            Typeface face = Typeface.createFromAsset(context.getAssets(), "robotothin.ttf");
            textView.setTypeface(face);

        }
        //   if (position==2) t.setBackgroundResource(R.drawable.alltag_bitmap);
        //   if (position==8) t.setBackgroundResource(R.drawable.service_bitmap);
        //if (position==13) t.setBackgroundResource(R.drawable.leben_bitmap);
        //  if (position==13) t.setBackgroundResource(R.drawable.optionen_bitmap);

        return rowView;
    }
}