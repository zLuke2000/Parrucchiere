package it.centoreluca.controller.clienti;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.Cliente;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Calendar;

public class CCliente extends Controller {

    // Nome e cognome
    @FXML private Label l_nome;
    @FXML private Label l_cognome;

    // Data di nascita
    @FXML private Label l_dataNascita;
    @FXML private TextField tf_dataNascita;

    // Telefono cellulare
    @FXML private Label l_telefonoCellulare;
    @FXML private TextField tf_telefonoCellulare;

    // Telefono fisso
    @FXML private Label l_telefonoFisso;
    @FXML private TextField tf_telefonoFisso;

    // Email
    @FXML private Label l_email;
    @FXML private TextField tf_email;

    // Colore
    @FXML private TextField tf_colore;

    // Note
    @FXML private Label l_note;
    @FXML private TextArea ta_note;

    // Icone
    @FXML private FontIcon fi_edit;
    @FXML private FontIcon fi_save;
    @FXML private FontIcon fi_salvataggio;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final DialogHelper dh = DialogHelper.getInstance();
    private final Database db = Database.getInstance();
    private CVistaClienti parent;
    private Cliente c;
    private Node n;

    public void impostaContenuto(CVistaClienti parent, Cliente c, Node n) {
        this.parent = parent;
        this.c = c;
        this.n = n;
        l_cognome.setText(cp.toTitleCase(c.getCognome()));
        l_nome.setText(cp.toTitleCase(c.getNome()));

        // Imposto la data di nascita
        if(c.getDataNascita() != null) {
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(c.getDataNascita().getTime());
            l_dataNascita.setText(data.get(Calendar.DAY_OF_MONTH) + " " + Mesi.values()[data.get(Calendar.MONTH)] + " " + data.get(Calendar.YEAR));
        } else {
            l_dataNascita.setText("");
        }
        // Imposto il numero di telefono cellulare
        if(c.getNumeroCellulare() != null) {
            l_telefonoCellulare.setText(c.getNumeroCellulare().substring(0, 3) + " " + c.getNumeroCellulare().substring(3));
        } else {
            l_telefonoCellulare.setText("");
        }
        //Imposto il numero di telefono fisso
        if(c.getNumeroFisso() != null) {
            l_telefonoFisso.setText(c.getNumeroFisso().substring(0, 4) + " " + c.getNumeroFisso().substring(4));
        } else {
            l_telefonoFisso.setText("");
        }
        l_email.setText(c.getEmail());
        l_note.setText(c.getNote());
        tf_colore.setText(c.getColore());
    }

    public void realtimeCheck() {
        //TODO controllo cambio colore
    }

    @FXML
    public void modifica() {
        // Inverto visualizzazione campi
        l_telefonoCellulare.setVisible(!fi_edit.isVisible());
        tf_telefonoCellulare.setVisible(fi_edit.isVisible());
        l_telefonoFisso.setVisible(!fi_edit.isVisible());
        tf_telefonoFisso.setVisible(fi_edit.isVisible());
        l_email.setVisible(!fi_edit.isVisible());
        tf_email.setVisible(fi_edit.isVisible());
        l_note.setVisible(!fi_edit.isVisible());
        ta_note.setVisible(fi_edit.isVisible());

        if(fi_edit.isVisible()) {
            copiaCampi(l_telefonoCellulare, tf_telefonoCellulare);
            copiaCampi(l_telefonoFisso, tf_telefonoFisso);
            copiaCampi(l_email, tf_email);
            copiaCampi(l_note, ta_note);
        }

        // Inverto icone
        fi_edit.setVisible(!fi_edit.isVisible());
        fi_save.setVisible(!fi_save.isVisible());
    }

    @FXML
    public void elimina(MouseEvent me) {
        if(me.getClickCount() > 1) {
            if(db.rimuoviCliente(c).getResult()) {
                rimuoviNodo(parent.vb_container, n);
            }
        }
    }

    private void copiaCampi(Label l, TextInputControl tic) {
        tic.setText(l.getText());
    }

}
