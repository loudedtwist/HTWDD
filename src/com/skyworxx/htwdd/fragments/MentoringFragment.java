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
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
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


public class MentoringFragment extends Fragment {
	public String[] links;
	public String[] titles;
	public String[] location;
	public String[] shortinfo;
	 public  LinearLayout wait;
	 public int mode;
	 WebView web;
	 public MentoringFragment() {
			
		}
	 
	public MentoringFragment(int i) {
		// TODO Auto-generated constructor stub
	mode=i;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 return inflater.inflate(R.layout.mentoring, null);
		
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 web=(WebView) getView().findViewById(R.id.webView1);
		worker w = new worker();
		 w.execute(mode);
		
		
		wait = (LinearLayout) getView().findViewById(R.id.waitheute);
   	 wait.setVisibility(View.VISIBLE);
		

			
		
	}
	
	
	private class worker extends AsyncTask<Object, Integer, String> {
		

		@Override
		protected String doInBackground(Object... params) {

			HTTPDownloader downloader =null;
			if (mode==0) downloader=new HTTPDownloader("http://www2.htw-dresden.de/~mumm/mentoring2/mentoren/");
			if (mode==1) downloader=new HTTPDownloader("http://www2.htw-dresden.de/~mumm/");
			if (mode==2) downloader=new HTTPDownloader("http://www2.htw-dresden.de/~mumm/uber-uns/kontakt/");
			//if (mode==3) downloader=new HTTPDownloader("http://www2.htw-dresden.de/~mumm/uber-uns/");
			
			
			String result=downloader.getStringUTF8();			
			result=result.replace("https", "http");			
			result= result.substring(result.indexOf("<div id=\"content\">"),result.indexOf("<!-- #content -->"));
			
		/*	if (mode==0){ 
				downloader=new HTTPDownloader("http://www2.htw-dresden.de/~mumm/uber-uns/");
				String result2=downloader.getStringUTF8();			
				result2=result2.replace("https", "http");			
				result2= result2.substring(result2.indexOf("<div id=\"content\">"),result2.indexOf("<!-- #content -->"));
				result+=result2;
			}*/
			
		return result;
			
		}
	
		

		
		@Override
		protected void onPostExecute(String html) {
			try{
			//	ProgressBar p= (ProgressBar) getView().findViewById(R.id.waitIndicator);
			
	//	p.setVisibility(View.GONE);
		
		 final String mimeType = "text/html";
	        final String encoding = "UTF-8";
	
	        String styles="<html><head>" +
	        		" <link rel=\"stylesheet\" href=\"http://www2.htw-dresden.de/~mumm/wp-content/plugins/buddypress/bp-themes/bp-htw/style.css\" type=\"text/css\" media=\"screen\" />" +
	        		"<link rel='stylesheet' id='NextGEN-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nextgen-gallery/css/nggallery.css?ver=1.0.0' type='text/css' media='screen' />"+
					"<link rel='stylesheet' id='shutter-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nextgen-gallery/shutter/shutter-reloaded.css?ver=1.3.4' type='text/css' media='screen' />"+
					"<link rel='stylesheet' id='jq_ui_css-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/jquery-ui-1.8.13.custom.css?ver=1.8.13' type='text/css' media='all' />"+
					"<link rel='stylesheet' id='categories-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/cat_colors.css?ver=0.9.9.2' type='text/css' media='all' />"+
					"<link rel='stylesheet' id='custom-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/ajax-event-calendar/css/custom.css?ver=0.9.9.2' type='text/css' media='all' />"+
					"<link rel='stylesheet' id='lightboxStyle-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/lightbox-plus/css/shadowed/colorbox.css?ver=2.0.2' type='text/css' media='screen' />"+
					"<link rel='stylesheet' id='galleryview-css'  href='http://www2.htw-dresden.de/~mumm/wp-content/plugins/nggGalleryview/galleryview.css?ver=1.0.1' type='text/css' media='screen' />"+
					"</head><body>";
	        
	        wait = (LinearLayout) getView().findViewById(R.id.waitheute);
	      	 wait.setVisibility(View.GONE);

	        web.loadDataWithBaseURL("", styles+html+"</body></html>", mimeType, encoding, "");
	        web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
			}
			
			catch (Exception e){
			//	Toast.makeText(getActivity(), e.toString()+" pruning failed", Toast.LENGTH_SHORT).show();							
			}
			
			
			
		}
		
		
	
	}

	 
	
	
	}
