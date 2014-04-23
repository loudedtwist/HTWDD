package com.skyworxx.htwdd;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class datepicker extends Activity
{

    private TextView mDateDisplay;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mTimeDisplay;
    private Button mPickTime;

    private int mhour;
    private int mminute;

    static final int TIME_DIALOG_ID = 1;

    static final int DATE_DIALOG_ID = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminanfrage);


        mDateDisplay = (TextView) findViewById(R.id.date);
        mPickDate = (Button) findViewById(R.id.datepicker);
        mTimeDisplay = (TextView) findViewById(R.id.time);
        mPickTime = (Button) findViewById(R.id.timepicker2);
        Button anfragen = (Button) findViewById(R.id.button2);

        //Pick time's click event listener
        mPickTime.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                showDialog(TIME_DIALOG_ID);
            }

        });

        //PickDate's click event listener
        mPickDate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                showDialog(DATE_DIALOG_ID);

            }
        });

        anfragen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                EditText name = (EditText) findViewById(R.id.editText1);

                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Terminanfrage: " + name.getText() + " [Über HTWDD App]");
                intent.putExtra(Intent.EXTRA_TEXT, "Guten Tag,\nIch würde gerne einen Termin mit Ihnen vereinbaren.\n\nMfg\n" + name.getText().toString() + "\n\n" + mDateDisplay.getText() + "\n" + mTimeDisplay.getText() + "\n\n\nDiese Email wurde automatisch von der HTWDD App generiert.");
                intent.setData(Uri.parse("mailto:tsonntag@htw-dresden.de")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);

            }
        });


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mhour = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);


    }

    //-------------------------------------------update date----------------------------------------//
    private void updateDate()
    {

        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear).append(" ")
        );
        showDialog(TIME_DIALOG_ID);

    }

    //-------------------------------------------update time----------------------------------------//
    public void updatetime()
    {
        mTimeDisplay.setText(
                new StringBuilder()
                        .append(pad(mhour)).append(":")
                        .append(pad(mminute))
        );
    }

    private static String pad(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


//Datepicker dialog generation

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener()
            {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth)
                {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDate();
                }
            };


    // Timepicker dialog generation
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    mhour = hourOfDay;
                    mminute = minute;
                    updatetime();
                }


            };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mhour, mminute, false);

        }
        return null;
    }

}