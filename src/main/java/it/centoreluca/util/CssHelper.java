package it.centoreluca.util;

import it.centoreluca.models.Appuntamento;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class CssHelper {

    private static CssHelper instance=null;

    private CssHelper(){ }

    public static CssHelper getInstance(){
        if(instance == null){
            instance = new CssHelper();
        }
        return instance;
    }

    public void toError(Control c, String testoTooltip) {
        toDefault(c);
        c.getStyleClass().add("field-error");
        if(testoTooltip != null) {
            Tooltip t = new Tooltip(testoTooltip);
            t.getStyleClass().add("tooltip-error");
            t.setShowDelay(Duration.ZERO);
            c.setTooltip(t);
        }
    }

    public void toValid(Control c) {
        toDefault(c);
        c.getStyleClass().add("field-valid");
    }

    public void toInfo(Control c, String testoTooltip) {
        toDefault(c);
        c.getStyleClass().add("field-info");
        if(testoTooltip != null) {
            Tooltip t = new Tooltip(testoTooltip);
            t.getStyleClass().add("tooltip-info");
            t.setShowDelay(Duration.ZERO);
            c.setTooltip(t);
        }
    }

    public void toDefault(Control c) {
        c.getStyleClass().remove("field-error");
        c.getStyleClass().remove("field-valid");
        c.setTooltip(null);
    }

    public void impostaStatoAppuntamento(AnchorPane ap_root, Appuntamento.Stato stato) {
        ap_root.getStyleClass().clear();
        switch (stato) {
            case ANNULLATO -> ap_root.getStyleClass().add("error-background");
            case ATTENZIONE -> ap_root.getStyleClass().add("warning-background");
            case COMPLETATO -> ap_root.getStyleClass().add("success-background");
            case DEFAULT -> ap_root.getStyleClass().add("opaco-20");
        }
    }

}
