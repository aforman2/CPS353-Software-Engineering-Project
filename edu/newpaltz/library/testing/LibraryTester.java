package edu.newpaltz.library.testing;

import edu.newpaltz.library.models.Book;
import edu.newpaltz.library.models.Loan;
import java.util.Date;

public class LibraryTester {

    public static void main(String[] args) {
        System.out.println("=== STARTING AUTOMATED UNIT TESTS ===");
        
        testBookAvailability();
        testLoanOverdueLogic();
        
        System.out.println("=== ALL TESTS COMPLETED ===");
    }

    // White Box Test: Checking the internal logic of the Book class
    public static void testBookAvailability() {
        System.out.print("Testing Book Status Logic... ");
        
        // Create a book with 0 copies
        Book emptyBook = new Book(1, "Test Title", "Author", "123", 0);
        
        if (emptyBook.getStatus().equals("Out of Stock") && !emptyBook.isAvailable()) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED (Expected Out of Stock)");
        }
    }

    // White Box Test: Checking the date logic in the Loan class
    public static void testLoanOverdueLogic() {
        System.out.print("Testing Loan Overdue Logic... ");
        
        // Create a date in the past (yesterday)
        long yesterdayMillis = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        Date yesterday = new Date(yesterdayMillis);
        
        // Create a loan that was due yesterday, but not returned
        Loan lateLoan = new Loan(101, "Late Book", yesterday, null);
        
        if (lateLoan.isOverdue()) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED (Loan should be overdue)");
        }
    }
}