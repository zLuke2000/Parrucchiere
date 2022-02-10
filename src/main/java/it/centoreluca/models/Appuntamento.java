package it.centoreluca.models;

import it.centoreluca.database.Database;
import it.centoreluca.util.CssHelper;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;

public class Appuntamento {

    private String nome;
    private String cognome;
    private Timestamp orario;
    private String note;
    private Stato stato;

    private AnchorPane ap;
    private final Database db = Database.getInstance();
    private final CssHelper css = CssHelper.getInstance();

    public Appuntamento(String nome, String cognome, Timestamp orario, String note, Stato stato) {
        this.nome = nome;
        this.cognome = cognome;
        this.orario = orario;
        this.note = note;
        this.stato = stato;
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
        db.cambiaStato(this);
        css.impostaStatoAppuntamento(this.ap, this.stato);
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
