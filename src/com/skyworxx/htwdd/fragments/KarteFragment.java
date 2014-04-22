package com.skyworxx.htwdd.fragments;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


/*import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;*/
import com.skyworxx.htwdd.BelegungsAdapter;
import com.skyworxx.htwdd.DatabaseHandlerTimetable;
import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.SmallMensaArrayAdapter;
import com.skyworxx.htwdd.Wizard1;

import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.Type_Stunde;

public class KarteFragment extends Fragment {
public int mapid;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		/*
		To Whomever sees this piece of code:
		
		I Know this is a terrible, terrible, terrible hack.
		There is no other easy way to embed a mapfragment in a sliding layout.
		My reason behind this is: a user will never open a map more than 5 times without restarting the app.
		So why create a custom mapfragment from scratch whe only 0.01% of all users will encounter this problem?
		
		Still, I'm sorry for you seeing this.
		
		
		*/
		
		
		try{
        	mapid=0;
		return inflater.inflate(R.layout.maps, container, false);   
        } catch (Exception e){
        	
        	try {
        		mapid=1;
        	 return inflater.inflate(R.layout.maps2, container, false);  	
        	}
        	catch (Exception e2){
        		

            	try {
            		mapid=2;
            	 return inflater.inflate(R.layout.maps3, container, false);  	
            	}
            	catch (Exception e3){
            		
            		

                	try {
                		mapid=3;
                	 return inflater.inflate(R.layout.maps4, container, false);  	
                	}
                	catch (Exception e4){
                		

                    	try {
                    		mapid=4;
                    	 return inflater.inflate(R.layout.maps5, container, false);  	
                    	}
                    	catch (Exception e5){};
                		
                		
                	};
            		
            	};
        		
        		
        	};
        };
        
        return null;
    }
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    /*    
		 GoogleMap mMap;
		 SupportMapFragment mMapFragment=null;

		if (mapid==0)mMapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map));
		if (mapid==1) mMapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map2));
		if (mapid==2) mMapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map3));
		if (mapid==3) mMapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map4));
		if (mapid==4) mMapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map5));
		
		mMap = mMapFragment.getMap();

		mMap.addMarker(new MarkerOptions()
		        .position(new LatLng(51.03742,  	13.73526))
		        .title("Z-Gebäude"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03628,  	13.73520))
        .title("S-Gebäude"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03617,  	13.73561))
        .title("Audimax S239"));
		
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03630,  	 	13.73602))
        .title("Bibliothek"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03542,  	 13.73560))
        .title("N-Gebäude"));

		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03421,  	 13.73413))
        .title("Mensa Reichenbachstraße"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03495,  13.73532))
        .title("Netto"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03549,  13.73630))
        .title("A-Gebäude"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03532,  13.73693))
        .title("L-Gebäude"));
		
		mMap.addMarker(new MarkerOptions()
        .position(new LatLng(51.03365,  13.73555))
        .title("Dönermann"));

		*/
		
	}
	

	
}
