public class Student extends Users {
    private int booksBorrowed;
    private final int MAX_BOOKS = 5;

    public Student(String fName, String lName, String uName, String uPassword) {
        super(fName, lName, uName, uPassword); // Calls the abstract class constructor
        this.booksBorrowed = 0;
    }

    public boolean canBorrow() {
        return booksBorrowed < MAX_BOOKS;
    }
}