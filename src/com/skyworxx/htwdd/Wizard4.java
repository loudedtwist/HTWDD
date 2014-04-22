package com.skyworxx.htwdd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;



import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;




public class Wizard4 extends Activity {
    /** Called when the activity is first created. */
	
	 ProgressDialog dialog;
	int firststart=0;
	
    public void onCreate(Bundle savedInstanceState) {
    	
    
  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard4);
        
        Button b1=(Button) findViewById(R.id.button1);
        Button b2=(Button) findViewById(R.id.button2);
        final EditText imret = (EditText) findViewById(R.id.editText1);
       final EditText stdgnret =  (EditText) findViewById(R.id.EditText01);
       final EditText stdgrupperet =  (EditText) findViewById(R.id.EditText02);


		
		Spinner abschlussret = (Spinner) findViewById(R.id.spinner1);
		
		
        final Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
             if (firststart>1){
            	 
            	String[] full =  spinner.getSelectedItem().toString().split("/");          
           		imret.setText(full[0]);
           		stdgnret.setText(full[1]);
           		stdgrupperet.setText(full[2]);
           	
             }
         	firststart++;
            } 

            public void onNothingSelected(AdapterView<?> adapterView) {
            	  if (firststart>1);
            	return;
            } 
        });
 
    
   

        
        worker w = new worker();
  		w.execute();    
  	  dialog = ProgressDialog.show(Wizard4.this, "", "Verbinde zur HTW...", true);
       
    
        b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(), Wizard3.class);            
	            startActivity(nextScreen);
			}
		});        
        
        
        
        
        
        b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				
				EditText imret = (EditText) findViewById(R.id.editText1);
				EditText stdgnret = (EditText) findViewById(R.id.EditText01);	
				EditText stdgrupperet = (EditText) findViewById(R.id.EditText02);	
				Spinner abschlussret = (Spinner) findViewById(R.id.spinner1);
				
				String imstring,stdgstring,stdgruppestring,abschlussstring = null;
				abschlussstring="B";
				
				imstring = imret.getText().toString();
				stdgstring = stdgnret.getText().toString();
				stdgruppestring = stdgrupperet.getText().toString();
				
				if (abschlussret.getSelectedItemId()==0)
					abschlussstring = "B";		
					if (abschlussret.getSelectedItemId()==1)
						abschlussstring = "M";		
					if (abschlussret.getSelectedItemId()==2)
						abschlussstring = "D";
					
					
					SharedPreferences app_preferences = PreferenceManager
							.getDefaultSharedPreferences(Wizard4.this);

					SharedPreferences.Editor editor = app_preferences.edit();
				
				

					editor.putString("im", imstring);
					editor.putString("stdg", stdgstring);
					editor.putString("studgruppe", stdgruppestring);
					editor.putString("abschluss", abschlussstring);
					
					editor.commit(); // Very important
				
				
				
				Intent nextScreen = new Intent(getApplicationContext(), Wizard5.class);            
	            startActivity(nextScreen);	finish();			
			}
		});
      
     
		
		
		
		final SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		if (app_preferences.getString("abschluss", "").equals("B"))		
			abschlussret.setSelection(0);
			if (app_preferences.getString("abschluss", "").equals("M"))		
			abschlussret.setSelection(1);
			if (app_preferences.getString("abschluss", "").equals("D"))		
			abschlussret.setSelection(2);
        
			imret.setText(app_preferences.getString("im", ""));
			stdgnret.setText(app_preferences.getString("stdg", ""));
			stdgrupperet.setText(app_preferences.getString("studgruppe", ""));
        
        
    }
    
  
    
	public class worker extends AsyncTask<Object, Void, String[]> {
		@Override
		protected String[] doInBackground(Object... params) {
			
		//	String[] result=null;
			String response="";
			String tokens[] = null;
			try{
				URL url = new URL(
						"http://www2.htw-dresden.de/~rawa/cgi-bin/auf/raiplan_eing.php");
				URLConnection conn = url.openConnection();
			

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
			
				while ((line = rd.readLine()) != null) {
					response += line;
				}
				// wr.close();
				rd.close();
		

				response=response.substring(response.indexOf("verwendeten Gruppen im Stundenplan"));
				
				 tokens = response.split("<td>");
			//	 result=new String[tokens.length-1];
				
				for (int i=1;i<tokens.length;i++){
					tokens[i]=tokens[i].split("</td>")[0];	
					
		
				
					
				}
				tokens[0]="IM/STDG/STDGRP";
			}catch (Exception e) {
				
				}
			

			
			return tokens;
		}

		@Override
		protected void onPostExecute(String[] result) {
			try{
				
				  // Array of choices
		       

		        // Selection of the spinner
		        Spinner spinner = (Spinner) findViewById(R.id.Spinner01);

		        // Application of the Array to the Spinner
		        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Wizard4.this,   android.R.layout.simple_spinner_item, result);
		        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		        spinner.setAdapter(spinnerArrayAdapter);
		        dialog.dismiss();
		    	firststart++;
			
			}catch (Exception e){};
			
			
			
		}
		
		
	}
    

}