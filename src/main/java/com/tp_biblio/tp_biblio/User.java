package com.tp_biblio.tp_biblio;

public class User {
    private String name;
    private String email;
    private int ongoingBorrows;
    private boolean baddie;
    private int id;

    public User(int id, String name, String email, int ongoingBorrows, boolean baddie_status) {
        this.name = name;
        this.email = email;
        this.ongoingBorrows = ongoingBorrows;
        this.baddie = baddie_status;
        this.id = id;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getOngoingBorrows() { return ongoingBorrows; }
    public boolean isBaddie() { return baddie; }
    public int getId() { return this.id; }
}
