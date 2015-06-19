package de.htwdd.types;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import de.htwdd.R;

public class Lesson implements Cloneable
{
    public int internID;
    public String lessonTag;
    public String name;
    public String type;
    public int week;
    public int day;
    public int ds;
    public Time beginTime;
    public Time endTime;
    public String professor;
    public String weeksOnly;
    public String rooms;

    public Lesson clone() throws CloneNotSupportedException
    {
        return (Lesson) super.clone();
    }

    public int getTypeInt()
    {
        if (type.contains("V"))
            return 0;
        else if (type.contains("Pr"))
            return 1;
        else if (type.contains("Ü"))
            return 2;
        else return 3;
    }

    public void setTypeInt(int value)
    {
        switch (value)
        {
            case 0:
                type = "V";
                break;
            case 1:
                type = "Pr";
                break;
            case 2:
                type = "Ü";
                break;
        }
    }
}
