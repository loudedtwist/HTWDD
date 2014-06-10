package de.htwdd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import de.htwdd.R;
import de.htwdd.fragments.MenuFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends SlidingFragmentActivity
{

    private int mTitleRes;
    protected ListFragment mFrag;

    public BaseActivity(int titleRes)
    {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        mFrag = new MenuFragment();
        t.replace(R.id.menu_frame, mFrag);
        t.commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                toggle();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    public class BasePagerAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> mFragments = new ArrayList<Fragment>();
        private ViewPager mPager;

        public BasePagerAdapter(FragmentManager fm, ViewPager vp)
        {
            super(fm);
            mPager = vp;
            mPager.setAdapter(this);
            for (int i = 0; i < 3; i++)
            {
                addTab(new MenuFragment());
            }
        }

        public void addTab(Fragment frag)
        {
            mFragments.add(frag);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragments.size();
        }
    }

}
