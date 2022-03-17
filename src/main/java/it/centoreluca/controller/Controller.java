package it.centoreluca.controller;

import it.centoreluca.models.Personale;
import it.centoreluca.util.CssHelper;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Calendar;

public abstract class Controller {

    private final CssHelper css = CssHelper.getInstance();

    public void impostaParametri(Stage stage, Controller parent) { }

    public void impostaUlterioriParametri(Calendar data, Personale personale) { }

    public void callback(boolean status) { }

    public void rimuoviNodo(Pane container, Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), n);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(ae -> container.getChildren().remove(n));
        ft.play();
    }

    public void reimpostaCampi(TextInputControl... tics) {
        for(TextInputControl t: tics) {
            t.setText("");
            css.toDefault(t);
        }
    }

}
