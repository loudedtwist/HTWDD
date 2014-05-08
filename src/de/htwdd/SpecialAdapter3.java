package de.htwdd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import de.htwdd.R;

import java.util.HashMap;
import java.util.List;

public class SpecialAdapter3 extends SimpleAdapter
{
    private int[] colors = new int[]{0xFF222222, 0x00000000};
    List<HashMap<String, String>> items;

    public SpecialAdapter3(Context context, List<HashMap<String, String>> items,
                           int resource, String[] from, int[] to)
    {
        super(context, items, resource, from, to);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);


        // int colorPos = position % colors.length;
        int colorPos = 1;
        if ((position == 0) || (position == 8))
        {
            //colorPos = 0;
            //view.findViewById(R.id.gradient).setVisibility(View.VISIBLE);
        }
        view.setBackgroundColor(colors[colorPos]);

        try
        {
            ImageView image_iv = (ImageView) view.findViewById(R.id.image);
            image_iv.setVisibility(View.VISIBLE);

            if (items.get(position).get("art").contains("Borsi"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.borsi));
            else if (items.get(position).get("art").contains("Gutz"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.gutz));
            else if (items.get(position).get("art").contains("Club Aquarium"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.aqua));
            else if (items.get(position).get("art").contains("Club 11"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.club11));
            else if (items.get(position).get("art").contains("Bärenzwinger"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.baeren));
            else if (items.get(position).get("art").contains("HängeMathe"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.mathe));
            else if (items.get(position).get("art").contains("Club Mensa"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.clubmensa));
            else if (items.get(position).get("art").contains("New Feeling"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.cnf));
            else if (items.get(position).get("art").contains("Novitatis"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.novitatis));
            else if (items.get(position).get("art").contains("Traumtänzer"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.traum));
            else if (items.get(position).get("art").contains("Count Down"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.counddown));
            else if (items.get(position).get("art").contains("Heinrich-Cotta"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.cotta));
            else if (items.get(position).get("art").contains("GAG 18"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.gag));
            else if (items.get(position).get("art").contains("Kino im Kasten"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.kik));
            else if (items.get(position).get("art").contains("Klub Neue Mensa"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.knm));
            else if (items.get(position).get("art").contains("Wu5"))
                image_iv.setImageDrawable(view.getResources().getDrawable(R.drawable.wu5));
            else
                image_iv.setVisibility(View.GONE);

        } catch (Exception e)
        {
        }
        ;


        return view;
    }
}
