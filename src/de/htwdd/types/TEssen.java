package de.htwdd.types;

import android.graphics.Bitmap;

public class TEssen
{


    //private variables
    int _id;
    String _tag;
    int _woche;
    String _title;
    String _preis;
    String _sonst;
    Bitmap _bild;
    String _bildset;
    public String _comments;
    public int _pro;
    public String mensa;
    public int _contra;

    // Empty constructor
    public TEssen()
    {
    }

    public TEssen(int id, String tag, int woche, String title, String preis, String sonst, Bitmap bild, String bildset)
    {
        this._id = id;
        this._tag = tag;
        this._woche = woche;
        this._title = title;
        this._preis = preis;
        this._sonst = sonst;
        this._bild = bild;
        this._bildset = bildset;
    }

    public TEssen(String tag, int woche, String title, String preis, String sonst, Bitmap bild, String bildset)
    {
        this._tag = tag;
        this._woche = woche;
        this._title = title;
        this._preis = preis;
        this._sonst = sonst;
        this._bild = bild;
        this._bildset = bildset;
    }

    public int getID()
    {
        return this._id;
    }

    public void setID(int id)
    {
        this._id = id;
    }

    public String getTag()
    {
        return this._tag;
    }

    public void setTag(String tag)
    {
        this._tag = tag;
    }

    public int getWoche()
    {
        return this._woche;
    }

    public void setWoche(int woche)
    {
        this._woche = woche;
    }

    public String getTitle()
    {
        return this._title;
    }

    public void setTitle(String title)
    {
        title = title.replace("&amp;amp;", "&");
        this._title = title;
    }

    public String getPreis()
    {
        return this._preis;
    }

    // setting name
    public void setPreis(String preis)
    {
        this._preis = preis;
    }

    public String getSonst()
    {
        return this._sonst;
    }

    public void setSonst(String sonst)
    {
        this._sonst = sonst;
    }

    // getting name
    public Bitmap getBild()
    {
        return this._bild;
    }

    // setting name
    public void setBild(Bitmap bild)
    {
        this._bild = bild;
    }

    public String getBildset()
    {
        return this._bildset;
    }

    // setting name
    public void setBildset(String bildset)
    {
        this._bildset = bildset;
    }


}
