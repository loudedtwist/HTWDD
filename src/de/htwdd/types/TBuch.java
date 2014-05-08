package de.htwdd.types;

import java.util.Date;


public class TBuch
{


    //private variables
    public String _barcode;
    public String _titel;
    public String _shorttitel;
    public String _verfasser;
    public Date _bis;
    public Date _verab;
    public int _anzv;

    // Empty constructor
    public TBuch()
    {

    }

    public TBuch(String barcode, String titel, String shorttitel, String verfasser, Date bis, Date verab, int anzv)
    {
        this._barcode = barcode;
        this._titel = titel;
        this._shorttitel = shorttitel;
        this._verfasser = verfasser;
        this._bis = bis;
        this._verab = verab;
        this._anzv = anzv;
    }

}

