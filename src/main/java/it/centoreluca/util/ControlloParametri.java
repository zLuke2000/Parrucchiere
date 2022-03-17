package it.centoreluca.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.util.Calendar;
import java.util.GregorianCalendar;
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

    public boolean testoSemplice(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[\\w\\d\\s]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
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

    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toLowerCase().toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public Calendar data(TextField giorno, TextField mese, TextField anno) {
        if(numeri(giorno, 1, 2) & (numeri(mese, 1, 2)) & (numeri(anno, 4, 4))) {
            Calendar data = new GregorianCalendar(Integer.parseInt(anno.getText().trim()), Integer.parseInt(mese.getText().trim()) - 1, Integer.parseInt(giorno.getText().trim()));
            data.setLenient(false);
            try {
                data.getTime();
                return data;
            } catch (Exception e) {
                cssHelper.toError(giorno, new Tooltip("Data non valida"));
                cssHelper.toError(mese, new Tooltip("Data non valida"));
                cssHelper.toError(anno, new Tooltip("Data non valida"));
            }
        }
        return null;
    }

    public boolean email(TextField email) {
        if(email.getText().trim().length() <= 0) {
            cssHelper.toError(email, new Tooltip("Inserire email"));
            return false;
        }  else {
            rPattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
            rMatcher = rPattern.matcher(email.getText().trim());
            if(rMatcher.matches()) {
                cssHelper.toValid(email);
                return true;
            } else {
                cssHelper.toError(email, new Tooltip("Email non valida"));
                return false;
            }
        }
    }
}
