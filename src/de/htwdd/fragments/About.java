package de.htwdd.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.htwdd.R;


public class About extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        TextView about_email = (TextView) activity.findViewById(R.id.about_email);
        about_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendEmail(activity,new String[]{getText(R.string.about_email).toString()},"Write Email to HTWDresden App", "[Android]", "");
            }
        });


        TextView about_website = (TextView) activity.findViewById(R.id.about_link_website);
        about_website.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.about_link_website)));
                startActivity(browserIntent);
            }
        });

        TextView about_github = (TextView) activity.findViewById(R.id.about_link_github);
        about_github.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.about_link_github)));
                startActivity(browserIntent);
            }
        });
    }

    public static void sendEmail(Context context, String[] recipientList, String title, String subject, String body)
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipientList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, title));
    }

}