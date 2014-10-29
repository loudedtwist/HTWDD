package de.htwdd;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.htwdd.types.Medium;

public class LibrarySearchAdapter extends BaseAdapter
{
    List<Medium> mediums;
    LayoutInflater inflater;

    public LibrarySearchAdapter(Context contexts, List<Medium> mediums)
    {
        this.mediums = mediums;
        this.inflater= (LayoutInflater)contexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return mediums.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mediums.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view==null)
            view = inflater.inflate(R.layout.fragment_library_search_item, viewGroup, false);

        TextView title = (TextView)view.findViewById(R.id.BiBSeachItemTitle);
        title.setText(mediums.get(i).Title);
        return view;
    }
}
