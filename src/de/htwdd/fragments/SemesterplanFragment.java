package de.htwdd.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.htwdd.HTTPDownloader;
import de.htwdd.R;


public class SemesterplanFragment extends Fragment
{


    public SemesterplanFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.semesterplan, null);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


    }


    private class worker extends AsyncTask<Object, Integer, String[]>
    {


        @Override
        protected String[] doInBackground(Object... params)
        {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

            HTTPDownloader downloader = new HTTPDownloader("http://www2.htw-dresden.de/~rawa/cgi-bin/auf/sem_plan.php");


            String result = downloader.getsafeString();

            String semester = "";

            if (result.contains("Wintersemester")) semester = "Wintersemester";
            if (result.contains("Sommersemester")) semester = "Sommersemester";

            String[] semesterplan = {semester};


            return semesterplan;

        }

        protected void onProgressUpdate(Integer... progress)
        {


        }


        @Override
        protected void onPostExecute(String[] essen)
        {
            try
            {
                TextView semester = (TextView) getActivity().findViewById(R.id.semester);

                semester.setText(essen[0]);

            } catch (Exception e)
            {

            }
        }

    }


}
