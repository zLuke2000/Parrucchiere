package it.centoreluca.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Result {

    private List<Object> list;
    private HashMap<Integer, List<String>> map;
    private Operation opType;
    private Error errorType;
    private boolean result;

    public Result(boolean result, Operation opType) {
        this.result = result;
        this.opType = opType;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public <T> List<T> getList(Class<T> customClass) {
        List<T> lista = new ArrayList<>();
        for(Object o: list) {
            lista.add(((T) o));
        }
        return lista;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public HashMap<Integer, List<String>> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, List<String>> map) {
        this.map = map;
    }

    public Operation getOpType() {
        return opType;
    }

    public void setOpType(Operation opType) {
        this.opType = opType;
    }

    public Error getError() {
        return errorType;
    }

    public void setError(Error errorType) {
        this.errorType = errorType;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public enum Operation {
        /* APPUNTAMENTO */
        REGISTRA_APPUNTAMENTO, LEGGI_APPUNTAMENTI_GIORNO, RIMUOVI_APPUNTAMENTO, CAMBIA_STATO,
        /* CLIENTE */
        LEGGI_CLIENTI, RIMUOVI_CLIENTE, REGISTRA_CLIENTE, MODIFICA_CAMPI_CLIENTE, LEGGI_CLIENTI_RECENTI,
        /* PERSONALE */
        LEGGI_PERSONALE, REGISTRA_PERSONALE, RIMUOVI_PERSONALE, MODIFICA_CAMPI_PERSONALE,
        /* SERVIZIO */
        REGISTRA_SERVIZIO, LEGGI_SERVIZI, RIMUOVI_SERVIZIO, MODIFICA_CAMPI_SERVIZIO,
        /* BACKUP */
        APPUNTAMENTI_BACKUP, APP_SER_BACKUP, CLI_SER_BACKUP, SERVIZI_BACKUP, PERSONALE_BACKUP, RIMUOVI_SERVIZI_ASSOCIATI, RIMUOVI_APPUNTAMENTI_ASSOCIATI, CONTROLLO_COMPLEANNO, CLIENTI_BACKUP
    }

    public enum Error {
        ERRORE, VALORE_DUPLICATO, TUPLA_UTILIZZATA
    }
}
