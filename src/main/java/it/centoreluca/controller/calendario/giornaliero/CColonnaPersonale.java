package it.centoreluca.controller.calendario.giornaliero;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.database.Database;
import it.centoreluca.models.Appuntamento;
import it.centoreluca.models.Personale;
import it.centoreluca.models.Result;
import it.centoreluca.thread.Orario;
import it.centoreluca.util.ControlloParametri;
import it.centoreluca.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CColonnaPersonale extends Controller {
    @FXML private Label l_personale;
    @FXML private GridPane gp_agenda;

    private final Database db = Database.getInstance();
    private final Orario ora = Orario.getInstance();
    private final DialogHelper dh = DialogHelper.getInstance();
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final List<AnchorPane> listaAppuntamenti = new ArrayList<>();

    private Personale pers;
    private final Calendar data = new GregorianCalendar();

    public void impostaContenuto(Personale dip, Calendar data) {
        this.pers = dip;
        this.data.setTimeInMillis(data.getTimeInMillis());
        l_personale.setText(cp.toTitleCase(dip.getUsername()));
        caricaAppuntamenti();
    }

    public void caricaAppuntamenti() {
        rimuoviAppuntamenti();
        Result rs = db.leggiAppuntamenti(data, pers.getId());
        if(rs.getResult()) {
            for (Appuntamento a : rs.getList(Appuntamento.class)) {
                int rowSpan = a.getDurata()/15;
                if(rowSpan == 0) {
                    rowSpan = 1;
                }
                FXMLLoader fxmlLoader;
                if(rowSpan <= 3) {
                    fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioG/Appuntamento1to3.fxml"));
                } else if(rowSpan <= 7) {
                    fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioG/Appuntamento4to7.fxml"));
                } else {
                    fxmlLoader = new FXMLLoader(App.class.getResource("fxml/fragment/calendarioG/Appuntamento8plus.fxml"));
                }

                try {
                    AnchorPane ap = fxmlLoader.load();
                    listaAppuntamenti.add(ap);
                    //TODO evitare sovrapposizioni
                    int colonna = 1;

                    Calendar orario = new GregorianCalendar();
                    orario.setTimeInMillis(a.getOrarioInizio().getTime());

                    /* Impostazione riga ora e minuto */
                    int ora = orario.get(Calendar.HOUR_OF_DAY);
                    int riga = 0;
                    if(orario.get(Calendar.HOUR_OF_DAY) >= 8 ) {
                        riga += (ora-8)*4;
                        riga += orario.get(Calendar.MINUTE)/15;
                    }

                    gp_agenda.add(ap, colonna, riga, 1, rowSpan);

                    CAppuntamentoDinamico controllerAppuntamento = fxmlLoader.getController();
                    controllerAppuntamento.impostaContenuto(this, a);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /* Aggiungo l'appuntamento al Thread dedicato al controllo dell'orario */
                ora.aggiungiAppuntamento(a);
            }
        }
    }

    private void rimuoviAppuntamenti() {
        gp_agenda.getChildren().removeAll(listaAppuntamenti);
    }

    public void nuovoAppuntamento() {
        dh.newDialog("fxml/dialog/NuovoAppG", "Nuovo Appuntamento", this, data, pers, null);
    }

    @Override
    public void callback(boolean status) {
        if(status) {
            caricaAppuntamenti();
        }
    }
}
