package it.centoreluca.models;

public class Personale {
    String nome;
    String cognome;
    String username;
    int id;
    String note;

    public Personale(String nome, String cognome, String username, int id, String note) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
