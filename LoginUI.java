import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginUI {

    // 1. CONSTRUCTOR: This lets the Logout button call "new LoginUI()"
    public LoginUI() {
        createAndShowGUI();
    }

    public static void main(String[] args) {
        // Standard launch using the constructor
        SwingUtilities.invokeLater(() -> new LoginUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("SUNY New Paltz Library - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Header
        JLabel titleLabel = new JLabel("SUNY New Paltz Library");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 61, 121));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Inputs
        gbc.gridwidth = 1; gbc.gridy = 1;
        frame.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1; frame.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; frame.add(passField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

        // Action: Login
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "SELECT * FROM patrons WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Load user object and switch to Dashboard
                    patron user = patron.loadFromDatabase(username);
                    new LibraryDashboard(user).setVisible(true);
                    frame.dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database connection error.");
            }
        });

        // Action: Create Account
        createAccountButton.addActionListener(e -> showSignUpDialog(frame));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

private static void showSignUpDialog(JFrame parentFrame) {
    // Create a simple panel with a grid for inputs
    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField userField = new JTextField();
    JPasswordField passField = new JPasswordField();

    panel.add(new JLabel("First Name:"));
    panel.add(firstNameField);
    panel.add(new JLabel("Last Name:"));
    panel.add(lastNameField);
    panel.add(new JLabel("Username:"));
    panel.add(userField);
    panel.add(new JLabel("Password:"));
    panel.add(passField);

    int result = JOptionPane.showConfirmDialog(parentFrame, panel, 
            "Create New Library Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String fName = firstNameField.getText();
        String lName = lastNameField.getText();
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        if (fName.isEmpty() || lName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "All fields are required!");
            return;
        }

        saveUserToDatabase(fName, lName, user, pass, parentFrame);
    }
}
private static void saveUserToDatabase(String fName, String lName, String user, String pass, JFrame parent) {
    // Default role for new signups is 'Student'
    String sql = "INSERT INTO patrons (first_name, last_name, username, password, role) VALUES (?, ?, ?, ?, 'Student')";

    try (Connection conn = DatabaseConfig.getConnection()) {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, fName);
        pstmt.setString(2, lName);
        pstmt.setString(3, user);
        pstmt.setString(4, pass);

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(parent, "Account created successfully! You can now login.");
        }
    } catch (SQLException ex) {
        if (ex.getMessage().contains("Duplicate entry")) {
            JOptionPane.showMessageDialog(parent, "Username already exists. Please choose another.");
        } else {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error saving to database.");
        }
    }
}
}