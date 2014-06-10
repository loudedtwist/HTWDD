package de.htwdd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import de.htwdd.R;

import de.htwdd.types.Type_Stunde;

public class EditTimetable extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editstunde);


        Bundle b = getIntent().getExtras();
        int tag = b.getInt("tag");
        int stunde = b.getInt("stunde");
        int week = b.getInt("week");

        if (week == 2) week = 0;


        // Toast.makeText(this, ("Woche:" + week + " Tag:" + tag + " Stunde:" + pos ), Toast.LENGTH_SHORT).show();

        Spinner spgerade = (Spinner) findViewById(R.id.spinner1);
        Spinner sptag = (Spinner) findViewById(R.id.spinnerTag);
        Spinner spstunde = (Spinner) findViewById(R.id.spinnerStunde);
        spgerade.setSelection(week);
        sptag.setSelection(tag);
        spstunde.setSelection(stunde - 1);


        final DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(this);


        if (week == 0) week = 2;

        String daystring = "";
        if (tag == 0) daystring = "Montag";
        if (tag == 1) daystring = "Dienstag";
        if (tag == 2) daystring = "Mittwoch";
        if (tag == 3) daystring = "Donnerstag";
        if (tag == 4) daystring = "Freitag";

        final Type_Stunde type_Stunde = db.getStunde(daystring, week, stunde);


        EditText edittextbezeichnung = (EditText) findViewById(R.id.editTextBezeichnung);
        EditText edittextraum = (EditText) findViewById(R.id.editTextRaum);

        edittextbezeichnung.setText(type_Stunde.getName());
        edittextraum.setText(type_Stunde.getRaum());

        int typint = 0;
        if (type_Stunde.getTyp() != null)
        {
            if (type_Stunde.getTyp().equals("Vorlesung")) typint = 0;
            if (type_Stunde.getTyp().equals("Praktikum")) typint = 1;
            if (type_Stunde.getTyp().equals("Übung")) typint = 2;
        }
        Spinner sptyp = (Spinner) findViewById(R.id.spinnerTyp);
        sptyp.setSelection(typint);

        Button btnloesch = (Button) findViewById(R.id.eloesch);
        Button btnedit = (Button) findViewById(R.id.btnstundedit);


        btnloesch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {


                Log.d("Deleting", type_Stunde.getWoche() + " " + type_Stunde.getTag() + " " + type_Stunde.getStunde() + " " + type_Stunde.getName() + " " + type_Stunde.getTyp() + " " + type_Stunde.getRaum() + " ");
                db.deleteStunde(type_Stunde);
                db.addContact(new Type_Stunde(type_Stunde.getWoche(), type_Stunde.getTag(), type_Stunde.getStunde(), "(leer)", "", ""));
                Toast.makeText(arg0.getContext(), ("Eintrag gelöscht"), Toast.LENGTH_SHORT).show();
                // android.support.v4.view.ViewPager v = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);


                finish();
            }
        });


        btnedit.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {

                Toast.makeText(arg0.getContext(), ("Bitte warten"), Toast.LENGTH_SHORT).show();

                Spinner spgerade = (Spinner) findViewById(R.id.spinner1);
                Spinner sptag = (Spinner) findViewById(R.id.spinnerTag);
                Spinner spstunde = (Spinner) findViewById(R.id.spinnerStunde);

                //Toast.makeText(arg0.getContext(), (int) sptag.getSelectedItemId(), Toast.LENGTH_SHORT).show();

// 	       @SuppressWarnings("unused")
//		int bla =(int) sptag.getSelectedItemId();
// 	       int r;
// 	       r=0;
// 	         	        
// 	        
                String daystring = "";
                if ((int) sptag.getSelectedItemId() == 0) daystring = "Montag";
                if ((int) sptag.getSelectedItemId() == 1) daystring = "Dienstag";
                if ((int) sptag.getSelectedItemId() == 2) daystring = "Mittwoch";
                if ((int) sptag.getSelectedItemId() == 3) daystring = "Donnerstag";
                if ((int) sptag.getSelectedItemId() == 4) daystring = "Freitag";

                EditText edittextbezeichnung = (EditText) findViewById(R.id.editTextBezeichnung);
                EditText edittextraum = (EditText) findViewById(R.id.editTextRaum);


                Spinner sptyp = (Spinner) findViewById(R.id.spinnerTyp);
                String rtyp = "";
                if ((int) sptyp.getSelectedItemId() == 0) rtyp = "Vorlesung";
                if ((int) sptyp.getSelectedItemId() == 1) rtyp = "Praktikum";
                if ((int) sptyp.getSelectedItemId() == 2) rtyp = "Übung";
                if ((int) sptyp.getSelectedItemId() == 3) rtyp = "anderes";

                int woche = (int) spgerade.getSelectedItemId();
                if (woche == 0) woche = 2;

//	   	   stunde.setName(edittextbezeichnung.getText().toString());
//	   	stunde.setRaum(edittextraum.getText().toString());
//	   	stunde.setWoche(woche);
//	   	stunde.setTag(daystring);
//	   	stunde.setTyp(rtyp);
//	   	stunde.setStunde( (int) spstunde.getSelectedItemId()+1);
//	       
                type_Stunde.setName("(leer)");
                type_Stunde.setTyp("");
                type_Stunde.setRaum("");
                db.overwriteStunde(type_Stunde);

                Type_Stunde s = new Type_Stunde();
                s.setName(edittextbezeichnung.getText().toString());
                s.setRaum(edittextraum.getText().toString());
                s.setWoche(woche);
                s.setTag(daystring);
                s.setTyp(rtyp);
                s.setStunde((int) spstunde.getSelectedItemId() + 1);

                db.overwriteStunde(s);
                Toast.makeText(arg0.getContext(), ("Eintrag geändert"), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}