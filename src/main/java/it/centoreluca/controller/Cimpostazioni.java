package it.centoreluca.controller;

import it.centoreluca.database.Database;
import it.centoreluca.enumerator.Setting;
import it.centoreluca.util.Impostazioni;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Cimpostazioni {
    @FXML private ColorPicker cp_background;
    @FXML private Button b_background;
    @FXML private ColorPicker cp_info;
    @FXML private Button b_info;
    @FXML private ColorPicker cp_success;
    @FXML private Button b_success;
    @FXML private ColorPicker cp_warning;
    @FXML private Button b_warning;
    @FXML private ColorPicker cp_error;
    @FXML private Button b_error;

    private final Database db = Database.getInstance();
    private final Impostazioni imp = Impostazioni.getInstance();

    @FXML
    private void initialize() {
        cp_background.setValue(Color.web(Impostazioni.currentSetting.get(Setting.BACKGROUND)));
        cp_info.setValue(Color.web(Impostazioni.currentSetting.get(Setting.INFO)));
        cp_success.setValue(Color.web(Impostazioni.currentSetting.get(Setting.SUCCESS)));
        cp_warning.setValue(Color.web(Impostazioni.currentSetting.get(Setting.WARNING)));
        cp_error.setValue(Color.web(Impostazioni.currentSetting.get(Setting.ERROR)));
    }

    @FXML
    private void reset(ActionEvent ae) {
        if(ae.getSource().equals(b_background)) {
            db.aggiornaImpostazione(Setting.BACKGROUND, Impostazioni.defaultSetting.get(Setting.BACKGROUND));
            Impostazioni.currentSetting.put(Setting.BACKGROUND, Impostazioni.defaultSetting.get(Setting.BACKGROUND));
            imp.aggiornaAPRoot();
            cp_background.setValue(Color.web(Impostazioni.currentSetting.get(Setting.BACKGROUND)));
        }else if(ae.getSource().equals(b_info)) {
            db.aggiornaImpostazione(Setting.INFO, Impostazioni.defaultSetting.get(Setting.INFO));
            Impostazioni.currentSetting.put(Setting.INFO, Impostazioni.defaultSetting.get(Setting.INFO));
            cp_info.setValue(Color.web(Impostazioni.currentSetting.get(Setting.INFO)));
        }else if(ae.getSource().equals(b_success)) {
            db.aggiornaImpostazione(Setting.SUCCESS, Impostazioni.defaultSetting.get(Setting.SUCCESS));
            Impostazioni.currentSetting.put(Setting.SUCCESS, Impostazioni.defaultSetting.get(Setting.SUCCESS));
            cp_success.setValue(Color.web(Impostazioni.currentSetting.get(Setting.SUCCESS)));
        }else if(ae.getSource().equals(b_warning)) {
            db.aggiornaImpostazione(Setting.WARNING, Impostazioni.defaultSetting.get(Setting.WARNING));
            Impostazioni.currentSetting.put(Setting.WARNING, Impostazioni.defaultSetting.get(Setting.WARNING));
            cp_warning.setValue(Color.web(Impostazioni.currentSetting.get(Setting.WARNING)));
        }else if(ae.getSource().equals(b_error)) {
            db.aggiornaImpostazione(Setting.ERROR, Impostazioni.defaultSetting.get(Setting.ERROR));
            Impostazioni.currentSetting.put(Setting.ERROR, Impostazioni.defaultSetting.get(Setting.ERROR));
            cp_error.setValue(Color.web(Impostazioni.currentSetting.get(Setting.ERROR)));
        }
    }

    @FXML
    private void cambioColore(ActionEvent ae) {
        if(ae.getSource().equals(cp_background)) {
            db.aggiornaImpostazione(Setting.BACKGROUND, String.valueOf(cp_background.getValue()).replace("0x", "#"));
            Impostazioni.currentSetting.put(Setting.BACKGROUND, String.valueOf(cp_background.getValue()).replace("0x", "#"));
            imp.aggiornaAPRoot();
            cp_background.setValue(Color.web(Impostazioni.currentSetting.get(Setting.BACKGROUND)));
        }else if(ae.getSource().equals(cp_info)) {
            db.aggiornaImpostazione(Setting.INFO, String.valueOf(cp_info.getValue()).replace("0x", "#"));
            Impostazioni.currentSetting.put(Setting.INFO, String.valueOf(cp_info.getValue()).replace("0x", "#"));
            cp_info.setValue(Color.web(Impostazioni.currentSetting.get(Setting.INFO)));
        }else if(ae.getSource().equals(cp_success)) {
            db.aggiornaImpostazione(Setting.SUCCESS, String.valueOf(cp_success.getValue()).replace("0x", "#"));
            Impostazioni.currentSetting.put(Setting.SUCCESS, String.valueOf(cp_success.getValue()).replace("0x", "#"));
            cp_success.setValue(Color.web(Impostazioni.currentSetting.get(Setting.SUCCESS)));
        }else if(ae.getSource().equals(cp_warning)) {
            db.aggiornaImpostazione(Setting.WARNING, String.valueOf(cp_warning.getValue()).replace("0x", "#"));
            Impostazioni.currentSetting.put(Setting.WARNING, String.valueOf(cp_warning.getValue()).replace("0x", "#"));
            cp_warning.setValue(Color.web(Impostazioni.currentSetting.get(Setting.WARNING)));
        }else if(ae.getSource().equals(cp_error)) {
            db.aggiornaImpostazione(Setting.ERROR, String.valueOf(cp_error.getValue()).replace("0x", "#"));
            Impostazioni.currentSetting.put(Setting.ERROR, String.valueOf(cp_error.getValue()).replace("0x", "#"));
            cp_error.setValue(Color.web(Impostazioni.currentSetting.get(Setting.ERROR)));
        }
    }

}
