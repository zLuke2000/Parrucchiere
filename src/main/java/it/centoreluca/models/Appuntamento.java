package it.centoreluca.models;

import it.centoreluca.database.Database;
import it.centoreluca.util.CssHelper;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Appuntamento {

    private int id;
    private Timestamp orario;
    private Cliente cliente;
    private Personale personale;
    private List<Servizio> servizi = new ArrayList<>();
    private String note;
    private Stato stato;

    private AnchorPane ap;

    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    public Appuntamento() { }

    public Appuntamento(int id, Timestamp orario, String note, Stato stato) {
        this.id = id;
        this.orario = orario;
        this.note = note;
        this.stato = stato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getOrarioInizio() {
        return orario;
    }

    public void setOrarioInizio(Timestamp orario) {
        this.orario = orario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Personale getPersonale() {
        return personale;
    }

    public void setPersonale(Personale personale) {
        this.personale = personale;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
        System.out.println(stato);
        db.cambiaStatoAppuntamento(this);
        css.impostaStatoAppuntamento(this.ap, this.stato);
    }

    public List<Servizio> getServizi() {
        return servizi;
    }

    public void setServizi(List<Servizio> servizio) {
        this.servizi = servizio;
    }

    public AnchorPane getPane() {
        return ap;
    }

    public void setPane(AnchorPane ap) {
        this.ap = ap;
        css.impostaStatoAppuntamento(ap, this.stato);
    }

    public int getDurata() {
        int durata = 0;
        for(Servizio serv: servizi) {
            durata += serv.getDurata();
        }
        return durata;
    }

    public Calendar getOrarioFine() {
        Calendar ora = Calendar.getInstance();
        ora.setTimeInMillis(orario.getTime());
        ora.add(Calendar.MINUTE, getDurata());
        return ora;
    }

    public String[] backupRow() {
        return new String[]{String.valueOf(id), orario.toString(), String.valueOf(cliente.getId()), String.valueOf(personale.getId()), stato.toString(), note};
    }

    /**
     * Enumeratore per la gestione dello stato dell'appuntamento
     */
    public enum Stato {
        DEFAULT, ANNULLATO, ATTENZIONE, COMPLETATO
    }
}
