package com.skyworxx.htwdd.fragments;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.DatabaseHandlerRoomTimetable;
import com.skyworxx.htwdd.DatabaseHandlerTimetable;
import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.MyAdapter;
import com.skyworxx.htwdd.MyRoomAdapter;
import com.skyworxx.htwdd.R;


import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.Type_Stunde;


public class RaumplanFragment extends Fragment {
public int fragmentwidth,fragmentheight;
public int week_id;	
public String raum;

public RaumplanFragment(int fragmentwidth,int fragmentheight, int i,String raum){
	
	this.fragmentheight=fragmentheight;
	this.fragmentwidth=fragmentwidth;
week_id=i;;
this.raum=raum;
}
public RaumplanFragment(){
	
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.stundenplan, null);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//worker w = new worker();
		//w.execute();
		showweek(week_id);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//worker w = new worker();
		//w.execute();
		showweek(week_id);
	}
	
	
	
	 public void showweek(int week){
    	 
		 if (week==0){
			 
			
		 return;
		 }
		 
		 
	    //    try { 
	     	   
		GridView gridview = (GridView) getView().findViewById(R.id.grid);
	       
	     
	    	
	        int width =  fragmentwidth;
	        int height = fragmentheight;         
	    	int planweek=week%2;
	        if (planweek==0) planweek=2;           
	   
	        gridview.setAdapter(new MyRoomAdapter(getActivity(),planweek,width,height,raum));
	        gridview.setColumnWidth(width/6);
	        
	        ProgressBar p= (ProgressBar) getView().findViewById(R.id.waitIndicator);
			
			p.setVisibility(View.GONE);
			
			gridview .setVisibility(View.VISIBLE);
			
//	        
//	        }catch (Exception e){
//
//	        	int a=0;
//	        	a++;
//	        	
//	        }
	    	
	    }
	    
	 
}
