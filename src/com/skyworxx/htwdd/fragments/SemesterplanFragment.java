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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.view.Window;

import com.skyworxx.htwdd.EventPopup;
import com.skyworxx.htwdd.HTTPDownloader;

import com.skyworxx.htwdd.CareerArrayAdapter;
import com.skyworxx.htwdd.EditTimetable;
import com.skyworxx.htwdd.MensaArrayAdapter;
import com.skyworxx.htwdd.MensaWocheArrayAdapter;
import com.skyworxx.htwdd.MenuArrayAdapter;
import com.skyworxx.htwdd.R;
import com.skyworxx.htwdd.SpecialAdapter3;
import com.skyworxx.htwdd.TerminAnfragePopup;
import com.skyworxx.htwdd.types.TEssen;
import com.skyworxx.htwdd.types.TEvent;


public class SemesterplanFragment extends Fragment {
 
	 
	public SemesterplanFragment(){
		
	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
			 return inflater.inflate(R.layout.semesterplan, null);
	
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		





	}
	 
	

	private class worker extends AsyncTask<Object, Integer, String[]> {
		

		@Override
		protected String[] doInBackground(Object... params) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			HTTPDownloader downloader=new HTTPDownloader("http://www2.htw-dresden.de/~rawa/cgi-bin/auf/sem_plan.php");
			
	
			String result=downloader.getsafeString();
			
			String semester="";
			
			if (result.contains("Wintersemester")) semester= "Wintersemester";
			if (result.contains("Sommersemester")) semester= "Sommersemester";
			
			String[] semesterplan={semester};
			
		
			
	
		return semesterplan;
			
		}
	
		protected void onProgressUpdate(Integer... progress) {
	      	
	
	     }

		
		@Override
		protected void onPostExecute(String[] essen) {
			try{
				TextView semester=(TextView) getActivity().findViewById(R.id.semester);
				
				semester.setText(essen[0]);
				
			}
			catch (Exception e){
	
		}
		}

}
	
	
}
