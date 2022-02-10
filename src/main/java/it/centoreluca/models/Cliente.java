package it.centoreluca.models;

public class Cliente {

    private String nome;
    private String cognome;
    private String note;

    public Cliente(String nome, String cognome, String note) {
        this.nome = nome;
        this.cognome = cognome;
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
