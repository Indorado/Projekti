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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class Alueet implements Initializable {

    @FXML
    private TableColumn<AlueTaulukko, String> sar_id;
    @FXML
    private Button btnPoista;
    @FXML
    private Button btnPaivita;
    @FXML
    private TableColumn<AlueTaulukko, String> sar_nimi;
    @FXML
    private TextField tfAlue1;
    @FXML
    private TableColumn<AlueTaulukko, String> sar_alueid;
    @FXML
    private Button btnTyhjenna;
    @FXML
    private TextField tfNimi1;
    @FXML
    private Button btnTallenna1;
    @FXML
    private Button btnMuokkaa1;
    @FXML
    private TextField tfID1;
    @FXML
    private TableView<AlueTaulukko> table;

    ObservableList<AlueTaulukko> lista = FXCollections.observableArrayList();
    int index = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Paivita();
        Tyhjenna();
        // TODO
    }

    @FXML
    private void Tallenna1(ActionEvent event) {
        // Tallennetaan uusi toiminta-alue ja päivitetään listaus:
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("insert into toimintaalue(toimintaalue_id, nimi) values(?,?)");
            if (!tfID1.getText().trim().isEmpty()) {
                int id1 = Integer.parseInt(tfAlue1.getText());
                s.setInt(1, id1);
                s.setString(2, tfID1.getText());
            }
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Toiminta-alue tallennettu");
            Tyhjenna();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Täytä kaikki tiedot ennen tallentamista");
        }
        Paivita();
    }

    @FXML
    private void Valittu(MouseEvent event) {
        // Valitaan toiminta-alue:
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        tfID1.setText(lista.get(index).getNimi());
        tfAlue1.setText(lista.get(index).getToimintaalue_id());
        btnTallenna1.setDisable(true);
        btnMuokkaa1.setDisable(false);
    }

    @FXML
    private void Muokkaa(ActionEvent event) {
        // Muokataan toiminta-aluetta ja päivitetään listaus:
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("update toimintaalue set nimi=? where toimintaalue_id=?");
            s.setString(1, tfID1.getText());
            s.setString(2, tfAlue1.getText());
            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Toiminta-alue päivitetty");
            Tyhjenna();

            c.close(); //sulje yhteys

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Täytä kaikki tiedot ennen tallentamista");
        }
        Paivita();
    }

    private void Paivita() {
        lista.clear();
        try {
            Connection c = MySql.Connector.getConnection();
            ResultSet rs = c.createStatement().executeQuery("select * from toimintaalue");
            while (rs.next()) {
                lista.add(new Alueet.AlueTaulukko(rs.getString("toimintaalue_id"), rs.getString("nimi")));
            }
            c.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        sar_id.setCellValueFactory(new PropertyValueFactory<>("toimintaalue_id"));
        sar_nimi.setCellValueFactory(new PropertyValueFactory<>("nimi"));

        table.setItems(lista);
    }

    @FXML
    private void Paivita(ActionEvent event) {
        Paivita();
    }

    @FXML
    private void Poista(ActionEvent event) {
        try {
            Connection c = MySql.Connector.getConnection();
            PreparedStatement s = c.prepareStatement("delete from toimintaalue where toimintaalue_id =?");
            s.setString(1, tfAlue1.getText());
            s.executeUpdate();
            Paivita();
            JOptionPane.showMessageDialog(null, "Toiminta-alue poistettu");
            Tyhjenna();
            c.close(); //sulje yhteys
        } catch (Exception e) {
            System.out.println(e);
        }
        Paivita();
    }

    @FXML
    private void Tyhjenna(ActionEvent event) {
        Tyhjenna();
    }

    @FXML
    private void Tyhjenna() {
        tfAlue1.setText("");
        tfID1.setText(null);
        btnTallenna1.setDisable(false);
        btnMuokkaa1.setDisable(true);
    }

    public class AlueTaulukko {

        String toimintaalue_id;
        String nimi;
        public AlueTaulukko(String toimintaalue_id, String nimi) {
            this.nimi = nimi;
            this.toimintaalue_id = toimintaalue_id;
        }

        public String getToimintaalue_id() {
            return toimintaalue_id;
        }

        public void setToimintaalue_id(String toimintaalue_id) {
            this.toimintaalue_id = toimintaalue_id;
        }

        public String getNimi() {
            return nimi;
        }
        public void setNimi(String nimi) {
            this.nimi = nimi;
        }
    }
}
