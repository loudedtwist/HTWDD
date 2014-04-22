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
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;



import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.skyworxx.htwdd.fragments.ResponsiveUIActivity;



public class Wizard6 extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard6);
        
        Button b1=(Button) findViewById(R.id.button1);
        Button b2=(Button) findViewById(R.id.button2);
       
        
        b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(getApplicationContext(), Wizard5.class);            
	            startActivity(nextScreen);
			}
		});         
        
        b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				
				
				SharedPreferences app_preferences = PreferenceManager
						.getDefaultSharedPreferences(Wizard6.this);

				SharedPreferences.Editor editor = app_preferences.edit();
			
			

			
				editor.putString("wizardrun", "yes");
				
				editor.commit(); // Very important
				
				
				
				
				
				
				Intent nextScreen = new Intent(getApplicationContext(), ResponsiveUIActivity.class);            
	            startActivity(nextScreen);
				
				finish();
				
			}
		});
      
        
        
        
        
        
        
    }
    
  
    
    

}