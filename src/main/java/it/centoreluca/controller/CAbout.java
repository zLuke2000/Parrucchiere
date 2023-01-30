package it.centoreluca.controller;

import it.centoreluca.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.JSONArray;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CAbout {
    public Label l_autore;
    public Label l_versioneCorrente;
    public Label l_gitHub;
    public Label l_ultimaVersione;
    public Label l_connessioneDatabase;

    private final Database db = Database.getInstance();
    private final String gitHub = "https://github.com/zLuke2000/Parrucchiere";
    private String latestVersion;
    public Label l_linkNuovaVersione;

    @FXML
    private void initialize() {
        l_autore.setText("Centore Luca");
        String currentVersion = "v1.0.1";
        l_versioneCorrente.setText(currentVersion);
        l_gitHub.setText(gitHub);

        Queue<String> tags = new LinkedList<>();
        try {
            JSONArray ja = new JSONArray(new Scanner(new URL("https://api.github.com/repos/zLuke2000/Parrucchiere/tags").openStream()).next());
            for(int i=0; i<ja.length(); i++) {
                tags.add(ja.query("/" + i + "/name").toString());
            }
            latestVersion = tags.peek();
            l_ultimaVersione.setText(latestVersion);
            if(!currentVersion.equals(latestVersion)) {
                l_linkNuovaVersione.setDisable(false);
                l_linkNuovaVersione.setVisible(true);
            }
        } catch (IOException e) {
            l_ultimaVersione.setText("connessione internet non presente");
        }

        DatabaseMetaData dbmd = db.getConnectionMetadata();
        try {
            l_connessioneDatabase.setText(dbmd.getURL() + " - " + dbmd.getUserName());
        } catch (SQLException e) {
            l_connessioneDatabase.setText("Nessuna connessione al database");
        }
    }

    @FXML
    private void apriGitHub() {
        try {
            Desktop.getDesktop().browse(new URL(gitHub).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void apriDownload() {
        try {
            Desktop.getDesktop().browse(new URL(gitHub + "/releases/tag/" + latestVersion).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}