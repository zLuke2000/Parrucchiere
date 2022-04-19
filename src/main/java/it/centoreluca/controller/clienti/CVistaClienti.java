package it.centoreluca.controller.clienti;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Result;
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
import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

public class CVistaClienti extends Controller {

    @FXML private TextField tf_ricerca;
    @FXML protected VBox vb_container;
    @FXML private TextField tf_nome;
    @FXML private TextField tf_cognome;
    @FXML private TextField tf_dataG;
    @FXML private TextField tf_dataM;
    @FXML private TextField tf_dataA;
    @FXML private TextField tf_email;
    @FXML private TextField tf_telefonoFisso;
    @FXML private TextField tf_telefonoCellulare;
    @FXML private TextArea ta_note;
    @FXML private Label l_registrazioneOk;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    @FXML
    public void conferma() {
        boolean check = true;
        if(cp.testoSempliceSenzaNumeri(tf_nome, 2, 50) & cp.testoSempliceSenzaNumeri(tf_cognome, 2, 50)) {
            Cliente c = new Cliente(tf_nome.getText().trim().toLowerCase(Locale.ROOT), tf_cognome.getText().trim().toLowerCase(Locale.ROOT));

            /* Controllo della data di nascita, se corretta viene registrata */
            if(tf_dataG.getText().trim().length() > 0 || tf_dataM.getText().trim().length() > 0 || tf_dataA.getText().trim().length() > 0) {
                Calendar data = cp.data(tf_dataG, tf_dataM, tf_dataA);
                if (data != null) {
                    c.setDataNascita(new Date(data.getTimeInMillis()));
                } else {
                    check = false;
                }
            }
            /* Controllo del numero cellulare, se corretto viene registrato */
            if(tf_telefonoCellulare.getText().trim().length() > 0) {
                if (cp.numeri(tf_telefonoCellulare, 10, 16)) {
                    c.setNumeroCellulare(tf_telefonoCellulare.getText().trim());
                } else {
                    check = false;
                }
            }
            /* Controllo del numero fisso, se corretto viene registrato */
            if(tf_telefonoFisso.getText().trim().length() > 0) {
                if (cp.numeri(tf_telefonoFisso, 10, 16)) {
                    c.setNumeroFisso(tf_telefonoFisso.getText().trim());
                } else {
                    check = false;
                }
            }
            /* Controllo dell'email, se corretta viene registrata */
            if(tf_email.getText().trim().length() > 0) {
                if (cp.email(tf_email)) {
                    c.setEmail(tf_email.getText().trim());
                } else {
                    check = false;
                }
            }
            /* Controllo della nota, se corretta viene registrata */
            if(ta_note.getText().trim().length() > 0) {
                if (cp.testoSempliceConNumeri(ta_note, 0, 512)) {
                    c.setNote(ta_note.getText().trim());
                } else {
                    check = false;
                }
            }

            if(check) {
                Result res = db.registraCliente(c);
                if (res.getResult()) {
                    l_registrazioneOk.setText("Cliente registrato con successo");
                    css.toValid(l_registrazioneOk);
                    l_registrazioneOk.setVisible(true);
                    reimpostaCampi(true, true, tf_nome, tf_cognome, tf_dataG, tf_dataM, tf_dataA, tf_telefonoCellulare, tf_telefonoFisso, tf_email, ta_note);
                } else {
                    l_registrazioneOk.setText("Errore durante la registrazione");
                    css.toError(l_registrazioneOk, null);
                    l_registrazioneOk.setVisible(true);
                }
            } else {
                l_registrazioneOk.setText("Errore compilazione campi");
                css.toError(l_registrazioneOk, null);
                l_registrazioneOk.setVisible(true);
            }
        }
    }

    @FXML
    public void realtimeCheck(KeyEvent ke) {
        if(ke.getSource().equals(tf_ricerca)) {
            if(tf_ricerca.getText().trim().length() >= 2) {
                // Rimuovo tutti i clienti nel VBOX
                vb_container.getChildren().clear();
                // Carico i clienti nel VBOX che contengono almeno DUE lettere tra quelle indicate
                Result rs = db.leggiClienti(tf_ricerca.getText().trim());
                // Animazione
                SequentialTransition st = new SequentialTransition();
                for (Cliente a: rs.getList(Cliente.class)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/clienti/Cliente.fxml"));
                    try {
                        AnchorPane ap = fxmlLoader.load();
                        vb_container.getChildren().add(ap);
                        FadeTransition ft = new FadeTransition(Duration.millis(500), ap);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        st.getChildren().add(ft);
                        CCliente clienteController = fxmlLoader.getController();
                        clienteController.impostaContenuto(this, a, ap);
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
            reimpostaCampi(false, true, tf_nome, tf_cognome, tf_dataG, tf_dataM, tf_dataA, tf_telefonoCellulare, tf_telefonoFisso, tf_email, ta_note);
            l_registrazioneOk.setVisible(false);
        }
    }



}
