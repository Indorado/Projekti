package Projekti;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import MySql.Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class VarausController implements Initializable {
    //Table:
    @FXML public TableView<Varaus> tblVaraus = new TableView<>();
    //TableColumn:
    @FXML public TableColumn<Varaus,String> tcVarausID = new TableColumn<>("ID");
    @FXML public TableColumn<Varaus,String> tcAsiakas = new TableColumn<>("Asiakas");
    @FXML public TableColumn<Varaus,String> tcKohde = new TableColumn<>("Kohde");
    @FXML public TableColumn<Varaus,String> tcVarPvm = new TableColumn<>("Varaus");
    @FXML public TableColumn<Varaus,String> tcVahPvm = new TableColumn<>("Vahvistus");
    @FXML public TableColumn<Varaus,String> tcAlkuPvm = new TableColumn<>("Alku");
    @FXML public TableColumn<Varaus,String> tcLuovPvm = new TableColumn<>("Luovutus");
    @FXML public TableColumn<Varaus,String> tcPalvelut = new TableColumn<>("Palvelut");
    //DatePickers:
    @FXML public DatePicker dtpVarauspvm = new DatePicker();
    @FXML public DatePicker dtpAlkupvm = new DatePicker();
    @FXML public DatePicker dtpVahvistus = new DatePicker();
    @FXML public DatePicker dtpLuovutus = new DatePicker();
    //Comboboxit:
    @FXML public ComboBox<String> cbKohde = new ComboBox<>();
    @FXML public ComboBox<String> cbAsiakas = new ComboBox<>();
    //Tekstikentät:
    @FXML public TextField tfVaraus = new TextField();
    //Checkboxit:
    @FXML public CheckBox chbPalvelu1 = new CheckBox();
    @FXML public CheckBox chbPalvelu2 = new CheckBox();
    @FXML public CheckBox chbPalvelu3 = new CheckBox();
    @FXML public CheckBox chbPalvelu4 = new CheckBox();
    @FXML public CheckBox chbPalvelu5 = new CheckBox();
    //Buttons:
    @FXML public Button btnMuokkaa_varaus = new Button();
    @FXML public Button btnPoista_varaus = new Button();
    @FXML public Button btnLisaa_varaus = new Button();
    @FXML public Button btnPaivita = new Button();
    @FXML public Button btnTyhjenna = new Button();
    //Listat:
    public ObservableList<Varaus> varauslista = FXCollections.observableArrayList();
    public ObservableList<String> asiakaslista = FXCollections.observableArrayList();
    public ObservableList<String> kohdelista = FXCollections.observableArrayList();
    public ObservableList<String> palvelulista = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        muokkaaListat();
        //Alustetaan comboboksit, tableview ja checkboxit
        haeAsiakkaat();
        haeKohteet();
        haePalvelut();
        haeVaraukset();
        //Alustetaan DatePickerit
        muokkaaDTP(dtpVarauspvm);
        muokkaaDTP(dtpAlkupvm);
        muokkaaDTP(dtpVahvistus);
        muokkaaDTP(dtpLuovutus);
        muokkaaColumn();
        //Napit muokkaa, lisää ja poista otetaan pois käytöstä
        btnMuokkaa_varaus.setDisable(true);
        btnPoista_varaus.setDisable(true);
        btnLisaa_varaus.setDisable(true);
        //Asetetaan kuuntelija(t):
        tblVaraus.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount()>1) {
                valitutRivit();
                btnMuokkaa_varaus.setDisable(false);
                btnPoista_varaus.setDisable(false);
            }
        });

    }
    public void Lisaa(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistus");
        alert.setHeaderText("Haluatko varmasti lisätä tiedot?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Tarkasta päivämäärät
            tarkistaKentat();
            //Tallenna tietokantaan
            tallennaTK();
            //Lisää taulukkoon
            lisaaTaulukkoon();
            //Tyhjennä valinnat
            tyhjenna();
        }else{
            alert.close();
        }
        haeVaraukset();


    }
    public void valitutRivit(){
        try {
            if (tblVaraus.getSelectionModel().getSelectedItem() != null) {
                Varaus valitut = tblVaraus.getSelectionModel().getSelectedItem();
                tfVaraus.setText(valitut.getID());
                cbAsiakas.setValue(valitut.getAsiakas());
                cbKohde.setValue(valitut.getKohde());
                dtpVarauspvm.setValue(LocalDate.parse(valitut.getVarauspvm().substring(0, 10)));
                dtpVahvistus.setValue(LocalDate.parse(valitut.getVahvpvm().substring(0, 10)));
                dtpAlkupvm.setValue(LocalDate.parse(valitut.getAlkupvm().substring(0, 10)));
                dtpLuovutus.setValue(LocalDate.parse(valitut.getLuovpvm().substring(0, 10)));
                if (valitut.getPalvelu().contains("Porosafari")) {
                    chbPalvelu1.setSelected(true);
                }
                if (valitut.getPalvelu().contains("Koiravaljakko")) {
                    chbPalvelu2.setSelected(true);
                }
                if (valitut.getPalvelu().contains("Airsoft")) {
                    chbPalvelu3.setSelected(true);
                }
                if (valitut.getPalvelu().contains("Hevosajelu")) {
                    chbPalvelu4.setSelected(true);
                }
                if (valitut.getPalvelu().contains("Vesiskootteriajelu")) {
                    chbPalvelu5.setSelected(true);
                }

            }else{
                tyhjenna();
            }
        }catch (Exception e){
            ExceptionMessage(e);
        }
    }
    public void paivita(){
        muokkaaListat();
        haeAsiakkaat();
        haeKohteet();
        haePalvelut();
        tblVaraus.refresh();
        haeVaraukset();

    }
    public void poista(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistus");
        alert.setHeaderText("Haluatko varmasti poistaa valitut tiedot?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Poista valitut tiedot taulukosta ja tietokannasta
            if (tblVaraus.getSelectionModel().getSelectedItem() != null && tfVaraus != null){
                try {
                    Connection c = Connector.getConnection();
                    assert c != null;

                    PreparedStatement vp =
                            c.prepareStatement("DELETE FROM varauksen_palvelut WHERE palvelu_id=? AND varaus_id=?");
                    vp.setInt(1, Integer.parseInt(muutaPalveluID(c))); //Muutetaan palvelunimi id:ksi
                    vp.setInt(2, Integer.parseInt(tfVaraus.getText()));
                    vp.executeUpdate();


                    PreparedStatement p =
                            c.prepareStatement("DELETE FROM varaus WHERE varaus_id =?");
                    p.setInt(1, Integer.parseInt(tfVaraus.getText())); //Poistetaan varaus_id:n perusteella
                    p.executeUpdate();



                    c.close();
                    //Tyhjennä kentät
                    tyhjenna();
                    //Nollaa Tableview valinta
                    tblVaraus.getSelectionModel().select(-1);
                }catch(Exception e){
                    ExceptionMessage(e);
                }
            }
        }else{alert.close();}
        haeVaraukset();
    }
    public void muokkaa(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistus");
        alert.setHeaderText("Haluatko varmasti muokata valittuja tietoja?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Muokkaa valittuja tietoja taulukosta ja tietokannasta
            if (tblVaraus.getSelectionModel().getSelectedItem() != null && tfVaraus != null){
                try {
                    Connection c = Connector.getConnection();
                    assert c != null;
                    PreparedStatement var =
                            c.prepareStatement("update varaus set asiakas_id=?, mokki_mokki_id=?," +
                                    "varattu_pvm=? ,vahvistus_pvm=? ,varattu_alkupvm=? ,varattu_loppupvm=? where varaus_id=?");
                    var.setInt(1,muutaAsiakasID(cbAsiakas.getValue(),c)); //Muutetaan comboboksin nimi tallennettavaksi id:ksi
                    var.setInt(2,muutaKohdeID(cbKohde.getValue(),c)); //Muutetaan comboboksin nimi tallennettavaksi id:ksi
                    var.setString(3,dtpVarauspvm.getValue().toString());
                    var.setString(4,dtpVahvistus.getValue().toString());
                    var.setString(5,dtpAlkupvm.getValue().toString());
                    var.setString(6,dtpLuovutus.getValue().toString());
                    var.setInt(7,Integer.parseInt(tfVaraus.getText()));
                    var.executeUpdate();

                    PreparedStatement vp =
                            c.prepareStatement("update varauksen_palvelut set palvelu_id=?,lkm=? where varaus_id=?");
                    vp.setString(1,muutaPalveluID(c)); //Muutetaan palvelunimi id:ksi
                    vp.setString(2,"1");
                    vp.setInt(3,Integer.parseInt(tfVaraus.getText()));
                    vp.executeUpdate();
                    c.close();
                    //Tyhjennä kentät
                    tyhjenna();
                    //Nollaa Tableview valinta
                    tblVaraus.getSelectionModel().select(-1);
                }catch(Exception e){
                    ExceptionMessage(e);
                }
            }
        }else{
            alert.close();
        }
        haeVaraukset();
    }
    public void muokkaaListat(){ //Asetetaan listat elementteihin
        tblVaraus.setItems(null);
        varauslista.clear();
        tblVaraus.setItems(varauslista);
        cbKohde.setItems(null);
        cbKohde.setItems(kohdelista);
        cbAsiakas.setItems(null);
        cbAsiakas.setItems(asiakaslista);
        cbAsiakas.setVisibleRowCount(5);
    }
    public void muokkaaDTP(DatePicker dtp){ // Muokataan DatePickerit toimimaan ainoastaan nykyisestä päivästä eteenpäin
        dtp.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty); //super -> muuten solu voi mennä rikki
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
    }
    public void muokkaaColumn(){ //Muokataan kolumnit vastaamaan Varaus-luokan arvoja
        tcVarausID.setCellValueFactory(c -> c.getValue().varausIDProperty());
        tcAsiakas.setCellValueFactory(c -> c.getValue().asiakasProperty());
        tcKohde.setCellValueFactory(c -> c.getValue().kohdeProperty());
        tcVarPvm.setCellValueFactory(c -> c.getValue().varauspvmProperty());
        tcVahPvm.setCellValueFactory(c -> c.getValue().vahvpvmProperty());
        tcAlkuPvm.setCellValueFactory(c -> c.getValue().alkupvmProperty());
        tcLuovPvm.setCellValueFactory(c -> c.getValue().luovpvmProperty());
        tcPalvelut.setCellValueFactory(c -> c.getValue().palveluProperty());
    }
    public void tyhjenna(){ // Tyhjennetään kentät
        tfVaraus.setText(null);
        cbAsiakas.setValue(null);
        cbKohde.setValue(null);
        dtpVarauspvm.setValue(null);
        dtpAlkupvm.setValue(null);
        dtpVahvistus.setValue(null);
        dtpLuovutus.setValue(null);
        chbPalvelu1.setSelected(false);
        chbPalvelu2.setSelected(false);
        chbPalvelu3.setSelected(false);
        chbPalvelu4.setSelected(false);
        chbPalvelu5.setSelected(false);

    }
    public void haeVaraukset(){ //Haetaan varaukset Taulukkoon
        try {
            varauslista.clear();
            String query1 = "SELECT * FROM varaus";
            Connection c = Connector.getConnection();
            assert c != null;
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query1);
            while(rs.next()) {
                //Luodaan varaus-objekti
                Varaus v = new Varaus();
                //Haetaan muuttujat suoraan:
                v.setID(rs.getString("varaus_id"));
                v.setVarauspvm(rs.getString("varattu_pvm"));
                v.setVahvpvm(rs.getString("vahvistus_pvm"));
                v.setAlkupvm(rs.getString("varattu_alkupvm"));
                v.setLuovpvm(rs.getString("varattu_loppupvm"));
                //Haetaan loput muuttujat eri taulukosta oikeaan muotoon.
                PreparedStatement s2 = c.prepareStatement("SELECT * FROM asiakas WHERE asiakas_id =?");
                s2.setInt(1,Integer.parseInt(rs.getString("asiakas_id")));
                ResultSet rs2 = s2.executeQuery();
                while(rs2.next()) {
                    v.setAsiakas(rs2.getString("etunimi")+" "+rs2.getString("sukunimi"));
                }
                PreparedStatement s3 = c.prepareStatement("SELECT * FROM mokki WHERE mokki_id =?");
                s3.setInt(1,Integer.parseInt(rs.getString("mokki_mokki_id")));
                ResultSet rs3 = s3.executeQuery();
                while(rs3.next()) {
                    v.setKohde(rs3.getString("mokkinimi"));
                }
                PreparedStatement s4 = c.prepareStatement("SELECT p.nimi FROM palvelu p join varauksen_palvelut vp"
                        + " on vp.palvelu_id=p.palvelu_id"
                        +" join varaus v"
                        +" on v.varaus_id = vp.varaus_id");
                //s4.setInt(1,Integer.parseInt(rs.getString("varaus_id")));
                ResultSet rs4 = s4.executeQuery();
                while(rs4.next()) {
                    v.setPalvelu(rs4.getString("p.nimi"));
                }
                //Tarkistetaan onko listalla samaa objektia

                if(varauslista.contains(v)){
                    return;
                }else{
                    varauslista.add(v);
                }

            }
            c.close();
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
    }
    public void haeAsiakkaat(){ //Haetaan asiakkaat comboboksiin
        try {
            String query = "SELECT * FROM asiakas";
            Connection c = Connector.getConnection();
            assert c != null;
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                Asiakas a = new Asiakas();
                a.setEtunimi((rs.getString("etunimi")));
                a.setSukunimi((rs.getString("sukunimi")));
                a.setAsiakasID(rs.getString("asiakas_id"));
                if(asiakaslista.contains(a.getEtunimi()+" "+a.getSukunimi())){
                    return;
                }else{
                    asiakaslista.add(a.getEtunimi()+" "+a.getSukunimi());
                }
            }
            c.close();
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
    }
    public void haeKohteet(){ //Ladataan kohteet comboboksiin
        try {
            String query = "SELECT * FROM mokki";
            Connection c = Connector.getConnection();
            assert c != null;
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                Kohde k = new Kohde();
                k.setNimi(rs.getString("mokkinimi"));
                rs.getString("mokki_id");
                if(kohdelista.contains(k.getNimi())){
                    return;
                }else{
                    kohdelista.add(k.getNimi());
                }
            }
            c.close();
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
    }
    public void haePalvelut(){ //Haetaan palvelut checkboxeihin
        try {
            String query = "SELECT * FROM palvelu";
            Connection c = Connector.getConnection();
            assert c != null;
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()) {
                Palvelu p = new Palvelu();
                p.setNimi(rs.getString("nimi"));
                p.setId(rs.getString("palvelu_id"));
                palvelulista.add(p.getNimi());
            }
            if(palvelulista !=null && palvelulista.size() > 4){
                chbPalvelu1.setText(palvelulista.get(0));
                chbPalvelu2.setText(palvelulista.get(1));
                chbPalvelu3.setText(palvelulista.get(2));
                chbPalvelu4.setText(palvelulista.get(3));
                chbPalvelu5.setText(palvelulista.get(4));
            }
            c.close();
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
    }
    public String tarkastaPalvelut(){ //Tarkastetaan checkboxien tilat
        String palvelut = "";
        if(chbPalvelu1.isSelected()){
            palvelut += "Porosafari";
        }
        if(chbPalvelu2.isSelected()){
            palvelut += "Koiravaljakko";
        }
        if(chbPalvelu3.isSelected()){
            palvelut += "Airsoft";
        }
        if(chbPalvelu4.isSelected()){
            palvelut += "Hevosajelu";
        }
        if(chbPalvelu5.isSelected()){
            palvelut += "Vesiskootteriajelu";
        }
        //Palautetaan valitut palvelut
        return palvelut.replace(" ",", ");
    }
    public void tarkistaTaytto(){
        if(cbAsiakas != null && cbKohde != null && dtpVahvistus.getValue() != null
                && dtpAlkupvm.getValue() != null && dtpVarauspvm.getValue() != null
                && dtpLuovutus.getValue() != null){
            if(chbPalvelu1.isSelected() || chbPalvelu2.isSelected()
                    || chbPalvelu3.isSelected() || chbPalvelu4.isSelected()
                    || chbPalvelu5.isSelected()){
                btnLisaa_varaus.setDisable(false);
            }
        }
        else{
            btnLisaa_varaus.setDisable(true);
        }
    }

    public void lisaaTaulukkoon(){
        varauslista.add(new Varaus(cbKohde.getValue(),
                cbAsiakas.getValue(),
                dtpVarauspvm.getValue().toString(),
                dtpAlkupvm.getValue().toString(),
                dtpVahvistus.getValue().toString(),
                dtpLuovutus.getValue().toString(),
                tarkastaPalvelut()));
    }
    public void tallennaTK(){ //Tallennetaan varaus tietokantaan
        try {
            if(!tfVaraus.getText().equals("")){
                Connection c = Connector.getConnection();
                assert c != null;
                PreparedStatement var =
                        c.prepareStatement("insert into varaus(varaus_id,asiakas_id,mokki_mokki_id," +
                                "varattu_pvm,vahvistus_pvm,varattu_alkupvm,varattu_loppupvm) values(?,?,?,?,?,?,?)");
                var.setInt(1,Integer.parseInt(tfVaraus.getText()));
                var.setInt(2,muutaAsiakasID(cbAsiakas.getValue(),c)); //Muutetaan comboboksin nimi tallennettavaksi id:ksi
                var.setInt(3,muutaKohdeID(cbKohde.getValue(),c)); //Muutetaan comboboksin nimi tallennettavaksi id:ksi
                var.setString(4,dtpVarauspvm.getValue().toString());
                var.setString(5,dtpVahvistus.getValue().toString());
                var.setString(6,dtpAlkupvm.getValue().toString());
                var.setString(7,dtpLuovutus.getValue().toString());
                var.executeUpdate();

                PreparedStatement vp =
                        c.prepareStatement("insert into varauksen_palvelut(varaus_id,palvelu_id,lkm) values(?,?,?)");
                vp.setInt(1,Integer.parseInt(tfVaraus.getText()));
                vp.setString(2,muutaPalveluID(c)); //Muutetaan palvelunimi id:ksi
                vp.setString(3,"1");
                vp.executeUpdate();
                c.close();
            }
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
    }
    public int muutaAsiakasID(String nimi, Connection c){
        try {
            PreparedStatement s = c.prepareStatement("SELECT asiakas_id FROM asiakas WHERE asiakas.etunimi =? AND asiakas.sukunimi =?");
            s.setString(1,nimi.split(" ")[0].trim());
            s.setString(2,nimi.split(" ")[1].trim());
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                return rs.getInt("asiakas_id");
            }
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
        return 0;
    }
    public int muutaKohdeID(String mokkinimi, Connection c){
        try {
            PreparedStatement s = c.prepareStatement("SELECT mokki_id FROM mokki WHERE mokkinimi = ?");
            s.setString(1,mokkinimi);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                return rs.getInt("mokki_id");
            }
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
        return 0;
    }
    public String muutaPalveluID(Connection c){
        try {
            PreparedStatement s = c.prepareStatement("SELECT palvelu_id FROM palvelu WHERE nimi =?");
            s.setString(1,tarkastaPalvelut());
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                return rs.getString("palvelu_id");
            }
        }
        catch(Exception e) {
            ExceptionMessage(e);
        }
        return null;
    }
    public void tarkistaKentat() { //Tarkistetaan että kentissä on oikeat arvot ja merkit.
        LocalDate alku = dtpAlkupvm.getValue();
        LocalDate luov = dtpLuovutus.getValue();
        LocalDate vara = dtpVarauspvm.getValue();
        LocalDate vahv = dtpVahvistus.getValue();
        if(alku == null || luov == null || vara == null || vahv == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tyhjä päivämäärä");
            alert.setHeaderText(null);
            alert.setContentText("Päivämäärä puuttuu. Kaikki päivämäärät tulee olla täytetty.");
            alert.show();
            return;
        }
        if(alku.compareTo(luov)>0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Väärä päivämäärä järjestys");
            alert.setHeaderText(null);
            alert.setContentText("Varauksen alkupäivämäärä on luovutuspäivämäärän jälkeen.");
            alert.show();
            return;
        }
        if(vahv.compareTo(vara)<0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Väärä päivämäärä järjestys");
            alert.setHeaderText(null);
            alert.setContentText("Varauksen vahvistuspäivämäärä on asetettu varauspäivämäärää ennen.");
            alert.show();
            return;
        }
    }
    public void ExceptionMessage(Exception e){
        //Luodaan ikkuna jossa virhettä voi selata sen sijaan että se tulostettaisiin konsoliin.
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe!");
        alert.setHeaderText("Tapahtui virhe");

        // Luodaan suurennettava virhe ruutu.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Ilmoita virheestä ohjelmiston kehittäjälle!");
        //Luodaan tekstialue
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane gp = new GridPane();
        gp.setMaxWidth(Double.MAX_VALUE);
        gp.add(label, 0, 0);
        gp.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(gp);
        alert.showAndWait();
    }
}