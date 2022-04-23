package it.centoreluca.controller.personale;

import it.centoreluca.controller.Controller;
import it.centoreluca.controller.dialog.CDialog;
import it.centoreluca.database.Database;
import it.centoreluca.models.Personale;
import it.centoreluca.models.Result;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

public class CPersonale extends Controller {

    // Username
    @FXML private Label l_username;
    @FXML private TextField tf_username;

    // Labels
    @FXML private Label l_cognome;
    @FXML private Label l_nome;

    // Note
    @FXML private Label l_note;
    @FXML private TextArea ta_note;

    // Icone
    @FXML private FontIcon fi_edit;
    @FXML private FontIcon fi_save;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final DialogHelper dh = DialogHelper.getInstance();
    private final Database db = Database.getInstance();
    private CVistaPersonale parent;
    private Personale p;
    private Node n;

    public void impostaContenuto(CVistaPersonale parent, Personale p, Node n) {
        this.parent = parent;
        this.p = p;
        this.n = n;
        // Imposto label
        l_username.setText(cp.toTitleCase(p.getUsername()));
        l_cognome.setText(cp.toTitleCase(p.getCognome()));
        l_nome.setText(cp.toTitleCase(p.getNome()));
        l_note.setText(p.getNote());
    }

    @FXML
    private void modifica() {
        boolean stato = false;
        // Copio campi e salvataggio
        if(fi_edit.isVisible()) {
            copiaCampi(l_note, ta_note);
            copiaCampi(l_username, tf_username);
            stato = true;
        } else {
            if(cp.testoSempliceConNumeri(ta_note, 0, 255) & cp.testoSempliceSenzaNumeri(tf_username, 2, 16)) {
                p.setUsername(tf_username.getText().trim());
                p.setNote(ta_note.getText().trim());
                if(cambiamenti(l_note, ta_note) || cambiamenti(l_username, tf_username)) {
                    if (db.modificaCampiPersonale(p).getResult()) {
                        l_username.setText(p.getUsername());
                        l_note.setText(p.getNote());
                        stato = true;
                    }
                } else {
                    stato = true;
                }
            }
        }

        if(stato) {
            // Inverto visualizzazione campi
            l_username.setVisible(!fi_edit.isVisible());
            tf_username.setVisible(fi_edit.isVisible());
            l_note.setVisible(!fi_edit.isVisible());
            ta_note.setVisible(fi_edit.isVisible());

            // Inverto icone
            fi_edit.setVisible(!fi_edit.isVisible());
            fi_save.setVisible(!fi_save.isVisible());
        }
    }

    @FXML
    private void elimina(MouseEvent me) {
        if(me.getClickCount() > 1) {
            Result res = db.rimuoviPersonale(p);
            if(res.getResult()) {
                rimuoviNodo(parent.vb_container, n);
            } else if(res.getError().equals(Result.Error.TUPLA_UTILIZZATA)) {
                // ALERT
                CDialog alert = (CDialog) dh.newDialog("Generico", "ATTENZIONE", this);
                alert.impostaTipo(CDialog.Tipo.ATTENZIONE);
                alert.impostaTitolo("Rimozione personale");
                alert.impostaDescrizione("Il personale che sta per essere rimosso \u00E8 associato ad appuntamenti\n\nL'operazione non potr\u00E0 essere annullata, procedere con la rimozione?");
                alert.rinominaPulsanteDefault("NO");
                Button b = new Button("SI");
                b.setOnAction(event -> {
                    if(db.rimuoviAppuntamentiAssociati(p).getResult()) {
                        rimuoviNodo(parent.vb_container, n);
                        alert.chiudi();
                    }
                });
                alert.aggiungiPulsante(b);
                dh.display();
            }
        }
    }
}
