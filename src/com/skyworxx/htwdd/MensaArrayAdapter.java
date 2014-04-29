package com.skyworxx.htwdd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.skyworxx.htwdd.types.TEssen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import org.apache.commons.lang3.StringEscapeUtils;

public class MensaArrayAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private String[] prices;
    private String[] titles;
    private TEssen[] essen;
    ViewGroup parent;

    SharedPreferences app_preferences;


    public MensaArrayAdapter(Context context, String[] titles, TEssen[] essen)
    {
        super(context, R.layout.mensarow, titles);
        this.context = context;
        this.essen = essen;
        this.titles = titles;

        app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mensarow, parent, false);
        TextView titles_tv = (TextView) rowView.findViewById(R.id.titel);
        TextView prices_tv = (TextView) rowView.findViewById(R.id.preis);

        ImageView image_iv = (ImageView) rowView.findViewById(R.id.image);
        this.parent = parent;

        Button button;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            button = new Button(context, null, android.R.attr.borderlessButtonStyle);
            button.setTextColor(Color.parseColor("#33B5E5"));
        }
        else
            button = new Button(context, null, android.R.attr.buttonStyleSmall);

        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        button.setText("mehr Informationen");
        LinearLayout ln = (LinearLayout) rowView.findViewById(R.id.menurow);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.studentenwerk-dresden.de/mensen/speiseplan/details-" + essen[position].getID() + ".html?pni=1"));
                context.startActivity(browserIntent);
            }
        });

        ln.addView(button, lp);

        if (essen[position].mensa == null)
            essen[position].mensa = "Reichenbachstraße";

        if (essen[position].getBild() != null)
            image_iv.setImageBitmap(essen[position].getBild());
        else
            image_iv.setVisibility(View.GONE);

        titles_tv.setText(titles[position]);
        prices_tv.setText(essen[position].getSonst());

        return rowView;
    }
}