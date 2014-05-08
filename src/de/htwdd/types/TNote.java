package de.htwdd.types;


public class TNote
{

    public String _Nr;
    public String _Titel;
    public String _Sem;
    public String _Note;
    public String _Bestanden;
    public String _Credits;
    public String _ECTS;
    public String _Vermerk;
    public String _Versuch;
    public String _Datum;


    public TNote()
    {
        this._Nr = "leer";
        this._Titel = "leer";
        this._Sem = "leer";
        this._Note = "leer";
        this._Bestanden = "leer";
        this._Credits = "leer";
        this._ECTS = "leer";
        this._Vermerk = "leer";
        this._Versuch = "leer";
        this._Datum = "leer";
    }

    public TNote(String Nr, String Titel, String Sem, String Note, String Bestanden, String Credits, String ECTS, String Vermerk, String Versuch, String Datum)
    {
        this._Nr = Nr;
        this._Titel = Titel;
        this._Sem = Sem;
        this._Note = Note;
        this._Bestanden = Bestanden;
        this._Credits = Credits;
        this._ECTS = ECTS;
        this._Vermerk = Vermerk;
        this._Versuch = Versuch;
        this._Datum = Datum;
    }


}