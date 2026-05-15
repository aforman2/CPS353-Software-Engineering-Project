package edu.newpaltz.library.models;
public class Student extends User {
    private int booksBorrowed;
    private final int MAX_BOOKS = 3;

    public Student(String fName, String lName, String uName, String uPassword) {
        super(fName, lName, uName, uPassword); // Calls the abstract class constructor
        this.booksBorrowed = 0;
    }

    public boolean canBorrow() {
        return booksBorrowed < MAX_BOOKS;
    }
}