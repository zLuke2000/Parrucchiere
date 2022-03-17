package it.centoreluca.controller.calendario.settimanale;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.enumerator.Mesi;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CCalendario extends Controller {

    @FXML private Button b_menoMese;
    @FXML private Button b_menoSettimana;
    @FXML private Label l_data;
    @FXML private Button b_piuSettimana;
    @FXML private Button b_piuMese;
    @FXML private HBox hb_container;

    private final Calendar data = new GregorianCalendar();
    private final Calendar dataTemp = new GregorianCalendar();

    @FXML
    private void initialize() {
        cambiaLabel();
    }

    @FXML
    private void aggiornaData(ActionEvent ae) {
        if(ae.getSource().equals(b_menoMese)) {
            data.add(Calendar.MONTH, -1);
        } else if(ae.getSource().equals(b_menoSettimana)) {
            data.add(Calendar.DAY_OF_YEAR, -7);
        } else if(ae.getSource().equals(b_piuSettimana)) {
            data.add(Calendar.DAY_OF_YEAR, 7);
        } else if(ae.getSource().equals(b_piuMese)) {
            data.add(Calendar.MONTH, 1);
        }
        cambiaLabel();
    }

    private void cambiaLabel() {
        dataTemp.setTimeInMillis(data.getTimeInMillis());
        dataTemp.add(Calendar.DAY_OF_YEAR, 6);
        l_data.setText(data.get(Calendar.DAY_OF_MONTH) + " " + Mesi.values()[data.get(Calendar.MONTH)] + " " + data.get(Calendar.YEAR) + "  -  " +
                       dataTemp.get(Calendar.DAY_OF_MONTH) + " " + Mesi.values()[dataTemp.get(Calendar.MONTH)] + " " + dataTemp.get(Calendar.YEAR));
        dataTemp.add(Calendar.DAY_OF_YEAR, -6);
        caricaFragment();
    }

    private void caricaFragment() {
        rimuoviFragment();
        for (int i = 0; i < 7; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioS/Colonna.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                hb_container.getChildren().add(ap);
                /* ANIMAZIONE */
                FadeTransition ft = new FadeTransition(Duration.millis(250), ap);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                /* FINE ANIMAZIONE */
                CColonna colonnaController = fxmlLoader.getController();
                colonnaController.impostaContenuto(dataTemp);
                dataTemp.add(Calendar.DAY_OF_YEAR, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void rimuoviFragment() {
        hb_container.getChildren().clear();
    }

}
