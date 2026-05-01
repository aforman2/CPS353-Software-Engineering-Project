package edu.newpaltz.library.ui;
import javax.swing.*; // GUI components (JFrame, JButton, etc.)
import javax.swing.table.DefaultTableModel; // Handles the data grid for tables
import java.awt.*; // Layouts and Colors
import java.sql.*; // Database connectivity
import edu.newpaltz.library.models.Book; // Book model for representing book data
import edu.newpaltz.library.models.patron; // Patron model for representing user data
import edu.newpaltz.library.config.DatabaseConfig; // Database connection configuration

// dashboard is a window (inherits all properties of standard operating system)
public class LibraryDashboard extends JFrame {
    private patron currentUser; //store logged-in user information
    
    // Browse Tab Components
    private DefaultTableModel browseModel; //backend data grid for searching books
    private JTable browseTable; //frontend visuual table for searching books
    private JTextField searchField;

    // My Account Tab Components
    private DefaultTableModel myLoansModel; //data grid for user's personal checkouts
    private JTable myLoansTable; //visual table for the user's personal checkouts

    // Global Column Names
    private final String[] browseCols = {"ID", "Title", "Author", "ISBN", "Status"}; //header for browse/search page
    private final String[] loanCols = {"Book ID", "Title", "Due Date"}; //header for my loans page

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

        JButton logoutBtn = new JButton("Logout");
        header.add(logoutBtn, BorderLayout.EAST);
        //goes back to login page after logging out
        logoutBtn.addActionListener(e -> {
            new LoginUI(); 
            this.dispose(); 
        });

        add(header, BorderLayout.NORTH);

        // 2. TABS INITIALIZATION (UI must be built before loading data)
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Browse Library", createBrowsePanel());
        tabs.addTab("My Account", createMyAccountPanel());
        add(tabs, BorderLayout.CENTER);

        // 3. Initial Data Load = fills the tables with data from the database when the dashboard is first opened
        performSearch();
        loadMyLoans();

        setLocationRelativeTo(null);
    }

    // VIEW 1: THE BROWSE PANEL
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

        // Table in Middle - FIXED: Removed local variable declaration
        browseModel = new DefaultTableModel(browseCols, 0);
        browseTable = new JTable(browseModel);
        browseTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

        // Table in Middle - FIXED: Removed local variable declaration
        myLoansModel = new DefaultTableModel(loanCols, 0);
        myLoansTable = new JTable(myLoansModel);
        panel.add(new JScrollPane(myLoansTable), BorderLayout.CENTER);

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
        browseModel.setRowCount(0); // Clear befor every new search
        
        // FIXED: Using 'book' (singular) to match your DB
        String sql = "SELECT * FROM book WHERE title LIKE ? OR author LIKE ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book( //convert database row into java object (Object-Relational Mapping best swe principle)
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("available_copies")
                );

                Object[] row = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getStatus() 
                };
                browseModel.addRow(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- LOGIC: LOAD PERSONAL LOANS ---
    private void loadMyLoans() {
        myLoansModel.setRowCount(0);

        // FIXED: Using 'book' (singular)
        String sql = "SELECT b.id AS bId, b.title, l.due_date " +
                     "FROM loans l " +
                     "JOIN book b ON l.book_id = b.id " +
                     "WHERE l.patron_id = ? AND l.return_date IS NULL";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("bId"),     
                    rs.getString("title"), 
                    rs.getDate("due_date") 
                };
                myLoansModel.addRow(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- LOGIC: CHECKOUT ---
    private void handleCheckout() {
        int[] selectedRows = browseTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one book.");
            return;
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String countSql = "SELECT COUNT(*) FROM loans WHERE patron_id = ? AND return_date IS NULL";
            PreparedStatement countPstmt = conn.prepareStatement(countSql);
            countPstmt.setInt(1, currentUser.getId());
            ResultSet rs = countPstmt.executeQuery();
            
            int currentLoans = 0;
            if (rs.next()) currentLoans = rs.getInt(1);

            if (currentLoans + selectedRows.length > 3) {
                JOptionPane.showMessageDialog(this, "Limit reached! You can only have 3 books out. Currently: " + currentLoans);
                return;
            }

            conn.setAutoCommit(false); //starts a transaction
            
            for (int row : selectedRows) {
                int bookId = (int) browseModel.getValueAt(row, 0);
                
                String loanSql = "INSERT INTO loans (patron_id, book_id, loan_date, due_date) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
                PreparedStatement lp = conn.prepareStatement(loanSql);
                lp.setInt(1, currentUser.getId());
                lp.setInt(2, bookId);
                lp.executeUpdate();

                // FIXED: Using 'book' (singular)
                String invSql = "UPDATE book SET available_copies = available_copies - 1 WHERE id = ? AND available_copies > 0";
                PreparedStatement ip = conn.prepareStatement(invSql);
                ip.setInt(1, bookId);
                
                if (ip.executeUpdate() == 0) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Book ID " + bookId + " is unavailable.");
                    return;
                }
            }

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "Checkout successful!");
            performSearch();
            loadMyLoans();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- LOGIC: RETURN ---
    private void handleReturn() {
        int row = myLoansTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.");
            return;
        }

        int bookId = (int) myLoansModel.getValueAt(row, 0);

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE loans SET return_date = CURDATE() WHERE patron_id = ? AND book_id = ? AND return_date IS NULL";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setInt(2, bookId);

            if (pstmt.executeUpdate() > 0) {
                // FIXED: Using 'book' (singular)
                PreparedStatement inv = conn.prepareStatement("UPDATE book SET available_copies = available_copies + 1 WHERE id = ?");
                inv.setInt(1, bookId);
                inv.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book Returned!");
                performSearch();
                loadMyLoans();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}