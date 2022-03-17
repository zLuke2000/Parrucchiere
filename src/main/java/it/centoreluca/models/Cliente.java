package it.centoreluca.models;

import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;

public class Cliente {

    private String nome;
    private String cognome;
    private Timestamp dataNascita;
    private String numeroCellulare;
    private String numeroFisso;
    private String email;

    private String colore;
    private String note;

    private AnchorPane ap;

    public Cliente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Costruttore completo per la classe cliente, utile per la lettura completa da DB
     * @param nome nome
     * @param cognome cognome
     * @param dataNascita data di nascita
     * @param numeroCellulare numero di telefono cellulare
     * @param numeroFisso numero di telefono fisso
     * @param email email
     * @param colore colore attuale applicato al cliente
     * @param note note
     */
    public Cliente(String nome, String cognome, Timestamp dataNascita, String numeroCellulare, String numeroFisso, String email, String colore, String note) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.numeroCellulare = numeroCellulare;
        this.numeroFisso = numeroFisso;
        this.email = email;
        this.colore = colore;
        this.note = note;
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

    public Timestamp getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Timestamp dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getNumeroCellulare() {
        return numeroCellulare;
    }

    public void setNumeroCellulare(String numeroCellulare) {
        this.numeroCellulare = numeroCellulare;
    }

    public String getNumeroFisso() {
        return numeroFisso;
    }

    public void setNumeroFisso(String numeroFisso) {
        this.numeroFisso = numeroFisso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return nome + ' ' + cognome;
    }

}
