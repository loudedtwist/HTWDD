package de.htwdd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class WizardMatrikel extends Activity
{

    public DatabaseHandlerTimetable db;
    public SharedPreferences app_preferences;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_matrikel);

        db = new DatabaseHandlerTimetable(this);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(getApplicationContext(), WizardWelcome.class);
                startActivity(nextScreen);
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                EditText matnret = (EditText) findViewById(R.id.Matrikel);
                EditText pwret = (EditText) findViewById(R.id.MatrikelPassword);

                String matstring, pwstring = null;
                matstring = matnret.getText().toString();
                pwstring = pwret.getText().toString();

                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WizardMatrikel.this);
                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("matnr", matstring);
                editor.putString("pw", pwstring);
                editor.commit(); // Very important

                Intent nextScreen = new Intent(getApplicationContext(), WizardStudyGroup.class);
                startActivity(nextScreen);
            }
        });

        EditText matnret = (EditText) findViewById(R.id.Matrikel);
        EditText pwret = (EditText) findViewById(R.id.MatrikelPassword);

        matnret.setText(app_preferences.getString("matnr", ""));
        pwret.setText(app_preferences.getString("pw", ""));
    }
}