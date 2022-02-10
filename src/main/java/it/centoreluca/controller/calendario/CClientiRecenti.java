package it.centoreluca.controller.calendario;

import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CClientiRecenti extends Controller {

    @FXML private Button b_seleziona1;
    @FXML private Button b_seleziona2;
    @FXML private Button b_seleziona3;
    @FXML private Button b_seleziona4;
    @FXML private Button b_seleziona5;
    @FXML private Button b_seleziona6;
    @FXML private Button b_seleziona7;
    @FXML private Button b_seleziona8;
    @FXML private Button b_seleziona9;
    @FXML private Button b_seleziona10;

    private Stage stage;
    private CNuovoAppuntamento parent;
    private final Database db = Database.getInstance();
    private final List<Button> listaBottoni = new ArrayList<>();
    private List<Cliente> cr;

    @FXML
    private void initialize() {
        listaBottoni.add(b_seleziona1);
        listaBottoni.add(b_seleziona2);
        listaBottoni.add(b_seleziona3);
        listaBottoni.add(b_seleziona4);
        listaBottoni.add(b_seleziona5);
        listaBottoni.add(b_seleziona6);
        listaBottoni.add(b_seleziona7);
        listaBottoni.add(b_seleziona8);
        listaBottoni.add(b_seleziona9);
        listaBottoni.add(b_seleziona10);
    }

    public void impostaParametri(Stage stage, Controller parent, Calendar data) {
        this.stage = stage;
        this.parent = (CNuovoAppuntamento) parent;
        Result rs = db.leggiRecenti(10);
        if(rs.getResult()) {
            cr = rs.getList(Cliente.class);
            for (int i = 0; i < 10; i++) {
                if(i < cr.size()) {
                    listaBottoni.get(i).setText(cr.get(i).toString());
                } else {
                    listaBottoni.get(i).setText("");
                    listaBottoni.get(i).setDisable(true);
                }
            }
        }
        stage.centerOnScreen();
    }

    @FXML
    private void seleziona(ActionEvent ae) {
        for (int i = 0; i < 10; i++) {
            if(listaBottoni.get(i).getText().equals(((Button) ae.getSource()).getText())) {
                parent.impostaNomeCognome(cr.get(i).getNome(), cr.get(i).getCognome());
                chiudiDialog();
                break;
            }
        }
    }

    @FXML
    private void chiudiDialog() {
        stage.close();
    }
}
