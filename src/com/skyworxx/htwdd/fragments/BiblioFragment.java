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

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.skyworxx.htwdd.BiblioListAdapter;

import com.skyworxx.htwdd.HTTPDownloader;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.R;

import com.skyworxx.htwdd.types.TBuch;
import com.skyworxx.htwdd.types.TEssen;


public class BiblioFragment extends Fragment {
	public  SharedPreferences app_preferences;
	public int currentim;
	public static String ids[];
	public static int anzv[];
	public String token;
	public static int globalposition;
	public String mid;
	public String bib;
	
	public BiblioFragment() {
	
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
		return inflater.inflate(R.layout.bibliolist, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		   
        app_preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		bib=app_preferences.getString("bib", "0");
     	if ((bib.contains("s"))||(bib.contains("S"))) bib=bib.substring(1);
     	
    		
		
		worker w = new worker();
		w.execute(bib,app_preferences.getString("bibpw", "0"));
		
	}
	
	  
    
		private class worker extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... params) {

			
				String line2="";
				String line="";
				URL url;
				
			
				try {
				
				//-------get Token------------------------	
					
				url = new URL("http://bsv2.bib.htw-dresden.de/libero/WebOpac.cls");		
				URLConnection conn = (java.net.HttpURLConnection)url.openConnection();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				
				while (!(line2.contains("</html>"))) {
					line = rd.readLine();
					line2 += line;
				}
				// wr.close();
				rd.close();

				line2 = line2.split("Benutzerfunktionen")[3];	
				line2 = line2.split("Mein Konto")[0];		
				line2=line2.substring(line2.indexOf("href=\"")+6,line2.indexOf("\" title="));		
				token= line2.substring(line2.indexOf("TOKEN=")+6);
				token=token.substring(0,token.indexOf("&amp;"));
				
				
				//-------Connect to get books------------
			
				
				String data = URLEncoder.encode("MGWCHD", "UTF-8")	+ "="	+ URLEncoder.encode("0", "UTF-8");
				
				 data += "&" + URLEncoder.encode("TOKEN", "UTF-8") + "=" 	+ URLEncoder.encode(token, "UTF-8");
				 data += "&" + URLEncoder.encode("TOKENX", "UTF-8") + "=" 	+ URLEncoder.encode(token, "UTF-8");
				 data += "&" + URLEncoder.encode("DATA", "UTF-8") + "=" 	+ URLEncoder.encode("HTW", "UTF-8");
				 data += "&" + URLEncoder.encode("usercode", "UTF-8") + "=" 	+ URLEncoder.encode("", "UTF-8");
				 data += "&" + URLEncoder.encode("VERSION", "UTF-8") + "=" 	+ URLEncoder.encode("2", "UTF-8");
				 
				 data += "&" + URLEncoder.encode("RSN", "UTF-8") + "=" 	+ URLEncoder.encode("0", "UTF-8");
				 data += "&" + URLEncoder.encode("BARCODE", "UTF-8") + "=" 	+ URLEncoder.encode("", "UTF-8");
				 data += "&" + URLEncoder.encode("TOKENZ", "UTF-8") + "=" 	+ URLEncoder.encode(token, "UTF-8");
				 
				 data += "&" + URLEncoder.encode("ACTION", "UTF-8") + "=" 	+ URLEncoder.encode("MEMLOGIN", "UTF-8");
				 
				 data += "&" + URLEncoder.encode("usernum", "UTF-8") + "=" 	+ URLEncoder.encode(params[0], "UTF-8");
				 data += "&" + URLEncoder.encode("password", "UTF-8") + "=" 	+ URLEncoder.encode(params[1], "UTF-8");
				 data += "&" + URLEncoder.encode("btnlogin", "UTF-8") + "=" 	+ URLEncoder.encode("LOGIN", "UTF-8");

				// Send data
				url = new URL("http://bsv2.bib.htw-dresden.de/libero/WebOpac.cls");
				conn = (java.net.HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);

				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
			
				// Get the response
				 rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				
				line2="";
					
				while (!(line2.contains("</html>"))) {
					line = rd.readLine();
					line2 += line;
				}
				// wr.close();
				rd.close();
				
				
				if (line2.contains("Die Kombination von Benutzernummer und Kennwort")) return line2;

				
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
				
				
				
				return line2;
		}
			
