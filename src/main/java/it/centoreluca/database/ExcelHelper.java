package it.centoreluca.database;

import it.centoreluca.enumerator.Mesi;
import it.centoreluca.models.*;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelHelper {

    private final Database db = Database.getInstance();
    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private final Map<String, String[]> dati = new TreeMap<>();

    private XSSFSheet spreadsheet;

    private static String path = System.getProperty("user.dir") + "/Backup_";
    private static ExcelHelper instance = null;

    private ExcelHelper() {}

    public static ExcelHelper getInstance() {
        if(instance == null) {
            instance = new ExcelHelper();
        }
        return instance;
    }

    public boolean backupXLSX() {
        // Recupera il mese e anno corrente
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        path += Mesi.values()[calendar.get(Calendar.MONTH)] + "_" + calendar.get(Calendar.YEAR) + ".xlsx";

        // Leggo in ordine: Personale, Servizi, Clienti, Appuntamenti, Appuntamento-Servizi, Cliente-Servizi
        //                  [0]        [1]      [2]      [3]           [3]                   [4]
        Result[] res = db.backup();

        /*
        ----------------------------------------
        -------- Esportazione personale --------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("Personale");
        // Intestazione colonne
        dati.put("1", new String[]{"ID", "Nome", "Cognome", "Username", "Note"});
        // Dati
        for(int i = 0; i < res[0].getList(Personale.class).size(); i++) {
             dati.put(String.valueOf(i + 2), res[0].getList(Personale.class).get(i).backupRow());
        }
        scriviDati();

        /*
        ----------------------------------------
        --------- Esportazione servizi ---------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("Servizi");
        // Intestazione colonne
        dati.put("1", new String[]{"ID", "Nome", "Durata", "Note"});
        // Dati
        for(int i = 0; i < res[1].getList(Servizio.class).size(); i++) {
            dati.put(String.valueOf(i + 2), res[1].getList(Servizio.class).get(i).backupRow());
        }
        scriviDati();

        /*
        ----------------------------------------
        --------- Esportazione clienti ---------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("Clienti");
        // Intestazione colonne
        dati.put("1", new String[]{"ID", "Nome", "Cognome", "Data di nascita", "Num cellulare", "Num fisso", "eMail", "Colore", "Note"});
        // Dati
        for(int i = 0; i < res[2].getList(Cliente.class).size(); i++) {
            dati.put(String.valueOf(i + 2), res[2].getList(Cliente.class).get(i).backupRow());
        }
        scriviDati();

        /*
        ----------------------------------------
        ------ Esportazione appuntamenti -------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("Appuntamenti");
        // Intestazione colonne
        dati.put("1", new String[]{"ID", "Data e ora", "ID Cliente", "ID Personale", "Stato", "Note"});
        // Dati
        for(int i = 0; i < res[3].getList(Appuntamento.class).size(); i++) {
            dati.put(String.valueOf(i + 2), res[3].getList(Appuntamento.class).get(i).backupRow());
        }
        scriviDati();

        /*
        ----------------------------------------
        -------- Appuntamentio-Servizi ---------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("app-serv");
        // Intestazione colonne
        dati.put("1", new String[]{"ID Appuntamento", "ID Servizio"});
        // Dati
        Appuntamento a;
        for(int i = 0; i < res[3].getList(Appuntamento.class).size(); i++) {
            a = res[3].getList(Appuntamento.class).get(i);
            for (Servizio s : a.getServizi()) {
                dati.put(String.valueOf(i + 2), new String[]{String.valueOf(a.getId()), String.valueOf(s.getId())});
            }
        }
        scriviDati();

        /*
        ----------------------------------------
        ----------- Cliente-Servizi ------------
        ----------------------------------------
        */
        spreadsheet = workbook.createSheet("cli-serv");
        // Intestazione colonne
        dati.put("1", new String[]{"ID Cliente", "ID Servizio"});
        // Dati
        final int[] contatoreRiga = {2};
        res[4].getMap().forEach((idC, listIDS) -> {
            String[] s = new String[listIDS.size()+1];
            s[0] = String.valueOf(idC);
            int contatoreColonna = 1;
            for(String idS: listIDS) {
                s[contatoreColonna] = idS;
                contatoreColonna++;
            }
            dati.put(String.valueOf(contatoreRiga[0]), s);
            contatoreRiga[0]++;
        });

        scriviDati();

        // Scrivo l'intero documento come file
        FileOutputStream out;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);
            out.close();
            return true;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("File in uso");
            alert.setContentText("Il file potrebbe essere aperto da un'altra applicazione");
            alert.showAndWait();
        }
        return false;
    }

    private void scriviDati() {
        // Scrivo i dati nel foglio di excel
        Set<String> keyId = dati.keySet();
        int rowId = 0;
        XSSFRow row;
        for (String key : keyId) {
            row = spreadsheet.createRow(rowId++);
            String[] stringArr = dati.get(key);
            int cellId = 0;

            for (String str : stringArr) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue(str);
            }
        }
        dati.clear();
    }
}