package com.tp_biblio.tp_biblio;

import java.sql.*;
import java.time.LocalDate;

public class MySQLController {
    // DB link and credentials
    private static final String jdbc_link = "jdbc:mysql://localhost:3306/biblio_db";
    private static final String user = "root";
    private static final String pass = "root";
    // default query strings
    private static final String get_all_books = "SELECT * FROM books WHERE title LIKE ? OR isbn LIKE ?";
    private static final String get_book_details_from_id = "SELECT * FROM books WHERE id = ?";
    private static final String login_query = "SELECT id, role FROM users WHERE name = ? AND password = ? LIMIT 1";
    private static final String get_user_info_query = "SELECT u.*, (SELECT COUNT(*) FROM borrows AS b WHERE b.user_id = ?) AS ongoing_borrows FROM users AS u WHERE u.id = ?";
    private static final String get_authors_by_book_id = "SELECT author_id FROM books_to_authors WHERE book_id = ?";
    private static final String get_authors_by_author_id = "SELECT * FROM authors WHERE id = ?";
    private static final String add_borrowed_book = "INSERT INTO borrowed_books VALUES (?, ?, ?, ?)";
    private static final String change_stock = "UPDATE books SET already_borrowed_quantity = already_borrowed_quantity + ? WHERE id = ?";
    private static final String get_ongoing_borrows = "SELECT users.name AS user_name, books.title AS book_title, borrows.start_date, borrows.due_date FROM borrowed_books as borrows JOIN users ON borrows.user_id = users.id JOIN books ON borrows.book_id = books.id WHERE users.name LIKE ?";

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

    public static ResultSet QueryAvailableBooks(String title_or_isbn) {
        assert isConnected();
        try {
            PreparedStatement ps;
            ps = db_con.prepareStatement(get_all_books);
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

    public static ResultSet QueryBookDetails(Integer id) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(get_book_details_from_id);
            ps.setInt(1, id);
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
            System.out.println("Successfully queried the database :");
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
}
