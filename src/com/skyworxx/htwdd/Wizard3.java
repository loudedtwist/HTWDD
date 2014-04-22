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




public class Wizard3 extends Activity {
	
	public DatabaseHandlerTimetable db;
	public SharedPreferences app_preferences;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard3);
        
        Button b1=(Button) findViewById(R.id.button1);
        Button b2=(Button) findViewById(R.id.button2);
       
       db = new DatabaseHandlerTimetable(this);             
	app_preferences = PreferenceManager.getDefaultSharedPreferences(this);       	
        
        
        
        b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(), Wizard1.class);            
	            startActivity(nextScreen);
			}
		});        
        
        b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				EditText matnret = (EditText) findViewById(R.id.EditText01);		
				EditText pwret = (EditText) findViewById(R.id.editText1);
					 
				String matstring,pwstring = null;				
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