package de.htwdd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.htwdd.fragments.RoomTimetableDetailsFragment;

public class RoomTimetableDetailsActivity extends SlidingFragmentActivity implements ActionBar.TabListener
{
    private String room;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.activity_frame);
        getSlidingMenu().setSlidingEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Hole Parameter
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("room").isEmpty())
            return;

        // Hole Raum
        room = bundle.getString("room");

        // ActionBar anpassen, Tabs hinzufügen
        getSupportActionBar().removeAllTabs();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setTitle(getString(R.string.title_activity_room_timetable_details) + " " +room);

        int week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
        int nextweek = week==52?1:week+1;

        ActionBar.Tab tab, tab2;
        tab = getSupportActionBar().newTab();
        tab.setText("aktuelle Woche (" + week + ")");
        tab.setTabListener(this);
        getSupportActionBar().addTab(tab);

        tab2 = getSupportActionBar().newTab();
        tab2.setText("nächste Woche (" + nextweek + ")");
        tab2.setTabListener(this);
        getSupportActionBar().addTab(tab2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        Fragment mContent;
        int week    = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
        int nextweek= week==52?1:week+1;

        Bundle bundle = new Bundle();
        bundle.putString("room", room);

        switch (tab.getPosition())
        {
            case 0:
                bundle.putInt("week", week);
                break;
            case 1:
                bundle.putInt("week", nextweek);
                break;
        }
        mContent = new RoomTimetableDetailsFragment();
        mContent.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }
}
