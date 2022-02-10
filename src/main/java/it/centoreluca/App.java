package it.centoreluca;

import it.centoreluca.database.Database;
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

    public static Stage stage;
    public static Database db = Database.getInstance();
    private Double xOffset;
    private Double yOffset;
    private final Double bordo = 128.0;

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
        stage.setTitle("Ricci & Capricci");
        stage.setWidth(width - bordo);
        stage.setHeight(height - bordo);
        stage.centerOnScreen();
        stage.show();

        // Trascinamento finestra
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
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
        launch(args);
    }
}