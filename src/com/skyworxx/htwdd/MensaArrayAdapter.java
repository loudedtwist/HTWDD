package com.skyworxx.htwdd;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import org.apache.commons.lang3.StringEscapeUtils;

import com.skyworxx.htwdd.fragments.ResponsiveUIActivity;
import com.skyworxx.htwdd.types.TEssen;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MensaArrayAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private String[] prices;
	  private String[] titles;
	  private TEssen[] essen;
	  ViewGroup parent;
	  
SharedPreferences app_preferences;


	  public MensaArrayAdapter(Context context, String[] titles, TEssen[] essen) {
	    super(context, R.layout.mensarow, titles);
	    this.context = context;
	    this.essen=essen;
	    this.titles = titles;
	    
	    app_preferences = PreferenceManager.getDefaultSharedPreferences(context);   
	    
	    
	  }

	  @Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.mensarow, parent, false);
	    TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
	 //   TextView mensa_tv = (TextView) rowView.findViewById(R.id.mensa);
	    TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);
	    TextView  comments_tv = (TextView) rowView.findViewById(R.id.commenttext);
	   
	    TextView pro_tv = (TextView) rowView.findViewById(R.id.pro);
	    TextView contra_tv = (TextView) rowView.findViewById(R.id.contra);
	    ImageView image_iv = (ImageView) rowView.findViewById(R.id.image);
	    LinearLayout vote_ln=(LinearLayout) rowView.findViewById(R.id.vote);
	    this.parent=parent;
	    
	    
	    
	    
	    
		Button button;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 14){
			//?android:attr/borderlessButtonStyle

		 button =new Button(context, null, android.R.attr.borderlessButtonStyle); 
				//	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
		 button.setTextColor(Color.parseColor("#33B5E5"));

		}
		else
			 button =new Button(context, null, android.R.attr.buttonStyleSmall); 
			
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

		button.setText("mehr Informationen");
		LinearLayout ln=(LinearLayout) rowView.findViewById(R.id.menurow);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		button.setOnClickListener(new View.OnClickListener() {
			  @Override 
		      public void onClick(View arg0) {
			
				  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.studentenwerk-dresden.de/mensen/speiseplan/details-"+essen[position].getID()+".html?pni=1"));
				 context.startActivity(browserIntent);
					
		      }
		});
		
		
		
		ln.addView(button, lp);
	    
	    
	    
	    
	    
	    
	
	   Button commentbutton = (Button) rowView.findViewById(R.id.commentbutton);
	    ImageButton contra_button = (ImageButton) rowView.findViewById(R.id.ImageButton01);
	    ImageButton pro_button = (ImageButton) rowView.findViewById(R.id.ImageButton02);
	    
	    if (essen[position].mensa==null){
	    	essen[position].mensa="Reichenbachstraße";
	    	
	    }
	    
	    if (essen[position].mensa!=null){
		
		    //	mensa_tv.setVisibility(View.VISIBLE);
	   // mensa_tv.setText(essen[position].mensa);
	    }
	    
	    if (essen[position].getTitle()!=null)
	    if (essen[position].getTitle().contains("ein Angebot")){
	    	vote_ln.setVisibility(View.GONE);
	    	commentbutton.setVisibility(View.GONE);
	    	 comments_tv.setVisibility(View.GONE);
	    	
	    }
