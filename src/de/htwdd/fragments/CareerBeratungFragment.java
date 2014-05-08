package de.htwdd.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import de.htwdd.R;
import de.htwdd.datepicker;


public class CareerBeratungFragment extends Fragment
{

    public int mode;

    public CareerBeratungFragment()
    {

    }

    public CareerBeratungFragment(int i)
    {
        // TODO Auto-generated constructor stub
        mode = i;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.career_beratung, null);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        Button button, button2;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            //?android:attr/borderlessButtonStyle

            button = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            //	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
            button.setTextColor(Color.parseColor("#33B5E5"));

            button2 = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            //	 (Button) inflater.inflate(android.R.attr.borderlessButtonStyle,parent, false);
            button2.setTextColor(Color.parseColor("#33B5E5"));

        }
        else
        {
            button = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
            button2 = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
        }
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button.setText("Termin anfragen");

        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button2.setText("Jobbörse im Browser öffnen");

        LinearLayout ln = (LinearLayout) getActivity().findViewById(R.id.menulayout);
        LinearLayout jln = (LinearLayout) getActivity().findViewById(R.id.joblayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        ln.addView(button, lp);
        jln.addView(button2, lp);

        button.setOnClickListener(new MyOnClickListener());
        button2.setOnClickListener(new MyOnClickListener2());


    }


    class MyOnClickListener implements OnClickListener
    {
        public void onClick(View v)
        {
            Intent i = new Intent(getActivity(), datepicker.class);
            getActivity().startActivity(i);
        }
    }

    class MyOnClickListener2 implements OnClickListener
    {
        public void onClick(View v)
        {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.htw-dresden.de/jobboerse/"));
            getActivity().startActivity(i);
        }
    }

}
