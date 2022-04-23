package it.centoreluca;

import it.centoreluca.database.ExcelHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static final ExcelHelper eh = ExcelHelper.getInstance();

    public static Stage stage;
    private Double xOffset;
    private Double yOffset;
    private boolean drag;

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        // Prendo la dimensione corrente dello schermo
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        double width = size.getWidth();
        double height = size.getHeight();
        // Carico la finestra principale
        Scene scene = new Scene(Objects.requireNonNull(loadFXML("Avvio")));
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("icon/hairdresser.png"))));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Parrucchiere");
        double bordo = 128.0;
        stage.setWidth(width - bordo);
        stage.setHeight(height - bordo);
        stage.centerOnScreen();
        stage.show();

        // Trascinamento finestra
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
            drag = yOffset < 45;
        });
        scene.setOnMouseDragged(mouseEvent -> {
            if(drag) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
    }

    public static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        eh.backupXLSX();
        launch(args);
    }
}