package com.tp_biblio.tp_biblio;

import javafx.fxml.FXML;
import java.sql.*;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}