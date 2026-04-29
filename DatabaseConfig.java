import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/newpaltzlibrary";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASS = "ATf123$!ATf123$!"; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}