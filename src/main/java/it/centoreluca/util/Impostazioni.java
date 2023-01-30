package it.centoreluca.util;

import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Setting;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;

public class Impostazioni {

    private static Impostazioni instance = null;

    private AnchorPane ap_root;
    private final Database db = Database.getInstance();
    public static final HashMap<Setting, String> currentSetting = new HashMap<>();
    public static final HashMap<Setting, String> defaultSetting = new HashMap<>();

    private Impostazioni() {
        db.leggiImpostazioni(currentSetting, defaultSetting);
    }

    public static Impostazioni getInstance(){
        if(instance == null){
            instance = new Impostazioni();
        }
        return instance;
    }

    public void impostaAPRoot(AnchorPane ap_root) {
        this.ap_root = ap_root;
    }

    public void aggiornaAPRoot() {
        ap_root.setStyle("-fx-background-color: " + Impostazioni.currentSetting.get(Setting.BACKGROUND));
        System.out.println(Impostazioni.currentSetting.get(Setting.BACKGROUND));
    }
}
