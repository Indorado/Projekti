package Projekti;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Varaus {
    private  StringProperty sID;
    private  StringProperty sKohde;
    private  StringProperty sAsiakas;
    private  StringProperty sVarauspvm;
    private  StringProperty sAlkupvm;
    private  StringProperty sVahvpvm;
    private  StringProperty sLuovpvm;
    private  StringProperty sPalvelu;
    public Varaus(){ }
    public Varaus(String kohde, String asiakas, String varauspvm, String alkupvm, String vahvpvm, String luovpvm, String palvelu) {
        setKohde(kohde);
        setAsiakas(asiakas);
        setVarauspvm(varauspvm);
        setAlkupvm(alkupvm);
        setVahvpvm(vahvpvm);
        setLuovpvm(luovpvm);
        setPalvelu(palvelu);
    }
    public String getID(){ return varausIDProperty().get(); }
    public void setID(String id){ varausIDProperty().set(id); }
    public StringProperty varausIDProperty() {
        if (sID == null) sID = new SimpleStringProperty(this, "sID");
        return sID;
    }
    public String getKohde(){ return kohdeProperty().get(); }
    public void setKohde(String kohde){ kohdeProperty().set(kohde); }
    public StringProperty kohdeProperty() {
        if (sKohde == null) sKohde = new SimpleStringProperty(this, "sKohde");
        return sKohde;
    }
    public String getAsiakas(){ return asiakasProperty().get(); }
    public void setAsiakas(String asiakas){ asiakasProperty().set(asiakas); }
    public StringProperty asiakasProperty() {
        if (sAsiakas == null) sAsiakas = new SimpleStringProperty(this, "sAsiakas");
        return sAsiakas;
    }
    public String getVarauspvm(){ return varauspvmProperty().get(); }
    public void setVarauspvm(String varauspvm){ varauspvmProperty().set(varauspvm); }
    public StringProperty varauspvmProperty() {
        if (sVarauspvm == null) sVarauspvm = new SimpleStringProperty(this, "sVarauspvm");
        return sVarauspvm;
    }
    public String getAlkupvm(){ return alkupvmProperty().get(); }
    public void setAlkupvm(String alkupvm){ alkupvmProperty().set(alkupvm); }
    public StringProperty alkupvmProperty() {
        if (sAlkupvm == null) sAlkupvm = new SimpleStringProperty(this, "sAlkupvm");
        return sAlkupvm;
    }
    public String getVahvpvm(){ return vahvpvmProperty().get(); }
    public void setVahvpvm(String vahvpvm){ vahvpvmProperty().set(vahvpvm); }
    public StringProperty vahvpvmProperty() {
        if (sVahvpvm == null) sVahvpvm = new SimpleStringProperty(this, "sVahvpvm");
        return sVahvpvm;
    }
    public String getLuovpvm(){ return luovpvmProperty().get(); }
    public void setLuovpvm(String luovpvm){ luovpvmProperty().set(luovpvm); }
    public StringProperty luovpvmProperty() {
        if (sLuovpvm == null) sLuovpvm = new SimpleStringProperty(this, "sLuovpvm");
        return sLuovpvm;
    }
    public String getPalvelu(){ return palveluProperty().get(); }
    public void setPalvelu(String palvelu){ palveluProperty().set(palvelu); }
    public StringProperty palveluProperty() {
        if (sPalvelu == null) sPalvelu = new SimpleStringProperty(this, "sPalvelu");
        return sPalvelu;
    }

}
