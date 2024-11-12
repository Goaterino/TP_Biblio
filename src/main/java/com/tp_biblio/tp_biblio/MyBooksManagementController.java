package com.tp_biblio.tp_biblio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

public class MyBooksManagementController extends BaseController {

    private final ObservableList<Borrow> displayedBorrows = FXCollections.observableArrayList();

    @FXML
    private TextField QueryField;
    @FXML
    private TableView<Borrow> BorrowsTableView;
    @FXML
    private TableColumn<Borrow, String> bookTitleColumn;
    @FXML
    private TableColumn<Borrow, String> borrowDateColumn;
    @FXML
    private TableColumn<Borrow, String> dueDateColumn;
    @FXML
    private CheckBox PastDueCheckBox;
    @FXML
    private Button GiveBackButton;

    private Borrow selectedBorrow;

    @FXML
    public void initialize() {
        super.initialize();
        initializeBorrowsTableView();
    }

    private void initializeBorrowsTableView() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        BorrowsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                onBorrowSelected(newValue);
            }
        });
        onSearchButtonClick();
    }

    private void onBorrowSelected(Borrow borrow) {
        GiveBackButton.setDisable(borrow == null);
        selectedBorrow = borrow;
    }

    @FXML
    protected void onSearchButtonClick() {
        try {
            String searchQuery = QueryField.getText();
            if (searchQuery.isEmpty()) {
                searchQuery = "%";
            } else {
                searchQuery = "%" + searchQuery + "%";
            }

            boolean onlyPastDue = PastDueCheckBox.isSelected();
            ResultSet rs = MySQLController.QueryUserBorrows(searchQuery, onlyPastDue);
            if (rs != null) {
                displayedBorrows.clear();
                while (rs.next()) {
                    Borrow borrow = new Borrow(
                        rs.getString("user_name"),
                        rs.getString("book_title"),
                        rs.getString("start_date"),
                        rs.getString("due_date"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id")
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
    protected void onGiveBackButtonClick() {
        if (selectedBorrow != null) {
            try {
                MySQLController.unBorrowBook(selectedBorrow.getBookId(), selectedBorrow.getUserId(), selectedBorrow.getDueDate());
                showConfirmationAlert("Successfully gave back "+selectedBorrow.getBookTitle()+" !");
                QueryField.setText("");
                onSearchButtonClick();
            } catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Book Borrowed Successfully");
        alert.setContentText(message);
        alert.showAndWait();
        System.out.println("test1");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in Borrowing Book");
        alert.setContentText(message);
        alert.showAndWait();
    }
}