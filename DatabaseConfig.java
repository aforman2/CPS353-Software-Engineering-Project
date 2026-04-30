import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/newpaltzlibrary";
    private static final String USER = "root"; 
    private static final String PASS = "Vonnitta#4305"; 

    public static Connection getConnection() throws SQLException {
        try {
            // This ensures the JAR is loaded before trying to connect
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}