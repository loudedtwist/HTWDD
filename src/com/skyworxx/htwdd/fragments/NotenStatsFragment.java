package com.skyworxx.htwdd.fragments;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.DatabaseHandlerNoten;
import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.NotenStatsAdapter;
import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.Stats_element;


import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.TNote;


public class NotenStatsFragment extends Fragment {
	  public SharedPreferences app_preferences;
	    public String stg,stg2;
		public String abschl,abschl2;
		public DatabaseHandlerNoten db;
		public int count;
		public int count2;
		public int mode;
	    
		final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	                return true;
	        }
	};

	public NotenStatsFragment(int i) {
			// TODO Auto-generated constructor stub
		mode=i;
		}
	
	public NotenStatsFragment() {
	
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.noten, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    db = new DatabaseHandlerNoten(getActivity());
		   app_preferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
	        
		   
		  
			   LinearLayout l=(LinearLayout) getView().findViewById(R.id.linearLayout2);
				l.setVisibility(View.VISIBLE);
				ExpandableListView v = (ExpandableListView) getView().findViewById(R.id.noten);		  
				v.setVisibility(View.GONE);
		   
				 getView().findViewById(R.id.wait).setVisibility(View.GONE);
				 
				 
				 
				 List<TNote> e =db.getAllNoten();
	    			

					TNote [] noten =new TNote[e.size()];			
					for (int i=0;i<e.size();i++)
						noten[i]=e.get(i);
	    		 
					double finalmark=0;		
					int validcount=0;
					double mark=0;
					double credits=0;
					

					float totalworstmark=0.0f;
					float totalbestmark=5.0f;
					
					for (int i=0;i<e.size();i++){
					try{
							double c= Float.parseFloat(noten[i]._Note.replace(',', '.'));
							double cr= Float.parseFloat(noten[i]._Credits);
							if (cr>0){
					    		mark+=(c*cr);
					    		validcount++;
					    		credits+=cr;
							}
							totalworstmark=(float) Math.max(totalworstmark, c);
							totalbestmark=(float) Math.min(totalbestmark, c);
							
						}			
							catch (Exception e2)
						{				
						};
				
			    	}
		
					//if (validcount!=0)
					if (credits!=0)
					finalmark = mark/credits;
					finalmark = Math.round(finalmark*100.0) / 100.0;
	    		 
					//Toast.makeText(Noten.this,"Gesamtnote:"+finalmark  ,Toast.LENGTH_LONG).show();
					
					
					 ListView v2 = (ListView) getView().findViewById(R.id.stats);
					 
					 int semesterCount=0;
					 
					 
					 String[] all_sem=db.getAllSem();
					
					 
					 
					 Stats_element[] statsElements=new Stats_element[all_sem.length+1];
					 statsElements[0]=new Stats_element();
					 
					 statsElements[0].finalmark=(float) finalmark;
						statsElements[0].worstmark=totalworstmark;
						statsElements[0].bestmark=totalbestmark;
						statsElements[0].semester="Studium";
						statsElements[0].validcount=validcount;
						statsElements[0].credits=(float) credits;
					 
				for (int i=1;i<all_sem.length+1;i++){
					
					statsElements[i]=new Stats_element();
					
				List<TNote> sem_noten=	db.getSemNoten(all_sem[i-1]);
				
				
				
				double sem_finalmark=0;		
				int sem_validcount=0;
				double sem_mark=0;
				double sem_credits=0;
				
				float worstmark=0.0f;
				float bestmark=5.0f;
				
				for (int j=0;j<sem_noten.size();j++){
			
					if (sem_noten.get(j)!=null){
						try{
						double c= Float.parseFloat(sem_noten.get(j)._Note.replace(',', '.'));
						String credits2=sem_noten.get(j)._Credits;
						if (credits2.length()==0)credits2="0.0";
						double cr= Float.parseFloat(credits2);
						if (cr>0){
							sem_mark+=(c*cr);
							sem_validcount++;
				    		sem_credits+=cr;
						}
						worstmark=(float) Math.max(worstmark, c);
						bestmark=(float) Math.min(bestmark, c);
						}catch (Exception e2){}
					}
						
						
						
							
					
			
		    	}
				
				if (credits!=0)
					sem_finalmark = sem_mark/sem_credits;
				sem_finalmark = Math.round(sem_finalmark*100.0) / 100.0;
				
				statsElements[i].finalmark=(float) sem_finalmark;
				statsElements[i].worstmark=worstmark;
				statsElements[i].bestmark=bestmark;
				statsElements[i].semester=all_sem[i-1];
				statsElements[i].validcount=sem_validcount;
				statsElements[i].credits=(float) sem_credits;
				
				
				
				
					
				}
					 
					 String titles[]=new String[statsElements.length];
					 
					 for (int i=0;i<titles.length;i++)
						 titles[i]=statsElements[i].semester;
					
					 
					 //Gesamtdurchschnitt und infos
					 
					 
				
					NotenStatsAdapter colorAdapter = new NotenStatsAdapter(getActivity(),titles, statsElements);
					v2.setAdapter(colorAdapter);
					v2.setDividerHeight(0);
				 
		
	}
	
	
	}

