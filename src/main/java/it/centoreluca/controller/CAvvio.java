package it.centoreluca.controller;

import it.centoreluca.App;
import it.centoreluca.thread.Orario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CAvvio extends Controller {

    @FXML private AnchorPane p_container;
    @FXML private FontIcon fi_schermoInteroOn;
    @FXML private FontIcon fi_schermoInteroOff;
    @FXML private Button b_menuCalendario;
    @FXML private Button b_menuClienti;
    @FXML private Button b_menuGenerico1;
    @FXML private Button b_menuGenerico2;
    @FXML private Button b_menuGenerico3;
    @FXML private Button b_menuPersonale;
    @FXML private Label l_orologio;

    private final Stage stage = App.stage;
    private final Double defaultWidth = stage.getWidth();
    private final Double defaultHeight = stage.getHeight();
    private final Orario ora = Orario.getInstance();

    @FXML
    private void initialize() {
        loadDashboard("Calendario");
        ora.impostaEtichetta(l_orologio);
    }

    @FXML
    private void schermoInteroApp() {
        if(fi_schermoInteroOn.isVisible()) {
            stage.setMaximized(true);
            fi_schermoInteroOn.setVisible(false);
            fi_schermoInteroOff.setVisible(true);
        } else {
            stage.setMaximized(false);
            stage.setWidth(defaultWidth);
            stage.setHeight(defaultHeight);
            fi_schermoInteroOn.setVisible(true);
            fi_schermoInteroOff.setVisible(false);
        }
    }

    @FXML
    public void selettoreMenu(ActionEvent ae) {
        if(ae.getSource().equals(b_menuCalendario)) {
            loadDashboard("Calendario");
        } else if(ae.getSource().equals(b_menuClienti)) {
            loadDashboard("VistaClienti");
        } else if(ae.getSource().equals(b_menuGenerico1)) {
            loadDashboard("Menu1");
        } else if(ae.getSource().equals(b_menuGenerico2)) {
            loadDashboard("Menu2");
        } else if(ae.getSource().equals(b_menuGenerico3)) {
            loadDashboard("Menu3");
        } else if(ae.getSource().equals(b_menuPersonale)) {
            loadDashboard("VistaPersonale");
        }
    }

    private void loadDashboard(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/" + fxml +".fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            p_container.getChildren().clear();
            p_container.getChildren().add(ap);
            AnchorPane.setTopAnchor(ap, 0.0);
            AnchorPane.setRightAnchor(ap, 0.0);
            AnchorPane.setBottomAnchor(ap, 0.0);
            AnchorPane.setLeftAnchor(ap, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void riduciApp() {
        stage.setIconified(true);
    }

    @FXML
    private void chiudiApp() {
        stage.close();
        Platform.exit();
    }

}
