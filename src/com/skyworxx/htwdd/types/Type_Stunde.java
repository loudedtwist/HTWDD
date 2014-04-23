package com.skyworxx.htwdd.types;

public class Type_Stunde
{


    //private variables
    int _id;
    int _woche;
    String _tag;
    int _stunde;
    String _name;
    String _typ;
    String _raum;

    // Empty constructor
    public Type_Stunde()
    {

    }

    public Type_Stunde(int id, int woche, String tag, int stunde, String name, String typ, String raum)
    {
        this._id = id;
        this._woche = woche;
        this._tag = tag;
        this._stunde = stunde;
        this._name = name;
        this._typ = typ;
        this._raum = raum;
    }

    public Type_Stunde(int woche, String tag, int stunde, String name, String typ, String raum)
    {

        this._woche = woche;
        this._tag = tag;
        this._stunde = stunde;
        this._name = name;
        this._typ = typ;
        this._raum = raum;
    }

    public int getID()
    {
        return this._id;
    }

    public void setID(int id)
    {
        this._id = id;
    }


    public int getWoche()
    {
        return this._woche;
    }

    public void setWoche(int woche)
    {
        this._woche = woche;
    }

    public String getTag()
    {
        return this._tag;
    }

    // setting name
    public void setTag(String tag)
    {
        this._tag = tag;
    }

    public int getStunde()
    {
        return this._stunde;
    }

    public void setStunde(int stunde)
    {
        this._stunde = stunde;
    }


    // getting name
    public String getName()
    {
        return this._name;
    }

    // setting name
    public void setName(String name)
    {
        this._name = name;
    }

    // getting name
    public String getTyp()
    {
        return this._typ;
    }

    // setting name
    public void setTyp(String typ)
    {
        this._typ = typ;
    }


    // getting name
    public String getRaum()
    {
        return this._raum;
    }

    // setting name
    public void setRaum(String raum)
    {
        this._raum = raum;
    }

}
