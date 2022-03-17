package it.centoreluca.controller.calendario.giornaliero;

import it.centoreluca.controller.Controller;
import it.centoreluca.models.Appuntamento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

public class CAppuntamentoDinamico extends Controller {
    @FXML private AnchorPane ap_root;
    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_orario;
    @FXML private Label l_note;
    @FXML private FontIcon fi_annulla;
    @FXML private FontIcon fi_reimposta;
    @FXML private FontIcon fi_completa;

    @FXML
    void statoAppuntamento(MouseEvent mouseEvent) {
    }

    public void impostaContenuto(CColonnaPersonale cColonnaPersonale, Appuntamento a) {
    }
}
