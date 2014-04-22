package com.skyworxx.htwdd.fragments;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.EventPopup;
import com.skyworxx.htwdd.HTTPDownloader;

import com.skyworxx.htwdd.CareerArrayAdapter;
import com.skyworxx.htwdd.EditTimetable;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.SpecialAdapter3;
import com.skyworxx.htwdd.TerminAnfragePopup;
import com.skyworxx.htwdd.datepicker;
import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.TEvent;


public  class CareerBeratungFragment extends Fragment {

	 public int mode;
	 public CareerBeratungFragment() {
		
		}
	 
	public CareerBeratungFragment(int i) {
		// TODO Auto-generated constructor stub
	mode=i;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
			 return inflater.inflate(R.layout.career_beratung, null);
	
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		



Button button,button2;
int currentapiVersion = android.os.Build.VERSION.SDK_INT;
if (currentapiVersion >= 14){
	//?android:attr/borderlessButtonStyle

 button =new Button(getActivity(), null, android.R.attr.borderlessButtonStyle); 
		//	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
 button.setTextColor(Color.parseColor("#33B5E5"));
 
 button2 =new Button(getActivity(), null, android.R.attr.borderlessButtonStyle); 
	//	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
button2.setTextColor(Color.parseColor("#33B5E5"));

}
else{
	 button =new Button(getActivity(), null, android.R.attr.buttonStyleSmall); 
button2 =new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
}
button.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

button.setText("Termin anfragen");

button2.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

button2.setText("Jobbörse im Browser öffnen");

LinearLayout ln=(LinearLayout) getActivity().findViewById(R.id.menulayout);
LinearLayout jln=(LinearLayout) getActivity().findViewById(R.id.joblayout);
LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

ln.addView(button, lp);
jln.addView(button2, lp);

button.setOnClickListener(new MyOnClickListener());
button2.setOnClickListener(new MyOnClickListener2());





	}
	
	
	
	 
	class MyOnClickListener implements OnClickListener  
	    {        
	     public void onClick(View v)  
	     {  
	           Intent i = new Intent(getActivity(), datepicker.class);
	           getActivity().startActivity(i);
	     }  
	    }  
	
	class MyOnClickListener2 implements OnClickListener  
    {        
     public void onClick(View v)  
     {  
    
    	 Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.htw-dresden.de/jobboerse/"));
    	 getActivity().startActivity(i);
     }  
    } 
	
}
