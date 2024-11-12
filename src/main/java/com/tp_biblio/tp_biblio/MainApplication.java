package com.tp_biblio.tp_biblio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {

    public static Integer logged_user_id = -1;
    public static Stage MainStage;
    public static boolean is_logged_user_admin = false;
    public static Stage PopupStage;

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
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("biblio-browsing-view.fxml")));
                    scene = new Scene(root, 835, 418);
                    MainStage.setScene(scene);
                } catch (javafx.fxml.LoadException e) {
                    e.printStackTrace();
                }
                break;
            case "borrow-management-view":
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("borrow-management-view.fxml")));
                    scene = new Scene(root, 835, 418);
                    MainStage.setScene(scene);
                } catch (javafx.fxml.LoadException e) {
                    e.printStackTrace();
                }
                break;
            case "user-management-view":
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("user-management-view.fxml")));
                    scene = new Scene(root, 835, 418);
                    MainStage.setScene(scene);
                } catch (javafx.fxml.LoadException e) {
                    e.printStackTrace();
                }
                break;
            case "mybooks-management-view":
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("mybooks-management-view.fxml")));
                    scene = new Scene(root, 835, 418);
                    MainStage.setScene(scene);
                } catch (javafx.fxml.LoadException e) {
                    e.printStackTrace();
                }
                break;
            default:
                root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login-view.fxml")));
                scene = new Scene(root, 600, 400);
                MainStage.setScene(scene);
                break;
        }

    }

    public static void Disconnect() {
        is_logged_user_admin = false;
        logged_user_id = -1;
        try {
            SwitchScene("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBookPopupWindow(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("confirm-borrow-view.fxml"));
            Pane root = loader.load();

            BorrowConfirmController controller = loader.getController();
            controller.setBookDetails(selectedBook);

            PopupStage = new Stage();
            PopupStage.initModality(Modality.APPLICATION_MODAL);
            PopupStage.setTitle("Confirm Borrow");

            Scene scene = new Scene(root);
            PopupStage.setScene(scene);
            PopupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUserPopupWindow(int selectedUserId) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("edit-user-status-view.fxml"));
            Pane root = loader.load();

            EditUserStatusController controller = loader.getController();
            controller.InitializeWithUserId(selectedUserId);

            PopupStage = new Stage();
            PopupStage.initModality(Modality.APPLICATION_MODAL);
            PopupStage.setTitle("Edit User Status");

            Scene scene = new Scene(root);
            PopupStage.setScene(scene);
            PopupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MySQLController.connect();
        launch();
    }
}