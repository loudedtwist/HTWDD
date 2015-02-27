package de.htwdd.classes;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.htwdd.types.Exam;

public class Exams
{
    public ArrayList<Exam> exams = new ArrayList<Exam>();


    private int parseExams(String url)
    {
        HTTPDownloader downloader = new HTTPDownloader(url);
        String response = downloader.getStringUTF8();

        if (downloader.ResponseCode != 200)
            return downloader.ResponseCode;

        try
        {
            JSONArray array = new JSONArray(response);
            int countExams  = array.length();

            for (int i=0; i < countExams; i++)
            {
                Exam exam = new Exam();
                JSONObject object = array.getJSONObject(i);

                exam.Title          = object.getString("Title");
                exam.ExamType       = object.getString("ExamType");
                exam.StudyBranch    = object.getString("StudyBranch");
                exam.Day            = object.getString("Day");
                exam.StartTime      = object.getString("StartTime");
                exam.EndTime        = object.getString("EndTime");
                exam.Examiner       = object.getString("Examiner");
                exam.NextChance     = object.getString("NextChance");
                exam.Rooms          = "";

                JSONArray arrayRooms = object.getJSONArray("Rooms");
                int countRooms = arrayRooms.length();

                for (int y= 0; y < countRooms; y++)
                {
                    exam.Rooms = arrayRooms.getString(y);

                    if (y < countRooms-1)
                        exam.Rooms += ", ";
                }

                exams.add(exam);
            }
        }
        catch (Exception e)
        {
            return 900;
        }

        Collections.sort(this.exams, new ComparatorDay());
        return downloader.ResponseCode;
    }

    public int getExamsInside(int StgJhr, String Stg, String AbSc, String Stgri)
    {
        return parseExams("https://www2.htw-dresden.de/~app/API/GetExams.php?StgJhr=20"+String.format("%02d", StgJhr) + "&Stg=" + Stg + "&AbSc=" + AbSc + "&Stgri=" + Stgri);
    }

    public int getExamsInside(String Prof)
    {
        return parseExams("https://www2.htw-dresden.de/~app/API/GetExams.phpetExams.php?Prof=" + Prof);
    }

    public class ComparatorDay implements Comparator<Exam>
    {
        public int compare(Exam paramExam1, Exam paramExam2)
        {
            return paramExam1.Day.compareTo(paramExam2.Day);
        }
    }
}
