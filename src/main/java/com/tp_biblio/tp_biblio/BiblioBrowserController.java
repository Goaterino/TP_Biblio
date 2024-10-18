package com.tp_biblio.tp_biblio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;


public class BiblioBrowserController {
    private final String book_regex = "id: \\s*(\\d+)";
    private final Pattern book_pattern = Pattern.compile(book_regex);
    private final ObservableList<String> displayed_books = FXCollections.observableArrayList();
    private final Map<String, Text> BiblioGridMap = new HashMap<>();

    @FXML
    private TextField QueryField;
    @FXML
    private ListView<String> BooksListView;
    @FXML
    private GridPane BiblioGridPane;
    @FXML
    private CheckBox OnlyAvailableBooksCheckBox;

    @FXML
    public void initialize() {
        initializeBiblioGridPane();
    }

    @FXML
    protected void onSearchButtonClick() {
        try {
            String title = QueryField.getText();
            if (title.isEmpty()) {
                title = "%";
            } else {
                title = "%" + title + "%";
            }
            ResultSet rs = MySQLController.QueryAvailableBooks(title, OnlyAvailableBooksCheckBox.isSelected());
            if (rs!=null) {
                displayed_books.clear();
                while (rs.next()) {
                    displayed_books.add(rs.getString("title") + " (id: " + rs.getInt("id") + ")");
                }
                BooksListView.setItems(displayed_books);
                BooksListView.setPrefWidth(100);
                BooksListView.setPrefHeight(100);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onListedBookClick() {
        try {
            String selectedBook = BooksListView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                int selectedBookId = extractID(selectedBook);
                ResultSet details_rs = MySQLController.QueryBookDetails(selectedBookId);
                if (details_rs!=null) {
                    while (details_rs.next()) {
                        BiblioGridMap.get("title").setText(details_rs.getString("title"));
                        BiblioGridMap.get("authors").setText(details_rs.getString("authors"));
                        BiblioGridMap.get("editor").setText(details_rs.getString("editor"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int extractID(String book_string) {
        Matcher matcher = book_pattern.matcher(book_string);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    private void initializeBiblioGridPane() {

        BiblioGridPane.add(new Text("Title"), 0, 0);
        BiblioGridPane.add(new Text("Author(s)"), 0, 1);
        BiblioGridPane.add(new Text("Editor"), 0, 2);

        Text title = new Text("");
        Text author = new Text("");
        Text editor = new Text("");
        BiblioGridPane.add(title, 1, 0);
        BiblioGridPane.add(author, 1, 1);
        BiblioGridPane.add(editor, 1, 2);
        BiblioGridMap.put("title", title);
        BiblioGridMap.put("authors", author);
        BiblioGridMap.put("editor", editor);
    }
}