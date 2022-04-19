package it.centoreluca.controller.calendario.giornaliero;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Appuntamento;
import it.centoreluca.thread.Orario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public class CAppuntamentoDinamico extends Controller {

    @FXML private AnchorPane ap_root;

    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private VBox vb_altro;
    @FXML private Label l_colore;
    @FXML private Label l_note;

    @FXML private FontIcon fi_annulla;
    @FXML private FontIcon fi_reimposta;
    @FXML private FontIcon fi_completa;

    private final Database db = Database.getInstance();
    private final Orario ora = Orario.getInstance();
    private CColonnaPersonale parent;
    private Appuntamento a;

    public void impostaContenuto(CColonnaPersonale parent, Appuntamento a) {
        this.parent = parent;
        this.a = a;
        a.setPane(ap_root);
        l_nome.setText(a.getCliente().getNome());
        l_cognome.setText(a.getCliente().getCognome());

        /* Imposto il colore se il fragment ha il campo */
        if(l_colore != null) {
            if(a.getCliente().getColore() != null) {
                l_colore.setText("COLORE: " + a.getCliente().getColore());
            } else {
                ap_root.getChildren().remove(l_colore);
                vb_altro.getChildren().remove(l_colore);
            }
        }
        /* Imposto le note se il fragment ha il campo */
        if(l_note != null) {
            if(Objects.equals(a.getNote(), "")) {
                vb_altro.getChildren().remove(l_note);
            } else {
                l_note.setText(a.getNote());
            }
        }
    }

    @FXML
    void statoAppuntamento(MouseEvent me) {
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
