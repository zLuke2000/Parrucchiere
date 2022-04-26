package it.centoreluca.controller.dialog;

import it.centoreluca.controller.Controller;
import it.centoreluca.controller.clienti.CCliente;
import it.centoreluca.database.Database;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Servizio;
import it.centoreluca.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CPreferiti extends Controller {

    @FXML private Label l_titolo;
    @FXML private VBox vb_servizi;

    private final Database db = Database.getInstance();
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final HashMap<ToggleButton, Servizio> preferitiTG = new HashMap<>();
    private Stage stage;
    private CCliente parent;
    private Cliente c;

    @FXML
    private void initialize() {
        // Carico i fragment dei servizi
        for(Servizio s: db.leggiServizi("**").getList(Servizio.class)) {
            ToggleButton tg = new ToggleButton(s.getNome());
            tg.setMaxWidth(Double.MAX_VALUE);
            tg.getStyleClass().add("tg-servizio");
            vb_servizi.getChildren().add(tg);
            preferitiTG.put(tg, s);
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setParent(Controller parent) {
        this.parent = (CCliente) parent;
    }

    public void impostaCliente(Cliente c) {
        this.c = c;
        l_titolo.setText("Preferiti - " + cp.toTitleCase(c.getNome()) + " " + cp.toTitleCase(c.getCognome()));
        List<Integer> serviziCliente = db.leggiIdServPref(c.getId()).getList(Integer.class);
        preferitiTG.forEach((tg, s) -> {
            if(serviziCliente.contains(s.getId())) {
                tg.setSelected(true);
            }
        });
    }

    @FXML
    private void conferma() {
        List<Integer> nuoviPreferiti = new ArrayList<>();
        preferitiTG.forEach((tg, s) -> {
            if(tg.isSelected()) {
                nuoviPreferiti.add(s.getId());
            }
        });
        if(db.aggiornaServPref(c.getId(), nuoviPreferiti).getResult()) {
            parent.caricaPreferiti();
            chiudi();
        }
    }

    @FXML
    private void chiudi() {
        stage.close();
    }
}
