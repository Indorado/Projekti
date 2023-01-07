package Projekti;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Toiminta_alue {
    private StringProperty alueid;
    private StringProperty nimi;
    public Toiminta_alue(){}
    public Toiminta_alue(String id, String nimi){
        setID(id);
        setNimi(nimi);
    }
    public String getID(){ return alueIDProperty().get(); }
    public void setID(String alueid){alueIDProperty().set(alueid);}
    public StringProperty alueIDProperty() {
        if (alueid == null) alueid = new SimpleStringProperty(this, "alueid");
        return alueid;
    }
    public String getNimi(){return nimiProperty().get();}
    public void setNimi(String nimi){nimiProperty().set(nimi);}
    public StringProperty nimiProperty() {
        if (nimi == null) nimi = new SimpleStringProperty(this, "nimi");
        return nimi;
    }

}
