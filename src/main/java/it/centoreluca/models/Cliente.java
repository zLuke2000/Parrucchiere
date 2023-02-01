package it.centoreluca.models;

import java.sql.Date;

public class Cliente {

    private int id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String numeroCellulare = "";
    private String numeroFisso = "";
    private String email = "";
    private String colore = "";
    private String note = "";

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
    public Cliente(int id, String nome, String cognome, Date dataNascita, String numeroCellulare, String numeroFisso, String email, String colore, String note) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.numeroCellulare = numeroCellulare;
        this.numeroFisso = numeroFisso;
        this.email = email;
        this.colore = colore;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
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

    public String[] backupRow() {
        return new String[]{String.valueOf(id), nome, cognome, String.valueOf(dataNascita), numeroCellulare, numeroFisso, email, colore, note};
    }
}
