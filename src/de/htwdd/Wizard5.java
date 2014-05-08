package de.htwdd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.htwdd.R;


public class Wizard5 extends Activity
{
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard5);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);


        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(getApplicationContext(), Wizard4.class);
                startActivity(nextScreen);
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {


                EditText bibnummerret = (EditText) findViewById(R.id.editText1);
                EditText bibpasswortret = (EditText) findViewById(R.id.EditText02);


                String bibnummerstring, bibpasswortstring = null;

                bibnummerstring = bibnummerret.getText().toString();
                bibpasswortstring = bibpasswortret.getText().toString();

                if (bibnummerstring.contains("s")) bibnummerstring = bibnummerstring.substring(1);


                SharedPreferences app_preferences = PreferenceManager
                        .getDefaultSharedPreferences(Wizard5.this);

                SharedPreferences.Editor editor = app_preferences.edit();


                editor.putString("bib", bibnummerstring);
                editor.putString("bibpw", bibpasswortstring);
                editor.commit(); // Very important


                Intent nextScreen = new Intent(getApplicationContext(), Wizard6.class);
                startActivity(nextScreen);
                finish();


            }
        });

        EditText bibnummerret = (EditText) findViewById(R.id.editText1);
        EditText bibpasswortret = (EditText) findViewById(R.id.EditText02);

        final SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        bibnummerret.setText(app_preferences.getString("bib", ""));
        bibpasswortret.setText(app_preferences.getString("bibpw", ""));


    }


}