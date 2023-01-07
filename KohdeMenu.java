package Projekti;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ResourceBundle;

import MySql.Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class KohdeMenu implements Initializable {
    public Button btnkohde_poista;
    public Button btnkohde_tyhjenna;
    public Button btnkohde_Tallenna;
    public Button btnkohde_Muokkaa;

    @FXML
    private TableColumn<KohdeTaulukko, String> tab_10;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_8;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_7;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_2;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_5;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_9;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_1;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_6;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_4;
    @FXML
    private TableColumn<KohdeTaulukko, String> tab_3;
    @FXML
    private TextField tfnimi;
    @FXML
    private TextField tfhinta;
    @FXML
    private TextField tfalv;
    @FXML
    private TextField tfkohdeid;
    @FXML
    private TextField tfalueid;
    @FXML
    private TextField tfkuvaus;
    @FXML
    private TextField tfpostinro;
    @FXML
    private TextField tfosoite;
    @FXML
    private TextField tfvarustelu;
    @FXML
    private TextField tfhlo;
    @FXML
    private TableView tblKohde;


    ObservableList<KohdeTaulukko> kohdelista = FXCollections.observableArrayList();

    int index = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Paivita();
        Tyhjenna();
    }
    @FXML
    private void TallennaKohde(ActionEvent event) {
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("insert into mokki(mokki_id,toimintaalue_id, mokkinimi, katuosoite, postinro, henkilomaara, hinta, alv,varustelu, kuvaus) values(?,?,?,?,?,?,?,?,?,?)");

            if (!tfkohdeid.equals("") && !tfalueid.equals("") && !tfnimi.equals("") && !tfosoite.equals("") && !tfpostinro.equals("") && !tfhlo.equals("") && !tfhinta.equals("") && !tfalv.equals("") && !tfvarustelu.equals("") && !tfkuvaus.equals("")) {

                s.setString(1,tfkohdeid.getText());
                s.setString(2,tfalueid.getText());
                s.setString(3,tfnimi.getText());
                s.setString(4,tfosoite.getText());
                s.setString(5,tfpostinro.getText());
                s.setString(6,tfhlo.getText());
                s.setString(7,tfhinta.getText());
                s.setString(8,tfalv.getText());
                s.setString(9,tfvarustelu.getText());
                s.setString(10,tfkuvaus.getText());
                PostinroCheck(tfpostinro.getText(), c);
                s.executeUpdate();
                Paivita();
                JOptionPane.showMessageDialog(null, "Kohde tallennettu");
                Tyhjenna();

            } c.close();
        }
        catch(Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Täytä kaikki tiedot ennen tallentamista");
        }

        Paivita();
    }


    @FXML
    private void Valittu (MouseEvent event){
        index = tblKohde.getSelectionModel().getSelectedIndex();
        if (index <=-1){
            return;
        }
        tfvarustelu.setText(kohdelista.get(index).getVarustelu());
        tfkohdeid.setText(kohdelista.get(index).getMokki_id());
        tfalv.setText(kohdelista.get(index).getAlv());
        tfalueid.setText(kohdelista.get(index).getToimintaalue_id());
        tfkuvaus.setText(kohdelista.get(index).getKuvaus());
        tfpostinro.setText(kohdelista.get(index).getPostinro());
        tfosoite.setText(kohdelista.get(index).getKatuosoite());
        tfhlo.setText(kohdelista.get(index).getHenkilomaara());
        tfnimi.setText(kohdelista.get(index).getMokkinimi());
        tfhinta.setText(kohdelista.get(index).getHinta());
        btnkohde_Tallenna.setDisable(true);
        btnkohde_Muokkaa.setDisable(false);
    }
    @FXML
    private void MuokkaaKohde (ActionEvent event) {
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("update mokki set toimintaalue_id=?,postinro=?, mokkinimi=?,katuosoite=?, kuvaus=?, henkilomaara=?, varustelu=?, hinta=?, alv=? where mokki_id=?");

            s.setString(1,tfalueid.getText());
            s.setString(2,tfpostinro.getText());
            s.setString(3,tfnimi.getText());
            s.setString(4,tfosoite.getText());
            s.setString(5,tfkuvaus.getText());
            s.setString(6,tfhlo.getText());
            s.setString(7,tfvarustelu.getText());
            s.setString(8,tfhinta.getText());
            s.setString(9,tfalv.getText());
            s.setString(10, tfkohdeid.getText());

            PostinroCheck(tfpostinro.getText(), c);
            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Kohteet päivitetty");
            Tyhjenna();

            c.close();
        }
        catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Täytä kaikki tiedot ennen tallentamista");
        }
        Paivita();
    }

    private void Paivita(){
        kohdelista.clear();
        try {
            Connection c = MySql.Connector.getConnection();

            ResultSet rs = c.createStatement().executeQuery("select * from mokki");

            while (rs.next()) {
                kohdelista.add((new KohdeTaulukko(rs.getString("mokki_id"),rs.getString("toimintaalue_id"),
                        rs.getString("postinro"),rs.getString("mokkinimi"),
                        rs.getString("katuosoite"),rs.getString("kuvaus"),
                        rs.getString("henkilomaara"),rs.getString("varustelu"),
                        rs.getString("hinta"),rs.getString("alv"))));

            }
        }

        catch(Exception e) {
            System.out.println(e);
        }
        tab_1.setCellValueFactory(new  PropertyValueFactory<>("mokki_id"));
        tab_2.setCellValueFactory(new  PropertyValueFactory<>("toimintaalue_id"));
        tab_3.setCellValueFactory(new PropertyValueFactory<>("mokkinimi"));
        tab_4.setCellValueFactory(new PropertyValueFactory<>("katuosoite"));
        tab_5.setCellValueFactory(new PropertyValueFactory<>("postinro"));
        tab_6.setCellValueFactory(new PropertyValueFactory<>("henkilomaara"));
        tab_7.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));
        tab_8.setCellValueFactory(new PropertyValueFactory<>("varustelu"));
        tab_9.setCellValueFactory(new PropertyValueFactory<>("hinta"));
        tab_10.setCellValueFactory(new PropertyValueFactory<>("alv"));

        tblKohde.setItems(kohdelista);
    }

    @FXML
    private void PoistaKohde(ActionEvent event) {
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("delete from mokki where mokki_id =?");
            s.setString(1,kohdelista.get(index).getMokki_id());
            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Kohde poistettu");
            Tyhjenna();

            c.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        Paivita();

    }

    @FXML
    private void Tyhjenna(ActionEvent event) {
        Tyhjenna();
    }

    private void Tyhjenna() {

        tfkohdeid.setText("");
        tfosoite.setText("");
        tfhlo.setText("");
        tfalueid.setText("");
        tfpostinro.setText("");
        tfvarustelu.setText("");
        tfkuvaus.setText("");
        tfhinta.setText("");
        tfnimi.setText("");
        tfalv.setText("");
        btnkohde_Tallenna.setDisable(false);
        btnkohde_Muokkaa.setDisable(true);
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
                String tmipaikka = tfpostinro.getText();
                is.setString(2,tmipaikka);
                int exec = is.executeUpdate();

                System.out.println("uusi tietue '" + postinro + ", " + tmipaikka + "' lisätty tauluun posti!");
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public class KohdeTaulukko {
        String mokkinimi,katuosoite,henkilomaara,hinta,kuvaus,postinro,varustelu,mokki_id, alv, toimintaalue_id;
        public KohdeTaulukko(String mokki_id, String toimintaalue_id, String postinro,
                             String mokkinimi, String katuosoite, String kuvaus,
                             String henkilomaara, String varustelu, String hinta, String alv ) {
            this.mokkinimi = mokkinimi;
            this.katuosoite = katuosoite;
            this.henkilomaara = henkilomaara;
            this.hinta = hinta;
            this.kuvaus = kuvaus;
            this.postinro = postinro;
            this.varustelu = varustelu;
            this.mokki_id = mokki_id;
            this.alv = alv;
            this.toimintaalue_id = toimintaalue_id;
        }
        public String getToimintaalue_id() {
            return toimintaalue_id;
        }

        public void setToimintaalue_id(String toimintaalue_id) {
            this.toimintaalue_id = toimintaalue_id;
        }

        public String getAlv() {
            return alv;
        }

        public void setAlv(String alv) {
            this.alv = alv;
        }

        public String getHenkilomaara() {
            return henkilomaara;
        }

        public void setHenkilomaara(String henkilomaara) {
            this.henkilomaara = henkilomaara;
        }

        public String getMokki_id() {
            return mokki_id;
        }

        public void setMokki_id(String mokki_id) {
            this.mokki_id = mokki_id;
        }

        public String getMokkinimi() {
            return mokkinimi;
        }

        public void setMokkinimi(String mokkinimi) {
            this.mokkinimi = mokkinimi;
        }

        public String getKatuosoite() {
            return katuosoite;
        }

        public void setKatuosoite(String katuosoite) {
            this.katuosoite = katuosoite;
        }

        public String getHinta() {
            return hinta;
        }

        public void setHinta(String hinta) {
            this.hinta = hinta;
        }

        public String getKuvaus() {
            return kuvaus;
        }

        public void setKuvaus(String kuvaus) {
            this.kuvaus = kuvaus;
        }

        public String getPostinro() {
            return postinro;
        }

        public void setPostinro(String postinro) {
            this.postinro = postinro;
        }

        public String getVarustelu() {
            return varustelu;
        }

        public void setVarustelu(String varustelu) {
            this.varustelu = varustelu;
        }
    }
}

