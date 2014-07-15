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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.career_beratung, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Button ButtonTermin, ButtonJobboerse, ButtonMentoring;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= 14)
        {
            ButtonTermin = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            ButtonTermin.setTextColor(Color.parseColor("#33B5E5"));

            ButtonJobboerse = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            ButtonJobboerse.setTextColor(Color.parseColor("#33B5E5"));

            ButtonMentoring = new Button(getActivity(), null, android.R.attr.borderlessButtonStyle);
            ButtonMentoring.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
        {
            ButtonTermin    = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
            ButtonJobboerse = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
            ButtonMentoring = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
        }

        ButtonTermin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ButtonTermin.setText("Termin anfragen");

        ButtonJobboerse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ButtonJobboerse.setText("Jobbörse im Browser öffnen");

        ButtonMentoring.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ButtonMentoring.setText("Mentoring im Browser öffnen");

        LinearLayout layoutBeratung  = (LinearLayout) getActivity().findViewById(R.id.CareerBeratung);
        LinearLayout layoutJobboerse = (LinearLayout) getActivity().findViewById(R.id.CareerJobboerse);
        LinearLayout layoutMentoring = (LinearLayout) getActivity().findViewById(R.id.CareerMentoring);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        layoutBeratung.addView(ButtonTermin, lp);
        layoutJobboerse.addView(ButtonJobboerse, lp);
        layoutMentoring.addView(ButtonMentoring, lp);

        ButtonTermin.setOnClickListener(new TerminOnClickListener());
        ButtonJobboerse.setOnClickListener(new JobboerseOnClickListener());
        ButtonMentoring.setOnClickListener(new MentoringOnClickListener());
    }


    class TerminOnClickListener implements OnClickListener
    {
        public void onClick(View v)
        {
            Intent i = new Intent(getActivity(), datepicker.class);
            getActivity().startActivity(i);
        }
    }

    class JobboerseOnClickListener implements OnClickListener
    {
        public void onClick(View v)
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.htw-dresden.de/jobboerse/"));
            getActivity().startActivity(i);
        }
    }

    class MentoringOnClickListener implements OnClickListener
    {
        public void onClick(View v)
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www2.htw-dresden.de/~mumm/mentoring/"));
            getActivity().startActivity(i);
        }
    }
}