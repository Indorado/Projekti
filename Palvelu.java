package Projekti;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Palvelu{
    private StringProperty id;
    private StringProperty nimi;
    private StringProperty tyyppi;
    private StringProperty kuvaus;
    private StringProperty hinta;
    private StringProperty alv;
    public Palvelu(){}
    //public Palvelu(String nimi, String id){ this.nimi = nimi; this.id=id; }
    public String getId(){return idProperty().get();}
    public void setId(String id){idProperty().set(id);}
    public StringProperty idProperty() {
        if (id == null) id = new SimpleStringProperty(this, "id");
        return id;
    }
    public String getNimi(){ return nimiProperty().get(); }
    public void setNimi(String nimi){ nimiProperty().set(nimi);}
    public StringProperty nimiProperty() {
        if (nimi == null) nimi = new SimpleStringProperty(this, "nimi");
        return nimi;
    }
    public String getTyyppi(){return tyyppiProperty().get();}
    public void setTyyppi(String tyyppi){tyyppiProperty().set(tyyppi);}
    public StringProperty tyyppiProperty() {
        if (tyyppi == null) tyyppi = new SimpleStringProperty(this, "tyyppi");
        return tyyppi;
    }
    public String getKuvaus(){return kuvausProperty().get();}
    public void setKuvaus(String kuvaus){kuvausProperty().set(kuvaus);}
    public StringProperty kuvausProperty() {
        if (kuvaus == null) kuvaus = new SimpleStringProperty(this, "kuvaus");
        return kuvaus;
    }
    public String getHinta(){return hintaProperty().get();}
    public void setHinta(String hinta){hintaProperty().set(hinta);}
    public StringProperty hintaProperty() {
        if (hinta == null) hinta = new SimpleStringProperty(this, "hinta");
        return hinta;
    }
    public String getALV(){return alvProperty().get();}
    public void setALV(String alv){alvProperty().set(alv);}
    public StringProperty alvProperty() {
        if (alv == null) alv = new SimpleStringProperty(this, "alv");
        return alv;
    }

}
