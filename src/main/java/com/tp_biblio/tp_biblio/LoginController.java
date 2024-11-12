package com.tp_biblio.tp_biblio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoginController {
    @FXML
    public Button LogInButton;
    @FXML
    private TextField UsernameField;
    @FXML
    private PasswordField PasswordField;

    @FXML
    public void initialize() {
        LogInButton.setDisable(true);
    }

    @FXML
    protected void onCredentialsInput() {
        if (!UsernameField.getText().isEmpty() && !PasswordField.getText().isEmpty()) {
            LogInButton.setDisable(false);
        } else {
            LogInButton.setDisable(true);
        }
    }

    @FXML
    protected void onLoginButtonClick() {
        try {
            ResultSet rs = MySQLController.LogIn(UsernameField.getText(), PasswordField.getText());
            if (rs!=null) {
                rs.next();
                MainApplication.logged_user_id = rs.getInt("id");
                MainApplication.is_logged_user_admin = rs.getString("role").equals("admin");
                System.out.println("Login Successful, logged as "+MainApplication.logged_user_id);
                Thread.sleep(1000);
                MainApplication.SwitchScene("biblio-browser-view");
            }
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
    }


}
