package it.centoreluca.util;

import it.centoreluca.App;
import it.centoreluca.controller.Controller;
import it.centoreluca.controller.calendario.giornaliero.CColonnaPersonale;
import it.centoreluca.controller.clienti.CCliente;
import it.centoreluca.models.Cliente;
import it.centoreluca.models.Personale;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

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

    public Controller newDialog(String fxmlName, String title, Controller parentController, Calendar data, Personale personale, Cliente cliente) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName + ".fxml"));
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        stage = new Stage();
        Controller c = fxmlLoader.getController();
        c.impostaParametri(stage, parentController);
        if(parentController instanceof CColonnaPersonale) {
            c.impostaUlterioriParametri(data, personale);
        } else if(parentController instanceof CCliente) {
            c.impostaCliente(cliente);
        }

        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("icon/hairdresser.png"))));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);

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
        return c;
    }

}
