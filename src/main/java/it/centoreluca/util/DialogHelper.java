package it.centoreluca.util;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Calendar;

public class DialogHelper {

    private Parent parent;
    private Double xOffset;
    private Double yOffset;
    private Stage stage;
    private static DialogHelper instance = null;

    private DialogHelper() {}

    public static DialogHelper getInstance() {
        if(instance == null) {
            instance = new DialogHelper();
        }
        return instance;
    }

    public void newDialog(String fxmlName, Pane rootPane, Controller parentController, Calendar data) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName + ".fxml"));
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        stage = new Stage();
        Controller c = fxmlLoader.getController();
        c.impostaParametri(stage, parentController, data);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);

        if(rootPane != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(2000), rootPane);
            ft.setFromValue(1.0);
            ft.setToValue(0.1);
            ft.play();
        }

        // Trascinamento finestra
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.showAndWait();
    }

}
