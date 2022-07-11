package it.centoreluca.controller.clienti;

import it.centoreluca.controller.Controller;
import it.centoreluca.controller.dialog.CDialog;
import it.centoreluca.controller.dialog.CPreferiti;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Result;
import it.centoreluca.models.Servizio;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.CssHelper;
import it.centoreluca.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Calendar;
import java.util.Locale;

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

    @FXML private HBox hb_serviziPreferiti;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final DialogHelper dh = DialogHelper.getInstance();
    private final CssHelper css = CssHelper.getInstance();
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

        caricaPreferiti();
    }

    @FXML
    private void coloreRealtime() {
        if(tf_colore.getText().trim().length() > 128) {
            css.toError(tf_colore,"Massimo 128 caratteri");
        } else {
            css.toValid(tf_colore);
            c.setColore(tf_colore.getText().trim().toUpperCase(Locale.ROOT));
            db.modificaColoreCliente(c);
        }
    }

    @FXML
    private void preferiti() {
        CPreferiti controller = (CPreferiti) dh.newDialog("Preferiti", "Gestione preferiti", this);
        controller.impostaCliente(c);
        dh.display();
    }

    /**
     * Metodo per caricare le chips dei preferiti per ogni cliente
     */
    public void caricaPreferiti() {

        Result res = db.leggiServPref(c.getId());
        if(res.getResult()) {
            hb_serviziPreferiti.getChildren().clear();
            for(Servizio s: res.getList(Servizio.class)) {
                Label l = new Label(s.getNome());
                hb_serviziPreferiti.getChildren().add(l);
            }
        }
    }

    @FXML
    private void modifica() {
        boolean stato;
        // Copio campi e salvataggio
        if(fi_edit.isVisible()) {
            //todo: copiare campo data di nascita
            copiaCampi(l_telefonoCellulare, tf_telefonoCellulare);
            copiaCampi(l_telefonoFisso, tf_telefonoFisso);
            copiaCampi(l_email, tf_email);
            copiaCampi(l_note, ta_note);
            stato = true;
        } else {
            boolean modifiche = false;
            if(cambiamenti(l_telefonoCellulare, tf_telefonoCellulare) & cp.numeri(tf_telefonoCellulare, 10, 16)) {
                c.setNumeroCellulare(tf_telefonoCellulare.getText().trim());
                modifiche = true;
            }
            if(cambiamenti(l_telefonoFisso, tf_telefonoFisso) & cp.numeri(tf_telefonoFisso, 10, 16)) {
                c.setNumeroFisso(tf_telefonoCellulare.getText().trim());
                modifiche = true;
            }
            if(cambiamenti(l_email, tf_email) & cp.email(tf_email)) {
                c.setEmail(tf_telefonoCellulare.getText().trim());
                modifiche = true;
            }
            if(cambiamenti(l_note, ta_note) & cp.testoSempliceConNumeri(ta_note, 0, 512)) {
                c.setNote(ta_note.getText().trim());
                modifiche = true;
            }

            if(modifiche & db.modificaCampiCliente(c).getResult()) {
                l_telefonoCellulare.setText(c.getNumeroCellulare());
                l_telefonoFisso.setText(c.getNumeroFisso());
                l_email.setText(c.getEmail());
                l_note.setText(c.getNote());
                stato = true;
            } else {
                stato = false;
            }
        }

        if(stato) {
            // Inverto visualizzazione campi
            l_telefonoCellulare.setVisible(!fi_edit.isVisible());
            tf_telefonoCellulare.setVisible(fi_edit.isVisible());
            l_telefonoFisso.setVisible(!fi_edit.isVisible());
            tf_telefonoFisso.setVisible(fi_edit.isVisible());
            l_email.setVisible(!fi_edit.isVisible());
            tf_email.setVisible(fi_edit.isVisible());
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
            Result res = db.rimuoviCliente(c);
            if(res.getResult()) {
                rimuoviNodo(parent.vb_container, n);
            } else if(res.getError().equals(Result.Error.TUPLA_UTILIZZATA)) {
                // ALERT
                CDialog alert = (CDialog) dh.newDialog("Generico", "ATTENZIONE", this);
                alert.impostaTipo(CDialog.Tipo.ATTENZIONE);
                alert.impostaTitolo("Rimozione cliente");
                alert.impostaDescrizione("Il cliente che sta per essere rimosso \u00E8 associato ad appuntamenti\n\nL'operazione non potr\u00E0 essere annullata, procedere con la rimozione?");
                alert.rinominaPulsanteDefault("NO");
                Button b = new Button("SI");
                b.setOnAction(event -> {
                    if(db.rimuoviClientiAssociati(c).getResult()) {
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
