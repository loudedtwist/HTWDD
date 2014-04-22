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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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




public class Wizard2 extends Fragment {
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wizard2, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		  Button b1=(Button) getActivity().findViewById(R.id.button1);
	        Button b2=(Button) getActivity().findViewById(R.id.button2);
	       
	         	
	        
	        
	        
	        b1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=VYWUBBQ6T6PT2"));
	            	startActivity(browserIntent);
	            	
				}
			});        
	        
	        b2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.Mark-Schramm.de"));
	            	startActivity(browserIntent);
					
				}
			});
	      
	      
			
		
	}
	


  
    
    

}