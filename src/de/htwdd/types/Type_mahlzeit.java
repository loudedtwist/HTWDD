package de.htwdd.types;

import android.graphics.Bitmap;


public class Type_mahlzeit
{
    public Bitmap bitmap;

    public String beschreibung;
    public String preis;
    public String sid;


    public Type_mahlzeit(Bitmap b, String besch, String p, String s)
    {
        this.bitmap = b;

        this.beschreibung = besch;
        this.preis = p;
        this.sid = s;
    }


    public void setBitmap(Bitmap b)
    {
        this.bitmap = b;
    }


    public void setBeschreibung(String b)
    {
        this.beschreibung = b;
    }

    public void setPreis(String p)
    {
        this.preis = p;
    }

    public void setSID(String sid)
    {
        this.sid = sid;
    }


    public Bitmap getBitmap()
    {
        return this.bitmap;
    }


    public String getBeschreibung()
    {
        if (this.beschreibung == null) return "";

        return this.beschreibung;
    }

    public String getPreis()
    {
        return this.preis;
    }

    public String getSID()
    {
        return this.sid;
    }

}