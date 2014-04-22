package com.skyworxx.htwdd.fragments;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;

import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.SpecialAdapter3;

import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.TPruefung;


public class PrufungenFragment extends Fragment {
	public  SharedPreferences app_preferences;
	public int currentim;
	
	public PrufungenFragment(){
	
	}
    
public static Date parsedate(String in){
		
		in=in.trim();
		in=in.replaceAll("Jan", "1");
		in=in.replaceAll("Feb", "2");
		in=in.replaceAll("Mär", "3");
		in=in.replaceAll("Apr", "4");
		in=in.replaceAll("Mai", "5");
		in=in.replaceAll("Jun", "6");
		in=in.replaceAll("Jul", "7");
		in=in.replaceAll("Sep", "8");
		in=in.replaceAll("Aug", "9");
		in=in.replaceAll("Okt", "10");
		in=in.replaceAll("Nov", "11");
		in=in.replaceAll("Dez", "12");
		
		DateFormat formatter ;
		Date date = new Date() ; 
		  formatter = new SimpleDateFormat("dd M yyyy");
		  try {
			date = (Date)formatter.parse(in);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		  
		return date;  
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pruefungen, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	        app_preferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
	        
//	      
	        currentim=0;
		     try{
		       currentim=Integer.parseInt(app_preferences.getString("im", "0"));
		     }catch(Exception e){};

	       
		  
		    
		     
		     if (app_preferences.getString("stdg", "0").equals("0"))
		    	 Toast.makeText(getActivity(),
							"Es wurde in den Einstellungen kein Studiengang gesetzt, daher werden nun alle Prüfungen ausgegeben.", Toast.LENGTH_LONG)
							.show();
		     
		     if (app_preferences.getString("im", "0").equals("0"))
		    	 Toast.makeText(getActivity(),
							"Es wurde in den Einstellungen kein Immatrikulationsjahr gesetzt.", Toast.LENGTH_LONG)
							.show();
		
		
		     if (app_preferences.getString("prof_name", "NA").equals("NA")){
		     
		    	 worker w = new worker();
		    	 w.execute(String.valueOf(currentim),app_preferences.getString("stdg", "0"),app_preferences.getString("abschluss", "B"),""+1);
		     }else
		     {
		    	 worker w = new worker();
		    	 w.execute(String.valueOf(currentim),app_preferences.getString("prof_name", "0"),app_preferences.getString("abschluss", "B"),""+3);
		      
		    	 
		    	 
		     }
	}
	
    
private class worker extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

		
			String line;
			String line2 = "";
			try{
				
				// name="was" value=1
				
				
				String data = URLEncoder.encode("feld1", "UTF-8")	+ "="	+ URLEncoder.encode(params[0], "UTF-8");
				
				 data += "&" + URLEncoder.encode("feld2", "UTF-8") + "=" 	+ URLEncoder.encode(params[1], "UTF-8");
				 data += "&" + URLEncoder.encode("feld3", "UTF-8") + "=" 	+ URLEncoder.encode(params[2], "UTF-8");
				 data += "&" + URLEncoder.encode("was", "UTF-8") + "=" 	+ URLEncoder.encode(params[3], "UTF-8");

				// Send data
				URL url = new URL(
						"http://www2.htw-dresden.de/~rawa/cgi-bin/pr_abfrage.php");
				URLConnection conn = (java.net.HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
	
				
			  
	//			conn.setAllowUserInteraction(false);  
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
			
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "ISO-8859-1"));
				
				while (!(line2.contains("</html>"))) {
					line = rd.readLine();
					line2 += line;
				}
				// wr.close();
				rd.close();

				String tokens[] = line2.split("N/W</td></tr>");
				line2 = tokens[1];
				
			}
			catch (Exception e){
		
				return "\n\nDer interne Fehler war: "+e.toString();
			}
			
			return line2;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
		
	 
		//	result= result.substring(result.indexOf("N/W</td></tr>"));
		//	result=result.substring(0, result.indexOf("N/W</td>"));
			
			String tokens[] = result.split("<tr><td>");
			
			
	
			TPruefung[] pruefung = new TPruefung[tokens.length-1];
			
			
			
