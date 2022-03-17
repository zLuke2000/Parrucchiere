package it.centoreluca.database;

import it.centoreluca.models.*;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

public class Database {

    private static Database instance = null;
    private final DBHelper dbh = new DBHelper();
    private final Calendar dataDB = new GregorianCalendar();
    private final Connection conn;
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

    /*
     * SEZIONE APPUNTAMENTI
     */

    public Result registraAppuntamento(@NotNull Appuntamento a) {
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

    public Result rimuoviAppuntamento(@NotNull Appuntamento a) {
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

    /**
     * Metodo lettura appuntamenti
     * @param data data per calcolare l'intervallo
     * @param personale personale, inserire -1 per non filtrare
     * @return lista di appuntamenti
     */
    public Result leggiAppuntamenti(@NotNull Calendar data, int personale) {
        dataDB.setTimeInMillis(data.getTimeInMillis());
        res = new Result(false, Result.Operation.LEGGI_APPUNTAMENTI_GIORNO);

        /* Leggo tutti i servizi */
        HashMap<Integer, Servizio> mappaServizi = new HashMap<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.servizi " +
                    "ORDER BY id");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Servizio s = new Servizio(rs.getInt("id"), rs.getString("nome"), rs.getInt("durata"), rs.getString("note"));
                mappaServizi.put(rs.getInt("id"), s);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        /* Leggo tutti il personale */
        HashMap<Integer, Personale> mappaPersonale = new HashMap<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.personale " +
                    "ORDER BY id");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Personale d = new Personale(rs.getString("nome"), rs.getString("cognome"), rs.getString("nickname"), rs.getInt("id"), rs.getString("note"));
                mappaPersonale.put(rs.getInt("id"), d);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        /* Leggo gli appuntamenti */
        try {
            if (personale == -1) {
                /* Leggo gli appuntamenti in un intervallo di date */
                pstmt = conn.prepareStatement("SELECT * " +
                        "FROM public.appuntamenti " +
                        "WHERE data >= ? AND data < ?" +
                        "ORDER BY data");
            } else {
                /* Leggo gli appuntamenti in un intervallo di date e di un certo dipendente */
                pstmt = conn.prepareStatement("SELECT * " +
                        "FROM public.appuntamenti " +
                        "WHERE data >= ? AND data < ? AND dipendente = ?" +
                        "ORDER BY data");
                pstmt.setInt(3, personale);
            }

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
                Appuntamento a = new Appuntamento(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getTimestamp("data"),
                        rs.getString("note"),
                        Appuntamento.Stato.valueOf(rs.getString("stato")),
                        null,
                        mappaPersonale.get(rs.getInt("personale")));
                Integer[] arrayServizi = (Integer[]) rs.getArray("servizi").getArray();
                for(int i = 0; i < arrayServizi.length-1; i++) {
                    a.getServizi().add(mappaServizi.get(arrayServizi[i]));
                }
                listaAppuntamenti.add(a);
            }
            res.setList(listaAppuntamenti);
            res.setResult(true);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return res;
    }

