package de.htwdd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import de.htwdd.MyRoomAdapter;
import de.htwdd.R;


public class RaumplanFragment extends Fragment
{
    public int fragmentwidth, fragmentheight;
    public int week_id;
    public String raum;

    public RaumplanFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentheight  = getArguments().getInt("fragmentheight");
        fragmentwidth   = getArguments().getInt("fragmentwidth");
        week_id         = getArguments().getInt("weekID");
        raum            = getArguments().getString("raum");

        return inflater.inflate(R.layout.stundenplan, null);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showweek(week_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        showweek(week_id);
    }


    public void showweek(int week)
    {
        if (week == 0)
            return;

        int planweek = week % 2;
        if (planweek == 0)
            planweek = 2;

        GridView gridview = (GridView) getView().findViewById(R.id.grid);
        gridview.setAdapter(new MyRoomAdapter(getActivity(), planweek, fragmentwidth, fragmentheight, raum));
        gridview.setColumnWidth(fragmentwidth / 6);

        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);
        p.setVisibility(View.GONE);

        gridview.setVisibility(View.VISIBLE);
    }
}
