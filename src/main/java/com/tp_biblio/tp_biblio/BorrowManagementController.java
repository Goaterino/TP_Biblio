package com.tp_biblio.tp_biblio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;

import java.sql.ResultSet;

public class BorrowManagementController extends BaseController {

    private final ObservableList<Borrow> displayedBorrows = FXCollections.observableArrayList();

    @FXML
    private TextField QueryField;
    @FXML
    private TableView<Borrow> BorrowsTableView;
    @FXML
    private TableColumn<Borrow, Integer> borrowIdColumn;
    @FXML
    private TableColumn<Borrow, String> userNameColumn;
    @FXML
    private TableColumn<Borrow, String> bookTitleColumn;
    @FXML
    private TableColumn<Borrow, String> borrowDateColumn;
    @FXML
    private TableColumn<Borrow, String> dueDateColumn;
    @FXML
    private CheckBox BaddieStatusCheckBox;
    @FXML
    private Button EditUserButton;

    private Borrow selectedBorrow;

    @FXML
    public void initialize() {
        super.initialize();
        initializeBorrowsTableView();
    }

    private void initializeBorrowsTableView() {
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        BorrowsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedBorrow = newValue;
                onBorrowSelected(selectedBorrow);
            }
        });
    }

    private void onBorrowSelected(Borrow borrow) {
        EditUserButton.setDisable(borrow == null);
    }

    @FXML
    protected void onSearchButtonClick() {
        try {
            String userName = QueryField.getText();
            if (userName.isEmpty()) {
                userName = "%";
            } else {
                userName = "%" + userName + "%";
            }

            boolean onlyBaddies = BaddieStatusCheckBox.isSelected();
            ResultSet rs = MySQLController.QueryOngoingBorrows(userName, onlyBaddies);
            if (rs != null) {
                displayedBorrows.clear();
                while (rs.next()) {
                    Borrow borrow = new Borrow(
                            rs.getString("user_name"),
                            rs.getString("book_title"),
                            rs.getString("start_date"),
                            rs.getString("due_date")
                    );
                    displayedBorrows.add(borrow);
                }
                BorrowsTableView.setItems(displayedBorrows);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onEditUserButtonClick() {
        if (selectedBorrow != null) {
            System.out.println("Edit User functionality for: " + selectedBorrow.getUserName());
        }
    }
}