//	    	pro_button.setVisibility(View.GONE);
//	    	
//	    }
	    if (essen[position].getBild()!=null)
	    	image_iv.setImageBitmap(essen[position].getBild());
	    else
	    	 image_iv.setVisibility(View.GONE);
	    
	 //   RatingBar rate=(RatingBar) rowView.findViewById(R.id.ratingBar1);		
	//	if (essen[position]._rating!=99)  rate.setRating(essen[position]._rating);
	    
	   // ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    titles_tv.setText(titles[position]);
	    prices_tv.setText(essen[position].getSonst());
	 
	    
	    if (essen[position]._comments.length()!=0)
	    	comments_tv.setText(Html.fromHtml(essen[position]._comments));
	    else
	    	comments_tv.setVisibility(View.GONE);
	    
		pro_tv.setText(essen[position]._pro+" x");
		contra_tv.setText(essen[position]._contra+" x");
	//	if (app_preferences.getBoolean("enabled_comments", false)==false) commentbutton.setVisibility(View.GONE);
	
	
	commentbutton.setOnClickListener(new View.OnClickListener() {

		public void onClick(View arg0) {
			
			AlertDialog.Builder alert = new AlertDialog.Builder(context);

			

			alert.setTitle("Kommentar abgeben");
			alert.setMessage(essen[position].getTitle()+"\nKommentare sind anonym.");

			// Set an EditText view to get user input
			final EditText input = new EditText(context);
			alert.setView(input);

			alert.setPositiveButton("Abschicken", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			Editable value = input.getText();
			
			// Do something with value!
			
	
				
	
			SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
			java.util.Date myDate = new java.util.Date();
			String currentDate = timeStampFormat.format(myDate);
			
			String bib=app_preferences.getString("bib", "0");
			
			/*if (bib.length()<4) {
				Toast.makeText(getContext(), "Eine gültige S-Nummer muss angegeben sein um Kommentare zu veröffentlichen.",Toast.LENGTH_LONG).show();
				return;
				
			}*/
			
	     	if ((bib.contains("s"))||(bib.contains("S"))) bib=bib.substring(1);
			
			if (bib.length()<4) bib="NA";
			worker2 w2= new worker2();
			String url = "http://htwdd-app.de/functions/writecomments.php?id="+essen[position].getID()+"&comment="+URLEncoder.encode(value.toString())+"&name="+URLEncoder.encode(essen[position].getTitle())+"&mensa="+URLEncoder.encode(essen[position].mensa)+"&day="+URLEncoder.encode(currentDate);
		//	Toast.makeText(context, url, Toast.LENGTH_LONG).show();
			w2.execute(url,"comment_"+currentDate);
			//Toast.makeText(context,URLEncoder.encode(value.toString()), Toast.LENGTH_SHORT).show();
			
			
			
			
			}
			});

			alert.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
			}
			});

			alert.show();
			
			
			
			
		
		}
	});
	
	
	contra_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
				java.util.Date myDate = new java.util.Date();
				String currentDate = timeStampFormat.format(myDate);
				
				worker2 w2= new worker2();
				w2.execute("http://htwdd-app.de/functions/contra.php?id="+essen[position].getID(),""+currentDate);
			//	Toast.makeText(context,"Verbinde zum Server...", Toast.LENGTH_SHORT).show();
			
			}
		});
	
	
	pro_button.setOnClickListener(new View.OnClickListener() {

		public void onClick(View arg0) {
			SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
			java.util.Date myDate = new java.util.Date();
			String currentDate = timeStampFormat.format(myDate);
			
			worker2 w2= new worker2();
			w2.execute("http://htwdd-app.de/functions/pro.php?id="+essen[position].getID(),""+currentDate);
		//	Toast.makeText(context,"Verbinde zum Server...", Toast.LENGTH_SHORT).show();
		
		}
	});
	
	
	
	
	//    prices_tv.setText(prices[position]);
	   // textView.setTextColor(Color.WHITE);
	    
	   // textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
	    // Change the icon for Windows and iPhone
	  //  String s = prices[position];
	 
	   // t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, menuheight));
    	
	 

	    return rowView;
	  }
	  
	  
	  
	    private class worker2 extends AsyncTask<String, Integer, String> {
			@Override
			protected String doInBackground(String... params) {
			

				
				Calendar c = Calendar.getInstance(); 
				int hour = c.get(Calendar.HOUR_OF_DAY);
			
				
				if ((hour<11) || (hour>15)) return "Du kannst nur zu den Öffnungszeiten der Mensa Deine Bewertung abgeben.";
				
			//	boolean alreadyvoted = app_preferences.getBoolean(params[1], false);
			//	if (params[1].equals("0")) alreadyvoted=false;
				//if (alreadyvoted)  return "Du hast kannst maximal eine Bewertung pro Tag abgeben.";
				
				String line;
				String line2 = "";
															
				try{
				URL url = new URL(params[0]);
				URLConnection conn = url.openConnection();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				while ((line = rd.readLine()) != null) {
					line2 += line;
				}
				// wr.close();
				rd.close();
				}catch (Exception e){									
				return e.toString();
				};
				
				if (line2.contains("success")){
					
					
					
				}
			

				SharedPreferences.Editor editor = app_preferences.edit();
				editor.putBoolean(params[1], true);	
				editor.commit(); // Very important
			
				return "Deine Bewertung wurde abgegeben. Beim nächsten Laden wird sie angezeigt.";
				
				}
			
			
			@Override
			protected void onPostExecute(String res) {
				
					Toast.makeText(context,res, Toast.LENGTH_LONG).show();
				
					
					
				
			
				
			}
			
		}
	    
	    
	    
		
	    
	    
	  
	  
	  
	  
	  
	} 