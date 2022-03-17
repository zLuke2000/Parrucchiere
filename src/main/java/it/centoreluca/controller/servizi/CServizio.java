package it.centoreluca.controller.servizi;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Servizio;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CServizio extends Controller {

    @FXML private Label l_nome;
    @FXML private Label l_durata;
    @FXML private Label l_note;

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
    public void modifica() { }

    @FXML
    public void elimina(MouseEvent me) {
        if(me.getClickCount() > 1) {
            if(db.rimuoviServizio(s).getResult()) {
                rimuoviNodo(parent.vb_container, n);
            }
        }
    }
}
