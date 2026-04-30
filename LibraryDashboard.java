import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LibraryDashboard extends JFrame {
    private patron currentUser;
    
    // Browse Tab Components
    private DefaultTableModel browseModel;
    private JTable browseTable;
    private JTextField searchField;

    // My Account Tab Components
    private DefaultTableModel myLoansModel;
    private JTable myLoansTable;

    public LibraryDashboard(patron user) {
        this.currentUser = user;
        setTitle("SUNY New Paltz Library - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. TOP HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 61, 121)); // New Paltz Blue
        JLabel welcome = new JLabel("  Welcome, " + currentUser.getFirstName() + " (" + currentUser.getRole() + ")");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(welcome, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // 2. TABS INITIALIZATION
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Browse Library", createBrowsePanel());
        tabs.addTab("My Account", createMyAccountPanel());
        add(tabs, BorderLayout.CENTER);

        // Initial Data Load
        performSearch();
        loadMyLoans();

        setLocationRelativeTo(null);
    }

    // --- VIEW 1: THE BROWSE PANEL ---
    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Search Bar at Top
        JPanel searchBarPanel = new JPanel();
        searchField = new JTextField(25);
        JButton searchBtn = new JButton("Search Title/Author");
        searchBarPanel.add(new JLabel("Find a Book: "));
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchBtn);
        panel.add(searchBarPanel, BorderLayout.NORTH);

        // Table in Middle
        String[] cols = {"ID", "Title", "Author", "ISBN", "Status"};
        browseModel = new DefaultTableModel(cols, 0);
        browseTable = new JTable(browseModel);
        panel.add(new JScrollPane(browseTable), BorderLayout.CENTER);

        // Checkout Button at Bottom
        JButton checkoutBtn = new JButton("Confirm Checkout");
        checkoutBtn.setPreferredSize(new Dimension(200, 40));
        JPanel bottom = new JPanel();
        bottom.add(checkoutBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        // Listeners
        searchBtn.addActionListener(e -> performSearch());
        checkoutBtn.addActionListener(e -> handleCheckout());

        return panel;
    }

    // --- VIEW 2: THE MY ACCOUNT PANEL ---
    private JPanel createMyAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Books Currently Checked Out:", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.ITALIC, 14));
        panel.add(label, BorderLayout.NORTH);

        // Loans Table
        String[] cols = {"Book ID", "Title", "Due Date"};
        myLoansModel = new DefaultTableModel(cols, 0);
        myLoansTable = new JTable(myLoansModel);
        panel.add(new JScrollPane(myLoansTable), BorderLayout.CENTER);

        // Return Button
        JButton returnBtn = new JButton("Return Selected Book");
        returnBtn.setPreferredSize(new Dimension(200, 40));
        JPanel bottom = new JPanel();
        bottom.add(returnBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        returnBtn.addActionListener(e -> handleReturn());

        return panel;
    }

    // --- LOGIC: SEARCH ---
    private void performSearch() {
        String query = searchField.getText();
        browseModel.setRowCount(0);
        // Using "books" - ensure your table name matches (books or book)
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"), rs.getString("title"),
                    rs.getString("author"), rs.getString("isbn"),
                    (rs.getInt("available_copies") > 0 ? "Available" : "Out")
                };
                browseModel.addRow(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- LOGIC: LOAD PERSONAL LOANS ---
    private void loadMyLoans() {
        myLoansModel.setRowCount(0);
        String sql = "SELECT b.id, b.title, l.due_date FROM loans l " +
                     "JOIN books b ON l.book_id = b.id " +
                     "WHERE l.patron_id = ? AND l.return_date IS NULL";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = { rs.getInt("id"), rs.getString("title"), rs.getString("due_date") };
                myLoansModel.addRow(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- LOGIC: CHECKOUT WITH CONFIRMATION ---
    private void handleCheckout() {
        int row = browseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to checkout.");
            return;
        }

        int bookId = (int) browseModel.getValueAt(row, 0);
        String title = (String) browseModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Confirm checkout for: " + title + "?", "Checkout Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                conn.setAutoCommit(false);
                
                // 1. Add to loans
                String loanSql = "INSERT INTO loans (patron_id, book_id, loan_date, due_date) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
                PreparedStatement lp = conn.prepareStatement(loanSql);
                lp.setInt(1, currentUser.getId());
                lp.setInt(2, bookId);
                lp.executeUpdate();

                // 2. Decrement inventory
                String invSql = "UPDATE books SET available_copies = available_copies - 1 WHERE id = ? AND available_copies > 0";
                PreparedStatement ip = conn.prepareStatement(invSql);
                ip.setInt(1, bookId);
                
                if (ip.executeUpdate() > 0) {
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Success! Check 'My Account' for due dates.");
                    performSearch();
                    loadMyLoans(); // Update the other tab!
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Book is currently unavailable.");
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- LOGIC: RETURN ---
    private void handleReturn() {
        int row = myLoansTable.getSelectedRow();
        if (row == -1) return;

        int bookId = (int) myLoansModel.getValueAt(row, 0);

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE loans SET return_date = CURDATE() WHERE patron_id = ? AND book_id = ? AND return_date IS NULL";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setInt(2, bookId);

            if (pstmt.executeUpdate() > 0) {
                // Increment inventory
                PreparedStatement inv = conn.prepareStatement("UPDATE books SET available_copies = available_copies + 1 WHERE id = ?");
                inv.setInt(1, bookId);
                inv.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book Returned Successfully!");
                performSearch();
                loadMyLoans();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}