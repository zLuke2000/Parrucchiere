package it.centoreluca.database;

import it.centoreluca.controller.dialog.CDialog;
import it.centoreluca.util.DialogHelper;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private final static String protocol = "jdbc:postgresql://";
    private final static String host = "localhost/";
    private final static String resource = "parrucchiere";
    private final static String url = protocol + host + resource;

    private final static String username = "cassa";
    private final static String password = "hvM7aM6VHLu7ukrjvBNDWaWxFt2fA7C3";

    private final DialogHelper dh = DialogHelper.getInstance();
    private static Connection connection = null;

    public DBHelper() {}

    public Connection connetti() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                CDialog alert = (CDialog) dh.newDialog("Generico", "ATTENZIONE", null);
                alert.impostaTipo(CDialog.Tipo.ERRORE);
                alert.impostaTitolo("ERRORE DATABASE");
                alert.impostaDescrizione("Impossible stabilire una connessione con il database\n\n" + e);
                dh.display();
                Platform.exit();
                System.exit(2);
            }
        }
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }
}
