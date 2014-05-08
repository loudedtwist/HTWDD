package de.htwdd.types;


public class TPruefung
{


    public String _titel;
    public String _art;
    public String _tag;
    public String _zeit;
    public String _raum;
    public String _pruefer;


    public TPruefung(String titel, String art, String tag, String zeit, String raum, String pruefer)
    {
        this._titel = titel;
        this._tag = tag;
        this._zeit = zeit;
        this._raum = raum;
        this._art = art;
        this._pruefer = pruefer;
    }


    public void setTitel(String titel)
    {
        this._titel = titel;
    }


    public void setTag(String tag)
    {
        this._tag = tag;
    }

    public void setZeit(String zeit)
    {
        this._zeit = zeit;
    }

    public void setRaum(String raum)
    {
        this._raum = raum;
    }


}