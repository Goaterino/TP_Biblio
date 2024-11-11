package com.tp_biblio.tp_biblio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditUserStatusController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label userEmailLabel;
    @FXML
    private Label userOngoingBorrowsLabel;
    @FXML
    private Label userBaddieStatusLabel;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    private User user;

    // Constructor with User object
    public EditUserStatusController(User user) {
        this.user = user;
    }

    // Constructor with user_id, retrieves user details using MySQLController
    public EditUserStatusController(int userId) {
        try {
            ResultSet userInfo = MySQLController.GetUserInfo(userId);
            if (userInfo.next()) {
                this.user = new User(
                        userInfo.getInt("id"),
                        userInfo.getString("name"),
                        userInfo.getString("email"),
                        userInfo.getInt("ongoing_borrows"),
                        userInfo.getBoolean("baddie_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        if (user != null) {
            userNameLabel.setText(user.getName());
            userEmailLabel.setText(user.getEmail());
            userOngoingBorrowsLabel.setText(String.valueOf(user.getOngoingBorrows()));
            userBaddieStatusLabel.setText(user.isBaddie() ? "Yes" : "No");

            statusComboBox.getItems().addAll("Good Standing", "Baddie");
            statusComboBox.setValue(user.isBaddie() ? "Baddie" : "Good Standing");
        }

        confirmButton.setOnAction(event -> handleConfirmAction());
        cancelButton.setOnAction(event -> handleCancelAction());
    }

    private void handleConfirmAction() {
        // Implement logic to update user status here.
        closeWindow();
    }

    private void handleCancelAction() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
