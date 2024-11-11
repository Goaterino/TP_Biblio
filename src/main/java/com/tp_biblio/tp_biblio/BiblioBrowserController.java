package com.tp_biblio.tp_biblio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;


public class BiblioBrowserController extends BaseController {

    private final ObservableList<Book> displayedBooks = FXCollections.observableArrayList();
    private Book selectedBook = null;

    @FXML
    private TextField QueryField;
    @FXML
    private TableView<Book> BooksTableView;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> yearColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> availableColumn;
    @FXML
    private CheckBox OnlyAvailableBooksCheckBox;
    @FXML
    private Button BorrowButton;

    @FXML
    public void initialize() {
        super.initialize();
        initializeBooksTableView();
    }

    private void initializeBooksTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        BooksTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                onBookSelected(newValue);
            }
        });
    }

    private void onBookSelected(Book book) {
        selectedBook = book;
        BorrowButton.setDisable(!book.isAvailable());
    }


    @FXML
    protected void onSearchButtonClick() {
        try {
            String title_or_isbn = QueryField.getText();
            if (title_or_isbn.isEmpty()) {
                title_or_isbn = "%";
            } else {
                title_or_isbn = "%" + title_or_isbn + "%";
            }

            ResultSet rs = MySQLController.QueryAvailableBooks(title_or_isbn);
            if (rs != null) {
                displayedBooks.clear();
                while (rs.next()) {
                    int bookId = rs.getInt("id");

                    ResultSet authorsRs = MySQLController.QueryAuthorsByBookId(bookId);
                    StringBuilder authorsBuilder = new StringBuilder();

                    while (authorsRs.next()) {
                        int authorId = authorsRs.getInt("author_id");
                        ResultSet authorNameRs = MySQLController.QueryAuthorNameById(authorId);
                        if (authorNameRs.next()) {
                            authorsBuilder.append(authorNameRs.getString("name")).append(", ");
                        }
                    }

                    String authors = !authorsBuilder.isEmpty() ? authorsBuilder.substring(0, authorsBuilder.length() - 2) : "";

                    Book book = new Book(
                            bookId,
                            rs.getString("title"),
                            authors,
                            rs.getInt("year_of_parution"),
                            rs.getString("editor"),
                            rs.getString("isbn"),
                            rs.getInt("total_quantity"),
                            rs.getInt("already_borrowed_quantity")
                    );
                    displayedBooks.add(book);
                }
                BooksTableView.setItems(displayedBooks);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onBorrowButtonClick() {
        if (selectedBook != null && selectedBook.isAvailable()) {
            MainApplication.loadBookPopupWindow(selectedBook);
        }
    }
}