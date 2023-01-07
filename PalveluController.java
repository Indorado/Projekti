package Projekti;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Toni Tanskanen
 */
public class PalveluController implements Initializable {

    @FXML
    private TextField tfNimi1;
    @FXML
    private Button btnTallenna1;
    @FXML
    private TextField tfTyyppi;
    @FXML
    private TextField tfHinta1;
    @FXML
    private TextField tfAlv1;
    @FXML
    private Button btnMuokkaa1;
    @FXML
    private TextField tfAlue1;
    @FXML
    private TextArea taKuvaus1;
    @FXML
    private TableColumn<PalveluTaulukko, String> sar_nimi;
    @FXML
    private TableColumn<PalveluTaulukko, String> sar_ta;
    @FXML
    private TableColumn<PalveluTaulukko, String> sar_tyyppi;
    @FXML
    private TableColumn<PalveluTaulukko, String> sar_hinta;
    @FXML
    private TableView<PalveluTaulukko> table;

    ObservableList<PalveluTaulukko> lista = FXCollections.observableArrayList();

    int index = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Paivita();
        // TODO
    }

    @FXML
    private void Tallenna1(ActionEvent event) {
        try {
            Connection c = MySql.Connector.getConnection(); //aloita yhteys tietokantaan

            //luo PreparedStatement ja lisää
            PreparedStatement s = c.prepareStatement("insert into palvelu(toimintaalue_id, nimi, tyyppi, kuvaus, hinta, alv) values(?,?,?,?,?,?)");

            s.setString(1,tfAlue1.getText());
            s.setString(2,tfNimi1.getText());
            s.setString(3,tfTyyppi.getText());
            s.setString(4,taKuvaus1.getText());
            s.setString(5,tfHinta1.getText());
            s.setString(6,tfAlv1.getText());

            s.executeUpdate(); //executeUpdate() toteuttaa PreparedStatementin
            Paivita();
            JOptionPane.showMessageDialog(null, "Palvelu tallennettu");
            Tyhjenna();

            c.close(); //sulje yhteys
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    private void Valittu (MouseEvent event){
        // täyttää valitun palvelun tiedot tekstikenttiin
        index = table.getSelectionModel().getSelectedIndex();
        if (index <=-1){
            return;
        }

        tfNimi1.setText(lista.get(index).getNimi());
        tfAlue1.setText(lista.get(index).getToiminta_alueid());
        tfTyyppi.setText(lista.get(index).getTyyppi());
        tfHinta1.setText(lista.get(index).getHinta());
        tfAlv1.setText(lista.get(index).getAlv());
        taKuvaus1.setText(lista.get(index).getKuvaus());

    }
    @FXML
    private void Muokkaa (ActionEvent event) {
        // muokkaa valitun palvelun
        try {
            Connection c = MySql.Connector.getConnection(); // aloita yhteys tietokantaan
            // luo preparedstatementin ja lisää
            PreparedStatement s = c.prepareStatement("update palvelu set toimintaalue_id=?, nimi=?, tyyppi=?, kuvaus=?, hinta=?, alv=? where palvelu_id=?");
            s.setString(1, tfAlue1.getText());
            s.setString(2, tfNimi1.getText());
            s.setString(3, tfTyyppi.getText());
            s.setString(4, taKuvaus1.getText());
            s.setString(5, tfHinta1.getText());
            s.setString(6, tfAlv1.getText());
            s.setString(7, lista.get(index).getId());

            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Palvelu päivitetty");
            Tyhjenna();

            c.close(); //sulje yhteys

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void Paivita(){
        //päivittää taulukkoon palvelut tietokannasta
        lista.clear();
        try {
            Connection c = MySql.Connector.getConnection();

            ResultSet rs = c.createStatement().executeQuery("select * from palvelu p join toimintaalue ta on p.toimintaalue_id = ta.toimintaalue_id");

            while (rs.next()) {
                lista.add(new PalveluTaulukko(rs.getString("p.palvelu_id"),rs.getString("p.toimintaalue_id"),rs.getString("p.nimi"),rs.getString("p.tyyppi"),rs.getString("p.hinta")
                        ,rs.getString("p.alv"),rs.getString("p.kuvaus"),rs.getString("ta.nimi")));
            }
        }

        catch(Exception e) {
            System.out.println(e);
        }

        sar_nimi.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        sar_ta.setCellValueFactory(new PropertyValueFactory<>("toiminta_alue"));
        sar_tyyppi.setCellValueFactory(new PropertyValueFactory<>("tyyppi"));
        sar_hinta.setCellValueFactory(new PropertyValueFactory<>("hinta"));

        table.setItems(lista);
    }

    @FXML
    private void PoistaPalvelu(ActionEvent event) {
        // poistaa valitun palvelun
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("delete from palvelu where palvelu_id =?");
            s.setString(1, lista.get(index).getId());
            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Palvelu poistettu");
            Tyhjenna();

            c.close(); //sulje yhteys

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void Tyhjenna() {

        tfNimi1.setText("");
        tfAlue1.setText("");
        tfTyyppi.setText("");
        tfHinta1.setText("");
        tfAlv1.setText("");
        taKuvaus1.setText("");
    }


    public class PalveluTaulukko {
        String nimi,toiminta_alueid,tyyppi,hinta,id,kuvaus,alv,toiminta_alue;

        public PalveluTaulukko(String id, String toiminta_alueid, String nimi, String tyyppi, String hinta, String alv, String kuvaus, String toiminta_alue) {
            this.nimi = nimi;
            this.toiminta_alueid = toiminta_alueid;
            this.tyyppi = tyyppi;
            this.hinta = hinta;
            this.id = id;
            this.kuvaus = kuvaus;
            this.alv = alv;
            this.toiminta_alue = toiminta_alue;
        }

        public String getNimi() {return nimi;}

        public void setNimi(String nimi) {this.nimi = nimi;}

        public String getToiminta_alueid() {return toiminta_alueid;}

        public void setToiminta_alueid(String toiminta_alueid) {this.toiminta_alueid = toiminta_alueid;}

        public String getTyyppi() {return tyyppi;}

        public void setTyyppi(String tyyppi) {this.tyyppi = tyyppi;}

        public String getHinta() {return hinta;}

        public void setHinta(String hinta) {this.hinta = hinta;}

        public String getId() {return id;}

        public void setId(String id) {this.id = id;}

        public String getKuvaus() {return kuvaus;}

        public void setKuvaus(String kuvaus) {this.kuvaus = kuvaus;}

        public String getAlv() {return alv;}

        public void setAlv(String alv) {this.alv = alv;}

        public String getToiminta_alue() {return toiminta_alue;}

        public void setToiminta_alue(String toiminta_alue) {this.toiminta_alue = toiminta_alue;}
    }
}
