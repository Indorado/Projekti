package Projekti;
import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import MySql.Connector;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

public class AsiakasMenu implements Initializable {
    @FXML
    private Button btnTallenna1, btnMuokkaa1, btnPoista, btnTyhjennaFiltterit;
    @FXML
    private TextField filterSnimi;
    @FXML
    private TextField filterEnimi;
    @FXML
    private TextField tfAsiakasKatuosoite;
    @FXML
    private TextField tfAsiakasPostinro;
    @FXML
    private TextField tfAsiakasEmail;
    @FXML
    private TextField tfAsiakasPuhnro;
    @FXML
    private TextField tfAsiakasEtunimi;
    @FXML
    private TextField tfAsiakasSukunimi;
    @FXML
    private TextField tfAsiakasToimipaikka;
    @FXML
    private TableColumn<Asiakas, String> tab_enimi, tab_snimi, tab_sposti, tab_puhnro, tab_osoite, tab_postinro;
    @FXML
    private TableView<Asiakas> asiakas_table;
    ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList();
    FilteredList<Asiakas> filteredAsiakkaat = new FilteredList<>(asiakkaat);
    private int indeksi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Paivita();
    }
    public void Valittu(MouseEvent event) {
        indeksi = asiakas_table.getSelectionModel().getSelectedIndex();
        if (indeksi == -1) {
            Tyhjenna();

            return;
        }
        tfAsiakasEtunimi.setText(filteredAsiakkaat.get(indeksi).getEtunimi());
        tfAsiakasSukunimi.setText(filteredAsiakkaat.get(indeksi).getSukunimi());
        tfAsiakasEmail.setText(filteredAsiakkaat.get(indeksi).getEmail());
        tfAsiakasPuhnro.setText(filteredAsiakkaat.get(indeksi).getPuhnro());
        tfAsiakasKatuosoite.setText(filteredAsiakkaat.get(indeksi).getOsoite());
        tfAsiakasPostinro.setText(filteredAsiakkaat.get(indeksi).getPostinro());
        btnTallenna1.setDisable(true);
        btnMuokkaa1.setDisable(false);
    }
    public void Tyhjenna() {
        tfAsiakasEtunimi.setText("");
        tfAsiakasSukunimi.setText("");
        tfAsiakasEmail.setText("");
        tfAsiakasPuhnro.setText("");
        tfAsiakasKatuosoite.setText("");
        tfAsiakasPostinro.setText("");
        tfAsiakasToimipaikka.setText("");
        btnTallenna1.setDisable(false);
        btnMuokkaa1.setDisable(true);
    }
    public void TyhjennaFiltterit() {
        filterEnimi.setText("");
        filterSnimi.setText("");
        filteredAsiakkaat.setPredicate(s -> true);
    }
    public void Testaa() {
        String snimiFilter = filterSnimi.getText().toLowerCase();
        String enimiFilter = filterEnimi.getText().toLowerCase();
        if (snimiFilter.length() == 0 && enimiFilter.length() == 0) {
            filteredAsiakkaat.setPredicate(s -> true);
        } else if (enimiFilter.length() == 0) {
            filteredAsiakkaat.setPredicate(s -> s.getSukunimi().toLowerCase().contains(snimiFilter));
        } else if (snimiFilter.length() == 0) {
            filteredAsiakkaat.setPredicate(s -> s.getEtunimi().toLowerCase().contains(enimiFilter));
        } else {
            filteredAsiakkaat.setPredicate(s -> s.getSukunimi().toLowerCase().contains(snimiFilter) && s.getEtunimi().toLowerCase().contains(enimiFilter));
        }
    }
    public void Paivita() {
        asiakkaat.clear();
        try {
            Connection c = MySql.Connector.getConnection();

            ResultSet rs = c.createStatement().executeQuery("select * from asiakas");

            while (rs.next()) {
                asiakkaat.add(new Asiakas(rs.getInt("asiakas_id"), rs.getString("etunimi"),
                        rs.getString("sukunimi"),rs.getString("email"),
                        rs.getString("puhelinnro"),rs.getString("lahiosoite"),
                        rs.getString("postinro")));
            }

            c.close();
        } catch(Exception e){System.out.println(e);}
        tab_enimi.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        tab_snimi.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        tab_sposti.setCellValueFactory(new PropertyValueFactory<>("email"));
        tab_puhnro.setCellValueFactory(new PropertyValueFactory<>("puhnro"));
        tab_osoite.setCellValueFactory(new PropertyValueFactory<>("osoite"));
        tab_postinro.setCellValueFactory(new PropertyValueFactory<>("postinro"));



        asiakas_table.setItems(filteredAsiakkaat);

    } //WORKS
    public void Tallenna(ActionEvent actionEvent) throws Exception {
            try {
                Connection c = Connector.getConnection(); //aloita yhteys tietokantaan

                //luo PreparedStatement ja lisää
                PreparedStatement s = c.prepareStatement("insert into asiakas(postinro,etunimi,sukunimi,lahiosoite,email,puhelinnro) values(?,?,?,?,?,?)");
                String postinro = tfAsiakasPostinro.getText();
                String etunimi = tfAsiakasEtunimi.getText();
                String sukunimi = tfAsiakasSukunimi.getText();
                String katuosoite = tfAsiakasKatuosoite.getText();
                String email = tfAsiakasEmail.getText();
                String puhnro = tfAsiakasPuhnro.getText();
                //TÄÄ ON IHAN HIRVEE HYI ETTÄ
                if (!etunimi.equals("") && !sukunimi.equals("") && !email.equals("") && !postinro.equals("") && !katuosoite.equals("") && !puhnro.equals("")) {
                    s.setString(1, postinro);
                    s.setString(2,etunimi);
                    s.setString(3,sukunimi);
                    s.setString(4,katuosoite);
                    s.setString(5,email);
                    s.setString(6,puhnro);
                    //TARKASTA ONKO POSTINRO OLEMASSA; JOS EI OLE, LUO UUSI
                    PostinroCheck(tfAsiakasPostinro.getText(), c);
                    int exec = s.executeUpdate();
                }
                c.close(); //sulje yhteys
                Paivita();
                Tyhjenna();
            }
            catch(Exception e) {
                System.out.println(e);
            }
    }
    public void Poista(ActionEvent actionevent) throws Exception {
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("delete from asiakas where asiakas_id =?");
            s.setInt(1,filteredAsiakkaat.get(indeksi).getId());
            s.executeUpdate();
            Paivita();
            Tyhjenna();

            c.close(); //sulje yhteys

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void Muokkaa(ActionEvent actionEvent) throws Exception {
        Connection c = Connector.getConnection();
        PreparedStatement s = c.prepareStatement("update asiakas set postinro=?, etunimi=?, sukunimi=?, lahiosoite=?, email=?, puhelinnro=? where asiakas_id=?");
        String postinro = tfAsiakasPostinro.getText();
        String etunimi = tfAsiakasEtunimi.getText();
        String sukunimi = tfAsiakasSukunimi.getText();
        String katuosoite = tfAsiakasKatuosoite.getText();
        String email = tfAsiakasEmail.getText();
        String puhnro = tfAsiakasPuhnro.getText();
        //TÄÄ ON IHAN HIRVEE HYI ETTÄ
        if (!etunimi.equals("") && !sukunimi.equals("") && !email.equals("") && !postinro.equals("") && !katuosoite.equals("") && !puhnro.equals("")) {
            s.setString(1, postinro);
            s.setString(2,etunimi);
            s.setString(3,sukunimi);
            s.setString(4,katuosoite);
            s.setString(5,email);
            s.setString(6,puhnro);
            s.setInt(7,asiakkaat.get(indeksi).getId());
            //TARKASTA ONKO POSTINRO OLEMASSA; JOS EI OLE, LUO UUSI
            PostinroCheck(tfAsiakasPostinro.getText(), c);
            int exec = s.executeUpdate(); //executeUpdate() toteuttaa PreparedStatementin, palauttaa montako arvoa lisätty(?)
            Tyhjenna();
        }
        Paivita();

    }
    public void PostinroCheck(String postinro, Connection c) throws Exception {
       try {
           //hae tarkastettavaa postinumeroa taulusta "posti"
           PreparedStatement s = c.prepareStatement("select * from posti where postinro=?");
           s.setString(1,postinro);
           ResultSet rs = s.executeQuery();

           if (!rs.next()) { //mikäli tuloksia postinumerolla ei ole, luodaan uusi tietue
               PreparedStatement is = c.prepareStatement("insert into posti(postinro, toimipaikka) values(?,?)");
               is.setString(1,postinro);
               String tmipaikka = tfAsiakasToimipaikka.getText();
               is.setString(2,tmipaikka);
               int exec = is.executeUpdate();

               System.out.println("uusi tietue '" + postinro + ", " + tmipaikka + "' lisätty tauluun posti!");
           }

       } catch(Exception e) {
           System.out.println(e);
       }
    }
    public class Asiakas {
        int id;
        String etunimi, sukunimi, email, puhnro, osoite, postinro;
        public Asiakas(int id, String etunimi, String sukunimi, String email, String puhnro, String osoite, String postinro) {
            this.id = id;
            this.etunimi = etunimi;
            this.sukunimi = sukunimi;
            this.email = email;
            this.puhnro = puhnro;
            this.osoite = osoite;
            this.postinro = postinro;
        }
        public int getId() {return id;}
        public String getEtunimi() {return etunimi;}
        public String getSukunimi() {return sukunimi;}
        public String getEmail() {return email;}
        public String getPuhnro() {return puhnro;}
        public String getOsoite() {return osoite;}
        public String getPostinro() {return postinro;}

    }
}