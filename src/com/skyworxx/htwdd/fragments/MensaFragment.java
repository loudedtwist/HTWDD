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
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.EventPopup;
import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.types.TEssen;



public class MensaFragment extends ListFragment {

	int mensa_id=9;
	public SharedPreferences app_preferences;
	ArrayList mensen;
	public ProgressBar progressbar;
	public MensaFragment(int mensa_id) {
		mensen=null;
		this.mensa_id=mensa_id;
		    
	}
	
public MensaFragment(ArrayList mensen) {
	 mensa_id=-1;
		this.mensen=mensen;
	}

public MensaFragment() {
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list2, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());   
		 progressbar = (ProgressBar)getActivity().findViewById(R.id.progressBar1);
         
		
			worker w = new worker();
			w.execute();

		 
		
	}
    
private class worker extends AsyncTask<Calendar, Void, TEssen[]> {
		@Override
		protected  TEssen[] doInBackground(Calendar... params) {
//http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=9
		int progress=0;
		HTTPDownloader downloader=new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid="+mensa_id);
	
		String result=downloader.getString();
		
		String tokens[]=result.split("<title>");
		int a=0;
		while (tokens.length<3 && a<=4){
			
			result=downloader.getString();			
			tokens=result.split("<title>");
			a++;
			//return null;
		}
		if (tokens.length<3) return null;
		TEssen[] essen = new TEssen[tokens.length-2];
		
		int maxprogressperfood=100/essen.length;
		
		for (int i=0;i<essen.length;i++){
			essen[i]=new TEssen();
			progress+=maxprogressperfood;
			onProgressUpdate(progress);
			try{
				
				 essen[i].setTitle(tokens[i+2].substring(0, tokens[i+2].indexOf("(")));
			 
			}catch (Exception e){
				 essen[i].setTitle(tokens[i+2].substring(0, tokens[i+2].indexOf("<")));
				}
			
		
			 essen[i].setSonst(tokens[i+2].substring(tokens[i+2].indexOf("<description>")+13, tokens[i+2].indexOf("</description>")));
			 essen[i].setID(Integer.parseInt(tokens[i+2].substring(tokens[i+2].indexOf("details-")+8, tokens[i+2].indexOf(".html"))));
		
			 try{
			 String contra=new HTTPDownloader("http://www.htwdd-app.de/functions/count_contra.php?id="+essen[i].getID()).getString();
		
			 
			 if (contra.length()==0) contra="0";
			String pro=new HTTPDownloader("http://www.htwdd-app.de/functions/count_pro.php?id="+essen[i].getID()).getString();
				if (pro.length()==0) pro="0";
			 essen[i]._contra=Integer.parseInt(contra);
			 essen[i]._pro=Integer.parseInt(pro);
			 }catch (Exception e){
				 essen[i]._contra=0;
				 essen[i]._pro=0;
				 
				 
			 }
			 
			 try{
				String comments="";
				
				
			
					comments=new HTTPDownloader("http://www.htwdd-app.de/functions/getcomments.php?id="+essen[i].getID()).getString();
				if (comments.length()==0) essen[i]._comments="";
				if (comments.length()>0)
				 essen[i]._comments="Nutzerkommentare:<br><i>"+comments+"</i>";
				 // essen[i]._comments="";
			 }catch (Exception e){
				 essen[i]._comments="";
				 
				 
			 }
			 
			 Calendar calendar = Calendar.getInstance(Locale.GERMANY);				
				final int month = calendar.get(Calendar.MONTH)+1;
				final int year = calendar.get(Calendar.YEAR);
		
				String monthstring = String.valueOf(month);						
				if (month<10) monthstring= "0"+String.valueOf(month);
				
				
				String thumbs="/";
				
				int thumbmode=app_preferences.getInt("thumbnail", 2);
				
				if (thumbmode==1) thumbs="/thumbs/";
				
				Bitmap image;
				String url=
						"http://bilderspeiseplan.studentenwerk-dresden.de/m"+mensa_id+"/"
								+ String.valueOf(year)
								+ monthstring + thumbs + essen[i].getID()
								+ ".jpg";
			
			 
			 
				if (thumbmode!=0){	
					HTTPDownloader imagedownloader= new HTTPDownloader(url);
			
			essen[i].setBild(imagedownloader.getBitmap(essen[i].getID()));
				}
		}
			
			
			return essen;
	
			
			
		}
		
