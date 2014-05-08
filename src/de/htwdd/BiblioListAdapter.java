package de.htwdd;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class BiblioListAdapter extends SimpleAdapter
{
    private int[] colors = new int[]{0xFF222222, 0x00000000};

    public BiblioListAdapter(Context context, List<HashMap<String, String>> items,
                             int resource, String[] from, int[] to)
    {
        super(context, items, resource, from, to);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        // int colorPos = position % colors.length;
        int colorPos = 1;
        if ((position == 0) || (position == 8))
        {
            //colorPos = 0;
            //view.findViewById(R.id.gradient).setVisibility(View.VISIBLE);
        }
        view.setBackgroundColor(colors[colorPos]);


//		final int pos=position;
//		
//		view.setOnClickListener(new View.OnClickListener() {		      	  							
//		            public void onClick(View arg0) {	
//		            	
//		            	Intent i = new Intent(context2, MensaPopup.class);   		            	
//	            	i.putExtra("id", sids[pos]);
//	            	i.putExtra("title", titles[pos]);
//	            	context2.startActivity(i);
//		            	
//	            	
//	            	
//	            
//	            	
//	            	
//	            	
//		            }
//				});
//		


        return view;
    }
}
