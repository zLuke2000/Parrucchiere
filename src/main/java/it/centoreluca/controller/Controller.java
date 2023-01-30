package it.centoreluca.controller;

import it.centoreluca.util.CssHelper;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Controller {

    private final CssHelper css = CssHelper.getInstance();

    public void setStage(Stage stage) { }

    public void setParent(Controller parent) { }

    public void caricaAppuntamenti() { }

    public void rimuoviNodo(Pane container, Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), n);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(ae -> container.getChildren().remove(n));
        ft.play();
    }

    public void reimpostaCampi(boolean text, boolean style, TextInputControl... tics) {
        for(TextInputControl t: tics) {
            if(text) {
                t.setText("");
            }
            if(style) {
                css.toDefault(t);
            }
        }
    }

    public boolean cambiamenti(Label l, TextInputControl tic) {
        if(l.getText() != null) {
            return !l.getText().trim().equals(tic.getText().trim());
        } else {
            return tic.getText().trim().length() > 0;
        }
    }

    public void copiaCampi(Label l, TextInputControl tic) {
        if(l.getText() == null) {
            tic.setText("");
        } else {
            tic.setText(l.getText());
        }
    }

}
