package it.centoreluca.models;

public class Servizio {

    int id;
    String nome;
    int durata;
    String note;

    public Servizio(int id, String nome, int durata, String note) {
        this.id = id;
        this.nome = nome;
        this.durata = durata;
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

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStringaDurata() {

        if(durata == 0) {
            return "non specificata";
        }

        int ore = durata/60;
        int minuti = durata%60;
        String testo = "";

        if(ore == 1) {
            testo += ore + " ora";
        } else if(ore > 1) {
            testo += ore + " ore";
        }

        if(ore != 0 & minuti != 0) {
            testo += " e ";
        }

        if(minuti == 1) {
            testo += minuti + " minuto";
        } else if(minuti > 0) {
            testo += minuti + " minuti";
        }

        return testo;
    }
}
