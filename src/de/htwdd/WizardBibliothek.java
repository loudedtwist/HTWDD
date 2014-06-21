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
                Intent nextScreen = new Intent(getApplicationContext(), WizardStudyGroup.class);
                startActivity(nextScreen);
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                EditText bibnummerret = (EditText) findViewById(R.id.bibliotheksnummer);
                EditText bibpasswortret = (EditText) findViewById(R.id.bibPasswort);

                String bibnummerstring, bibpasswortstring = null;

                bibnummerstring = bibnummerret.getText().toString();
                bibpasswortstring = bibpasswortret.getText().toString();

                if (bibnummerstring.contains("s"))
                    bibnummerstring = bibnummerstring.substring(1);

                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WizardBibliothek.this);
                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("bib", bibnummerstring);
                editor.putString("bibpw", bibpasswortstring);
                editor.commit(); // Very important

                Intent nextScreen = new Intent(getApplicationContext(), WizardFinal.class);
                startActivity(nextScreen);
                finish();
            }
        });

        EditText bibnummerret = (EditText) findViewById(R.id.bibliotheksnummer);
        EditText bibpasswortret = (EditText) findViewById(R.id.bibPasswort);

        final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bibnummerret.setText(app_preferences.getString("bib", ""));
        bibpasswortret.setText(app_preferences.getString("bibpw", ""));
    }
}