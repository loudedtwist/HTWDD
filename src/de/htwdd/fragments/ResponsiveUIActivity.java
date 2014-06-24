package de.htwdd.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import de.htwdd.DatabaseHandlerNoten;
import de.htwdd.DatabaseHandlerRoomTimetable;
import de.htwdd.Preference;

import de.htwdd.R;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This activity is an example of a responsive Android UI.
 * On phones, the SlidingMenu will be enabled only in portrait mode.
 * In landscape mode, it will present itself as a dual pane layout.
 * On tablets, it will will do the same general thing. In portrait
 * mode, it will enable the SlidingMenu, and in landscape mode, it
 * will be a dual pane layout.
 *
 * @author jeremy
 */
public class ResponsiveUIActivity extends SlidingFragmentActivity implements ActionBar.TabListener
{
    private Fragment mContent;
    public int mode = 0;
    public String lastraum = "";
    public Boolean usedbackbutton = false;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e)
    {
        switch (keycode)
        {
            case KeyEvent.KEYCODE_MENU:
                getSlidingMenu().toggle();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

	/*
	@Override
	public void onBackPressed() {
		//if (!usedbackbutton){
			
			getSlidingMenu().showMenu();
		}
		else
			super.onBackPressed();
		
		usedbackbutton=true;
	}
	*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.responsive_ui);

        setContentView(R.layout.content_frame);

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null)
        {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            // show home as up so we can toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
        {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new CardFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mContent)
                .commit();


//		TypedValue tv = new TypedValue();
//		getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
//		int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);
//	
        // set the Behind View Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment())
                .commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

//		// show the explanation dialog
//		if (savedInstanceState == null)
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.what_is_this)
//			.setMessage(R.string.responsive_explanation)
//			.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                toggle();
        }

        Intent intent;
        final Bundle args = new Bundle();

        switch (item.getItemId())
        {
            // Noten löschen
            case 91:
                getSupportActionBar().setSelectedNavigationItem(0);

                DatabaseHandlerNoten db12 = new DatabaseHandlerNoten(this);
                db12.purge();

                mContent = new NotenFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h2 = new Handler();
                h2.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);

                break;

            // Noten neuladen
            case 95:
                getSupportActionBar().setSelectedNavigationItem(0);

                args.putInt("mode",1);
                mContent = new NotenFragment();
                mContent.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h12 = new Handler();
                h12.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);
                break;

            case 96:
                DatabaseHandlerRoomTimetable db = new DatabaseHandlerRoomTimetable(this);
                db.purge();

                mContent = new BelegungsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h = new Handler();
                h.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);
                break;

            case 97:
                AlertDialog.Builder editalert = new AlertDialog.Builder(this);

                editalert.setTitle("Raum hinzufügen");
                editalert.setMessage("Geb die Raumnummer mit Leerzeichen zwischen Gebäude und Zimmernummer ein\n(z.B. S 305)");


                final EditText input = new EditText(this);
//    		    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//    		    LinearLayout.LayoutParams.FILL_PARENT,
//    		    LinearLayout.LayoutParams.FILL_PARENT);
//    		    input.setLayoutParams(lp);
                editalert.setView(input);

                editalert.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String raum = input.getText().toString();

                        if (!raum.contains(" "))
                            raum = raum.substring(0, 1) + " " + raum.substring(1);
                        String firstchar = raum.substring(0, 1);
                        String leftover = raum.substring(1);
                        firstchar = firstchar.toUpperCase();
                        raum = raum.toUpperCase();

                        Toast.makeText(ResponsiveUIActivity.this, "Suche nach Raum " + firstchar + leftover, Toast.LENGTH_SHORT).show();

                        args.putString("raum", firstchar + leftover);
                        mContent = new BelegungsFragment();
                        mContent.setArguments(args);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, mContent)
                                .commit();
                        Handler h = new Handler();
                        h.postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                getSlidingMenu().showContent();
                            }
                        }, 50);
                    }
                });

                editalert.show();
                break;

            // Stundenplan aktualisieren
            case 98:
                args.putInt("fragmentheight",mContent.getView().getHeight());
                args.putInt("fragmentwidth", mContent.getView().getWidth());
                mContent = new StundenplanFragment();
                mContent.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h22 = new Handler();
                h22.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);
                break;

            case 99:
                intent = new Intent(this, Preference.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
//	}

    public void switchContent(final Fragment fragment, int position)
    {
        mode = position;
        this.invalidateOptionsMenu();

        ActionBar.Tab tab, tab2, tab3;

        switch (position)
        {
            case 1:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setTitle("Übersicht");
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                mContent = new CardFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h = new Handler();
                h.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);

                break;

            case 3:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                int week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
                int nextweek = week + 1;
                nextweek %= 52;

                getSupportActionBar().setTitle("Stundenplan");
                tab = getSupportActionBar().newTab();
                tab.setText("aktuelle Woche (" + week + ")");
                tab.setTabListener(this);
                getSupportActionBar().addTab(tab);

                tab2 = getSupportActionBar().newTab();
                tab2.setText("nächste Woche (" + nextweek + ")");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);
                break;

            case 4:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                getSupportActionBar().setTitle("Mensa");
                tab = getSupportActionBar().newTab();
                tab.setText("Heute");
                tab.setTabListener(this);
                getSupportActionBar().addTab(tab);

                tab2 = getSupportActionBar().newTab();
                tab2.setText("Woche");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);
                break;

            case 5:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                getSupportActionBar().setTitle("Noten & Prüfungen");
                tab = getSupportActionBar().newTab();
                tab.setText("Noten");
                tab.setTabListener(this);
                getSupportActionBar().addTab(tab);

                tab2 = getSupportActionBar().newTab();
                tab2.setText("Statistik");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);

                tab3 = getSupportActionBar().newTab();
                tab3.setText("Prüfungen");
                tab3.setTabListener(this);
                getSupportActionBar().addTab(tab3);
                break;

            case 6:
                try
                {
                    getSupportActionBar().removeAllTabs();
                    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    getSupportActionBar().setTitle("Belegungsplan");

                    mContent = new BelegungsFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, mContent)
                            .commit();
                    Handler h53 = new Handler();
                    h53.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            getSlidingMenu().showContent();
                        }
                    }, 50);
                } catch (Exception e)
                {
                }

                break;

            case 8:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                getSupportActionBar().setTitle("Career Service");
                tab = getSupportActionBar().newTab();
                tab.setText("Events");
                tab.setTabListener(this);
                getSupportActionBar().addTab(tab);

                tab2 = getSupportActionBar().newTab();
                tab2.setText("Workshops");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);

                tab3 = getSupportActionBar().newTab();
                tab3.setText("Beratung");
                tab3.setTabListener(this);
                getSupportActionBar().addTab(tab3);
                break;

            case 9:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                getSupportActionBar().setTitle("Mentoring");
                tab = getSupportActionBar().newTab();
                tab.setText("Mentoren");
                tab.setTabListener(this);
                getSupportActionBar().addTab(tab);

                tab2 = getSupportActionBar().newTab();
                tab2.setText("Aktuelles");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);

                tab3 = getSupportActionBar().newTab();
                tab3.setText("Kontakt");
                tab3.setTabListener(this);
                getSupportActionBar().addTab(tab3);
                break;

            case 10:
                getSupportActionBar().removeAllTabs();

                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getSupportActionBar().setTitle("Verwaltung");

                mContent = new SemesterplanFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h513 = new Handler();
                h513.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);

                break;

            case 11:
                getSupportActionBar().removeAllTabs();
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                getSupportActionBar().setTitle("Bibliothek");
                tab = getSupportActionBar().newTab();

                tab2 = getSupportActionBar().newTab();
                tab2.setText("Rückgabe");
                tab2.setTabListener(this);
                getSupportActionBar().addTab(tab2);

                tab3 = getSupportActionBar().newTab();
                tab3.setText("Suche");
                tab3.setTabListener(this);
                getSupportActionBar().addTab(tab3);
                break;

            case 13:
                Intent intent2 = new Intent(this, Preference.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                break;

            case 14:
                PackageManager manager = this.getPackageManager();
                PackageInfo info = null;
                try
                {
                    info = manager.getPackageInfo(this.getPackageName(), 0);
                } catch (NameNotFoundException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                getSupportActionBar().removeAllTabs();

                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getSupportActionBar().setTitle("HTWDD V" + info.versionName);


                mContent = new About();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, mContent)
                        .commit();
                Handler h2513 = new Handler();
                h2513.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        getSlidingMenu().showContent();
                    }
                }, 50);
                break;

            case 18:
                AlertDialog alertDialog12 = new AlertDialog.Builder(ResponsiveUIActivity.this).create();

                // Setting Dialog Title
                alertDialog12.setTitle("Cache Löschen");

                // Setting Dialog Message
                alertDialog12.setMessage("Der Cache (inklusive Stundenplan und Noten) wird gelöscht.");

                // Setting Icon to Dialog
                alertDialog12.setIcon(R.drawable.ic_launcher);

                alertDialog12.setButton2("zurück", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User clicked OK button
                    }
                });
                alertDialog12.setButton("Fortfahren", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        clearApplicationData();
                        ResponsiveUIActivity.this.finish();
                    }
                });

                // Showing Alert Message
                alertDialog12.show();
                break;

            default:
                break;
        }
    }

    public void clearApplicationData()
    {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists())
        {
            String[] children = appDir.list();
            for (String s : children)
            {
                if (!s.equals("lib"))
                {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    public void onBirdPressed(int pos)
    {
        //	Intent intent = BirdActivity.newInstance(this, pos);
        //	startActivity(intent);
    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
        int week    = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
        Bundle args = new Bundle();
        int nextweek = (week + 1)%52;

        // Übersicht Raumplan
        if ((mode == 6) && lastraum != null)
        {
            args.putInt("fragmentheight",mContent.getView().getHeight());
            args.putInt("fragmentwidth", mContent.getView().getWidth());
            args.putString("raum", lastraum);

            if (tab.getText().toString().contains("aktuelle"))
                args.putInt("weekID", week);
            else if (tab.getText().toString().contains("nächste"))
                args.putInt("weekID", nextweek);

            mContent = new RaumplanFragment();
        }
        // Stundenplan
        else if(mode == 3)
        {
            args.putInt("fragmentheight",mContent.getView().getHeight());
            args.putInt("fragmentwidth", mContent.getView().getWidth());

            if (tab.getText().toString().contains("aktuelle"))
                args.putInt("weekID", week);
            else if (tab.getText().toString().contains("nächste"))
                args.putInt("weekID", nextweek);

            mContent = new StundenplanFragment();
        }





        if (tab.getText().equals("Rückgabe"))
            mContent = new BiblioFragment();

        if (tab.getText().equals("Suche"))
            mContent = new BiblioSearchFragment();

        // Career Service
        if (tab.getText().equals("Events"))
        {
            args.putInt("mode",0);
            mContent = new CareerFragment();
        }
        else if (tab.getText().equals("Workshops"))
        {
            args.putInt("mode",1);
            mContent = new CareerFragment();
        }
        // Mentoring
        else if (tab.getText().equals("Mentoren"))
            mContent = new MentoringFragment();
        else if (tab.getText().equals("Aktuelles"))
        {
            args.putInt("mode", 1);
            mContent = new MentoringFragment();
        }
        else if (tab.getText().equals("Kontakt"))
        {
            args.putInt("mode", 2);
            mContent = new MentoringFragment();
        }
        // Mensa
        else if (tab.getText().equals("Heute") && (mode == 4))
            mContent = new MensaFragment();
        // Noten
        else if (tab.getText().equals("Noten"))
            mContent = new NotenFragment();
        else if (tab.getText().equals("Statistik"))
            mContent = new NotenStatsFragment();








        if (tab.getText().equals("Beratung"))
            mContent = new CareerBeratungFragment();





        if (tab.getText().equals("Prüfungen"))
            mContent = new PrufungenFragment();



        if (tab.getText().equals("Woche") && (mode == 4))
            mContent = new MensaWocheFragment();

        // Argumente übergeben
        mContent.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            public void run()
            {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        switch (mode)
        {
            case 3:
                menu.add(0, 98, 0, "Update")
                        .setIcon(R.drawable.ic_menu_refresh)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;

            case 5:
                menu.add(0, 91, 0, "Purge")
                        .setIcon(R.drawable.ic_menu_delete)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                menu.add(0, 95, 0, "Update")
                        .setIcon(R.drawable.ic_menu_refresh)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;

            case 6:
                menu.add(0, 96, 0, "Purge")
                        .setIcon(R.drawable.ic_menu_delete)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                menu.add(0, 97, 0, "Hinzufügen")
                        .setIcon(R.drawable.ic_menu_add)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;

            default:
                break;
        }

        return true;
    }

    public void showRaum(String raum)
    {
        Bundle args = new Bundle();
        args.putString("raum", raum);
        mContent = new BelegungsFragment();
        mContent.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mContent)
                .commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            public void run()
            {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    public void switchtoRoom(String raum)
    {
        lastraum = raum;

        int week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
        int nextweek = week + 1;
        nextweek %= 52;

        ActionBar.Tab tab, tab2;

        getSupportActionBar().removeAllTabs();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        getSupportActionBar().setTitle("Belegungsplan");

        tab = getSupportActionBar().newTab();
        tab.setText("aktuelle Woche (" + week + ")");
        tab.setTabListener(this);
        getSupportActionBar().addTab(tab);

        tab2 = getSupportActionBar().newTab();
        tab2.setText("nächste Woche (" + nextweek + ")");
        tab2.setTabListener(this);
        getSupportActionBar().addTab(tab2);
    }
}