package com.skyworxx.htwdd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Wizard2 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.wizard2, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Button b1 = (Button) getActivity().findViewById(R.id.button1);

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HTWDD/HTWDD"));
                startActivity(browserIntent);
            }
        });
    }
}