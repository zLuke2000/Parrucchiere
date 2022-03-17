package it.centoreluca.controller.personale;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Personale;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CPersonale extends Controller {

    @FXML private Label l_username;
    @FXML private Label l_cognome;
    @FXML private Label l_nome;
    @FXML private Label l_note;

    private final ControlloParametri cp = ControlloParametri.getInstance();
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
    public void modifica() {
    }

    @FXML
    public void elimina(MouseEvent me) {
        if(me.getClickCount() > 1) {
            if(db.rimuoviPersonale(p).getResult()) {
                rimuoviNodo(parent.vb_container, n);
            }
        }
    }
}
