package com.tp_biblio.tp_biblio;

public class Borrow {

    private String userName;
    private String bookTitle;
    private String borrowDate;
    private String dueDate;
    private int userId;
    private int bookId;

    public Borrow(String userName, String bookTitle, String borrowDate, String dueDate, int userId, int bookId) {
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.userId = userId;
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }
}
