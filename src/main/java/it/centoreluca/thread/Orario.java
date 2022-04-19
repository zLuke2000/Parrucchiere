package it.centoreluca.thread;

import it.centoreluca.models.Appuntamento;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Orario extends Thread {

    private static Orario instance = null;
    private final List<Appuntamento> listaAppuntamenti = new ArrayList<>();

    private Label etichettaOrologio;

    private Orario() {}

    public static Orario getInstance(){
        if(instance == null){
            instance = new Orario();
        }
        return instance;
    }

    public void impostaEtichetta(Label orologio) {
        this.etichettaOrologio = orologio;
        start();
    }

    @SuppressWarnings("all")
    @Override
    public void run() {
        super.run();
        String ora;
        String min;
        String sec;
        for(;;) {
            /* OROLOGIO */
            Calendar orario = Calendar.getInstance();
            if(orario.get(Calendar.HOUR_OF_DAY) < 10) {
                ora = "0" + orario.get(Calendar.HOUR_OF_DAY);
            } else {
                ora = "" + orario.get(Calendar.HOUR_OF_DAY);
            }
            if(orario.get(Calendar.MINUTE) < 10) {
                min = "0" + orario.get(Calendar.MINUTE);
            } else {
                min = "" + orario.get(Calendar.MINUTE);
            }
            if(orario.get(Calendar.SECOND) < 10) {
                sec = "0" + orario.get(Calendar.SECOND);
            } else {
                sec = "" + orario.get(Calendar.SECOND);
            }

            String finalOra = ora;
            String finalMin = min;
            String finalSec = sec;
            Platform.runLater(() -> etichettaOrologio.setText(String.join(" : ", finalOra, finalMin, finalSec)));

            /* GESTIONE APPUNTAMENTI */
            for(Appuntamento a: listaAppuntamenti) {
                if(a.getStato().equals(Appuntamento.Stato.DEFAULT)) {
                    if(a.getOrarioInizio().getTime() < orario.getTimeInMillis()) {
                        a.setStato(Appuntamento.Stato.ATTENZIONE);
                    }
                }
            }
            /* SLEEP di 250 per sincronizzazione */
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void aggiungiAppuntamento(Appuntamento a) {
        listaAppuntamenti.add(a);
    }

    public synchronized void rimuoviAppuntamento(Appuntamento a) {
        listaAppuntamenti.remove(a);
    }

}
