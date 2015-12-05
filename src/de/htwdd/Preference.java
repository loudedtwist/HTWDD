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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.htwdd.classes.CONST;
import de.htwdd.fragments.ResponsiveUIActivity;

public class Preference extends SherlockPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 1, 0, "Speichern").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(this, ResponsiveUIActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(CONST.PREFERENCES_AUTO_STUMMSCHALTEN)) {
            Context context = getApplicationContext();
            boolean value = sharedPreferences.getBoolean(key, false);

            if(value == true){
                //start background service:
                VolumeControllerService volumeControllerService = new VolumeControllerService();
                volumeControllerService.StartMultiAlarmVolumeController(context);

                //enable a receiver -> starts alarms on reboot
                ComponentName receiver = new ComponentName(context, VolumeControllerService.HtwddBootReceiver.class);
                PackageManager pm = context.getPackageManager();
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            }
            else{
                //cancel background service:
                VolumeControllerService volumeControllerService = new VolumeControllerService();
                volumeControllerService.cancelMultiAlarmVolumeController(context);
                volumeControllerService.resetSettingsFile(context);

                //disable a receiver, alarms will not be set on reboot
                ComponentName receiver = new ComponentName(context, VolumeControllerService.HtwddBootReceiver.class);
                PackageManager pm = context.getPackageManager();
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }

        }
    }
}