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



public class Wizard5 extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard5);
        
        Button b1=(Button) findViewById(R.id.button1);
        Button b2=(Button) findViewById(R.id.button2);
       
        
        b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(), Wizard4.class);            
	            startActivity(nextScreen);
			}
		});     
        
        b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				
				EditText bibnummerret = (EditText) findViewById(R.id.editText1);
				EditText bibpasswortret = (EditText) findViewById(R.id.EditText02);
				
				
				String bibnummerstring,bibpasswortstring = null;
				
				bibnummerstring=bibnummerret.getText().toString();
				bibpasswortstring=bibpasswortret.getText().toString();
				
				if (bibnummerstring.contains("s")) bibnummerstring=bibnummerstring.substring(1);
				
				
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