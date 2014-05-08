/*
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.htwdd;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.htwdd.R;
import de.htwdd.fragments.ResponsiveUIActivity;

public class Preference extends SherlockPreferenceActivity
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        menu.add("Save")
//            .setIcon(R.drawable.ic_compose)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(0, 1, 0, "Speichern")

                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        //  getSupportActionBar().setIcon(R.drawable.ic_pref);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.layout.preferences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case android.R.id.home:
                intent = new Intent(this, ResponsiveUIActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            case 1: //Speichern
                intent = new Intent(this, ResponsiveUIActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;

            default:
                ;
        }


        return true;
    }


}
