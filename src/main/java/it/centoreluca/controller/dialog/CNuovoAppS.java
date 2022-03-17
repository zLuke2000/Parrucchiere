package it.centoreluca.controller.dialog;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Personale;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.DialogHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CNuovoAppS extends Controller {

    @FXML private Label l_info;
    @FXML private TextField tf_nome;
    @FXML private TextField tf_cognome;
    @FXML private TextField tf_ora;
    @FXML private TextField tf_minuto;
    @FXML private TextArea ta_note;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final Database db = Database.getInstance();
    private final DialogHelper dh = DialogHelper.getInstance();
    private Stage stage;
    private Controller parent;
    private Personale p;
    private final Calendar data = new GregorianCalendar();

/*
    @Override
    public void impostaParametri(Stage stage, CColonnaPersonale parent) {
        this.stage = stage;
        this.parent = parent;
    }
 */

    public void impostaData(Calendar data) {
        this.data.setTimeInMillis(data.getTimeInMillis());
        l_info.setText("Appuntamento: " + data.get(Calendar.DAY_OF_MONTH) + "/" + data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR));
    }


    /*
    public void conferma() {
        if(cp.testoSempliceSenzaNumeri(tf_nome, 2, 50) & cp.testoSempliceSenzaNumeri(tf_cognome, 0, 50) & cp.ora(tf_ora) & cp.minuto(tf_minuto) & cp.testoSempliceConNumeri(ta_note, 0, 512)) {
            data.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tf_ora.getText().trim()));
            data.set(Calendar.MINUTE, Integer.parseInt(tf_minuto.getText().trim()));
            data.set(Calendar.SECOND, 0);
            data.set(Calendar.MILLISECOND, 0);
            Result res = db.registraAppuntamento(new Appuntamento(tf_nome.getText().trim().toLowerCase(Locale.ROOT), tf_cognome.getText().trim().toLowerCase(Locale.ROOT), new Timestamp(data.getTimeInMillis()), ta_note.getText().trim(), Appuntamento.Stato.DEFAULT, null, p));
            if(res.getResult()) {
                parent.caricaAppuntamenti();
                stage.close();
            }
        }
    }
     */

    public void chiudiDialog() {
        stage.close();
    }

    public void controlloRealtime(KeyEvent ke) {
        if(ke.getSource().equals(tf_ora) & tf_ora.getText().trim().length() >= 2) {
            if(cp.ora(tf_ora)) {
                tf_minuto.requestFocus();
            }
        } else if(ke.getSource().equals(tf_minuto) & tf_minuto.getText().trim().length() >= 2) {
            cp.minuto(tf_minuto);
        } else if(ke.getSource().equals(tf_minuto) & tf_minuto.getText().trim().length() == 0) {
            tf_ora.requestFocus();
        }
    }

    public void impostaNomeCognome(String nome, String cognome) {
        tf_nome.setText(nome);
        tf_cognome.setText(cognome);
    }

    @FXML
    public void conferma(ActionEvent actionEvent) {
    }

    @FXML
    void mostraRecenti(ActionEvent actionEvent) {
    }
}
