package Projekti;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asiakas{
    private StringProperty id;
    private StringProperty etunimi;
    private StringProperty sukunimi;
    private StringProperty postinro;
    private StringProperty lahiosoite;
    private StringProperty email;
    private StringProperty puhelinnro;
    public Asiakas(){}
    public Asiakas(String id,String etunimi,String sukunimi, String postinro,String lahiosoite, String email, String puhelinnro){

    }

    public String getAsiakasID() { return asiakasIDProperty().get(); }
    public void setAsiakasID(String id){ asiakasIDProperty().set(id) ; }
    public StringProperty asiakasIDProperty() {
        if (id == null) id = new SimpleStringProperty(this, "id");
        return id;
    }
    public String getEtunimi() { return etunimiProperty().get(); }
    public void setEtunimi(String etunimi) { etunimiProperty().set(etunimi); }
    public StringProperty etunimiProperty() {
        if (etunimi == null) etunimi = new SimpleStringProperty(this, "etunimi");
        return etunimi;
    }
    public String getSukunimi() { return sukunimiProperty().get(); }
    public void setSukunimi(String sukunimi) { sukunimiProperty().set(sukunimi); }
    public StringProperty sukunimiProperty() {
        if (sukunimi == null) sukunimi = new SimpleStringProperty(this, "sukunimi");
        return sukunimi;
    }
    public String getPostinro() { return postinroProperty().get(); }
    public void setPostinro(String postinro) { postinroProperty().set(postinro); }
    public StringProperty postinroProperty() {
        if (postinro == null) postinro = new SimpleStringProperty(this, "postinro");
        return postinro;
    }
    public String getLahiosoite() { return lahiosoiteProperty().get(); }
    public void setLahiosoite(String lahiosoite) { lahiosoiteProperty().set(lahiosoite); }
    public StringProperty lahiosoiteProperty() {
        if (lahiosoite == null) lahiosoite = new SimpleStringProperty(this, "lahiosoite");
        return lahiosoite;
    }
    public String getEmail() { return emailProperty().get(); }
    public void setEmail(String email) { emailProperty().set(email); }
    public StringProperty emailProperty() {
        if (email == null) email = new SimpleStringProperty(this, "email");
        return email;
    }
    public String getPuhelinnro() { return puhelinnroProperty().get(); }
    public void setPuhelinnro(String puhelinnro) { puhelinnroProperty().set(puhelinnro); }
    public StringProperty puhelinnroProperty() {
        if (puhelinnro == null) puhelinnro = new SimpleStringProperty(this, "puhelinnro");
        return puhelinnro;
    }


}
