package Projekti;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Kohde {
    private StringProperty nimi;
    private StringProperty katuosoite;
    private StringProperty kuvaus;
    private StringProperty varustelu;
    private StringProperty henkilomaara;
    private StringProperty hinta;
    private StringProperty alv;
    public Kohde(){}
    //public Kohde(String nimi){ this.nimi =nimi; }
    public String getNimi(){ return nimiProperty().get();}
    public void setNimi(String nimi){ nimiProperty().set(nimi); }
    public StringProperty nimiProperty() {
        if (nimi == null) nimi = new SimpleStringProperty(this, "nimi");
        return nimi;
    }
    public String getKatuosoite(){ return katuosoiteProperty().get();}
    public void setKatuosoite(String katuosoite){ katuosoiteProperty().set(katuosoite); }
    public StringProperty katuosoiteProperty() {
        if (katuosoite == null) katuosoite = new SimpleStringProperty(this, "katuosoite");
        return katuosoite;
    }
    public String getKuvaus(){ return kuvausProperty().get();}
    public void setKuvaus(String kuvaus){ kuvausProperty().set(kuvaus); }
    public StringProperty kuvausProperty() {
        if (kuvaus == null) kuvaus = new SimpleStringProperty(this, "kuvaus");
        return kuvaus;
    }
    public String getVarustelu(){ return varusteluProperty().get();}
    public void setVarustelu(String varustelu){ varusteluProperty().set(varustelu); }
    public StringProperty varusteluProperty() {
        if (varustelu == null) varustelu = new SimpleStringProperty(this, "varustelu");
        return varustelu;
    }
    public String getHenkilomaara(){ return henkilomaaraProperty().get();}
    public void setHenkilomaara(String henkilomaara){ henkilomaaraProperty().set(henkilomaara); }
    public StringProperty henkilomaaraProperty() {
        if (henkilomaara == null) henkilomaara = new SimpleStringProperty(this, "henkilomaara");
        return henkilomaara;
    }
    public String getHinta(){ return hintaProperty().get();}
    public void setHinta(String hinta){ hintaProperty().set(hinta); }
    public StringProperty hintaProperty() {
        if (hinta == null) hinta = new SimpleStringProperty(this, "hinta");
        return hinta;
    }
    public String getALV(){ return alvProperty().get();}
    public void setALV(String alv){ alvProperty().set(alv); }
    public StringProperty alvProperty() {
        if (alv == null) alv = new SimpleStringProperty(this, "alv");
        return alv;
    }

}
