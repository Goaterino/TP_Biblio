package com.tp_biblio.tp_biblio;

import com.sun.tools.javac.Main;

import java.sql.*;
import java.time.LocalDate;

public class MySQLController {
    // DB link and credentials
    private static final String jdbc_link = "jdbc:mysql://localhost:3306/biblio_db";
    private static final String user = "root";
    private static final String pass = "root";
    // default query strings
    private static final String get_all_books = "SELECT * FROM books WHERE (title LIKE ? OR isbn LIKE ?)";
    private static final String login_query = "SELECT id, role FROM users WHERE name = ? AND password = ? LIMIT 1";
    private static final String get_user_info_query = "SELECT u.*, (SELECT COUNT(*) FROM borrowed_books AS b WHERE b.user_id = ?) AS ongoing_borrows FROM users AS u WHERE u.id = ?";
    private static final String get_authors_by_book_id = "SELECT author_id FROM books_to_authors WHERE book_id = ?";
    private static final String get_authors_by_author_id = "SELECT * FROM authors WHERE id = ?";
    private static final String add_borrowed_book = "INSERT INTO borrowed_books VALUES (?, ?, ?, ?)";
    private static final String get_ongoing_borrows = "SELECT users.name AS user_name, users.id AS user_id, books.id AS book_id, books.title AS book_title, borrows.start_date, borrows.due_date FROM borrowed_books as borrows JOIN users ON borrows.user_id = users.id JOIN books ON borrows.book_id = books.id WHERE users.name LIKE ?";
    private static final String get_all_user_info_query = "SELECT users.id AS user_id, users.name, users.email, COUNT(borrowed_books.book_id) AS ongoing_borrows, users.baddie_status FROM users LEFT JOIN borrowed_books ON borrowed_books.user_id = users.id WHERE users.name LIKE ?";
    private static final String get_ongoing_borrows_by_user_id = "SELECT users.name AS user_name, users.id AS user_id, books.id AS book_id, books.title AS book_title, borrows.start_date, borrows.due_date FROM borrowed_books as borrows JOIN books ON borrows.book_id = books.id JOIN users ON borrows.user_id = users.id WHERE books.title LIKE ? AND borrows.user_id = ?";

    // default update strings
    private static final String change_user_status_by_id = "UPDATE users SET baddie_status = ? WHERE id = ?";
    private static final String change_stock = "UPDATE books SET already_borrowed_quantity = already_borrowed_quantity + ? WHERE id = ?";
    private static final String delete_borrowed_book = "DELETE FROM borrowed_books WHERE user_id = ? AND book_id = ? AND due_date = ?";

    private static Connection db_con;

    public static void connect() {
        if (!isConnected()) {
            try {
                db_con = DriverManager.getConnection(jdbc_link, user, pass);
                System.out.println("Connected to database");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean isConnected() {
        return db_con != null;
    }

    public static ResultSet QueryAvailableBooks(String title_or_isbn, boolean onlyAvailableBooks) {
        assert isConnected();
        try {
            String query = get_all_books + (onlyAvailableBooks ? " AND total_quantity > already_borrowed_quantity" : "");
            PreparedStatement ps;
            ps = db_con.prepareStatement(query);
            ps.setString(1, title_or_isbn);
            ps.setString(2, title_or_isbn);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet QueryUserBorrows(String book_title, boolean onlyPastDue) {
        assert isConnected();
        try {
            String query = get_ongoing_borrows_by_user_id + ((onlyPastDue) ? " AND borrows.due_date <= "+Date.valueOf(LocalDate.now()) : "");
            PreparedStatement ps = db_con.prepareStatement(query);
            ps.setString(1, book_title);
            ps.setInt(2, MainApplication.logged_user_id);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet LogIn(String username, String password) {
        assert isConnected();
        try {
            if (username.length() <= 50) {
                PreparedStatement ps = db_con.prepareStatement(login_query);
                ps.setString(1, username);
                ps.setString(2, Utils.hashString(password));
                ResultSet rs = ps.executeQuery();
                System.out.println("Successfully queried the database :");
                System.out.println(ps);
                return rs;
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet GetUserInfo(Integer id) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(get_user_info_query);
            ps.setInt(1, id);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet GetAllUserInfo(String userName, boolean onlyBaddies) {
        assert isConnected();
        String query = get_all_user_info_query + (onlyBaddies ? " AND users.baddie_status = 1" : "") + " GROUP BY users.id, users.name, users.email, users.baddie_status";
        try {
            PreparedStatement ps = db_con.prepareStatement(query);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet QueryAuthorNameById(int authorId) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(get_authors_by_author_id);
            ps.setInt(1, authorId);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet QueryAuthorsByBookId(int bookId) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(get_authors_by_book_id);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void borrowBook(String selectedDuration, Book book) {
        assert isConnected();
        try {
            // adding book to list of borrowed books
            PreparedStatement ps = db_con.prepareStatement(add_borrowed_book);
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(Integer.parseInt(selectedDuration));
            ps.setInt(1, MainApplication.logged_user_id);
            ps.setInt(2, book.getId());
            ps.setDate(3, Date.valueOf(borrowDate));
            ps.setDate(4, Date.valueOf(dueDate));
            ps.executeUpdate();
            System.out.println("Successfully queried the database :");
            System.out.println(ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void changeStockNumber(int bookId, int delta) {
        assert isConnected();
        try {
            // adding book to list of borrowed books
            PreparedStatement ps = db_con.prepareStatement(change_stock);
            ps.setInt(2, bookId);
            ps.setInt(1, delta);
            ps.executeUpdate();
            System.out.println("Successfully updated the database :");
            System.out.println(ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ResultSet QueryOngoingBorrows(String userName, boolean onlyBaddies) {
        String query = get_ongoing_borrows + (onlyBaddies ? "AND users.baddie_status = 1 " : "");
        try {
            PreparedStatement ps = db_con.prepareStatement(query);
            ps.setString(1, userName);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error querying ongoing borrows: " + e.getMessage());
            return null;
        }
    }

    public static void changeUserStatus(int user_id, boolean baddie) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(change_user_status_by_id);
            ps.setBoolean(1, baddie);
            ps.setInt(2, user_id);
            ps.executeUpdate();
            System.out.println("Successfully updated the database :");
            System.out.println(ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void unBorrowBook(int book_id, int user_id, String due_date) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(delete_borrowed_book);
            ps.setInt(1, user_id);
            ps.setInt(2, book_id);
            ps.setString(3, due_date);
            ps.executeUpdate();
            System.out.println("Successfully updated the database :");
            System.out.println(ps);
            changeStockNumber(book_id, -1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
