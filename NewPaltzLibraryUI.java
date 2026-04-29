import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;

public class NewPaltzLibraryUI {
    public static void main(String[] args) {
        // Create the frame (the window)
        JFrame frame = new JFrame("SUNY New Paltz Library - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // 1. Title Label
        JLabel titleLabel = new JLabel("SUNY New Paltz Library");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 61, 121)); // New Paltz Blue-ish hue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        frame.add(titleLabel, gbc);

        // 2. Username Label and Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JTextField userField = new JTextField(15);
        frame.add(userField, gbc);

        // 3. Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        frame.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField passField = new JPasswordField(15);
        frame.add(passField, gbc);

        // 4. Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(buttonPanel, gbc);

        // Set visibility
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        // ... inside your main method, after creating the buttons ...

        // login button
        // LOGIN BUTTON LOGIC
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter all credentials.");
                return;
            }

            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "SELECT first_name FROM patrons WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                java.sql.ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Welcome back, " + rs.getString("first_name"));
                    // frame.dispose(); // Optional: Close login window
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            }
        });

        // CREATE ACCOUNT BUTTON LOGIC
        createAccountButton.addActionListener(e -> {
            showSignUpDialog(frame);

        });
    }

    private static void showSignUpDialog(JFrame parentFrame) {
        // Create the popup window (JDialog)
        JDialog signUpDialog = new JDialog(parentFrame, "Create New Account", true);
        signUpDialog.setSize(300, 400);
        signUpDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create fields for the popup
        JTextField fNameField = new JTextField(15);
        JTextField lNameField = new JTextField(15);
        JTextField uNameField = new JTextField(15);
        JPasswordField uPassField = new JPasswordField(15);
        JButton submitButton = new JButton("Register");
        submitButton.setBackground(new Color(0, 61, 121)); // New Paltz Blue
        submitButton.setForeground(Color.WHITE); // White text for contrast

        // Add components to the dialog (GridBagLayout positioning)
        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpDialog.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        signUpDialog.add(fNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        signUpDialog.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        signUpDialog.add(lNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpDialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        signUpDialog.add(uNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        signUpDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        signUpDialog.add(uPassField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        signUpDialog.add(submitButton, gbc);

        // Logic for the Submit Button
        submitButton.addActionListener(e -> {
            String fName = fNameField.getText();
            String lName = lNameField.getText();
            String uName = uNameField.getText();
            String uPass = new String(uPassField.getPassword());

            if (fName.isEmpty() || lName.isEmpty() || uName.isEmpty() || uPass.isEmpty()) {
                JOptionPane.showMessageDialog(signUpDialog, "All fields are required!");
                return;
            }

            // DATABASE LOGIC
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "INSERT INTO patrons (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, uName);
                pstmt.setString(2, uPass);
                pstmt.setString(3, fName);
                pstmt.setString(4, lName);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(signUpDialog, "Account created! You can now log in.");
                signUpDialog.dispose(); // Close the popup
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(signUpDialog, "Username already taken.");
                } else {
                    ex.printStackTrace();
                }
            }
        });

        signUpDialog.setLocationRelativeTo(parentFrame);
        signUpDialog.setVisible(true);
    }
}