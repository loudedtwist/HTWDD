package de.htwdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.htwdd.types.Exam;


public class ExamsAdapter extends BaseAdapter
{
    private List<Exam> examses;
    private LayoutInflater mLayoutInflater = null;

    public ExamsAdapter(Context context, List<Exam> paramList)
    {
        this.examses = paramList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return examses.size();
    }

    @Override
    public Object getItem(int i)
    {
        return examses.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        if (paramView == null)
            paramView = this.mLayoutInflater.inflate(R.layout.fragment_exams_item, paramViewGroup, false);

        TextView textTitle      = (TextView)paramView.findViewById(R.id.examTitle);
        TextView textType       = (TextView)paramView.findViewById(R.id.examTyp);
        TextView textStuyBranch = (TextView)paramView.findViewById(R.id.examStudyBranch);
        TextView textDate       = (TextView)paramView.findViewById(R.id.examDay);
        TextView textTime       = (TextView)paramView.findViewById(R.id.examTime);
        TextView textRoom       = (TextView)paramView.findViewById(R.id.examRoom);

        Exam exam = this.examses.get(paramInt);

        textTitle.setText(exam.Title);
        textType.setText(exam.ExamType);
        textStuyBranch.setText(exam.StudyBranch);
        textDate.setText(exam.Day);
        textRoom.setText(exam.Rooms);

        if (exam.EndTime.equals(""))
        {
            textTime.setText(exam.StartTime);
            return paramView;
        }

        textTime.setText(exam.StartTime + " - " + exam.EndTime);
        return paramView;
    }
}
