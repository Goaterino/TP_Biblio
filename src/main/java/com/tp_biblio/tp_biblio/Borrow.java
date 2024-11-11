package com.tp_biblio.tp_biblio;

public class Borrow {

    private String userName;
    private String bookTitle;
    private String borrowDate;
    private String dueDate;

    public Borrow(String userName, String bookTitle, String borrowDate, String dueDate) {
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
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
