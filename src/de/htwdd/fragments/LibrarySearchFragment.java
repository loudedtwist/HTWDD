package de.htwdd.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwdd.LibrarySearchAdapter;
import de.htwdd.R;
import de.htwdd.classes.Library;
import de.htwdd.types.Medium;

public class LibrarySearchFragment extends Fragment
{
    private LibrarySearchAdapter mAdapter;
    private Context context;
    private ArrayList<Medium> mediums = new ArrayList<Medium>();
    private Dialog dialog;

    private int ResultPage;
    private boolean loading = false;

    public LibrarySearchFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_search, container, false);

        context = inflater.getContext();

        // Baue Dialog für die Anzeigen von Medien-Datails zusammen
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_library_item_detail);
        dialog.setTitle("Medien Details");

        // Schließen Button
        Button close = (Button) dialog.findViewById(R.id.MediumInfoClose);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.cancel();
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.BiBSeachResults);
        final EditText editText = (EditText)view.findViewById(R.id.BiBSeachInput);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        final TextView textView = (TextView)view.findViewById(R.id.BiBSeachInfo);

        // Add OnclickListener
        Button button = (Button) view.findViewById(R.id.BiBSeachSubmit);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Hide Keyboard
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                // Lösche alte Ergbnisse
                mediums.clear();
                mAdapter.notifyDataSetChanged();
                textView.setVisibility(View.GONE);

                // Zeige Progressbar an
                progressBar.setVisibility(View.VISIBLE);

                // Setze Startpage zum Suchen auf 1
                ResultPage = 1;

                // Starte Suche
                new SearchWorker().execute(editText.getText().toString());
            }
        });

        // Füge OnItemClickListener zur ListView hinzu
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                Medium medium = (Medium) mAdapter.getItem(i);

                // Blende Progressbar ein
                final ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

                // Blende Inhalt aus
                final TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.library_details_table);
                tableLayout.setVisibility(View.GONE);

                // Dialog anzeigen
                dialog.show();

                // Starte das Laden der Medien-Details
                GetDetailsWorker worker = new GetDetailsWorker();
                worker.execute(medium.ID);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i)
            {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3)
            {
                if (((i+i2) == i3) && (mediums.size() != 0) && !loading)
                {
                    // Bereits einen Task gestartet, für weitere sperren
                    loading = true;

                    // Seitenzahl erhöhen
                    ResultPage++;

                    // Starte Suche
                    new SearchWorker().execute(editText.getText().toString());
                }
            }
        });

        // Set Adapter
        mAdapter = new LibrarySearchAdapter(context, mediums);
        listView.setAdapter(mAdapter);

        return view;
    }


    private class GetDetailsWorker extends AsyncTask<String, Void, Medium>
    {

        @Override
        protected Medium doInBackground(String... strings)
        {
            return Library.getMedium(strings[0]);
        }

        @Override
        protected void onPostExecute(Medium medium)
        {
            if (!isAdded())
                return;

            // Blende Progressbar aus
            final ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            // Internetverbindung verloren
            final TextView textView = (TextView)dialog.findViewById(R.id.library_info);
            if (medium == null)
            {
                textView.setVisibility(View.VISIBLE);
                return;
            }
            else
                textView.setVisibility(View.GONE);

            // Blende Inhalt ein
            final TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.library_details_table);
            tableLayout.setVisibility(View.VISIBLE);

            // Felder anzeigen
            final TextView title = (TextView)dialog.findViewById(R.id.library_title);
            title.setText(medium.Title);

            final TextView mediumText = (TextView)dialog.findViewById(R.id.library_medium);
            mediumText.setText(medium.Medium);

            final TextView linkText = (TextView)dialog.findViewById(R.id.library_katalog);
            linkText.setText(Html.fromHtml("<a href=\""+medium.Link+"\">Katalog</a>"));
            linkText.setMovementMethod(LinkMovementMethod.getInstance());

            final TableRow signaturTextRow = (TableRow) dialog.findViewById(R.id.library_location_row);
            final TableRow authorRow = (TableRow)dialog.findViewById(R.id.library_author_row);
            final TableRow availabilityRow = (TableRow)dialog.findViewById(R.id.library_availability_row);


            // Author vorhanden? -> anzeigen oder ausblenden
            if (medium.Author != null)
            {
                final TextView author = (TextView)dialog.findViewById(R.id.library_author);
                author.setText(medium.Author);
                authorRow.setVisibility(View.VISIBLE);
            }
            else
                authorRow.setVisibility(View.GONE);

            // Location vorhanden? -> anzeigen oder ausblenden
            if (medium.Signatur != null)
            {
                final TextView signaturText = (TextView) dialog.findViewById(R.id.library_location);
                signaturText.setText(medium.Signatur);
                signaturTextRow.setVisibility(View.VISIBLE);
            }
            else
                signaturTextRow.setVisibility(View.GONE);

            // Verfügbarkeit vorhanden? -> anzeigen oder ausblenden
            if (!medium.Availability.isEmpty())
            {
                final TextView availabilityText = (TextView) dialog.findViewById(R.id.library_availability);

                // Verfügbarkeit farbig darstellen
                if (medium.Availability.equals("Verfügbar"))
                    availabilityText.setTextColor(getResources().getColor(R.color.green));
                else if (medium.Availability.equals("Ausgeliehen"))
                    availabilityText.setTextColor(getResources().getColor(R.color.red));
                else if (medium.Availability.equals("Bestellt"))
                    availabilityText.setTextColor(getResources().getColor(R.color.faded_orange));
                else
                    availabilityText.setTextColor(getResources().getColor(R.color.black));

                availabilityText.setText(medium.Availability);
                availabilityRow.setVisibility(View.VISIBLE);
            }
            else
                availabilityRow.setVisibility(View.GONE);
        }
    }

    private class SearchWorker extends AsyncTask<String,Void,ArrayList<Medium>>
    {
        @Override
        protected ArrayList<Medium> doInBackground(String... SearchTerm)
        {
            Library library = new Library();
            return library.search(SearchTerm[0], ResultPage);
        }

        @Override
        protected void onPostExecute(ArrayList<Medium> mediums2)
        {
            if (!isAdded())
                return;

            loading = false;

            // Info-Feld anzeigen?
            final TextView textView = (TextView)getActivity().findViewById(R.id.BiBSeachInfo);
            if (mediums2 == null)
            {
                textView.setText(getActivity().getText(R.string.app_no_internet));
                textView.setVisibility(View.VISIBLE);
                mediums2 =  new ArrayList<Medium>();
            }
            else if (mediums2.size() == 0)
            {
                if (ResultPage == 1)
                    textView.setText("Keine Ergebnisse gefunden");
                else
                    textView.setText("Keine weiteren Ergebnisse gefunden");
                textView.setVisibility(View.VISIBLE);

                // Weiteres Nachladen von Medien sperren
                loading = true;
            }

            // alte Items entfernen (nur bei neuer Suche)
            if (ResultPage == 1)
                mediums.clear();

            // neue Items hinzufügen
            mediums.addAll(mediums2);

            // Blende Progressbar aus
            final ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            mAdapter.notifyDataSetChanged();
        }
    }
}