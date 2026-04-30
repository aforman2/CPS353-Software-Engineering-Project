import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class patron extends User {
    private int id; // Matches the 'id' column in MySQL
    private String role; // 'Student' or 'Librarian'

    // Updated Constructor
    public patron(int id, String fName, String lName, String uName, String uPass, String role) {
        super(fName, lName, uName, uPass);
        this.id = id;
        this.role = role;
    }

    // This replaces userBuilder - it loads a Patron object from the DB
    public static patron loadFromDatabase(String username) {
        String sql = "SELECT * FROM patrons WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new patron(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // GETTERS
    public int getId() { return id; }
    public String getRole() { return role; }

    // This will now query the 'loans' table instead of reading a .txt file
    public void listCheckedOutBooks() {
        System.out.println("\nBooks currently checked out by " + getFirstName() + ":");
        
        String sql = "SELECT b.title FROM books b " +
                     "JOIN loans l ON b.id = l.book_id " +
                     "WHERE l.patron_id = ? AND l.return_date IS NULL";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.id);
            ResultSet rs = pstmt.executeQuery();

            boolean hasBooks = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("title"));
                hasBooks = true;
            }
            if (!hasBooks) System.out.println("No active loans.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}