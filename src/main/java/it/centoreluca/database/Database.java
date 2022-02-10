package it.centoreluca.database;

import it.centoreluca.models.Appuntamento;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Database {

    private static Database instance = null;
    private final DBHelper dbh = new DBHelper();
    private final Calendar dataDB = new GregorianCalendar();
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Result res;

    private Database() {
        conn = dbh.connetti();
    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    public Result registraAppuntamento(Appuntamento a) {
        res = new Result(false, Result.Operation.REGISTRA_APPUNTAMENTO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.appuntamenti " +
                                              "VALUES (?, ?, ?, ?, ?)");
            pstmt.setTimestamp(1, a.getOrario());
            pstmt.setString(2, a.getNome());
            pstmt.setString(3, a.getCognome());
            pstmt.setString(4, a.getNote());
            pstmt.setString(5, String.valueOf(a.getStato()));
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.VALORE_DUPLICATO);
            //e.printStackTrace();
        }
        return res;
    }

    public Result leggiAppuntamenti(Calendar data) {
        dataDB.setTimeInMillis(data.getTimeInMillis());
        res = new Result(false, Result.Operation.LEGGI_APPUNTAMENTI_GIORNO);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                                              "FROM public.appuntamenti " +
                                              "WHERE data >= ? AND data < ?" +
                                              "ORDER BY data");

            dataDB.set(Calendar.HOUR_OF_DAY, 0);
            dataDB.set(Calendar.MINUTE, 0);
            dataDB.set(Calendar.SECOND, 0);
            dataDB.set(Calendar.MILLISECOND, 0);
            pstmt.setTimestamp(1, new Timestamp(dataDB.getTimeInMillis()));
            dataDB.add(Calendar.DAY_OF_YEAR, 1);
            pstmt.setTimestamp(2, new Timestamp(dataDB.getTimeInMillis()));
            rs = pstmt.executeQuery();

            List<Object> listaAppuntamenti = new ArrayList<>();
            while(rs.next()) {
                listaAppuntamenti.add(new Appuntamento(rs.getString("nome"), rs.getString("cognome"), rs.getTimestamp("data"), rs.getString("note"), Appuntamento.Stato.valueOf(rs.getString("stato"))));
            }
            res.setList(listaAppuntamenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiClienti() {

        res = new Result(false, Result.Operation.LEGGI_CLIENTI_RECENTI);
        try {
            pstmt = conn.prepareStatement("SELECT nome, cognome, note " +
                                              "FROM public.clienti " +
                                              "ORDER BY cognome ");
            rs = pstmt.executeQuery();

            List<Object> listaRecenti = new ArrayList<>();
            while(rs.next()) {
                listaRecenti.add(new Cliente(rs.getString("nome"), rs.getString("cognome"), rs.getString("note")));
            }
            res.setList(listaRecenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiRecenti(int limite) {

        res = new Result(false, Result.Operation.LEGGI_CLIENTI_RECENTI);
        try {
            pstmt = conn.prepareStatement("SELECT nome, cognome, count(nome) " +
                                              "FROM public.appuntamenti " +
                                              "GROUP BY nome, cognome " +
                                              "ORDER BY count(nome) DESC " +
                                              "LIMIT ?");
            pstmt.setInt(1, limite);
            rs = pstmt.executeQuery();

            List<Object> listaRecenti = new ArrayList<>();
            while(rs.next()) {
                listaRecenti.add(new Cliente(rs.getString("nome"), rs.getString("cognome"), null));
            }
            res.setList(listaRecenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result rimuoviAppuntamento(Appuntamento a) {
        res = new Result(false, Result.Operation.RIMUOVI_APPUNTAMENTO);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                                              "FROM public.appuntamenti " +
                                              "WHERE data = ? AND nome = ? AND cognome = ? AND note = ? ");
            pstmt.setTimestamp(1, a.getOrario());
            pstmt.setString(2, a.getNome());
            pstmt.setString(3, a.getCognome());
            pstmt.setString(4, a.getNote());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result cambiaStato(Appuntamento a) {
        res = new Result(false, Result.Operation.CAMBIA_STATO);
        try {
            pstmt = conn.prepareStatement("UPDATE public.appuntamenti " +
                                              "SET stato = ?" +
                                              "WHERE data = ? AND nome = ? AND cognome = ? AND note = ? ");
            pstmt.setString(1, String.valueOf(a.getStato()));
            pstmt.setTimestamp(2, a.getOrario());
            pstmt.setString(3, a.getNome());
            pstmt.setString(4, a.getCognome());
            pstmt.setString(5, a.getNote());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
