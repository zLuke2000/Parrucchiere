package it.centoreluca.controller.dialog;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CDialog extends Controller {

    @FXML private AnchorPane ap_root;
    @FXML private Label l_d_title;
    @FXML private Label l_d_description;
    @FXML private FlowPane fp_buttons;
    @FXML private Button b_d_close;

    private Stage stage;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void impostaTipo(Tipo t) {
        switch(t) {
            case INFO -> ap_root.getStylesheets().add(String.valueOf(App.class.getResource("style/dialog/infoD.css")));
            case ATTENZIONE -> ap_root.getStylesheets().add(String.valueOf(App.class.getResource("style/dialog/attenzioneD.css")));
            case ERRORE -> ap_root.getStylesheets().add(String.valueOf(App.class.getResource("style/dialog/erroreD.css")));
        }
    }

    public void impostaTitolo(String titolo) {
        l_d_title.setText(titolo);
    }

    public void impostaDescrizione(String desc) {
        l_d_description.setText(desc);
    }

    public void rinominaPulsanteDefault(String testo) {
        b_d_close.setText(testo);
    }

    public void aggiungiPulsante(Button b) {
        fp_buttons.getChildren().add(b);
    }

    @FXML
    public void chiudi() {
        stage.close();
    }

    public enum Tipo {
        INFO, ATTENZIONE, ERRORE
    }
}
