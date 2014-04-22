package com.skyworxx.htwdd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import com.skyworxx.htwdd.VersionedStrictModeWrapper.StrictModeWrapper;
import com.skyworxx.htwdd.VersionedStrictModeWrapper.StrictModeWrapper.ThreadPolicyWrapper;



import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateMensaWidgetService extends Service {
	

	@Override
	public void onStart(Intent intent, int startId) {
		

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());

		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		ComponentName thisWidget = new ComponentName(getApplicationContext(),
				WidgetMensaProvider.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		

		for (int widgetId : allWidgetIds) {
			// Create some random data
			

			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),
					R.layout.widget_layout2);
		
		
	
		
//				
//				StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
//				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
//				    .permitNetwork()
//				    .build());
//		

			StrictModeWrapper strictMode = VersionedStrictModeWrapper.getInstance();
			ThreadPolicyWrapper origPolicy = strictMode.allowThreadDiskReads();
			//strictMode.allowThreadDiskWrites();
			strictMode.allowThreadNetwork();
			
			
		
			String daystring = "";
			String nextdaystring = "";
			Calendar calendar = Calendar.getInstance(Locale.GERMANY);
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			final int month = calendar.get(Calendar.MONTH)+1;
			String urlstring="http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse.html";
			final int year = calendar.get(Calendar.YEAR);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			String currentday="heute";
			
			int min =calendar.get(Calendar.MINUTE);
			
		String minstring;
		if (min<10) minstring = "0"+min;
		else
			minstring=Integer.toString(min);
			
			//Reset all textfields
			// remoteViews.setTextViewText(R.id.TextView001,"1");	
			 remoteViews.setTextViewText(R.id.TextView002,"");	
			 remoteViews.setTextViewText(R.id.TextView003,"");	
			 remoteViews.setTextViewText(R.id.TextView004,"");	
			 remoteViews.setTextViewText(R.id.update0,"");	
			 remoteViews.setTextViewText(R.id.textView02,"");	
			 remoteViews.setTextViewText(R.id.textView04,"");	
			 remoteViews.setTextViewText(R.id.textView06,"");
			 remoteViews.setTextViewText(R.id.TextView01,"");	
			 
			 SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
			 SimpleDateFormat formatter2= new SimpleDateFormat("dd.MM.");
			 String dateNow = formatter.format(calendar.getTime());
			 String dateNow2 = formatter2.format(calendar.getTime());
			
			 remoteViews.setTextViewText(R.id.textView1,"Stand: "+dateNow+" Uhr, "+dateNow2 );	
			
				
			//	day=2;
				
				if ((hour > 15)&&(day != 7)&&(day != 1)){
					day++;
					currentday="morgen";
					
				}

				if (day == 2) {
					daystring = "Montag";
				} else if (day == 3) {
					daystring = "Dienstag";
				} else if (day == 4) {
					daystring = "Mittwoch";
				} else if (day == 5) {
					daystring = "Donnerstag";
				} else if (day == 6) {
					daystring = "Freitag";
				} else if (day == 7) {
					daystring = "Montag";
					urlstring="http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse-w1.html";
				currentday="Montag";
				} else if (day == 1) {
					daystring = "Montag";
					urlstring="http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse-w1.html";
				currentday="Montag";
				}
				else if (day == 8) {
						daystring = "Montag";
						urlstring="http://www.studentenwerk-dresden.de/mensen/speiseplan/mensa-reichenbachstrasse-w1.html";
						currentday="Montag";
				}
				
				remoteViews.setTextViewText(R.id.TextView01,"Speiseplan von "+daystring);	
				
				
				if (day == 2) {
					nextdaystring = "Dienstag";
				} else if (day == 3) {
					nextdaystring = "Mittwoch";
				} else if (day == 4) {
					nextdaystring = "Donnerstag";
				} else if (day == 5) {
					nextdaystring = "Freitag";
				} else if (day == 6) {
					nextdaystring = "Samstag";
				} else if (day == 7) {
					nextdaystring = "Dienstag";
				} else if (day == 1) {
					nextdaystring = "Dienstag";			
				}
				else if (day == 8) {
					nextdaystring = "Dienstag";				
				}
				
	

				
				Type_mahlzeit[] meals=new Type_mahlzeit[8];
				
			
				URL url;
				try {
					url = new URL(urlstring);
			
				URLConnection conn = url.openConnection();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				String line2 = "";
				while ((line = rd.readLine()) != null) {
					line2 += line;
				}
				// wr.close();
				rd.close();
				
				String tokens[] = line2.split("<thead><tr><th class=\"text\">"
						+ daystring);
				line2 = tokens[1];
				
				
				
				String tokens2[] = line2
						.split(nextdaystring);

				
				if (tokens2[0].contains("Kein Angebot an diesem Tag.")){
					

					meals[0] = new Type_mahlzeit(null, "Fehler : Kein Angebot an diesem Tag.",null,  null);
				
					
				}
				
				
				
				String essen[] = tokens2[0].split("<td class=\"text\">");
			
				//
				
			//	String[] result = new String[8];
				String[] preise=new String[8];
				String[] titles=new String[8];
				Bitmap img[]=new Bitmap[8];
				String[] sids = new String[8];
//				
			
				int maxcount = essen.length;
				if (maxcount >9) maxcount =9;
					
					for (int i = 1; i < maxcount; i++) {

					essen[i] = essen[i].substring(9);
					
					String smalllink="";
					try {
					smalllink = essen[i].substring(0, essen[i].indexOf("?"));
					}catch (Exception e){};
					
					String sid="";
					
					try {
					sid = (smalllink.substring(smalllink.indexOf("-") + 1,
							smalllink.indexOf(".")));
					sids[i-1]=sid;
					}catch (Exception e){};
					
					String beschreibung="Kein Angebot";
					try {
					 beschreibung = essen[i].substring(
							essen[i].indexOf(">") + 1, essen[i].indexOf("</a>"));
					}catch (Exception e){};
					
					if ( essen[i].contains("mensaVital.png"))
						beschreibung = essen[i].substring(
								essen[i].indexOf("/></div> ") + 9, essen[i].indexOf("</a>"));

					essen[i] = essen[i].substring(essen[i].indexOf(">") + 1);
					essen[i] = essen[i].substring(essen[i].indexOf("preise\"><a href") + 10);
					essen[i] = essen[i].substring(essen[i].indexOf(">") + 1);


					String preis="0.0";
					try {
					preis= essen[i] = essen[i].substring(0,essen[i].indexOf("<"));
					if (!preis.equals("ausverkauft")) {
						preis = preis.substring(0, preis.indexOf("&"));
						preis = preis + "Euro";
					}
					}catch (Exception e){}


					preise[i - 1]=preis; //<--
					//meals[i-1].setPreis(preis);

//					if (i < 5) {
//
//					
//					
//						
//						
//						
//						Bitmap image;
//						URL myFileUrl = new URL(
//								"http://bilderspeiseplan.studentenwerk-dresden.de/m9/"
//										+ String.valueOf(year)
//										+ String.valueOf(month) + "/" + sid
//										+ ".jpg");
//
//						HttpURLConnection conn2 = (HttpURLConnection) myFileUrl
//								.openConnection();
//						conn2.setDoInput(true);
//						conn2.addRequestProperty("Referer", "httpReferer");
//						conn2.connect();
//						int code = conn2.getResponseCode();
//						if (code != 404) {
//							InputStream is = conn2.getInputStream();
//							image = BitmapFactory.decodeStream(is);
//							img[i - 1]=image;
//						//	meals[i-1].setBitmap(image);
//							//img[i - 1].setVisibility(1);
//							
//					        
////							img[i - 1].setOnClickListener(new View.OnClickListener() {
////					      	  
////					            public void onClick(View arg0) {
////					            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bilderspeiseplan.studentenwerk-dresden.de/m9/"
////											+ String.valueOf(year)
////											+ String.valueOf(month) + "/" + sid
////											+ ".jpg"));
////					                startActivity(browserIntent);
////					               
////					            }
////					     
////					    }
////					        );
//						}
//						
//					} else {
//						beschreibung = beschreibung.substring(26);
//						if (beschreibung.contains("izza"))
//							img[i - 1]=BitmapFactory.decodeResource(getResources(), R.drawable.pizza);
//						else
//						//meals[i-1].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pizza));
//						//if (beschreibung.contains("soße"))
//							img[i - 1]=BitmapFactory.decodeResource(getResources(), R.drawable.pasta);
//						//meals[i-1].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pasta));
//						//img[i - 1].setVisibility(1);
//						//titles[i - 1].setVisibility(1);
//						//preise[i - 1].setVisibility(1);
//					}
					
					if (beschreibung.contains("Pizza &amp; Pasta"))
						beschreibung = beschreibung.substring(27);

					titles[i - 1]=beschreibung;
					//meals[i-1].setBeschreibung(beschreibung);
				}
				
				
				
				
				
			for (int i=0;i<=7;i++) {
				meals[i] = new Type_mahlzeit(img[i], titles[i],preise[i],  sids[i]);	
				//if (i>=4) meals[i] = new mahlzeit(null, titles[i],preise[i],  sids[i]);
			}
			 
			 if (meals[0].getBeschreibung().length()>44)
				 remoteViews.setTextViewText(R.id.TextView001,meals[0].getBeschreibung().substring(0, 40)+"...");	
			 else
				 remoteViews.setTextViewText(R.id.TextView001,meals[0].getBeschreibung());	
			 
				 remoteViews.setTextViewText(R.id.TextView002,meals[0].getPreis());	
				 
			if (meals[1].getBeschreibung().length()>44) 
				remoteViews.setTextViewText(R.id.TextView003,meals[1].getBeschreibung().substring(0, 40)+"...");	
			else
				remoteViews.setTextViewText(R.id.TextView003,meals[1].getBeschreibung());	
				 	
				 remoteViews.setTextViewText(R.id.TextView004,meals[1].getPreis());	
				 
			if (meals[2].getBeschreibung().length()>44) 	 
				 remoteViews.setTextViewText(R.id.update0,meals[2].getBeschreibung().substring(0, 40)+"...");
			else
				 remoteViews.setTextViewText(R.id.update0,meals[2].getBeschreibung());
			
				 remoteViews.setTextViewText(R.id.textView02,meals[2].getPreis());	
				 
				 if (meals[3].getBeschreibung().length()>44) 	
					 
				remoteViews.setTextViewText(R.id.textView04,meals[3].getBeschreibung().substring(0, 40)+"...");	
				 else
					 remoteViews.setTextViewText(R.id.textView04,meals[3].getBeschreibung());		 
				 
				 
				 remoteViews.setTextViewText(R.id.textView06,meals[3].getPreis());	
			 
				 
				
			 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					  remoteViews.setTextViewText(R.id.TextView001,e.toString());	
					// remoteViews.setTextViewText(R.id.TextView001,e.toString());	
				}			 
			 
//			Open the App
				
//			Intent intent2 = new Intent(this.getApplicationContext(), HTWDDActivity.class);
//			
//			Bundle bundle = new Bundle();
//			bundle.putString("jumpto","mensa");		
//			intent2.putExtras(bundle);
//			
//			PendingIntent pendingIntent2 = PendingIntent.getActivity(this.getApplicationContext(), 0, intent2, 0);
//			// Get the layout for the App Widget and attach an on-click listener to the button
//			
//			remoteViews.setOnClickPendingIntent(R.id.widgetcontainer0, pendingIntent2);
			
	
				
				
					
			//		StrictMode.setThreadPolicy(old);
				strictMode.setThreadPolicy(origPolicy);
			
		
			
			
			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(),
					WidgetMensaProvider.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widgetcontainer0, pendingIntent);
			
			
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			
			
		}
		stopSelf();

		super.onStart(intent, startId);
	}

	
	
	
	
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}