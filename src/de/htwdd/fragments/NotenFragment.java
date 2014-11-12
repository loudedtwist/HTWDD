package de.htwdd.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.htwdd.NotenListAdapter;
import de.htwdd.R;
import de.htwdd.WizardWelcome;
import de.htwdd.classes.Noten;
import de.htwdd.types.Grade;


public class NotenFragment extends Fragment
{
    private int mode = 0;

    private NotenListAdapter mAdapter;
    private Context context;
    private int countNoten;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<Grade>> listDataChild = new HashMap<String, List<Grade>>();

    public NotenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (getArguments() != null)
            mode = getArguments().getInt("mode");

        context = inflater.getContext();

        View view = inflater.inflate(R.layout.fragment_noten, container, false);

        // Get the ListView
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        mAdapter = new NotenListAdapter(context, listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Noten noten = new Noten(context);
        noten.getNotenLocal();
        countNoten = noten.noten.length;

        // Hinweis falls keine Noten vorhanden
        if (countNoten == 0)
        {
            LinearLayout hinweis = (LinearLayout) getActivity().findViewById(R.id.NotenHinweis);
            hinweis.setVisibility(View.VISIBLE);
            TextView message = (TextView) getActivity().findViewById(R.id.NotenHinweisText);
            message.setText("Keine Noten vorhanden!");
        }

        // Kontrolle ob Parameter (SNummer, RZLogin) überhaupt vorhanden
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if ((sharedPreferences.getString("bib", "").length() < 5) || (sharedPreferences.getString("RZLogin", "").length()) < 3)
        {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Für die Notenanzeige müssen sNummer und RZ-Login angegeben werden\n\nSoll der Konfigurations-Assistent gestartet werden?")
                    .setTitle("Fehlende Daten")
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton("Assistenten starten", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent nextScreen = new Intent(getActivity(), WizardWelcome.class);
                            startActivity(nextScreen);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            LinearLayout hinweis = (LinearLayout) getActivity().findViewById(R.id.NotenHinweis);
                            hinweis.setVisibility(View.VISIBLE);
                            TextView message = (TextView) getActivity().findViewById(R.id.NotenHinweisText);
                            message.setText("Die Noten konnten auf Grund von fehlenden Daten nicht aktualisiert werden.");
                        }
                    }).show();
        }
        // Button aktuallisieren wurde gedrückt
        else if (mode == 1)
        {
            LinearLayout hinweis = (LinearLayout) getActivity().findViewById(R.id.NotenHinweis);
            hinweis.setVisibility(View.VISIBLE);
            ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.NotenProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) getActivity().findViewById(R.id.NotenHinweisText);
            message.setText("Lade Noten...");

            WorkerGetNoten worker = new WorkerGetNoten();
            worker.execute();
        }
        else
            sortData(noten);
    }

    private void sortData(Noten noten)
    {
        listDataHeader.clear();
        listDataChild.clear();

        for (Grade note : noten.noten)
        {
            String semester = Noten.getSemester(note.Semester);
            if (!listDataHeader.contains(semester))
            {
                listDataHeader.add(semester);
                listDataChild.put(semester, new ArrayList<Grade>());
            }
            listDataChild.get(semester).add(note);
        }
    }

    private class WorkerGetNoten extends AsyncTask<Void, Noten, Noten>
    {
        int responseCode;

        @Override
        protected Noten doInBackground(Void... voids)
        {
            Noten noten = new Noten(context);
            responseCode = noten.getNotenHIS();

            if (responseCode==200)
                noten.saveNotenLocal();

            return noten;
        }

        @Override
        protected void onPostExecute(Noten noten)
        {
            // check if the fragment is currently added to its activity
            // otherwise getActivity will throw an exception
            if(isAdded())
            {
                TextView message;

                // Blende ProgressBar aus
                LinearLayout hinweis = (LinearLayout) getActivity().findViewById(R.id.NotenHinweis);
                hinweis.setVisibility(View.GONE);
                ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.NotenProgressBar);
                progressBar.setVisibility(View.GONE);

                sortData(noten);
                mAdapter.notifyDataSetChanged();

                // Hinweis falls keine Noten vorhanden
                if (noten.noten.length == 0)
                {
                    hinweis = (LinearLayout)  getActivity().findViewById(R.id.NotenHinweis);
                    hinweis.setVisibility(View.VISIBLE);
                    message = (TextView)  getActivity().findViewById(R.id.NotenHinweisText);
                    message.setText("Keine Noten vorhanden!");
                }

                // Fehler ausgeben
                switch (responseCode)
                {
                    case 999:
                        hinweis = (LinearLayout)  getActivity().findViewById(R.id.NotenHinweis);
                        hinweis.setVisibility(View.VISIBLE);
                        message = (TextView)  getActivity().findViewById(R.id.NotenHinweisText);
                        message.setText("Fehler beim Parsen der Noten!");
                        break;
                    case 401:
                        hinweis = (LinearLayout)  getActivity().findViewById(R.id.NotenHinweis);
                        hinweis.setVisibility(View.VISIBLE);
                        message = (TextView)  getActivity().findViewById(R.id.NotenHinweisText);
                        message.setText("Fehler bei der Authentifikation!\n\nBitte überprüfe dein sNummer und RZ-Login in den Einstellungen.");
                        break;
                    case 200:
                        // Hinweis ob neue Noten vorhanden
                        if (countNoten == (countNoten = noten.noten.length))
                            Toast.makeText(context,"Keine neuen Noten",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context,"Neue Noten heruntergeladen",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        hinweis = (LinearLayout)  getActivity().findViewById(R.id.NotenHinweis);
                        hinweis.setVisibility(View.VISIBLE);
                        message = (TextView)  getActivity().findViewById(R.id.NotenHinweisText);
                        message.setText("Fehler beim herunterladen der Noten!");
                        break;
                }
            }
        }
    }
}