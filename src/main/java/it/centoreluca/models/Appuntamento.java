package it.centoreluca.models;

import it.centoreluca.database.Database;
import it.centoreluca.util.CssHelper;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;
import java.util.List;

public class Appuntamento {

    private String nome;
    private String cognome;
    private Timestamp orario;
    private String note;
    private Stato stato;
    private List<Servizio> servizio;
    private Personale personale;

    private AnchorPane ap;
    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    public Appuntamento(String nome, String cognome, Timestamp orario, String note, Stato stato, List<Servizio> servizi, Personale personale) {
        this.nome = nome;
        this.cognome = cognome;
        this.orario = orario;
        this.note = note;
        this.stato = stato;
        this.servizio = servizi;
        this.personale = personale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Timestamp getOrario() {
        return orario;
    }

    public void setOrario(Timestamp orario) {
        this.orario = orario;
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
        db.cambiaStatoAppuntamento(this);
        css.impostaStatoAppuntamento(this.ap, this.stato);
    }

    public List<Servizio> getServizi() {
        return servizio;
    }

    public void setServizi(List<Servizio> servizio) {
        this.servizio = servizio;
    }

    public Personale getIdDipendente() {
        return personale;
    }

    public void setIdDipendente(Personale personale) {
        this.personale = personale;
    }

    public double getDurata() {
        double durata = 0;
        for(Servizio serv: servizio) {
            durata += serv.getDurata();
        }
        return durata;
    }

    public AnchorPane getPane() {
        return ap;
    }

    public void setPane(AnchorPane ap) {
        this.ap = ap;
        css.impostaStatoAppuntamento(ap, this.stato);
    }

    /**
     * Enumeratore per la gestione dello stato dell'appuntamento
     */
    public enum Stato {
        DEFAULT, ANNULLATO, ATTENZIONE, COMPLETATO
    }
}
