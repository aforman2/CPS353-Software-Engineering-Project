import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("SUNY New Paltz Library - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("SUNY New Paltz Library");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 61, 121));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        frame.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1; frame.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; frame.add(passField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

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
                    // SUCCESS: Load the object and switch screens
                    patron user = patron.loadFromDatabase(username);
                    new LibraryDashboard(user).setVisible(true);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        createAccountButton.addActionListener(e -> showSignUpDialog(frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        });
    }

    private static void showSignUpDialog(JFrame parentFrame) {
        // ... (Insert your existing showSignUpDialog method here) ...
    }
}