package com.tp_biblio.tp_biblio;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;

public abstract class BaseController {

    @FXML
    protected ToolBar ToolBar;

    @FXML
    protected AnchorPane rootPane;

    @FXML
    protected void initialize() {
        populateToolBar();
    }

    private void populateToolBar() {
        // Create Buttons for each action
        Button browseBookButton = new Button("Browse Library");
        browseBookButton.setOnAction(event -> handleBrowseBookButtonAction());

        Button myBooksButton = new Button("My Borrowed Books");
        myBooksButton.setOnAction(event -> handleMyBooksButtonAction());

        Button Disconnect = new Button("Disconnect");
        Disconnect.setOnAction(event -> handleDisconnectAction());
        
        if (MainApplication.is_logged_user_admin) {
            Button InspectBurrows = new Button("Inspect Borrows");
            InspectBurrows.setOnAction(event -> handleInspectBorrowsAction());

            Button InspectUsers = new Button("Inspect Users");
            InspectUsers.setOnAction(event -> handleInspectUsersAction());

            ToolBar.getItems().addAll(browseBookButton, myBooksButton, InspectBurrows, InspectUsers, Disconnect);
        } else {
            ToolBar.getItems().addAll(browseBookButton, myBooksButton, Disconnect);
        }
        
    }

    protected void handleBrowseBookButtonAction() {
        try {
            MainApplication.SwitchScene("biblio-browser-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleMyBooksButtonAction() {
        try {
            MainApplication.SwitchScene("mybooks-management-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleInspectBorrowsAction() {
        try {
            MainApplication.SwitchScene("borrow-management-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleInspectUsersAction() {
        try {
            MainApplication.SwitchScene("user-management-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleDisconnectAction() {
        MainApplication.Disconnect();
    }
}
