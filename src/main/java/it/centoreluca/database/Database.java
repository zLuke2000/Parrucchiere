package it.centoreluca.database;

import it.centoreluca.enumerator.Setting;
import it.centoreluca.models.*;

import java.sql.*;
import java.util.*;

public class Database {

    private static Database instance = null;
    private final Calendar dataDB = new GregorianCalendar();
    private final Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Result res;

    private Database() {
        conn = new DBHelper().connetti();
    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    public DatabaseMetaData getConnectionMetadata() {
        try {
            return conn.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * SEZIONE APPUNTAMENTI
     */

    public Result registraAppuntamento(Appuntamento a) {
        res = new Result(false, Result.Operation.REGISTRA_APPUNTAMENTO);
        int id = -1;
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.appuntamenti (timestamp, cliente, personale, note) " +
                    "VALUES (?, ?, ?, ?)" +
                    "RETURNING id");
            pstmt.setTimestamp(1, a.getOrarioInizio());
            pstmt.setInt(2, a.getCliente().getId());
            pstmt.setInt(3, a.getPersonale().getId());
            pstmt.setString(4, a.getNote());
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.next();
            id = rs.getInt("id");
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.VALORE_DUPLICATO);
            e.printStackTrace();
        }

        if(res.getResult() && id != -1) {
            try {
                pstmt = conn.prepareStatement("INSERT INTO associative.servizi_appuntamenti (appuntamento, servizio) " +
                        "VALUES (?, ?)");
                for (Servizio s : a.getServizi()) {
                    pstmt.setInt(1, id);
                    pstmt.setInt(2, s.getId());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                res.setResult(true);
            } catch (SQLException e) {
                res.setError(Result.Error.VALORE_DUPLICATO);
                e.printStackTrace();
            }
        }
        return res;
    }

    public Result rimuoviAppuntamento(Appuntamento a) {
        res = new Result(false, Result.Operation.RIMUOVI_APPUNTAMENTO);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                    "FROM public.appuntamenti " +
                    "WHERE id = ?");
            pstmt.setInt(1, a.getId());
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
     * @param idPersonale personale, inserire -1 per non filtrare
     * @return lista di appuntamenti
     */
    public Result leggiAppuntamenti(Calendar data, int idPersonale) {
        dataDB.setTimeInMillis(data.getTimeInMillis());
        dataDB.set(Calendar.HOUR_OF_DAY, 0);
        dataDB.set(Calendar.MINUTE, 0);
        dataDB.set(Calendar.SECOND, 0);
        dataDB.set(Calendar.MILLISECOND, 0);
        Timestamp timeFrom = new Timestamp(dataDB.getTimeInMillis());
        dataDB.add(Calendar.DAY_OF_YEAR, 1);
        Timestamp timeTo = new Timestamp(dataDB.getTimeInMillis());
        res = new Result(false, Result.Operation.LEGGI_APPUNTAMENTI_GIORNO);

        /* Controllo se esistono appuntamenti associati alla data di oggi */
        try {
            pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                    "FROM public.appuntamenti " +
                    "WHERE timestamp >= ? AND timestamp < ?");
            pstmt.setTimestamp(1, timeFrom);
            pstmt.setTimestamp(2, timeTo);
            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {

                HashMap<Integer, Servizio> mappaServizi = new HashMap<>();
                HashMap<Integer, Cliente> mappaClienti = new HashMap<>();
                HashMap<Integer, Personale> mappaPersonale = new HashMap<>();

                /* Leggo tutti i servizi */
                Result res = leggiServizi("**");
                if(res.getResult()) {
                    for(Servizio s: res.getList(Servizio.class)) {
                        mappaServizi.put(s.getId(), s);
                    }
                }

                /* Leggo tutti i clienti */
                res = leggiClienti("**");
                if(res.getResult()) {
                    for(Cliente c: res.getList(Cliente.class)) {
                        mappaClienti.put(c.getId(), c);
                    }
                }

                /* Leggo tutto il personale */
                res = leggiPersonale();
                if(res.getResult()) {
                    for(Personale p: res.getList(Personale.class)) {
                        mappaPersonale.put(p.getId(), p);
                    }
                }

                /* Leggo gli appuntamenti */
                if (idPersonale == -1) {
                    /* Leggo gli appuntamenti in un intervallo di date */
                    pstmt = conn.prepareStatement("SELECT * " +
                            "FROM public.appuntamenti " +
                            "WHERE timestamp >= ? AND timestamp < ?" +
                            "ORDER BY timestamp");
                } else {
                    /* Leggo gli appuntamenti in un intervallo di date e di un certo dipendente */
                    pstmt = conn.prepareStatement("SELECT * " +
                            "FROM public.appuntamenti " +
                            "WHERE timestamp >= ? AND timestamp < ? AND personale = ?" +
                            "ORDER BY timestamp");
                    pstmt.setInt(3, idPersonale);
                }
                pstmt.setTimestamp(1, timeFrom);
                pstmt.setTimestamp(2, timeTo);
                rs = pstmt.executeQuery();

                List<Object> listaAppuntamenti = new ArrayList<>();
                while (rs.next()) {
                    Appuntamento a = new Appuntamento(rs.getInt("id"), rs.getTimestamp("timestamp"), rs.getString("note"), Appuntamento.Stato.valueOf(rs.getString("stato")));

                    /* Imposto il cliente */
                    a.setCliente(mappaClienti.get(rs.getInt("cliente")));

                    /* Imposto il personale */
                    a.setPersonale(mappaPersonale.get(rs.getInt("personale")));

                    /* Imposto i servizi */
                    pstmt = conn.prepareStatement("SELECT servizio " +
                            "FROM associative.servizi_appuntamenti " +
                            "WHERE appuntamento = ?");
                    pstmt.setInt(1, a.getId());
                    ResultSet rs3 = pstmt.executeQuery();
                    while (rs3.next()) {
                        a.getServizi().add(mappaServizi.get(rs3.getInt("servizio")));
                    }
                    listaAppuntamenti.add(a);
                }
                res.setList(listaAppuntamenti);
                res.setResult(true);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return res;
    }

    public Result cambiaStatoAppuntamento(Appuntamento a) {
        res = new Result(false, Result.Operation.CAMBIA_STATO);
        try {
            pstmt = conn.prepareStatement("UPDATE public.appuntamenti " +
                    "SET stato = ? " +
                    "WHERE id = ?");
            pstmt.setObject(1, a.getStato(), java.sql.Types.OTHER);
            pstmt.setInt(2, a.getId());
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

    public Result registraCliente(Cliente c) {
        res = new Result(false, Result.Operation.REGISTRA_CLIENTE);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.clienti (nome, cognome, data_nascita, n_cellulare, n_fisso, email, colore, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, c.getNome());
            pstmt.setString(2, c.getCognome());
            pstmt.setDate(3, c.getDataNascita());
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

    public Result rimuoviCliente(Cliente c) {
        res = new Result(false, Result.Operation.RIMUOVI_CLIENTE);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                                              "FROM public.clienti " +
                                              "WHERE id = ?");
            pstmt.setInt(1, c.getId());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            res.setError(Result.Error.TUPLA_UTILIZZATA);
        }
        return res;
    }

    /**
     * @param text testo da cercare all'interno del nome e cognome, inserire "**" per ottenere la lista completa
     * @return result associato all'operazione
     */
    public Result leggiClienti(String text) {
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
                listaClienti.add(new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getDate("data_nascita"),
                        rs.getString("n_cellulare"), rs.getString("n_fisso"), rs.getString("email"), rs.getString("colore"), rs.getString("note")));
            }
            res.setList(listaClienti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result modificaCampiCliente(Cliente c) {
        res = new Result(false, Result.Operation.MODIFICA_CAMPI_CLIENTE);
        try {
            pstmt = conn.prepareStatement("UPDATE public.clienti " +
                    "SET data_nascita = ?, n_cellulare = ?, n_fisso = ?, email = ?, note = ? " +
                    "WHERE id = ?");
            pstmt.setDate(1, c.getDataNascita());
            pstmt.setString(2, c.getNumeroCellulare());
            pstmt.setString(3, c.getNumeroFisso());
            pstmt.setString(4, c.getEmail());
            pstmt.setString(5, c.getNote());
            pstmt.setInt(6, c.getId());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.ERRORE);
            e.printStackTrace();
        }
        return res;
    }

    public Result modificaColoreCliente(Cliente c) {
        res = new Result(false, Result.Operation.MODIFICA_CAMPI_CLIENTE);
        try {
            pstmt = conn.prepareStatement("UPDATE public.clienti " +
                    "SET colore = ? " +
                    "WHERE id = ?");
            pstmt.setString(1, c.getColore());
            pstmt.setInt(2, c.getId());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.ERRORE);
            e.printStackTrace();
        }
        return res;
    }

    public synchronized Result controllaCompleanno(Calendar data) {
        Result res = new Result(false, Result.Operation.CONTROLLO_COMPLEANNO);
        try {
            pstmt = conn.prepareStatement("SELECT nome, cognome " +
                    "FROM clienti " +
                    "WHERE date_part('day', data_nascita) = ? AND date_part('month', data_nascita) = ?");
            pstmt.setInt(1, data.get(Calendar.DAY_OF_MONTH));
            pstmt.setInt(2, data.get(Calendar.MONTH)+1);
            rs = pstmt.executeQuery();

            List<Object> listaCompleanni = new ArrayList<>();
            while(rs.next()) {
                listaCompleanni.add(new Cliente(rs.getString("nome"), rs.getString("cognome")));
                res.setResult(true);
            }
            res.setList(listaCompleanni);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * SEZIONE PERSONALE
     */

    public Result registraPersonale(Personale p) {
        res = new Result(false, Result.Operation.REGISTRA_PERSONALE);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.personale (nome, cognome, username, note) " +
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

    public Result rimuoviPersonale(Personale p) {
        res = new Result(false, Result.Operation.RIMUOVI_PERSONALE);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                                              "FROM public.personale " +
                                              "WHERE id = ?");
            pstmt.setInt(1, p.getId());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            res.setError(Result.Error.TUPLA_UTILIZZATA);
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
                listaDipendenti.add(new Personale(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("username"), rs.getString("note")));
            }
            res.setList(listaDipendenti);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result modificaCampiPersonale(Personale p) {
        res = new Result(false, Result.Operation.MODIFICA_CAMPI_PERSONALE);
        try {
            pstmt = conn.prepareStatement("UPDATE public.personale " +
                    "SET note = ?, username = ? " +
                    "WHERE id = ?");
            pstmt.setString(1, p.getNote());
            pstmt.setString(2, p.getUsername());
            pstmt.setInt(3, p.getId());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.ERRORE);
            e.printStackTrace();
        }
        return res;
    }

    /*
     * SEZIONE SERVIZI
     */

    public Result registraServizio(Servizio s) {
        res = new Result(false, Result.Operation.REGISTRA_SERVIZIO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.servizi (nome, durata, note) " +
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

    public Result rimuoviServizio(Servizio s) {
        res = new Result(false, Result.Operation.RIMUOVI_SERVIZIO);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                                              "FROM public.servizi " +
                                              "WHERE id = ?");
            pstmt.setInt(1, s.getId());
            if(pstmt.executeUpdate() == 1) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            res.setError(Result.Error.TUPLA_UTILIZZATA);
        }
        return res;
    }

    public Result leggiServizi(String text) {
        res = new Result(false, Result.Operation.LEGGI_SERVIZI);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.servizi " +
                    "WHERE nome ILIKE ? " +
                    "ORDER BY nome");
            if(text.equals("**")) {
                pstmt.setString(1, "%");
            } else {
                pstmt.setString(1, "%" + text + "%");
            }
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

    public Result leggiIdServPref(int idCliente) {
        res = new Result(false, Result.Operation.LEGGI_SERVIZI);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM associative.servizi_preferiti " +
                    "WHERE cliente = ? " +
                    "ORDER BY servizio");
            pstmt.setInt(1, idCliente);
            rs = pstmt.executeQuery();

            List<Object> listaIdServizi = new ArrayList<>();
            while(rs.next()) {
                listaIdServizi.add(rs.getInt("servizio"));
            }
            res.setList(listaIdServizi);
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result leggiServPref(int idCliente) {
        res = new Result(false, Result.Operation.LEGGI_SERVIZI);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM associative.vista_sp " +
                    "WHERE cliente = ? " +
                    "ORDER BY id");
            pstmt.setInt(1, idCliente);
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

    public Result aggiornaServPref(int idCliente, List<Integer> idServizi) {
        res = new Result(false, Result.Operation.LEGGI_SERVIZI);
        try {
            pstmt = conn.prepareStatement("DELETE " +
                    "FROM associative.servizi_preferiti " +
                    "WHERE cliente = ? ");
            pstmt.setInt(1, idCliente);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("INSERT INTO associative.servizi_preferiti " +
                    "VALUES (?, ?)");

            for(Integer i: idServizi) {
                pstmt.setInt(1, idCliente);
                pstmt.setInt(2, i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            res.setResult(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Result modificaCampiServizio(Servizio s) {
        res = new Result(false, Result.Operation.MODIFICA_CAMPI_SERVIZIO);
        try {
            pstmt = conn.prepareStatement("UPDATE public.servizi " +
                    "SET note = ? " +
                    "WHERE id = ?");
            pstmt.setString(1, s.getNote());
            pstmt.setInt(2, s.getId());
            pstmt.executeUpdate();
            res.setResult(true);
        } catch (SQLException e) {
            res.setError(Result.Error.ERRORE);
            e.printStackTrace();
        }
        return res;
    }

    /*
     * SEZIONE ASSOCIATI PER RIMOZIONE
     */

    public Result rimuoviAppuntamentiAssociati(Personale p) {
        res = new Result(false, Result.Operation.RIMUOVI_APPUNTAMENTI_ASSOCIATI);

        try {
            // Rimuovo gli appuntamenti del personale
            pstmt = conn.prepareStatement("DELETE FROM public.appuntamenti " +
                    "WHERE personale = ? ");
            pstmt.setInt(1, p.getId());
            pstmt.executeUpdate();
            // Rimuovo il servizio
            if(rimuoviPersonale(p).getResult()) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setError(Result.Error.ERRORE);
        }
        return res;
    }

    public Result rimuoviClientiAssociati(Cliente c) {
        res = new Result(false, Result.Operation.RIMUOVI_APPUNTAMENTI_ASSOCIATI);

        try {
            // Rimuovo i servizi preferiti del cliente
            pstmt = conn.prepareStatement("DELETE FROM associative.servizi_preferiti " +
                    "WHERE cliente = ? ");
            pstmt.setInt(1, c.getId());
            pstmt.executeUpdate();
            // Rimuovo gli appuntamenti del cliente
            pstmt = conn.prepareStatement("DELETE FROM public.appuntamenti " +
                    "WHERE cliente = ? ");
            pstmt.setInt(1, c.getId());
            pstmt.executeUpdate();
            // Rimuovo il cliente
            if(rimuoviCliente(c).getResult()) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setError(Result.Error.ERRORE);
        }
        return res;
    }

    public Result rimuoviServiziAssociati(Servizio s) {
        res = new Result(false, Result.Operation.RIMUOVI_SERVIZI_ASSOCIATI);

        try {
            // Rimuovo i servizi preferiti associati
            pstmt = conn.prepareStatement("DELETE FROM associative.servizi_preferiti " +
                    "WHERE servizio = ? ");
            pstmt.setInt(1, s.getId());
            pstmt.executeUpdate();
            // Rimuovo il servizio dagli appuntamenti associati
            pstmt = conn.prepareStatement("DELETE FROM associative.servizi_appuntamenti " +
                    "WHERE servizio = ? ");
            pstmt.setInt(1, s.getId());
            pstmt.executeUpdate();
            // Rimuovo il servizio
            if(rimuoviServizio(s).getResult()) {
                res.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setError(Result.Error.ERRORE);
        }
        return res;
    }

    /*
     * SEZIONE BACKUP
     */

    public Result[] backup() {
        Result resS;
        Result resC;
        Result resP;
        Result resA = new Result(false, Result.Operation.APPUNTAMENTI_BACKUP);
        Result resCS = new Result(false, Result.Operation.CLI_SER_BACKUP);

        try {
            HashMap<Integer, Servizio> mappaServizi = new HashMap<>();
            HashMap<Integer, Cliente> mappaClienti = new HashMap<>();
            HashMap<Integer, Personale> mappaPersonale = new HashMap<>();
            HashMap<Integer, List<String>> mappaCS = new HashMap<>();

            /* Leggo tutti i servizi */
            resS = leggiServizi("**");
            resS.setOpType(Result.Operation.SERVIZI_BACKUP);
            if(resS.getResult()) {
                for(Servizio s: resS.getList(Servizio.class)) {
                    mappaServizi.put(s.getId(), s);
                }
            }

            /* Leggo tutti i clienti */
            resC = leggiClienti("**");
            resC.setOpType(Result.Operation.CLIENTI_BACKUP);
            if(resC.getResult()) {
                for(Cliente c: resC.getList(Cliente.class)) {
                    mappaClienti.put(c.getId(), c);
                }
            }

            /* Leggo tutto il personale */
            resP = leggiPersonale();
            resP.setOpType(Result.Operation.PERSONALE_BACKUP);
            if(resP.getResult()) {
                for(Personale p: resP.getList(Personale.class)) {
                    mappaPersonale.put(p.getId(), p);
                }
            }

            /* Leggo gli appuntamenti */
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.appuntamenti " +
                    "ORDER BY id");
            rs = pstmt.executeQuery();

            List<Object> listaAppuntamenti = new ArrayList<>();
            while (rs.next()) {
                Appuntamento a = new Appuntamento(rs.getInt("id"), rs.getTimestamp("timestamp"), rs.getString("note"), Appuntamento.Stato.valueOf(rs.getString("stato")));

                /* Imposto il cliente */
                a.setCliente(mappaClienti.get(rs.getInt("cliente")));

                /* Imposto il personale */
                a.setPersonale(mappaPersonale.get(rs.getInt("personale")));

                /* Imposto i servizi */
                pstmt = conn.prepareStatement("SELECT servizio " +
                        "FROM associative.servizi_appuntamenti " +
                        "WHERE appuntamento = ?");
                pstmt.setInt(1, a.getId());
                ResultSet rs3 = pstmt.executeQuery();
                while (rs3.next()) {
                    a.getServizi().add(mappaServizi.get(rs3.getInt("servizio")));
                }
                listaAppuntamenti.add(a);
            }
            resA.setList(listaAppuntamenti);
            resA.setResult(true);

            /* Leggo i servizi preferiti per ciascun cliente */
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM associative.servizi_preferiti");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if(mappaCS.get(rs.getInt("cliente")) != null) {
                    mappaCS.get(rs.getInt("cliente")).add(String.valueOf(rs.getInt("servizio")));
                } else {
                    List<String> lista = new ArrayList<>();
                    lista.add(String.valueOf((rs.getInt("servizio"))));
                    mappaCS.put(rs.getInt("cliente"), lista);
                }
            }
            resCS.setMap(mappaCS);
            return new Result[]{resP, resS, resC, resA, resCS};
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * SEZIONE IMPOSTAZIONI
     */

    public boolean leggiImpostazioni(HashMap<Setting, String> currentSetting, HashMap<Setting, String> defaultSetting) {
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM util.impostazioni ");
            rs = pstmt.executeQuery();

            currentSetting.clear();
            defaultSetting.clear();
            while(rs.next()) {
                currentSetting.put(Setting.valueOf(rs.getString("nome")), rs.getString("current"));
                defaultSetting.put(Setting.valueOf(rs.getString("nome")), rs.getString("default"));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean aggiornaImpostazione(Setting nome, String value) {
        try {
            pstmt = conn.prepareStatement("UPDATE util.impostazioni " +
                    "SET current = ? " +
                    "WHERE nome = ? ");
            pstmt.setString(1, value);
            pstmt.setString(2, String.valueOf(nome));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
