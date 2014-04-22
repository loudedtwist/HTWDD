package com.skyworxx.htwdd.fragments;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.EventPopup;
import com.skyworxx.htwdd.HTTPDownloader;

import com.skyworxx.htwdd.CareerArrayAdapter;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.SpecialAdapter3;
import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.TEvent;


public class CareerFragment extends Fragment {
	public String[] links;
	public String[] titles;
	public String[] location;
	public String[] shortinfo;
	 public  LinearLayout wait;
	 public int mode;
	 
	 public CareerFragment() {
			
		}
	 
	public CareerFragment(int i) {
		// TODO Auto-generated constructor stub
	mode=i;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mode!=2) return inflater.inflate(R.layout.career, null);
		else
			 return inflater.inflate(R.layout.career_beratung, null);
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	//	Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
		
		if (mode !=2){
		worker w = new worker();
		 w.execute(mode);
		
		
		wait = (LinearLayout) getView().findViewById(R.id.waitheute);
   	 wait.setVisibility(View.VISIBLE);
		}else
			
		{
			

			
		}
	}
	
	
	private class worker extends AsyncTask<Object, Integer, TEvent[]> {
		

		@Override
		protected TEvent[] doInBackground(Object... params) {

			HTTPDownloader downloader =null;
			if (mode==0) downloader=new HTTPDownloader("http://www.htw-dresden.de/fileadmin/userfiles/wiwi/Career_Service/Dateien_fuer_HTWDD_App/Termine_fuer_HTWDD_App_Veranstaltungen.csv");
			if (mode==1) downloader=new HTTPDownloader("http://www.htw-dresden.de/fileadmin/userfiles/wiwi/Career_Service/Dateien_fuer_HTWDD_App/Termine_fuer_HTWDD_App_Workshops.csv");
			
			
			List<String> result=downloader.getCSV();
			
			String[] events_s= new String[result.size()];
			
			TEvent[] events=new TEvent[events_s.length];
			
			
				for (int i=0;i<events_s.length;i++){
					
					
					events_s[i]=result.get(i);
					
					String details=events_s[i];
					
					String[] details2=details.split(";");
					events[i]=new TEvent();
	
					if (details2.length>0)
					events[i].title=details2[0];
					
				
					if (details2.length==7){

						if (details2.length>2)
							events[i].trainer=details2[1];
						else
							events[i].trainer="";
						
						if (details2.length>3)
							events[i].datum="Datum: "+details2[2]+" - Uhrzeit: "+details2[3];
						else
							events[i].datum="";
						
						if (details2.length>4)
							events[i].raum=details2[4];
						else
							events[i].raum="";
						
						if (details2.length>5)
							events[i].desc=details2[5];
						else
							events[i].desc="";
						
						if (details2.length>6)
							events[i].link=details2[6];
						else
							events[i].link="";
						
					}
					else {
						events[i].trainer=details2[1];
						
						if (details2.length>2)
							events[i].datum="Datum: "+details2[2]+" - Uhrzeit: "+details2[3];
						else
							events[i].datum="";
						
						//if (details2.length>3)
						//	events[i].raum=details2[3];
					//	else
							events[i].raum="";
						
						if (details2.length>4)
							events[i].desc=details2[4];
						else
							events[i].desc="";
						
						if (details2.length>5)
							events[i].link=details2[5];
						else
							events[i].link="";
						
				}
					
			
				
				
			}
			
		return events;
			
		}
	
		protected void onProgressUpdate(Integer... progress) {
	      	
	
	     }

		
		@Override
		protected void onPostExecute(TEvent[] events) {
			try{
			//	ProgressBar p= (ProgressBar) getView().findViewById(R.id.waitIndicator);
			
	//	p.setVisibility(View.GONE);
		
		ListView l= (ListView) getView().findViewById(R.id.eventlist);
		l.setVisibility(View.VISIBLE);
		l.setDividerHeight(0);
		
		
		
		
		TEvent[] events_new= new TEvent[events.length-1];
		
		for (int i=0;i<events_new.length;i++)
			events_new[i]=events[i+1];
		
		
		
		
		
		List list2 = new ArrayList();
		
		
		for (int i=0;i<events_new.length;i++){
				
			try{
					String datestring=events_new[i].datum;
					datestring= datestring.substring(7, datestring.indexOf("-")-1);					
					if (datestring.contains("/")) datestring= datestring.substring( datestring.indexOf("/")+1);					
					if (datestring.length()==10){				
						SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
						Date date = sdf.parse(datestring);					
						if (date.after(new Date())){ 						
							list2.add(events_new[i]);
						//	Toast.makeText(getActivity(),"added "+ date.toGMTString(), Toast.LENGTH_SHORT).show();												
						}		
					}else
					{
						//add anyway
						list2.add(events_new[i]);
					}
			}
			
			catch (Exception e){
			//	Toast.makeText(getActivity(), e.toString()+" pruning failed", Toast.LENGTH_SHORT).show();							
			}
			
			
			
		}
		
		if (list2.size()==0) {
			
			TEvent event = new TEvent();
			event.title="aktuell kein Angebot";
			event.desc="";
			event.raum="";
			event.zeit="";
			event.trainer="";
			event.datum="";
			list2.add(event);
		
		
		}
		
		
		TEvent[] pruned_events= new TEvent[list2.size()];
		
		for (int i=0;i<list2.size();i++)
			pruned_events[i]=(TEvent) list2.get(i);
		
		 String titles[]=new String[ pruned_events.length];
		 
		 for (int i=0;i< pruned_events.length;i++){
			 
			 titles[i]= pruned_events[i].title;
			 
			 
		 }
		
		//	wait = (LinearLayout) getView().findViewById(R.id.waitheute);
		   	 wait.setVisibility(View.GONE);
		 
	
			CareerArrayAdapter colorAdapter = new CareerArrayAdapter(getActivity(),titles,  pruned_events);
			l.setAdapter(colorAdapter);
			}
			catch (Exception e){
//			
//			
//			
		}
	}

}
	 
	
	
}
