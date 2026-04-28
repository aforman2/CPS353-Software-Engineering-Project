import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both credentials.");
                return;
            }

            try {
                // Attempt to load the patron from their folder
                patron existingUser = patron.userBuilder(username);

                // Check if password matches
                if (existingUser.getUPass().equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful! Welcome, " + existingUser.getFirstName());
                    
                    // For testing: print their info to console
                    existingUser.printPatronInfo();
                    existingUser.listCheckedOutBooks();
                    
                    // You could now hide this frame and open the main library dashboard
                    // frame.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
});
    }
}