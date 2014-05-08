package de.htwdd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import de.htwdd.R;

import de.htwdd.types.Type_Stunde;

import java.util.List;

public class MyAdapter extends BaseAdapter
{

    private Context context;
    private String[] texts = {" ", "Mo", "Di", "Mi", "Do", "Fr",
            "7:30", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "9:20", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "11:10", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "13:10", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "15:00", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "16:50", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
            "18:30", "SE S323", "CGKI S560", "CGKI S560", "CGKI S560", "CGKI S560",
    };
    private int height;
    private int width;
    public int week;

    public MyAdapter(Context context, int week, int width, int height)
    {
        this.context = context;
        this.height = height;
        this.width = width;
        this.week = week;


        DatabaseHandlerTimetable db = new DatabaseHandlerTimetable(context);

        List<Type_Stunde> mon = db.getStundenTag("Montag", week);
        db.close();
        Log.d("DB", "---------CLOSING DB-----------");
        db = new DatabaseHandlerTimetable(context);
        List<Type_Stunde> die = db.getStundenTag("Dienstag", week);
        db.close();
        Log.d("DB", "---------CLOSING DB-----------");
        db = new DatabaseHandlerTimetable(context);
        List<Type_Stunde> mit = db.getStundenTag("Mittwoch", week);
        db.close();
        Log.d("DB", "---------CLOSING DB-----------");
        db = new DatabaseHandlerTimetable(context);
        List<Type_Stunde> don = db.getStundenTag("Donnerstag", week);
        db.close();
        Log.d("DB", "---------CLOSING DB-----------");
        db = new DatabaseHandlerTimetable(context);
        List<Type_Stunde> fre = db.getStundenTag("Freitag", week);
        db.close();
        Log.d("DB", "---------CLOSING DB-----------");
        db = new DatabaseHandlerTimetable(context);

        Type_Stunde[] smon = new Type_Stunde[7];
        Type_Stunde[] sdie = new Type_Stunde[7];
        Type_Stunde[] smit = new Type_Stunde[7];
        Type_Stunde[] sdon = new Type_Stunde[7];
        Type_Stunde[] sfre = new Type_Stunde[7];


        for (int i = 0; i < 7; i++)
        {
            try
            {
                smon[i] = mon.get(i);
                sdie[i] = die.get(i);
                smit[i] = mit.get(i);
                sdon[i] = don.get(i);
                sfre[i] = fre.get(i);
            } catch (Exception e)
            {
            }
            ;
        }

        texts = new String[48];
        texts[0] = "Zeit\\Tag";
        texts[1] = "Mo";
        texts[2] = "Di";
        texts[3] = "Mi";
        texts[4] = "Do";
        texts[5] = "Fr";
        texts[6] = "7:30\n-\n9:00";
        texts[12] = "9:20\n-\n10:50";
        texts[18] = "11:10\n-\n12:40";
        texts[24] = "13:10\n-\n14:40";
        texts[30] = "15:00\n-\n16:30";
        texts[36] = "16:50\n-\n18:20";
        texts[42] = "18:30\n-\n20:00";


        for (int i = 6; i <= 47; i++)
        {

            try
            {
                if ((i == 7) || (i == 13) || (i == 19) || (i == 25) || (i == 31) || (i == 37) || (i == 43))
                    texts[i] = smon[(int) ((i - 1) / 6) - 1].getName() + '\n' + smon[(int) ((i - 1) / 6) - 1].getTyp() + '\n' + smon[(int) ((i - 1) / 6) - 1].getRaum();

                if ((i == 8) || (i == 14) || (i == 20) || (i == 26) || (i == 32) || (i == 38) || (i == 44))
                    texts[i] = sdie[(int) ((i - 2) / 6) - 1].getName() + '\n' + sdie[(int) ((i - 2) / 6) - 1].getTyp() + '\n' + sdie[(int) ((i - 2) / 6) - 1].getRaum();

                if ((i == 9) || (i == 15) || (i == 21) || (i == 27) || (i == 33) || (i == 39) || (i == 45))
                    texts[i] = smit[(int) ((i - 3) / 6) - 1].getName() + '\n' + smit[(int) ((i - 3) / 6) - 1].getTyp() + '\n' + smit[(int) ((i - 3) / 6) - 1].getRaum();

                if ((i == 10) || (i == 16) || (i == 22) || (i == 28) || (i == 34) || (i == 40) || (i == 46))
                    texts[i] = sdon[(int) ((i - 4) / 6) - 1].getName() + '\n' + sdon[(int) ((i - 4) / 6) - 1].getTyp() + '\n' + sdon[(int) ((i - 4) / 6) - 1].getRaum();

                if ((i == 11) || (i == 17) || (i == 23) || (i == 29) || (i == 35) || (i == 41) || (i == 47))
                    texts[i] = sfre[(int) ((i - 5) / 6) - 1].getName() + '\n' + sfre[(int) ((i - 5) / 6) - 1].getTyp() + '\n' + sfre[(int) ((i - 5) / 6) - 1].getRaum();

            } catch (Exception e)
            {
            }
            ;

        }


    }

