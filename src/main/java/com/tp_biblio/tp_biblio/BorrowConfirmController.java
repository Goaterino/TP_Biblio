package com.tp_biblio.tp_biblio;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BorrowConfirmController {

    @FXML
    private Label bookTitleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label isbnLabel;
    @FXML
    private ComboBox<String> borrowDurationComboBox;

    private Book book;

    @FXML
    private void handleConfirmAction() {
        String selectedDuration = borrowDurationComboBox.getValue();
        if (selectedDuration != null) {
            MySQLController.borrowBook(selectedDuration, book);
            MySQLController.changeStockNumber(book.getId(), +1);
            showConfirmationAlert("Book borrowed for " + selectedDuration + " days.");
            MainApplication.PopupStage.close();
            try {
                MainApplication.SwitchScene("biblio-browser-view");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Please select a borrow duration.");
        }
    }

    public void setBookDetails(Book book) {
        this.book = book;
        bookTitleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthors());
        isbnLabel.setText(book.getIsbn());
    }

    @FXML
    private void handleCancelAction() {
        MainApplication.PopupStage.close();
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Book Borrowed Successfully");
        alert.setContentText(message);
        alert.showAndWait();
        System.out.println("test1");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in Borrowing Book");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
