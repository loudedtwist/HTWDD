package com.skyworxx.htwdd;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CardExpendableListAdapater extends SimpleExpandableListAdapter {
Context context;
	public CardExpendableListAdapater(Context context,
			List<? extends Map<String, ?>> groupData, int expandedGroupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom,
			int[] childTo) {
		super(context, groupData, expandedGroupLayout, expandedGroupLayout, groupFrom,
				groupTo, childData, childLayout, childLayout, childFrom, childTo);
		// TODO Auto-generated constructor stub
		this.context=context;
	
	}

	 @Override
	  public View getGroupView(final int position,boolean enabled, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.notenrow, parent, false);
	   rowView.setBackgroundResource(R.drawable.search_bg_shadow);
	    
	  

	    return rowView;
	  }
	
	
}
