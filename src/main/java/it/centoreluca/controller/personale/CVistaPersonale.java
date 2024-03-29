package it.centoreluca.controller.personale;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Personale;
import it.centoreluca.models.Result;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.CssHelper;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;

public class CVistaPersonale extends Controller {

    @FXML protected VBox vb_container;
    @FXML private TextField tf_nome;
    @FXML private TextField tf_cognome;
    @FXML private TextField tf_username;
    @FXML private TextArea ta_note;
    @FXML private Label l_registrazioneOk;
    @FXML private TextField tf_ricerca;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    @FXML
    private void initialize() {
        caricaFragment();
    }

    private void caricaFragment() {
        vb_container.getChildren().clear();
        Result rs = db.leggiPersonale();
        // Animazione
        SequentialTransition st = new SequentialTransition();
        for (Personale p : rs.getList(Personale.class)) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/personale/Personale.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                vb_container.getChildren().add(ap);
                FadeTransition ft = new FadeTransition(Duration.millis(500), ap);
                ft.setFromValue(0);
                ft.setToValue(1);
                st.getChildren().add(ft);
                CPersonale personaleController = fxmlLoader.getController();
                personaleController.impostaContenuto(this, p, ap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        st.play();
    }

    @FXML
    public void conferma() {
        boolean check = true;
        if(cp.testoSempliceSenzaNumeri(tf_nome, 2, 32) & cp.testoSempliceSenzaNumeri(tf_cognome, 2, 32) & cp.testoSempliceSenzaNumeri(tf_username, 2, 16)) {
            Personale d = new Personale(-1, tf_nome.getText().trim().toLowerCase(Locale.ROOT), tf_cognome.getText().trim().toLowerCase(Locale.ROOT), tf_username.getText().trim().toLowerCase(Locale.ROOT), null);

            /* Controllo della nota, se corretta viene registrata */
            if(ta_note.getText().trim().length() > 0) {
                if (cp.testoSempliceConNumeri(ta_note, 0, 255)) {
                    d.setNote(ta_note.getText().trim());
                } else {
                    check = false;
                }
            }

            if(check) {
                if (db.registraPersonale(d).getResult()) {
                    l_registrazioneOk.setText("Personale registrato");
                    css.toValid(l_registrazioneOk);
                    l_registrazioneOk.setVisible(true);
                    caricaFragment();
                    reimpostaCampi(true, true, tf_nome, tf_cognome, tf_username, ta_note);
                } else {
                    l_registrazioneOk.setText("Errore registrazione");
                    css.toError(l_registrazioneOk, null);
                    l_registrazioneOk.setVisible(true);
                }
            } else {
                l_registrazioneOk.setText("Errore compilazione");
                css.toError(l_registrazioneOk, null);
                l_registrazioneOk.setVisible(true);
            }
        }
    }

    @FXML
    public void realtimeCheck() {
        reimpostaCampi(false, true, tf_nome, tf_cognome, tf_username, ta_note);
        l_registrazioneOk.setVisible(false);
    }

}
