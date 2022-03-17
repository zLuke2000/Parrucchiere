package it.centoreluca.controller.dialog;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class CDialog extends Controller {

    @FXML private AnchorPane ap_root;
    @FXML private Label l_d_title;
    @FXML private Label l_d_description;
    @FXML private FlowPane fp_buttons;
    @FXML private Button b_d_close;

    public void impostaTipo(Tipo t) {
        ap_root.getStyleClass().clear();
        switch(t) {
            case INFO -> ap_root.getStyleClass().add(String.valueOf(App.class.getResource("style/dialog/infoD.css")));
            case ATTENZIONE -> ap_root.getStyleClass().add(String.valueOf(App.class.getResource("style/dialog/attenzioneD.css")));
            case ERRORE -> ap_root.getStyleClass().add(String.valueOf(App.class.getResource("style/dialog/erroreD.css")));
        }
    }

    @FXML
    public void chiudi() {

    }

    public enum Tipo {
        INFO, ATTENZIONE, ERRORE
    }
}