			for (int i=1; i<tokens.length;i++){
				
				String tokens2[] = tokens[i].split("</td><td>");
			
			//	tokens2[5]=tokens2[5].substring(tokens2[5].indexOf(" ")+2);
				
				
				
				tokens2[9]=tokens2[9].replace("&rarr;", "->");
				tokens2[9]=tokens2[9].replace("<br>", "\n");	
				tokens2[5]=tokens2[5].replace("&uuml;", "ü");
				
				String datum=tokens2[7].substring(0,tokens2[7].indexOf("</td>"));
				
				String zeit=tokens2[7].substring(tokens2[7].indexOf("<td nowrap>")+11);
				zeit=zeit.substring(0,zeit.indexOf("</td>"));
				
				String raum=tokens2[7].substring(tokens2[7].indexOf("nowrap>")+7);
				raum=raum.substring(raum.indexOf("nowrap>")+7);
				
			pruefung[i-1]= new TPruefung(tokens2[5], tokens2[6], datum,
					zeit, raum, tokens2[9]);
				
			
				
			}
			
			
		
			 ListView v = (ListView) getView().findViewById(R.id.pruflist);
		      
		        
	    		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	    		HashMap<String, String> map = new HashMap<String, String>();
			
	    		for (int i=0;i<pruefung.length;i++){
	    			
	    		if 	(pruefung[i]._art.length()>1){
	    		map = new HashMap<String, String>();
	            map.put("titel",	pruefung[i]._titel );
	            map.put("art",	"Art:\t\t\t\t"+pruefung[i]._art );
	            map.put("tag","Datum:\t\t"+pruefung[i]._tag );
	            map.put("zeit","Uhrzeit:\t\t"+pruefung[i]._zeit );
	            map.put("raum","Raum:\t\t"+pruefung[i]._raum );
	            mylist.add(map);
	    		}

	    			
	      		
	      		
	    		}
	    		
	    		if (pruefung.length==0){
	    			map = new HashMap<String, String>();
		            map.put("titel",	"Keine Prüfungen" );
		            mylist.add(map);
	    		} 
	    		
		            map = new HashMap<String, String>();
		            map.put("titel",	"Zeige Prüfungen des IM-Jahres "+String.format("%02d", currentim-1 ));
		            mylist.add(map);
		            
		            map = new HashMap<String, String>();
		            map.put("titel",	"Zeige Prüfungen des IM-Jahres "+String.format("%02d", currentim+1 ));
		            mylist.add(map);
	    		
	     		
	      	  SpecialAdapter3 mSchedule = new SpecialAdapter3(getActivity(), mylist, R.layout.pruefrow,
	                  new String[] {"titel","art","tag", "zeit", "raum"}, new int[] {R.id.pruftitel,R.id.art,R.id.pruftag, R.id.prufzeit, R.id.prufraum});
	        		
	        			v.setAdapter(mSchedule);
	        			v.setDividerHeight(0);
	        			
	        			final int count=v.getCount()-1;
	        		
	          			v.setOnItemClickListener(new OnItemClickListener() {
	          			    public void onItemClick(AdapterView<?> l, View view, int position, long id) {
	          			                   int ID = l.getId();
	          			           
	          			               //    String strID = new Integer(ID).toString();
	          			              //     Toast.makeText(context, ("ID:" + ID+"  POS:" + id), Toast.LENGTH_SHORT).show();
	          			              
	          			                   
	          			         	    if ((id==count)&&(currentim>0)) {
	          			                   
	          			         	    	worker w = new worker();
	          			         	    	currentim=currentim+1;
	          			         	    	
	          			        			w.execute(String.valueOf(currentim),app_preferences.getString("stdg", "0"),app_preferences.getString("abschluss", "B"));
	          			                 }
	          			         	    
	          			         	 if ((id==count-1)&&(currentim>0)) {
	          			                   
	          			         	    	worker w = new worker();
	          			         	    	currentim=currentim-1;
	          			         	    	
	          			        			w.execute(String.valueOf(currentim),app_preferences.getString("stdg", "0"),app_preferences.getString("abschluss", "B"));
	          			                 }
	          			    }
	          			}
	        );
	          			
	        			
			

			
	       getView().findViewById(R.id.wait).setVisibility(View.GONE);
	   			
			
			
		}
		
	}
	 
  
	
}
