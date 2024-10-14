package com.tp_biblio.tp_biblio;

import java.sql.*;

public class MySQLController {
    // DB link and credentials
    private static final String jdbc_link = "jdbc:mysql://localhost:3306/biblio_db";
    private static final String user = "root";
    private static final String pass = "root";
    // default query strings
    private static final String get_current_available_books = "SELECT id, title FROM books WHERE is_currently_borrowed = false AND title LIKE ?";
    private static final String get_book_details_from_id = "SELECT * FROM books WHERE id = ?";
    private static Connection db_con;

    public static void connect() {
        try {
            db_con = DriverManager.getConnection(jdbc_link, user, pass);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean isConnected() {
        return db_con != null;
    }

    public static ResultSet QueryAvailableBooks(String title) {
        assert isConnected();
        try {
            PreparedStatement ps = db_con.prepareStatement(get_current_available_books);
            ps.setString(1, title);
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
}
