package it.centoreluca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private final static String protocol = "jdbc:postgresql://";
    private final static String host = "192.168.1.50/";
    private final static String resource = "parrucchiere";
    private final static String url = protocol + host + resource;

    private final static String username = "cassa";
    private final static String password = "hvM7aM6VHLu7ukrjvBNDWaWxFt2fA7C3";

    private static Connection connection = null;

    public DBHelper() {}

    public Connection connetti() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("[DBHelper] credenziali database errate o database non corretto");
                e.printStackTrace();
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
