package com.tp_biblio.tp_biblio;

public class Book {
    private int id;
    private String title;
    private String authors;
    private int year;
    private String editor;
    private String isbn;
    private int totalQuantity;
    private int borrowedQuantity;

    public Book(int id, String title, String authors, int year, String editor, String isbn, int totalQuantity, int borrowedQuantity) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.editor = editor;
        this.year = year;
        this.isbn = isbn;
        this.totalQuantity = totalQuantity;
        this.borrowedQuantity = borrowedQuantity;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAvailable() {
        return this.totalQuantity > this.borrowedQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
    
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getBorrowedQuantity() {
        return borrowedQuantity;
    }
    
    public void setBorrowedQuantity(int borrowedQuantity) {
        this.borrowedQuantity = borrowedQuantity;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}