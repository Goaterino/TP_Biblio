package com.tp_biblio.tp_biblio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

public class UserManagementController extends BaseController {

    private final ObservableList<User> displayedUsers = FXCollections.observableArrayList();

    @FXML
    private TextField QueryField;
    @FXML
    private TableView<User> UsersTableView;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, Integer> ongoingBorrowsColumn;
    @FXML
    private TableColumn<User, Boolean> statusColumn;
    @FXML
    private CheckBox BaddieStatusCheckBox;
    @FXML
    private Button EditUserButton;

    private User selectedUser;

    @FXML
    public void initialize() {
        super.initialize();
        initializeUsersTableView();
    }

    private void initializeUsersTableView() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ongoingBorrowsColumn.setCellValueFactory(new PropertyValueFactory<>("ongoingBorrows"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("baddieStatus"));

        UsersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                onUserSelected(newValue);
            }
        });
        onSearchButtonClick();
    }

    private void onUserSelected(User user) {
        EditUserButton.setDisable(user == null);
        selectedUser = user;
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
            ResultSet rs = MySQLController.GetAllUserInfo(userName, onlyBaddies);
            if (rs != null) {
                displayedUsers.clear();
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getInt("ongoing_borrows"),
                            rs.getBoolean("baddie_status")
                    );
                    displayedUsers.add(user);
                }
                UsersTableView.setItems(displayedUsers);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onEditUserButtonClick() {
        if (selectedUser != null) {
            MainApplication.loadUserPopupWindow(selectedUser.getId());
        }
    }
}