		protected void onProgressUpdate(Integer... values) {
		   // TODO Auto-generated method stub
		   progressbar.setProgress(values[0]);
		  }
		
		@Override
		protected void onPostExecute( TEssen[] essen) {
			
			try{
				ProgressBar p= (ProgressBar) getView().findViewById(R.id.waitIndicator);
			
		p.setVisibility(View.GONE);
		
		ListView l= (ListView) getView().findViewById(android.R.id.list);
		l.setVisibility(View.VISIBLE);
		l.setDividerHeight(0);
		
		 String titles[];
		
		if (essen!=null)
			if (essen.length<1){
			essen=new TEssen[1];
			essen[0]=new TEssen();
			essen[0].setTitle("Kein Angebot an diesem Tag.");
			essen[0]._comments="";
			}
		
		if (essen==null){
			essen=new TEssen[1];
			essen[0]=new TEssen();
			essen[0].setTitle("Kein Angebot an diesem Tag.");
			essen[0]._comments="";
		}
		
		 titles=new String[essen.length];
		 
		 for (int i=0;i<titles.length;i++)
			 titles[i]=essen[i].getTitle();
	
	
			MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(),titles, essen);
			setListAdapter(colorAdapter);
			}
			catch (Exception e){
			
				 essen=new TEssen[1];
				essen[0]=new TEssen();
				essen[0].setTitle("Fehler aufgetreten. Versuche es später erneut.");
				essen[0]._comments="";
				String titles[]=new String[essen.length];
				 
				 for (int i=0;i<titles.length;i++)
					 titles[i]=essen[i].getTitle();
			
				 try{
			
					MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(),titles, essen);
					setListAdapter(colorAdapter);
				 }catch (Exception e2){
				 
				 }
			
			
		}
			
	      
		}
		
	}
	 

    
