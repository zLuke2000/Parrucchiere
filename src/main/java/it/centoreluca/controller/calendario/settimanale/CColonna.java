package it.centoreluca.controller.calendario.settimanale;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.controller.dialog.CNuovoAppS;
import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Giorni;
import it.centoreluca.models.Appuntamento;
import it.centoreluca.models.Result;
import it.centoreluca.thread.Orario;
import it.centoreluca.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CColonna extends Controller {

    @FXML private VBox vb_container;
    @FXML private Label l_giorno;

    private final DialogHelper dh = DialogHelper.getInstance();
    private final Database db = Database.getInstance();
    private final Orario ora = Orario.getInstance();
    private final Calendar data = new GregorianCalendar();

    public void impostaContenuto(Calendar data) {
        this.data.setTimeInMillis(data.getTimeInMillis());
        int giornoSettimana = this.data.get(Calendar.DAY_OF_WEEK) - 2;
        if(giornoSettimana == -1) {
            giornoSettimana = 6;
        }
        l_giorno.setText(Giorni.values()[giornoSettimana] + " - " + (this.data.get(Calendar.DAY_OF_MONTH)));
        caricaAppuntamenti();
    }

    public void caricaAppuntamenti() {
        vb_container.getChildren().clear();
        Result rs = db.leggiAppuntamenti(data, -1);
        for (Appuntamento a: rs.getList(Appuntamento.class)) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioS/Appuntamento.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                vb_container.getChildren().add(ap);
                CAppuntamento appuntamentoController = fxmlLoader.getController();
                appuntamentoController.impostaContenuto(this, a);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* Aggiungo l'appuntamento al Thread dedicato al controllo dell'orario */
            ora.aggiungiAppuntamento(a);
        }
    }

    public void nuovoAppuntamento() {
        CNuovoAppS controller = (CNuovoAppS) dh.newDialog("fxml/fragment/dialog/NuovoAppuntamento", null, this, data, null);
        controller.impostaData(data);
    }
}
