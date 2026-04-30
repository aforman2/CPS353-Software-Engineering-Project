import java.util.Date;

public class Loan {
    private int bookId;
    private String bookTitle;
    private Date dueDate;
    private Date returnDate;

    public Loan(int bookId, String bookTitle, Date dueDate, Date returnDate) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    // Getters
    public int getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public Date getDueDate() { return dueDate; }
    
    // Business Logic: Is the book late?
    public boolean isOverdue() {
        if (returnDate != null) return false;
        return new Date().after(dueDate);
    }
}