private class worker2 extends AsyncTask<Calendar, Void, ArrayList> {
		@Override
		protected  ArrayList doInBackground(Calendar... params) {
//http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid=9
		
			
			ArrayList all_essen=new ArrayList();
			
			for (Object id : mensen){
				int idstring =(Integer) id;
				switch (idstring){
				
				case 2: 
					mensa_id=8;	
					break;
				case 3: 
					mensa_id=18;	
					break;
				case 4: 
					mensa_id=9;	
					break;			
				case 5: 
					mensa_id=5;	
					break;	
				case 6: 
					mensa_id=6;
					break;
				case 7: 
					mensa_id=32;
					break;
				case 8: 
					mensa_id=12;
					break;
				case 9: 
					mensa_id=22;
					break;		
				case 10: 
					mensa_id=7; 
					break;
				case 11: 
					mensa_id=1;	
					break;
				case 12: 
					mensa_id=13;	
					break;
				case 13: 
					mensa_id=14;	 
					break;
				case 14: 
					mensa_id=15;	 
					break;				
				case 15: 
					mensa_id=16;	 
					break;
				case 16: 
					mensa_id=19;	 
					break;
				case 17: 
					mensa_id=20;	 
					break;	
				
				
				}
				
				String mensa="MensaDD";
	    		switch (mensa_id){
	    		
	    			case 8: mensa="Neue Mensa";break;
	    			case 18: mensa="Alte Mensa";break;
	    			case 9: mensa="Mensa Reichenbachstrasse";break;
	    			case 5: mensa="Mensologie";break;
	    			case 6: mensa="Mensa Siedepunkt";break;
	    			case 32: mensa="Mensa Johannstadt";break;
	    			case 12: mensa="Mensa Blau";break;
	    			case 22: mensa="U-Boot";break;
	    			case 7: mensa="Mensa Tellerrandt";break;
	    			case 1: mensa="Mensa Zittau";break;
	    			case 13: mensa="Mensa Stimm-Gabel";break;
	    			case 14: mensa="Mensa Palucca Schule";break;
	    			case 15: mensa="Mensa Goerlitz";break;
	    			case 16: mensa="Mensa Haus VII";break;
	    			case 19: mensa="Mensa Sport";break;
	    			case 20: mensa="Mensa Kreuzgymnasium";break;
	    			
	    		
	    		}
				
				
				HTTPDownloader downloader=new HTTPDownloader("http://www.studentenwerk-dresden.de/feeds/speiseplan.rss?mid="+mensa_id);
			
				String result=downloader.getString();
				
				String tokens[]=result.split("<title>");
				
				int a=0;
				while (tokens.length<3 && a<=4){
					
					result=downloader.getString();			
					tokens=result.split("<title>");
					a++;
					//return null;
				}
				if (tokens.length<3) return null;
				
				
			
				TEssen[] essen = new TEssen[tokens.length-2];
				
				for (int i=0;i<essen.length;i++){
					essen[i]=new TEssen();
					try{
						
						 essen[i].setTitle(tokens[i+2].substring(0, tokens[i+2].indexOf("(")));
					 
					}catch (Exception e){
						 essen[i].setTitle(tokens[i+2].substring(0, tokens[i+2].indexOf("<")));
						}
					
				
					 essen[i].setSonst(tokens[i+2].substring(tokens[i+2].indexOf("<description>")+13, tokens[i+2].indexOf("</description>")));
					 essen[i].setID(Integer.parseInt(tokens[i+2].substring(tokens[i+2].indexOf("details-")+8, tokens[i+2].indexOf(".html"))));
				essen[i].mensa=mensa;
				
					 String contra=new HTTPDownloader("http://htwdd-app.de/functions/count_contra.php?id="+essen[i].getID()).getString();
						if (contra.length()==0) contra="0";
					String pro=new HTTPDownloader("http://htwdd-app.de/functions/count_pro.php?id="+essen[i].getID()).getString();
						if (pro.length()==0) pro="0";
					 essen[i]._contra=Integer.parseInt(contra);
					 essen[i]._pro=Integer.parseInt(pro);
					 
					 
					 
						String comments=new HTTPDownloader("http://htwdd-app.de/functions/getcomments.php?id="+essen[i].getID()).getStringUTF8();
						if (comments.length()==0) comments="<i>noch keine Kommentare</i>";
						 essen[i]._comments=comments;
					 
					 
					 Calendar calendar = Calendar.getInstance(Locale.GERMANY);				
						final int month = calendar.get(Calendar.MONTH)+1;
						final int year = calendar.get(Calendar.YEAR);
				
						String monthstring = String.valueOf(month);						
						if (month<10) monthstring= "0"+String.valueOf(month);
						
						
						String thumbs="/";
						
						int thumbmode=app_preferences.getInt("thumbnail", 2);
						
						if (thumbmode==1) thumbs="/thumbs/";
						
						Bitmap image;
						String url=
								"http://bilderspeiseplan.studentenwerk-dresden.de/m"+mensa_id+"/"
										+ String.valueOf(year)
										+ monthstring + thumbs + essen[i].getID()
										+ ".jpg";
					
					 
					 
						if (thumbmode!=0){	
							HTTPDownloader imagedownloader= new HTTPDownloader(url);
					
					essen[i].setBild(imagedownloader.getBitmap(essen[i].getID()));
						}
				}
				
				
			all_essen.add(essen);
			}
			
		
			return all_essen;
			
		
		}
		
		@Override
		protected void onPostExecute(ArrayList all_essen) {
			
			try{
				ProgressBar p= (ProgressBar) getView().findViewById(R.id.waitIndicator);
			
		p.setVisibility(View.GONE);
		
		ListView l= (ListView) getView().findViewById(android.R.id.list);
		l.setVisibility(View.VISIBLE);
		l.setDividerHeight(0);
		
		
		int essencount=0;
		
		for (Object e: all_essen)
		{
			essencount+=((TEssen[]) e).length;
			
		}
		TEssen[] essen= new TEssen[essencount];
		
		int a=0;
		for (Object e: all_essen)
		{
			TEssen[] e1=(TEssen[]) e;
			
			for (int i=0;i<e1.length;i++)
			{
				essen[a]=e1[i];
				a++;
			}
			
			
			
		}
		
		
		 String titles[];
		
		 
		 
		 
		 
		 
		if (essen.length<1){
		essen=new TEssen[1];
		essen[0]=new TEssen();
		essen[0].setTitle("Kein Angebot an diesem Tag.");
		}
		
		 titles=new String[essen.length];
		 
		 for (int i=0;i<titles.length;i++)
			 titles[i]=essen[i].getTitle();
	
	
			MensaArrayAdapter colorAdapter = new MensaArrayAdapter(getActivity(),titles, essen);
			setListAdapter(colorAdapter);
		}
		catch (Exception e){
			
			
		}
			
	      
		}
		
	}
	 

	
}
