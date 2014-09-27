package de.htwdd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class WizardBibliothek extends Activity
{
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_bibliothek);

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
                EditText WizardSNummer        = (EditText) findViewById(R.id.WizardSNummer);
                EditText Wizardbibpasswortret = (EditText) findViewById(R.id.bibPasswort);
                EditText WizardRZLogin        = (EditText) findViewById(R.id.WizardRZLogin);

                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WizardBibliothek.this);
                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("bib", WizardSNummer.getText().toString());
                editor.putString("RZLogin", WizardRZLogin.getText().toString());
                editor.putString("bibpw", Wizardbibpasswortret.getText().toString());
                editor.commit();

                Intent nextScreen = new Intent(getApplicationContext(), WizardStudyGroup.class);
                startActivity(nextScreen);
                finish();
            }
        });

        EditText WizardSNummer        = (EditText) findViewById(R.id.WizardSNummer);
        EditText Wizardbibpasswortret = (EditText) findViewById(R.id.bibPasswort);
        EditText WizardRZLogin        = (EditText) findViewById(R.id.WizardRZLogin);

        final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        WizardSNummer.setText(app_preferences.getString("bib",""));
        Wizardbibpasswortret.setText(app_preferences.getString("bibpw",""));
        WizardRZLogin.setText(app_preferences.getString("RZLogin",""));
    }
}