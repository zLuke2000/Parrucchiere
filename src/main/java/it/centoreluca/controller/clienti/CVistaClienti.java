package it.centoreluca.controller.clienti;

import it.centoreluca.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CVistaClienti extends Controller {

    @FXML private TextField tf_nome;
    @FXML private TextField tf_cognome;
    @FXML private TextArea ta_note;

    public void conferma(ActionEvent ae) {
        /*
        if(cp.testoSempliceSenzaNumeri(tf_nome, 2, 50) & cp.testoSempliceSenzaNumeri(tf_cognome, 0, 50) & cp.testoSempliceConNumeri(ta_note, 0, 512)) {
            data.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tf_ora.getText().trim()));
            data.set(Calendar.MINUTE, Integer.parseInt(tf_minuto.getText().trim()));
            data.set(Calendar.SECOND, 0);
            data.set(Calendar.MILLISECOND, 0);
            Result res = db.registraAppuntamento(new Appuntamento(tf_nome.getText().trim().toLowerCase(Locale.ROOT), tf_cognome.getText().trim().toLowerCase(Locale.ROOT), new Timestamp(data.getTimeInMillis()), ta_note.getText().trim(), Appuntamento.Stato.DEFAULT));
            if(res.getResult()) {
                parent.caricaAppuntamenti();
                stage.close();
            }
        }
        */
    }
}
