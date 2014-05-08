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

    public RaumplanFragment(int fragmentwidth, int fragmentheight, int i, String raum)
    {

        this.fragmentheight = fragmentheight;
        this.fragmentwidth = fragmentwidth;
        week_id = i;
        ;
        this.raum = raum;
    }

    public RaumplanFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.stundenplan, null);

    }

    @Override
    public void onResume()
    {
        super.onResume();

        //worker w = new worker();
        //w.execute();
        showweek(week_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //worker w = new worker();
        //w.execute();
        showweek(week_id);
    }


    public void showweek(int week)
    {

        if (week == 0)
        {


            return;
        }


        //    try {

        GridView gridview = (GridView) getView().findViewById(R.id.grid);


        int width = fragmentwidth;
        int height = fragmentheight;
        int planweek = week % 2;
        if (planweek == 0) planweek = 2;

        gridview.setAdapter(new MyRoomAdapter(getActivity(), planweek, width, height, raum));
        gridview.setColumnWidth(width / 6);

        ProgressBar p = (ProgressBar) getView().findViewById(R.id.waitIndicator);

        p.setVisibility(View.GONE);

        gridview.setVisibility(View.VISIBLE);

//	        
//	        }catch (Exception e){
//
//	        	int a=0;
//	        	a++;
//	        	
//	        }

    }


}
