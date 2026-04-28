public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    // Constructor to initialize the book
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true; // Default to available
    }

    // Getters
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }

    // Setter for status
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}