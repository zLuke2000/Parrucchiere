package it.centoreluca.controller.dialog;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Personale;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.List;

public class CNuovoAppG extends Controller {

    @FXML private Label l_info;
    @FXML private ComboBox<String> cb_cercaCliente;
    @FXML private TextArea ta_note;
    @FXML private TextField tf_oraInizio;
    @FXML private TextField tf_minutoInizio;
    @FXML private TextField tf_oraFine;
    @FXML private TextField tf_minutoFine;
    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_note;
    @FXML private ScrollPane sp_servizi;

    private Stage stage;
    private Controller parent;
    private Calendar data;
    private Personale personale;

    private final Database db = Database.getInstance();
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final List<Cliente> listaClienti = db.leggiClienti("**").getList(Cliente.class);

    @FXML
    private void initialize() {
        cb_cercaCliente.getItems().clear();
        for(Cliente c: listaClienti) {
            cb_cercaCliente.getItems().add(c.toString());
        }
    }

    @Override
    public void impostaParametri(Stage stage, Controller parent) {
        this.stage = stage;
        this.parent = parent;
    }

    @Override
    public void impostaUlterioriParametri(Calendar data, Personale personale) {
        this.data = data;
        this.personale = personale;
        l_info.setText(cp.toTitleCase(personale.getUsername()) + " - " + data.get(Calendar.DAY_OF_MONTH) + " " + Mesi.values()[data.get(Calendar.MONTH)]);
    }

    @FXML
    public void ricercaRealtime() {
        for(Cliente c: listaClienti) {
            if(cb_cercaCliente.getValue().equals(c.toString())) {
                l_nome.setText(c.getNome());
                l_cognome.setText(c.getCognome());
                l_note.setText(c.getNote());
            }

        }
    }

    @FXML
    public void controlloRealtime(KeyEvent e) {
        System.out.println(e);
    }

    @FXML
    public void cercaServizio() {
    }

    @FXML
    public void conferma() {
    }

    public void chiudi() {
        parent.callback(false);
        stage.close();
    }
}
