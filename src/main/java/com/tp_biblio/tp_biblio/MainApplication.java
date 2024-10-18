package com.tp_biblio.tp_biblio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {

    public static Integer logged_user_id = -1;
    public static Stage MainStage;

    @Override
    public void start(Stage PrimaryStage) throws IOException {
        MainStage = PrimaryStage;
        MainStage.setTitle("Bibliothèque!");
        SwitchScene("login-view");
        MainStage.show();
    }

    public static void SwitchScene(String scene_name) throws IOException {
        Pane root; Scene scene;
        switch (scene_name) {
            case "biblio-browser-view":
                root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("biblio-browsing-view.fxml")));
                scene = new Scene(root, 835, 418);
                break;
            default:
                root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login-view.fxml")));
                scene = new Scene(root, 600, 400);
                break;
        }
        MainStage.setScene(scene);
    }

    public static void main(String[] args) {
        MySQLController.connect();
        launch();
    }
}