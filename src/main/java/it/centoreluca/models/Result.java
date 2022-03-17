package it.centoreluca.models;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<Object> list;
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
        REGISTRA_APPUNTAMENTO, LEGGI_APPUNTAMENTI_GIORNO, RIMUOVI_APPUNTAMENTO, CAMBIA_STATO, LEGGI_CLIENTI, RIMUOVI_CLIENTE, REGISTRA_CLIENTE, REGISTRA_CAMPI_CLIENTE, LEGGI_PERSONALE, REGISTRA_SERVIZIO, LEGGI_SERVIZI, REGISTRA_PERSONALE, RIMUOVI_PERSONALE, RIMUOVI_SERVIZIO, LEGGI_CLIENTI_RECENTI
    }

    public enum Error {
        VALORE_DUPLICATO

    }
}
