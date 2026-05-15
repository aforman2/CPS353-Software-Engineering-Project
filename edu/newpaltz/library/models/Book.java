package edu.newpaltz.library.models;
public class Book {
    private int id;             //Needed to match the Primary Key in MySQL
    private String title;
    private String author;
    private String isbn;
    private int availableCopies; // Changed from boolean to int to support multiple copies

    // Constructor to initialize the book from Database data
    public Book(int id, String title, String author, String isbn, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availableCopies = availableCopies;
    }

    // getStatus Method - shows whether book is available
    public String getStatus() {
        if (this.availableCopies > 0) {
            return "Available (" + this.availableCopies + ")";
        } else {
            return "Out of Stock";
        }
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    
    // Updated Logic: Instead of just true/false, we check if count > 0
    public boolean isAvailable() { 
        return availableCopies > 0; 
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
}