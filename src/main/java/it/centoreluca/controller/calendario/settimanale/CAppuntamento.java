package it.centoreluca.controller.calendario.settimanale;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Appuntamento;
import it.centoreluca.thread.Orario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CAppuntamento extends Controller {

    @FXML private AnchorPane ap_root;

    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_orario;
    @FXML private Label l_note;

    @FXML private FontIcon fi_annulla;
    @FXML private FontIcon fi_reimposta;
    @FXML private FontIcon fi_completa;

    private final Database db = Database.getInstance();
    private final Orario ora = Orario.getInstance();
    private final Calendar orario = new GregorianCalendar();
    private CColonna parent;
    private Appuntamento a;

    public void impostaContenuto(CColonna parent, Appuntamento a) {
        this.parent = parent;
        this.a = a;
        l_nome.setText(a.getCliente().getNome());
        l_cognome.setText(a.getCliente().getCognome());
        orario.setTimeInMillis(a.getOrarioInizio().getTime());
        l_orario.setText(orario.get(Calendar.HOUR_OF_DAY) + ":" + orario.get(Calendar.MINUTE));
        l_note.setText(a.getNote());
        a.setPane(ap_root);
    }

    public void statoAppuntamento(MouseEvent me) {
        if(me.getSource().equals(fi_annulla)) {
            if(me.getClickCount() > 1) {
                ora.rimuoviAppuntamento(a);
                db.rimuoviAppuntamento(a);
            } else {
                a.setStato(Appuntamento.Stato.ANNULLATO);
            }
        } else if(me.getSource().equals(fi_reimposta)) {
            a.setStato(Appuntamento.Stato.DEFAULT);
        } else if(me.getSource().equals(fi_completa)) {
            a.setStato(Appuntamento.Stato.COMPLETATO);
        }
        parent.caricaAppuntamenti();
    }
}