			@Override
			protected void onPostExecute(String result) {
				
//				TextView titeltext=  (TextView) findViewById(R.id.titeltext);    	
//		    	titeltext.setText("Ausgeliehene Bücher von "+app_preferences.getString("bib", "0")+":");
//				 
		    	
				
				
				try{
				
				
				 
				 ListView v = (ListView) getView().findViewById(R.id.biblioListView);
			      
			        
		    		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		    		HashMap<String, String> map = new HashMap<String, String>();
					
		    		result=result.replace('"', '\'');
		    		mid=result.substring(result.indexOf("type='hidden' name='mid' value='")+32,result.indexOf("type='hidden' name='mid' value='")+37);
		    	//	Toast.makeText(ScreenBiblioActivity.this,mid, Toast.LENGTH_SHORT).show();
		    		
					if (result.contains("Ausgeliehene Medien")){
						result=result.substring(result.indexOf("<table summary='Ausgeliehene Medien'>"),result.indexOf("</tbody></table>"));
						 
						 String tokens[]=result.split("<tr class");
						
						 TBuch buecher[]=new TBuch[tokens.length-1];
						 
						 anzv= new int[tokens.length-1];
						 ids=new String[tokens.length-1];
						 
						 for (int i=1;i<tokens.length;i++){
							 buecher[i-1]=new TBuch();
							 
							 buecher[i-1]._barcode=tokens[i].substring(tokens[i].indexOf("headers='th1_1'>")+16, tokens[i].indexOf("</th>"));
							 ids[i-1]=buecher[i-1]._barcode;
							 
							 
							 //Get titel
							 try {buecher[i-1]._titel=tokens[i].substring(tokens[i].indexOf("headers='th1_2'>")+16, tokens[i].indexOf("<br/>"));}
							 catch (Exception e){
								 try {
								 buecher[i-1]._titel=tokens[i].substring(tokens[i].indexOf("headers='th1_2'>")+16, tokens[i].indexOf("<span class='ItemNotesLeader'>")); 
								 }catch (Exception e2){
									 
									 buecher[i-1]._titel=tokens[i].substring(tokens[i].indexOf("headers='th1_2'>")+16, tokens[i].indexOf("</td><td headers='th1_3'>")); 
								
								 }
							 }
							 
							 //make titel short
							 buecher[i-1]._shorttitel=buecher[i-1]._titel;
							 if (buecher[i-1]._titel.length()>100) { buecher[i-1]._shorttitel=buecher[i-1]._titel.substring(0,98)+"...";}
							 
							 //get delay date
							 try {
								 buecher[i-1]._verab=parsedate(tokens[i].substring(tokens[i].indexOf("erlaubt bis:")+12, tokens[i].indexOf(".</td>")));
							 } catch (Exception e){
								 Calendar currentDate = Calendar.getInstance();
								 buecher[i-1]._verab=currentDate.getTime();			
								 }
							
							 //get expire date
							 buecher[i-1]._bis=parsedate(tokens[i].substring(tokens[i].indexOf("headers='th1_4'>")+16, tokens[i].indexOf("</td><td headers='th1_5'>")));
							 
							 //get count
							 try{
							 buecher[i-1]._anzv=Integer.parseInt(tokens[i].substring(tokens[i].indexOf("headers='th1_5'>")+16, tokens[i].indexOf("</td><td headers='th1_7'>")));
							 anzv[i-1]=buecher[i-1]._anzv;
							 }catch (Exception e){buecher[i-1]._anzv=0;anzv[i-1]=0;};
							
							 
							 
							 //get author
							 buecher[i-1]._verfasser=tokens[i].substring(tokens[i].indexOf("headers='th1_3'>")+16, tokens[i].indexOf("</td><td headers='th1_4'>"));
							 
							 
							 
						 }
						 
						 
						
						
				    		SimpleDateFormat dateformat = new SimpleDateFormat("d. MMMM yyyy");
				    		
				    		
				    		 Calendar currentDate = Calendar.getInstance();
				    		 
				    		for (int i=0;i<buecher.length;i++){
				    			
				    			StringBuilder verab = new StringBuilder( dateformat.format( buecher[i]._verab ) );
				    		
				    			StringBuilder bis = new StringBuilder( dateformat.format( buecher[i]._bis ) );
				    			
				    	
				    			 
				    			
				    		map = new HashMap<String, String>();
				            map.put("titel",	buecher[i]._titel );
				            map.put("verfasser","Verfasser:\t"+	buecher[i]._verfasser );
				          //  if ((""+currentDate.getTime()).equals(""+buecher[i]._verab ) )
				            if (currentDate.getTime().compareTo(buecher[i]._verab)>0 )
				            	
				            	 if (buecher[i]._anzv<3 )
				            		 map.put("verab",buecher[i]._anzv+" Verlängerungen - weitere Verlängerung möglich" );
				            	 else
				            		 map.put("verab",buecher[i]._anzv+" Verlängerungen - weitere Verlängerung nicht möglich!" );
				            else{
				            	map.put("verab",buecher[i]._anzv+" Verlängerungen -Verlängerung möglich ab:\t\t"+verab );
				            	for (int a=0;a<ids.length;a++){
				            	if (ids[a].equals(buecher[i]._barcode)) ids[a]="0"; 
				            	}
				            }
				            map.put("bis",  "Rückgabe spätestens bis:\t\t"+bis);
			
				            mylist.add(map);
				    		}
				    		
				    		 
						 
					 
					}else
					{
					if (result.contains("Die Kombination von Benutzernummer und Kennwort")){
						map = new HashMap<String, String>();
			            map.put("titel",	"Es ist ein Fehler aufgetreten:\nDie Kombination von Bibliotheksnummer und Kennwort ist ungültig." );
			            mylist.add(map);
			            }
					
					else if (result.contains("Ungültige Benutzernummer")){
						map = new HashMap<String, String>();
			            map.put("titel",	"Es ist ein Fehler aufgetreten:\nBibliotheksnummer ist ungültig." );
			            mylist.add(map);
			            }
					
					else {
						map = new HashMap<String, String>();
			            map.put("titel",	"Du hast noch keine Bücher ausgeliehen." );
			            mylist.add(map);
					}
					
					}
						
					
					
				
					 BiblioListAdapter mSchedule = new  BiblioListAdapter(getActivity(), mylist, R.layout.buchrow,
			                  new String[] {"titel","verfasser","verab", "bis"}, new int[] {R.id.titel,R.id.verfasser,R.id.verab, R.id.bis});
			    v.setDividerHeight(0);    		
			        			v.setAdapter(mSchedule);
				

			          			v.setOnItemClickListener(new OnItemClickListener() {
			          			    public void onItemClick(AdapterView<?> l, View view, int position, long id) {
			          			                   int ID = l.getId();
			          			           
			          			                   globalposition=position;
			          			                //   Toast.makeText(Bibliothek.this, ("Barcode: " + ids[position]), Toast.LENGTH_SHORT).show();
			          			                 if (ids!=null){
			          			              
			          			                   
			          			                 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			          			                 
			          			                
			          			                 
			          			               if (!ids[globalposition].equals("0"))
			          			            	 if (anzv[globalposition]<3)
			          			               {
			          			                 builder.setMessage("Willst Du dieses Buch verl�ngern?")
			          			                        .setCancelable(false)
			          			                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			          			                            public void onClick(DialogInterface dialog, int id) {
			          			                               
			          			                         	   try{
			          			                            	String data = URLEncoder.encode("MGWCHD", "UTF-8")	+ "="	+ URLEncoder.encode("0", "UTF-8");
			          			                  			
			          			                 			 data += "&" + URLEncoder.encode("TOKEN", "UTF-8") + "=" 	+ URLEncoder.encode(token, "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("TOKENX", "UTF-8") + "=" 	+ URLEncoder.encode(token, "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("DATA", "UTF-8") + "=" 	+ URLEncoder.encode("HTW", "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("usercode", "UTF-8") + "=" 	+ URLEncoder.encode("", "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("VERSION", "UTF-8") + "=" 	+ URLEncoder.encode("2", "UTF-8");
			          			                 			 
			          			                 			 data += "&" + URLEncoder.encode("REN"+ids[globalposition], "UTF-8") + "=" 	+ URLEncoder.encode("1", "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("bno", "UTF-8") + "=" 	+ URLEncoder.encode(app_preferences.getString("bib", "0"), "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("mid", "UTF-8") + "=" 	+ URLEncoder.encode(mid, "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("ACTION", "UTF-8") + "=" 	+ URLEncoder.encode("MEMSLFISS", "UTF-8");
			          			                 			 data += "&" + URLEncoder.encode("SubmitIssued", "UTF-8") + "=" 	+ URLEncoder.encode("Senden", "UTF-8");

			          			                 			// Send data
			          			                 			 
			          			                 			URL url = new URL("http://bsv2.bib.htw-dresden.de/libero/WebOpac.cls");		
			          			                 			URLConnection conn = (java.net.HttpURLConnection)url.openConnection();		          			                 				          			                 		
			          			                 			conn.setDoOutput(true);

			          			                 			OutputStreamWriter wr = new OutputStreamWriter(
			          			                 					conn.getOutputStream());
			          			                 			wr.write(data);
			          			                 			wr.flush();
			          			                 		
			          			                 			BufferedReader  rd = new BufferedReader(new InputStreamReader(
			          			           					conn.getInputStream()));
			          			           			
			          			                 			String line2="";
			          			                 			String line="";
			          			           				
			          			                 			while (!(line2.contains("</html>"))) {
			          			                 				line = rd.readLine();
			          			                 				line2 += line;
			          			                 			}
			          			                 			// wr.close();
			          			                 			rd.close();
			          			           			
			          			                 			if (line2.contains("Anforderung konnte aus den folgenden")) Toast.makeText(getActivity(), "Verlängern nicht erfolgreich!", Toast.LENGTH_SHORT).show();
			          			                 			else{
			          			                 				Toast.makeText(getActivity(), "Verlängern erfolgreich!", Toast.LENGTH_SHORT).show();
			          			                 				worker w = new worker();
			          			                  			    w.execute(bib,app_preferences.getString("bibpw", "0"));
			          			                 			}
			          			                 			
			          			                 			
			          			                 			
			          			                         	   }
			          			                         	   catch (Exception e){
			          			                         		Toast.makeText(getActivity(), "Verlängern nicht erfolgreich!", Toast.LENGTH_SHORT).show();
			          			                         		   
			          			                         	   };
			          			                         	
			          			                            }
			          			                        })
			          			                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
			          			                            public void onClick(DialogInterface dialog, int id) {
			          			                                 dialog.cancel();
			          			                            }
			          			                        });
			          			                 AlertDialog alert = builder.create();
			          			                  alert.show();
			          			               }
			          			               else
			          			            	 Toast.makeText(getActivity(), "Verlängern nicht möglich.", Toast.LENGTH_SHORT).show();
			          			                   
			          			                   
			          			                   
			          			                   
			          			                   
			          			                   
			          			                 };
			          			                   
			          			         	  
			          			                 }
			          			    
			          			}
			        );
		      		
		      		
		          			
		        			
				try {
				
			//	 Toast.makeText(getActivity(), "Du hast noch keine Bücher ausgeliehen.", Toast.LENGTH_SHORT).show();
					v.setVisibility(View.VISIBLE);
		        getView().findViewById(R.id.waitIndicator).setVisibility(View.GONE);
				}catch (Exception e){};
				
				
			
			} catch (Exception e){};
			
		}
	    
		}
	
}
