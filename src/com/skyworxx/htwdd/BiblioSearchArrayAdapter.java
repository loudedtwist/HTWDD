package com.skyworxx.htwdd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;


import com.skyworxx.htwdd.types.TBuchSuche;
import com.skyworxx.htwdd.types.TEssen;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class BiblioSearchArrayAdapter extends ArrayAdapter<String> {
	  private final Context context;
	
	  private String[] titles;
	  private TBuchSuche[] essen;
	  ViewGroup parent;
	  


	  public BiblioSearchArrayAdapter(Context context, String[] titles, TBuchSuche[] buecher) {
	    super(context, R.layout.notenrow4, titles);
	    this.context = context;
	    this.essen=buecher;
	    this.titles = titles;
	    
	  
	    
	  }

	  @Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.notenrow4, parent, false);
	    TextView titles_tv = (TextView) rowView.findViewById(R.id.pruftitel);
	    TextView prices_tv = (TextView) rowView.findViewById(R.id.art);
	    TextView art_tv = (TextView) rowView.findViewById(R.id.pruftag);
	    ImageView image = (ImageView) rowView.findViewById(R.id.image);
	    
	    
	    this.parent=parent;
	
	 
	    
	    titles_tv.setText(titles[position]);
	    prices_tv.setText(essen[position]._verfasser);
	    if (essen[position]._pic!=null) image.setImageBitmap(essen[position]._pic);
	    if (essen[position]._info!=null)  art_tv.setText(Html.fromHtml(essen[position]._info));
	   // web_tv.loadData(essen[position], "text/html", "utf-8");
	
	
	

	    return rowView;
	  }
	  
	    
	  
	  
	  
	  
	  
	} 