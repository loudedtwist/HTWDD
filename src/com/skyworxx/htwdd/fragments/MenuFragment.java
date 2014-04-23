package com.skyworxx.htwdd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.R;

public class MenuFragment extends ListFragment
{
    public int menuheight;


    public MenuFragment()
    {

    }

    public MenuFragment(int height)
    {
        // TODO Auto-generated constructor stub
        menuheight = height;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        String[] birds = getResources().getStringArray(R.array.birds);
        MenuArrayAdapter colorAdapter = new MenuArrayAdapter(getActivity(), birds, menuheight);
        setListAdapter(colorAdapter);
    }


    @Override
    public void onListItemClick(ListView lv, View v, int position, long id)
    {


        if ((position != 0) && (position != 3) && (position != 9) && (position != 14))
        {
            Fragment newContent = new CardFragment();
            if (newContent != null)
                switchFragment(newContent, position);
        }
    }


    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment, int position)
    {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof ResponsiveUIActivity)
        {
            ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
            ra.usedbackbutton = false;
            ra.switchContent(fragment, position);
        }
    }
}
