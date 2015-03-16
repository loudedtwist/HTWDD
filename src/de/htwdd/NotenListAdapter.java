package de.htwdd;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import de.htwdd.types.Grade;

public class NotenListAdapter extends BaseExpandableListAdapter
{
    private List<String> listDataHeader;
    private HashMap<String, List<Grade>> listDataChild;
    LayoutInflater inflater;

    public NotenListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<Grade>> listDataChild)
    {
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount()
    {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        return listDataChild.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i)
    {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i2)
    {
        return listDataChild.get(listDataHeader.get(i)).get(i2);
    }

    @Override
    public long getGroupId(int i)
    {
        return i;
    }

    @Override
    public long getChildId(int i, int i2)
    {
        return i2;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
    {
        String HeaderTitle = (String) getGroup(i);

        if (view == null)
            view = inflater.inflate(R.layout.fragment_noten_group, viewGroup, false);

        TextView textView = (TextView) view.findViewById(R.id.expandableListHeader);
        textView.setText(HeaderTitle);

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup)
    {
        final Grade ChildText = (Grade) getChild(i,i2);

        if (view == null)
            view = inflater.inflate(R.layout.fragment_noten_item, viewGroup, false);

        TextView modul = (TextView) view.findViewById(R.id.expandableListItemModul);
        modul.setText(ChildText.Modul);
        modul.setTypeface(null, Typeface.NORMAL);
        modul.setTextColor(view.getResources().getColor(R.color.black));

        TextView vermerk = (TextView) view.findViewById(R.id.expandableListItemVermerk);
        vermerk.setText("");

        // Genauen Status setzen
        if (ChildText.Status.equals("AN"))
        {
            modul.setTypeface(null, Typeface.ITALIC);
            modul.setTextColor(view.getResources().getColor(R.color.faded_grey));

            // Student hat sich abgemeldet
            if (ChildText.Vermerk.equals("e"))
                vermerk.setText(R.string.grade_sign_off);
            // Student war krank
            else if (ChildText.Vermerk.equals("k"))
                vermerk.setText(R.string.grade_ill);
            // Student wurde nicht zugelassen
            else if (ChildText.Vermerk.equals("nz"))
                vermerk.setText(R.string.grade_not_allowed);
        }
        // Student hat bestanden
        else if (ChildText.Status.equals("BE"))
        {
            if (ChildText.Credits != 0.0f)
                modul.setTextColor(view.getResources().getColor(R.color.green));
            else
                modul.setTextColor(view.getResources().getColor(R.color.black));
        }
        // Student hat NICHT bestanden
        else if (ChildText.Status.equals("NB") || ChildText.Status.equals("EN"))
        {
            modul.setTextColor(view.getResources().getColor(R.color.red));
        }


        TextView note = (TextView) view.findViewById(R.id.expandableListItemNote);
        note.setText("Note: "+ChildText.Note);

        TextView credits = (TextView) view.findViewById(R.id.expandableListItemCredits);
        credits.setText("Credits: "+ChildText.Credits);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2)
    {
        return true;
    }
}
