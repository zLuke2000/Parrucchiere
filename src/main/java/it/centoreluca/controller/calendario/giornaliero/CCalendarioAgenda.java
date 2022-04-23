package it.centoreluca.controller.calendario.giornaliero;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Personale;
import it.centoreluca.models.Result;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CCalendarioAgenda extends Controller {

    @FXML private Label l_data;
    @FXML private Label l_compleanno;
    @FXML private FontIcon fi_compleanno;
    @FXML private HBox hb_container;

    private final Database db = Database.getInstance();
    private final Calendar data = new GregorianCalendar();

    @FXML
    private void initialize() {
        // Imposto la data corrente
        l_data.setText(data.get(Calendar.DAY_OF_MONTH) + " " + Mesi.values()[data.get(Calendar.MONTH)] + " " + data.get(Calendar.YEAR));

        // Controllo compleanni in un thread separato
        new Thread(() -> {
            Result res = db.controllaCompleanno(data);
            StringBuilder testo = new StringBuilder("COMPLEANNO:");
            if(res.getResult()) {
                for(Cliente c: res.getList(Cliente.class)) {
                    testo.append(" ").append(c.getCognome()).append(" ").append(c.getNome());
                }
                l_compleanno.setVisible(true);
                fi_compleanno.setVisible(true);
                l_compleanno.setText(testo.toString());
            }
        }).start();

        // Carico i fragment delle colonne
        caricaFragment();
    }

    private void caricaFragment() {
        rimuoviFragment();
        Result rs = db.leggiPersonale();
        // Animazione
        SequentialTransition st = new SequentialTransition();
        for(Personale d: rs.getList(Personale.class)) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioG/ColonnaPersonale.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                hb_container.getChildren().add(ap);
                /* ANIMAZIONE */
                FadeTransition ft = new FadeTransition(Duration.millis(250), ap);
                ft.setFromValue(0);
                ft.setToValue(1);
                st.getChildren().add(ft);
                /* FINE ANIMAZIONE */
                CColonnaPersonale controllerColonna = fxmlLoader.getController();
                controllerColonna.impostaContenuto(d, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        st.play();
    }

    private void rimuoviFragment() {
        hb_container.getChildren().clear();
    }
}