    public int getCount()
    {
        return 48;
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
//        TextView tv;
//     
//        
//        if (convertView == null) {
//            tv = new TextView(context);
//            tv.setLayoutParams(new GridView.LayoutParams(85, 85));
//        }
//        else {
//            tv = (TextView) convertView;
//        }
//
//            tv.setText(texts[position]);
//            
//      //      if (position%6==0) tv.setBackgroundColor(Color.RED);
//            tv.setTextSize(9);
//        return tv;

        Button btn;
        if (convertView == null)
        {
            // if it's not recycled, initialize some attributes
            btn = new Button(context);
            // btn.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/6, (parent.getHeight()/8)));
            btn.setLayoutParams(new GridView.LayoutParams(width / 6, height / 8));


            btn.setPadding(0, 0, 0, 0);
        }
        else
        {
            btn = (Button) convertView;
        }

        btn.setText(texts[position]);
        // filenames is an array of strings
        //  btn.setTextColor(Color.WHITE);
        //  btn.setBackgroundResource(R.drawable.button);
        btn.setBackgroundColor(context.getResources().getColor(R.color.faded_grey));
        if (((String) btn.getText()).contains("(leer)"))
        {
            btn.setBackgroundColor(Color.parseColor("#00000000"));
            btn.setText("");
        }

        if (((String) btn.getText()).contains("Vorlesung"))
        {
            btn.setBackgroundColor(context.getResources().getColor(R.color.faded_blue));
        }
        if (((String) btn.getText()).contains("Übung"))
        {
            btn.setBackgroundColor(context.getResources().getColor(R.color.faded_green));
        }
        if (((String) btn.getText()).contains("Praktikum"))
        {
            btn.setBackgroundColor(context.getResources().getColor(R.color.faded_orange));
        }

        //  btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.search_bg_shadow));


        if ((position < 7) || (position % 6 == 0))
        {
            btn.setBackgroundColor(Color.parseColor("#29000000"));
            if (position < 6)
                btn.setLayoutParams(new GridView.LayoutParams(width / 6, height / 20));

        }

        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        btn.setId(position);

        //make times and days non clickable
        if (!(position <= 6) && (position != 12) && (position != 18) && (position != 24) && (position != 30) && (position != 36) && (position != 42))
            btn.setOnClickListener(new MyOnClickListener(position));

        return btn;


    }

    class MyOnClickListener implements OnClickListener
    {
        private int position;

        public MyOnClickListener(int position)
        {
            this.position = position;
        }

        public void onClick(View v)
        {
            // Preform a function based on the position


            int tag = 0;
            int stunde = 0;


            tag = (position - 1) % 6;

            stunde = ((position - tag) / 6);


            Intent i = new Intent(context, EditTimetable.class);
            Bundle bundle = new Bundle();

            bundle.putInt("tag", tag);
            bundle.putInt("week", week);
            bundle.putInt("stunde", stunde);
            i.putExtras(bundle);


            context.startActivity(i);


            //	 Toast.makeText(context,"Woche"+week+" - Tag:"+tag+" - Stunde:"+stunde, Toast.LENGTH_SHORT).show();
        }
    }

}
