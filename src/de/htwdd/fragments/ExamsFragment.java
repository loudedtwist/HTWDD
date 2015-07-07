package de.htwdd.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.htwdd.ExamsAdapter;
import de.htwdd.R;
import de.htwdd.WizardWelcome;
import de.htwdd.classes.Exams;
import de.htwdd.types.Exam;


public class ExamsFragment extends Fragment
{
    private String AbSc;
    private String Prof;
    private String Stg;
    private int StgJhrCurr = 90;
    private String Stgri;
    private Context context;
    private ExamsAdapter examsAdapter;
    private List<Exam> examses = new ArrayList<Exam>();


    private void setNameButtons(Activity activity)
    {
        Button localButton1 = (Button)activity.findViewById(R.id.examButtonAdd);
        Button localButton2 = (Button)activity.findViewById(R.id.examButtonSub);
        localButton1.setText("Prüfungen v. IM: " + (1 + this.StgJhrCurr));
        localButton2.setText("Prüfungen v. IM: " + (-1 + this.StgJhrCurr));
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        View view = paramLayoutInflater.inflate(R.layout.fragment_exams, paramViewGroup, false);

        context             = paramLayoutInflater.getContext();
        examsAdapter        = new ExamsAdapter(getActivity(), examses);
        ListView listView   = (ListView) view.findViewById(R.id.listExams);

        // Füge Footer hinzu
        View footer = paramLayoutInflater.inflate(R.layout.fragment_exams_footer, listView, false);
        listView.addFooterView(footer);

        // Setze Adapter
        listView.setAdapter(examsAdapter);

        // Prüfe ob alle Daten zur Abfrage vorhanden sind
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (((sharedPreferences.getString("im", "").length() != 2) || (sharedPreferences.getString("stdg", "").length() != 3) || (sharedPreferences.getString("abschluss", "").length() != 1))
                && (sharedPreferences.getString("prof_name", "").length() <= 3))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.noData)
                    .setMessage(R.string.exam_noData)
                    .setPositiveButton(R.string.startAssistent, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent nextScreen = new Intent(getActivity(), WizardWelcome.class);
                            startActivity(nextScreen);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            TextView message = (TextView) getActivity().findViewById(R.id.examNotes);
                            message.setText(R.string.exam_noData2);
                            getActivity().findViewById(R.id.examProgressbar).setVisibility(View.GONE);
                        }
                    }).show();

            return view;
        }

        // Wenn Prof. gesetzt dann lade diese Prüfungen
        if (sharedPreferences.getString("prof_name", "").length() > 3)
        {
            Prof = sharedPreferences.getString("prof_name", "");
            new ExamWorkerProf().execute();
            return view;
        }

        StgJhrCurr  = Integer.parseInt(sharedPreferences.getString("im", ""));
        Stg         = sharedPreferences.getString("stdg", "");
        AbSc        = sharedPreferences.getString("abschluss", "");
        Stgri       = sharedPreferences.getString("stgri", "");

        Button buttonAdd = (Button)view.findViewById(R.id.examButtonAdd);
        Button buttonSub = (Button)view.findViewById(R.id.examButtonSub);
        buttonAdd.setText("Prüfungen v. IM: " + (1 + this.StgJhrCurr));
        buttonSub.setText("Prüfungen v. IM: " + (-1 + this.StgJhrCurr));
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                StgJhrCurr++;
                setNameButtons(getActivity());
                new ExamWorkerStudent().execute();
            }
        });
        buttonSub.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                StgJhrCurr--;
                setNameButtons(getActivity());
                new ExamWorkerStudent().execute();
            }
        });

        // Lade Prüfungen für Studenten
        new ExamWorkerStudent().execute();

        return view;
    }

    private void taskFinished(Exams paramExams, int ResponseCode)
    {
        if (!isAdded())
            return;

        Activity activity = getActivity();
        this.examses.clear();

        activity.findViewById(R.id.examProgressbar).setVisibility(View.GONE);

        // Buttons zum Wechseln des Studienjahrgangs ggf. ausblenden
        Button button1 = (Button) activity.findViewById(R.id.examButtonSub);
        Button button2 = (Button) activity.findViewById(R.id.examButtonAdd);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        if (StgJhrCurr > 5 && StgJhrCurr < 90)
            button1.setVisibility(View.VISIBLE);
        if (StgJhrCurr < calendar.get(Calendar.YEAR)-2000)
            button2.setVisibility(View.VISIBLE);

        // TextView auf default setzen
        TextView examNotes      = (TextView) activity.findViewById(R.id.examNotes);
        TextView examNoteTitle  = (TextView) activity.findViewById(R.id.examNoteTitle);
        TextView examNoteText   = (TextView) activity.findViewById(R.id.examNoteText);
        examNotes.setText("");
        examNoteTitle.setVisibility(View.GONE);
        examNoteText.setVisibility(View.GONE);

        // Hinweise anzeigen
        if (ResponseCode == 900)
            examNotes.setText(R.string.app_no_internet);
        else if (ResponseCode != 200)
            examNotes.setText(R.string.pars_error);
        else if (paramExams.exams.size() == 0)
            examNotes.setText(R.string.exma_noExams);
        else
        {
            examNoteTitle.setVisibility(View.VISIBLE);
            examNoteText.setVisibility(View.VISIBLE);
        }

        // ListView anzeigen
        activity.findViewById(R.id.listExams).setVisibility(View.VISIBLE);

        // Adapter über neue Daten informieren
        Collections.addAll(examses, paramExams.exams.toArray(new Exam[paramExams.exams.size()]));
        examsAdapter.notifyDataSetChanged();
    }


    // Downloader für Professoren
    private class ExamWorkerProf extends AsyncTask<Void, Void, Exams>
    {
        private int ResponseCode;

        protected Exams doInBackground(Void[] params)
        {
            Exams localExams = new Exams(context);
            ResponseCode = localExams.getExamsInside(Prof);
            return localExams;
        }

        protected void onPostExecute(Exams paramExams)
        {
            taskFinished(paramExams, this.ResponseCode);
        }
    }

    // Downloader für Studenten
    private class ExamWorkerStudent extends AsyncTask<Void, Void, Exams>
    {
        private int ResponseCode;

        protected Exams doInBackground(Void[] paramArrayOfVoid)
        {
            Exams localExams = new Exams(context);
            ResponseCode = localExams.getExamsInside(StgJhrCurr, Stg, AbSc, Stgri);
            return localExams;
        }

        protected void onPostExecute(Exams paramExams)
        {
            taskFinished(paramExams, this.ResponseCode);
        }
    }
}