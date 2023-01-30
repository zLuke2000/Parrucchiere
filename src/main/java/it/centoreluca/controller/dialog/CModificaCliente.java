package it.centoreluca.controller.dialog;

import it.centoreluca.controller.Controller;
import it.centoreluca.controller.clienti.CCliente;
import it.centoreluca.database.Database;
import it.centoreluca.models.Cliente;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Calendar;

public class CModificaCliente extends Controller {

    @FXML private Label l_titolo;
    @FXML private TextField tf_dataG;
    @FXML private TextField tf_dataM;
    @FXML private TextField tf_dataA;
    @FXML private TextField tf_telefonoCellulare;
    @FXML private TextField tf_telefonoFisso;
    @FXML private TextField tf_email;
    @FXML private TextArea ta_note;

    private final Database db = Database.getInstance();
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private Stage stage;
    private CCliente parent;
    private Cliente c;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setParent(Controller parent) {
        this.parent = (CCliente) parent;
    }

    public void impostaCliente(Cliente c) {
        this.c = c;
        l_titolo.setText("Modifica - " + cp.toTitleCase(c.getNome()) + " " + cp.toTitleCase(c.getCognome()));

        // Imposto la data di nascita
        if(c.getDataNascita() != null) {
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(c.getDataNascita().getTime());
            tf_dataG.setText(String.valueOf(data.get(Calendar.DAY_OF_MONTH)));
            tf_dataM.setText(String.valueOf(data.get(Calendar.MONTH)+1));
            tf_dataA.setText(String.valueOf(data.get(Calendar.YEAR)));
        }

        tf_telefonoCellulare.setText(c.getNumeroCellulare());
        tf_telefonoFisso.setText(c.getNumeroFisso());
        tf_email.setText(c.getEmail());
        ta_note.setText(c.getNote());
    }


    @FXML
    private void realtimeCheck(KeyEvent keyEvent) {
    }

    @FXML
    private void conferma() {
        boolean check = true;

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
            if (db.modificaCampiCliente(c).getResult()) {
                parent.refresh();
                chiudi();
            } else {
                //l_registrazioneOk.setText("Errore durante la registrazione");
                //css.toError(l_registrazioneOk, null);
                //l_registrazioneOk.setVisible(true);
            }
        } else {
            //l_registrazioneOk.setText("Errore compilazione campi");
            //css.toError(l_registrazioneOk, null);
            //l_registrazioneOk.setVisible(true);
        }
    }

    @FXML
    private void chiudi() {
        stage.close();
    }

}