    public Result cambiaStatoAppuntamento(@NotNull Appuntamento a) {
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

    /*
     * SEZIONE CLIENTI
     */

    public Result registraCliente(@NotNull Cliente c) {
        res = new Result(false, Result.Operation.REGISTRA_CLIENTE);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.clienti(nome, cognome, data_nascita, numero_cellulare, numero_fisso, email, colore, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, c.getNome());
            pstmt.setString(2, c.getCognome());
            pstmt.setTimestamp(3, c.getDataNascita());
            pstmt.setString(4, c.getNumeroCellulare());
            pstmt.setString(5, c.getNumeroFisso());
            pstmt.setString(6, c.getEmail());
            pstmt.setString(7, c.getColore());
            pstmt.setString(8, c.getNote());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.VALORE_DUPLICATO);
        }
        return res;
    }

    public Result rimuoviCliente(@NotNull Cliente c) {
        res = new Result(false, Result.Operation.RIMUOVI_CLIENTE);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                    "FROM public.clienti " +
                    "WHERE nome = ? AND cognome = ?");
            pstmt.setString(1, c.getNome());
            pstmt.setString(2, c.getCognome());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiClienti(@NotNull String text) {
        res = new Result(false, Result.Operation.LEGGI_CLIENTI);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.clienti " +
                    "WHERE nome ILIKE ? OR cognome ILIKE ?" +
                    "ORDER BY cognome ");
            if(text.equals("**")) {
                pstmt.setString(1, "%");
                pstmt.setString(2, "%");
            } else {
                pstmt.setString(1, "%" + text + "%");
                pstmt.setString(2, "%" + text + "%");
            }
            rs = pstmt.executeQuery();

            List<Object> listaClienti = new ArrayList<>();
            while(rs.next()) {
                listaClienti.add(new Cliente(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getTimestamp("data_nascita"),
                        rs.getString("numero_cellulare"),
                        rs.getString("numero_fisso"),
                        rs.getString("email"),
                        rs.getString("colore"),
                        rs.getString("note")));
            }
            res.setList(listaClienti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Metodo lettura dei clienti che hanno registrati piu appuntamenti
     */
    public Result leggiClientiFrequenti(int limite) {

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
                listaRecenti.add(new Cliente(rs.getString("nome"), rs.getString("cognome")));
            }
            res.setList(listaRecenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result modificaCampiCliente(@NotNull Cliente c) {
        res = new Result(false, Result.Operation.REGISTRA_CAMPI_CLIENTE);
        try {
            pstmt = conn.prepareStatement("UPDATE public.clienti" +
                    "SET data_nascita = ?, numero_cellulare = ?, numero_fisso = ?, email = ?, colore = ?, note = ?" +
                    "WHERE nome = ? AND cognome  = ?");
            pstmt.setTimestamp(1, c.getDataNascita());
            pstmt.setString(2, c.getNumeroCellulare());
            pstmt.setString(3, c.getNumeroFisso());
            pstmt.setString(4, c.getEmail());
            pstmt.setString(5, c.getColore());
            pstmt.setString(6, c.getNote());
            pstmt.setString(7, c.getNome());
            pstmt.setString(8, c.getCognome());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
//            res.setError(Result.Error.ERRORE);
            e.printStackTrace();
        }
        return res;
    }

    /*
     * SEZIONE PERSONALE
     */

    public Result registraPersonale(@NotNull Personale p) {
        res = new Result(false, Result.Operation.REGISTRA_PERSONALE);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.personale(nome, cognome, nickname, note) " +
                    "VALUES (?, ?, ?, ?)");
            pstmt.setString(1, p.getNome());
            pstmt.setString(2, p.getCognome());
            pstmt.setString(3, p.getUsername());
            pstmt.setString(4, p.getNote());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.VALORE_DUPLICATO);
            e.printStackTrace();
        }
        return res;
    }

    public Result rimuoviPersonale(@NotNull Personale p) {
        res = new Result(false, Result.Operation.RIMUOVI_PERSONALE);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                    "FROM public.personale " +
                    "WHERE nome = ? AND cognome = ? AND nickname = ?");
            pstmt.setString(1, p.getNome());
            pstmt.setString(2, p.getCognome());
            pstmt.setString(3, p.getUsername());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiPersonale() {
        res = new Result(false, Result.Operation.LEGGI_PERSONALE);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                                              "FROM public.personale");
            rs = pstmt.executeQuery();

            List<Object> listaDipendenti = new ArrayList<>();
            while(rs.next()) {
                listaDipendenti.add(new Personale(rs.getString("nome"), rs.getString("cognome"), rs.getString("nickname"), rs.getInt("id"), rs.getString("note")));
            }
            res.setList(listaDipendenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * SEZIONE SERVIZI
     */

    public Result registraServizio(@NotNull Servizio s) {
        res = new Result(false, Result.Operation.REGISTRA_SERVIZIO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.servizi(nome, durata, note)" +
                    "VALUES (?, ?, ?)");
            pstmt.setString(1, s.getNome());
            pstmt.setDouble(2, s.getDurata());
            pstmt.setString(3, s.getNote());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.VALORE_DUPLICATO);
            e.printStackTrace();
        }
        return res;
    }

    public Result rimuoviServizio(@NotNull Servizio s) {
        res = new Result(false, Result.Operation.RIMUOVI_SERVIZIO);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                    "FROM public.servizi " +
                    "WHERE id = ? AND nome = ? AND durata = ?");
            pstmt.setInt(1, s.getId());
            pstmt.setString(2, s.getNome());
            pstmt.setInt(3, s.getDurata());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiServizi(String text) {
        res = new Result(false, Result.Operation.LEGGI_SERVIZI);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.servizi " +
                    "WHERE nome ILIKE ?" +
                    "ORDER BY nome ");
            pstmt.setString(1, "%" + text + "%");
            rs = pstmt.executeQuery();

            List<Object> listaServizi = new ArrayList<>();
            while(rs.next()) {
                listaServizi.add(new Servizio(rs.getInt("id"), rs.getString("nome"), rs.getInt("durata"), rs.getString("note")));
            }
            res.setList(listaServizi);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
