package it.centoreluca.models;

public class Personale {

    private int id;
    private String nome;
    private String cognome;
    private String username;
    private String note;

    public Personale(int id, String nome, String cognome, String username, String note) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] backupRow() {
        return new String[]{String.valueOf(id), nome, cognome, username, note};
    }

}
