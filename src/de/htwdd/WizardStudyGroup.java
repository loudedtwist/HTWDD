package de.htwdd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class WizardStudyGroup extends Activity
{
    /**
     * Called when the activity is first created.
     */
    ProgressDialog dialog;
    int firststart = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_study_group);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        final EditText imret = (EditText) findViewById(R.id.bibliotheksnummer);
        final EditText stdgnret = (EditText) findViewById(R.id.studiengang);
        final EditText stdgrupperet = (EditText) findViewById(R.id.bibPasswort);
        final Spinner spinner = (Spinner) findViewById(R.id.schnellauswahl);
        Spinner abschlussret = (Spinner) findViewById(R.id.abschluss);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (firststart > 1)
                {
                    String[] full = spinner.getSelectedItem().toString().split("/");
                    imret.setText(full[0]);
                    stdgnret.setText(full[1]);
                    stdgrupperet.setText(full[2]);
                }
                firststart++;
            }

            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        worker w = new worker();
        w.execute();
        dialog = ProgressDialog.show(WizardStudyGroup.this, "", "Verbinde zur HTW...", true);

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent nextScreen = new Intent(getApplicationContext(), WizardMatrikel.class);
                startActivity(nextScreen);
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                EditText imret = (EditText) findViewById(R.id.bibliotheksnummer);
                EditText stdgnret = (EditText) findViewById(R.id.studiengang);
                EditText stdgrupperet = (EditText) findViewById(R.id.bibPasswort);
                Spinner abschlussret = (Spinner) findViewById(R.id.abschluss);

                String imstring, stdgstring, stdgruppestring, abschlussstring = null;
                abschlussstring = "B";

                imstring = imret.getText().toString();
                stdgstring = stdgnret.getText().toString();
                stdgruppestring = stdgrupperet.getText().toString();

                if (abschlussret.getSelectedItemId() == 0)
                    abschlussstring = "B";
                else if (abschlussret.getSelectedItemId() == 1)
                    abschlussstring = "M";
                else if (abschlussret.getSelectedItemId() == 2)
                    abschlussstring = "D";

                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WizardStudyGroup.this);
                SharedPreferences.Editor editor = app_preferences.edit();

                editor.putString("im", imstring);
                editor.putString("stdg", stdgstring);
                editor.putString("studgruppe", stdgruppestring);
                editor.putString("abschluss", abschlussstring);
                editor.commit(); // Very important

                Intent nextScreen = new Intent(getApplicationContext(), WizardBibliothek.class);
                startActivity(nextScreen);
                finish();
            }
        });


        final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (app_preferences.getString("abschluss", "").equals("B"))
            abschlussret.setSelection(0);
        else if (app_preferences.getString("abschluss", "").equals("M"))
            abschlussret.setSelection(1);
        else if (app_preferences.getString("abschluss", "").equals("D"))
            abschlussret.setSelection(2);

        imret.setText(app_preferences.getString("im", ""));
        stdgnret.setText(app_preferences.getString("stdg", ""));
        stdgrupperet.setText(app_preferences.getString("studgruppe", ""));
    }


    public class worker extends AsyncTask<Object, Void, String[]>
    {
        @Override
        protected String[] doInBackground(Object... params)
        {
            String response = "";
            String tokens[] = null;
            try
            {
                URL url = new URL("http://www2.htw-dresden.de/~rawa/cgi-bin/auf/raiplan_eing.php");
                URLConnection conn = url.openConnection();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null)
                    response += line;

                rd.close();

                response = response.substring(response.indexOf("verwendeten Gruppen im Stundenplan"));

                tokens = response.split("<td>");

                for (int i = 1; i < tokens.length; i++)
                    tokens[i] = tokens[i].split("</td>")[0];

                tokens[0] = "IM/STDG/STDGRP";
            } catch (Exception e)
            {
            }

            return tokens;
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            try
            {
                // Selection of the spinner
                Spinner spinner = (Spinner) findViewById(R.id.schnellauswahl);

                // Application of the Array to the Spinner
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(WizardStudyGroup.this, android.R.layout.simple_spinner_item, result);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                spinner.setAdapter(spinnerArrayAdapter);
                dialog.dismiss();
                firststart++;
            } catch (Exception e)
            {
            }

        }
    }
}