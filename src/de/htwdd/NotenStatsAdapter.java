package de.htwdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.htwdd.types.Stats;

public class NotenStatsAdapter extends BaseAdapter
{
    Context context;
    Stats[] statses;
    LayoutInflater inflater;

    public NotenStatsAdapter(Context context, Stats[] statses)
    {
        this.context = context;
        this.statses = statses;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return statses.length;
    }

    @Override
    public Object getItem(int i)
    {
        return statses[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_noten_stats_item, viewGroup, false);

        TextView semester = (TextView) view.findViewById(R.id.StatsSemester);
        TextView average  = (TextView) view.findViewById(R.id.StatsAverage);
        TextView note     = (TextView) view.findViewById(R.id.StatsNoten);
        TextView credits  = (TextView) view.findViewById(R.id.StatsCredits);
        TextView noteBest = (TextView) view.findViewById(R.id.StatsNoteBest);
        TextView noteWorst= (TextView) view.findViewById(R.id.StatsNoteWorst);

        semester.setText(statses[i].Semester);
        average.setText("Durchschnitt: "+String.format("%.2f",statses[i].Average));
        note.setText(statses[i].GradeCount+" Noten");
        credits.setText(statses[i].Credits+" Credits");
        noteBest.setText("beste Note: "+statses[i].GradeBest);
        noteWorst.setText("schlechteste Note: "+statses[i].GradeWorst);
        return view;
    }
}