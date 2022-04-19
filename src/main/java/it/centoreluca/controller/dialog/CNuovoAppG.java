package it.centoreluca.controller.dialog;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.*;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.CssHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CNuovoAppG extends Controller {

    // Titolo
    @FXML private Label l_info;

    // Sezione cliente
    @FXML private TextField tf_cercaCliente;
    @FXML private ComboBox<String> cb_selezionaCliente;
    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_note;

    // Sezione servizi
    @FXML private VBox vb_servizi;
    @FXML private TextField tf_oraInizio;
    @FXML private TextField tf_minutoInizio;
    @FXML private Label l_fine;
    @FXML private Label l_oraFine;
    @FXML private TextArea ta_note;

    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final List<Cliente> listaClienti = db.leggiClienti("**").getList(Cliente.class);
    private final List<Servizio> listaServizi = db.leggiServizi("**").getList(Servizio.class);
    private final HashMap<Integer, ToggleButton> preferitiTG = new HashMap<>();

    private Stage stage;
    private Controller parent;
    private Calendar data;
    private Personale personale;
    private Cliente clienteSelezionato;

    @FXML
    private void initialize() {
        l_nome.setText("");
        l_cognome.setText("");
        l_note.setText("");

        // Carico i fragment dei servizi
        for(Servizio s: listaServizi) {
            ToggleButton tg = new ToggleButton(s.getNome());
            tg.setMaxWidth(Double.MAX_VALUE);
            tg.getStyleClass().add("tg-servizio");
            tg.selectedProperty().addListener((observable, oldValue, newValue) -> calcolaOraFine());
            vb_servizi.getChildren().add(tg);
            preferitiTG.put(s.getId(), tg);
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
    private void controlloRealtime(KeyEvent ke) {
        if(ke.getSource().equals(tf_cercaCliente)) {
            l_nome.setText("");
            l_cognome.setText("");
            l_note.setText("");
            cb_selezionaCliente.getItems().clear();
            for (Cliente c : listaClienti) {
                if (c.getNome().contains(tf_cercaCliente.getText().trim()) || c.getCognome().contains(tf_cercaCliente.getText().trim())) {
                    cb_selezionaCliente.getItems().add(c.toString());
                }
            }
            cb_selezionaCliente.getSelectionModel().selectFirst();
        } else if (ke.getSource().equals(tf_oraInizio) | ke.getSource().equals(tf_minutoInizio)) {
            calcolaOraFine();
        }
    }

    @FXML
    private void ricercaRealtime() {
        for (Cliente c : listaClienti) {
            if (c.toString().equals(cb_selezionaCliente.getValue())) {
                clienteSelezionato = c;
                l_nome.setText(clienteSelezionato.getNome());
                l_cognome.setText(clienteSelezionato.getCognome());
                l_note.setText(clienteSelezionato.getNote());
                List<Integer> serviziCliente = db.leggiIdServPref(c.getId()).getList(Integer.class);
                preferitiTG.forEach((i, tg) -> tg.setSelected(serviziCliente.contains(i)));
                break;
            }
        }
    }

    private void calcolaOraFine() {
        l_fine.setVisible(false);
        l_oraFine.setVisible(false);
        if(tf_minutoInizio.getText().trim().length() > 0 && tf_oraInizio.getText().trim().length() > 0 && cp.ora(tf_oraInizio) && cp.minuto(tf_minutoInizio)) {
            AtomicInteger durata = new AtomicInteger(Integer.parseInt(tf_oraInizio.getText().trim())*60 + Integer.parseInt(tf_minutoInizio.getText().trim()));
            preferitiTG.forEach((i, tg) -> {
                if(tg.isSelected()) {
                    for(Servizio s: listaServizi) {
                        if(s.getId() == i) {
                            durata.addAndGet((s.getDurata()));
                            break;
                        }
                    }
                }
            });
            if(durata.get() < 1440) {
                l_fine.setVisible(true);
                l_oraFine.setVisible(true);
                if(durata.get() % 60 < 10) {
                    l_oraFine.setText(durata.get() / 60 + ":0" + durata.get() % 60);
                } else {
                    l_oraFine.setText(durata.get() / 60 + ":" + durata.get() % 60);
                }
            }
        }
    }

    @FXML
    private void conferma() {
        Appuntamento a = new Appuntamento();

        if(clienteSelezionato != null) {
            a.setCliente(clienteSelezionato);
            a.setPersonale(personale);

            preferitiTG.forEach((i, tg) -> {
                if(tg.isSelected()) {
                    for(Servizio s: listaServizi) {
                        if(s.getId() == i) {
                            a.getServizi().add(s);
                            break;
                        }
                    }
                }
            });

            if(cp.ora(tf_oraInizio) & cp.minuto(tf_minutoInizio)) {
                data.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tf_oraInizio.getText().trim()));
                data.set(Calendar.MINUTE, Integer.parseInt(tf_minutoInizio.getText().trim()));
                data.set(Calendar.SECOND, 0);
                data.set(Calendar.MILLISECOND, 0);
                a.setOrarioInizio(new Timestamp(data.getTimeInMillis()));
            }

            if(cp.testoSempliceConNumeri(ta_note, 0, 512)) {
                a.setNote(ta_note.getText().trim());
            }

            Result res = db.registraAppuntamento(a);
            if(res.getResult()) {
                parent.callback(true);
                chiudi();
            }
        } else {
            css.toError(tf_cercaCliente,"Selezionare un cliente");
        }
    }

    @FXML
    private void chiudi() {
        stage.close();
    }

}
