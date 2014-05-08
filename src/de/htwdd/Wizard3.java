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


public class Wizard3 extends Activity
{

    public DatabaseHandlerTimetable db;
    public SharedPreferences app_preferences;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard3);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);

        db = new DatabaseHandlerTimetable(this);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);


        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(getApplicationContext(), Wizard1.class);
                startActivity(nextScreen);
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {

                EditText matnret = (EditText) findViewById(R.id.EditText01);
                EditText pwret = (EditText) findViewById(R.id.editText1);

                String matstring, pwstring = null;
                matstring = matnret.getText().toString();
                pwstring = pwret.getText().toString();

                SharedPreferences app_preferences = PreferenceManager
                        .getDefaultSharedPreferences(Wizard3.this);

                SharedPreferences.Editor editor = app_preferences.edit();


                editor.putString("matnr", matstring);
                editor.putString("pw", pwstring);
                editor.commit(); // Very important


//				Stundenplan s=new Stundenplan();
//				worker w = s.new worker();
//				w.execute(db, app_preferences);


                Intent nextScreen = new Intent(getApplicationContext(), Wizard4.class);
                startActivity(nextScreen);


            }
        });


        EditText matnret = (EditText) findViewById(R.id.EditText01);
        EditText pwret = (EditText) findViewById(R.id.editText1);


        matnret.setText(app_preferences.getString("matnr", ""));

        pwret.setText(app_preferences.getString("pw", ""));


    }


}