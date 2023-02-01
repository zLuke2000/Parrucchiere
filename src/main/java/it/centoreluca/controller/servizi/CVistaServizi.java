package it.centoreluca.controller.servizi;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Result;
import it.centoreluca.models.Servizio;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.CssHelper;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;

public class CVistaServizi extends Controller {

    @FXML protected VBox vb_container;
    @FXML private TextField tf_nome;
    @FXML private TextField tf_ore;
    @FXML private TextField tf_minuti;
    @FXML private TextArea ta_note;
    @FXML private TextField tf_ricerca;
    @FXML private Label l_registrazioneOk;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    public void conferma() {
        boolean check = true;
        if(cp.testoSempliceSenzaNumeri(tf_nome, 2, 50) & cp.numeri(tf_ore, 0, 1) & cp.numeri(tf_minuti, 0, 2)) {
            Servizio s = new Servizio(-1, tf_nome.getText().trim().toLowerCase(Locale.ROOT), 0, null);

            /* Controllo durata (ore) */
            if(tf_ore.getText().trim().length() > 0) {
                int ore = Integer.parseInt(tf_ore.getText().trim());
                if(ore <= 8) {
                    s.setDurata(s.getDurata() + ore*60);
                } else {
                    css.toError(tf_ore,"Massimo 8 ore");
                    check = false;
                }
            }
            /* Controllo durata (minuti) */
            if(tf_minuti.getText().trim().length() > 0) {
                int minuti = Integer.parseInt(tf_minuti.getText().trim());
                if(minuti <= 59) {
                    s.setDurata(s.getDurata() + minuti);
                } else {
                    css.toError(tf_minuti,"Massimo 59 minuti");
                    check = false;
                }
            }
            /* Controllo della nota, se corretta viene registrata */
            if(ta_note.getText().trim().length() > 0) {
                if (cp.testoSempliceConNumeri(ta_note, 0, 512)) {
                    s.setNote(ta_note.getText().trim());
                } else {
                    check = false;
                }
            }

            if(check) {
                if (db.registraServizio(s).getResult()) {
                    l_registrazioneOk.setText("Servizio registrato");
                    css.toValid(l_registrazioneOk);
                    l_registrazioneOk.setVisible(true);
                    reimpostaCampi(true, true, tf_nome, tf_ore, tf_minuti, ta_note);
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
    public void realtimeCheck(KeyEvent ke) {
        if(ke.getSource().equals(tf_ricerca)) {
            if(tf_ricerca.getText().trim().length() >= 1) {
                // Rimuovo tutti i servizi nel VBOX
                vb_container.getChildren().clear();
                // Carico i servizi nel VBOX che contengono almeno UNA lettera tra quelle indicate
                Result rs = db.leggiServizi(tf_ricerca.getText().trim());
                // Animazione
                SequentialTransition st = new SequentialTransition();
                for (Servizio s: rs.getList(Servizio.class)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/servizi/Servizio.fxml"));
                    try {
                        AnchorPane ap = fxmlLoader.load();
                        vb_container.getChildren().add(ap);
                        FadeTransition ft = new FadeTransition(Duration.millis(500), ap);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        st.getChildren().add(ft);
                        CServizio servizioController = fxmlLoader.getController();
                        servizioController.impostaContenuto(this, s, ap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                st.play();
            } else {
                for(Node n: vb_container.getChildren()) {
                    rimuoviNodo(vb_container, n);
                }
            }
        } else {
            reimpostaCampi(false, true, tf_nome, tf_ore, tf_minuti, ta_note);
            l_registrazioneOk.setVisible(false);
        }
    }

}
