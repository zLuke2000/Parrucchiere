package it.centoreluca.controller.servizi;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Servizio;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

public class CServizio extends Controller {

    // Campi
    @FXML private Label l_nome;
    @FXML private Label l_durata;

    // Note
    @FXML private Label l_note;
    @FXML private TextArea ta_note;

    // Icone
    @FXML private FontIcon fi_edit;
    @FXML private FontIcon fi_save;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final Database db = Database.getInstance();
    private CVistaServizi parent;
    private Servizio s;
    private Node n;

    public void impostaContenuto(CVistaServizi parent, Servizio s, Node n) {
        this.parent = parent;
        this.s = s;
        this.n = n;
        // Imposto nome
        l_nome.setText(cp.toTitleCase(s.getNome()));
        // Imposto durata
        l_durata.setText("Durata: " + s.getStringaDurata());
        // Imposto note
        l_note.setText(s.getNote());
    }

    @FXML
    private void modifica() {
        boolean stato = false;
        // Copio campi e salvataggio
        if(fi_edit.isVisible()) {
            copiaCampi(l_note, ta_note);
            stato = true;
        } else {
            if(cp.testoSempliceConNumeri(ta_note, 0, 255)) {
                s.setNote(ta_note.getText().trim());
                if(cambiamenti(l_note, ta_note)) {
                    if (db.modificaCampiServizio(s).getResult()) {
                        l_note.setText(s.getNote());
                        stato = true;
                    }
                } else {
                    stato = true;
                }
            }
        }

        if(stato) {
            // Inverto visualizzazione campi
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
            if(db.rimuoviServizio(s).getResult()) {
                rimuoviNodo(parent.vb_container, n);
            }
        }
    }
}
