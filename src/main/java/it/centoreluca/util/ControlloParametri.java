package it.centoreluca.util;

import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlloParametri {

    private final CssHelper cssHelper = CssHelper.getInstance();
    private static ControlloParametri instance = null;
    private Pattern rPattern;
    private Matcher rMatcher;

    private ControlloParametri() {}

    public static ControlloParametri getInstance(){
        if(instance == null){
            instance = new ControlloParametri();
        }
        return instance;
    }

    public boolean testoSempliceConNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[A-Za-z\\d\\s]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
    }

    public boolean testoSempliceSenzaNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[\\D]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
    }

    public boolean numeri(TextInputControl tic, int minChar, int maxChar) {
        if(minChar == maxChar) {
            rPattern = Pattern.compile("[\\d]{" + minChar + "}");
        } else {
            rPattern = Pattern.compile("[\\d]{" + minChar + "," + maxChar + "}");
        }
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            if(minChar == maxChar) {
                cssHelper.toError(tic, new Tooltip("immettere " + minChar + " numeri"));
            } else {
                cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar +" numeri"));
            }
            return  false;
        }
    }

    public boolean ora(TextInputControl ora) {
        cssHelper.toDefault(ora);
        try {
            int oraTemp = Integer.parseInt(ora.getText().trim());
            if(oraTemp < 24) {
                cssHelper.toValid(ora);
                return true;
            } else {
                cssHelper.toError(ora, new Tooltip("L'ora deve essere comprese tra 0 e 23"));
            }
        } catch (NumberFormatException nfe) {
            cssHelper.toError(ora, new Tooltip("Immettere solo numeri"));
        }
        return false;
    }

    public boolean minuto(TextInputControl minuto) {
        cssHelper.toDefault(minuto);
        try {
            int minutoTemp = Integer.parseInt(minuto.getText().trim());
            if(minutoTemp < 60) {
                cssHelper.toValid(minuto);
                return true;
            } else {
                cssHelper.toError(minuto, new Tooltip("L'ora deve essere comprese tra 0 e 59"));
            }
        } catch (NumberFormatException nfe) {
            cssHelper.toError(minuto, new Tooltip("Immettere solo numeri"));
        }
        return false;
    }
}
