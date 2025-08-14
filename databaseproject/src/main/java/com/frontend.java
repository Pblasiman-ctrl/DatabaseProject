package com;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class frontend {
    // set up contants for database connection
    static final String databasePrefix = "bookstore2";
    static final String netID = "dbadmin";
    static final String hostName = "localhost";
    static final String databaseURL = "jdbc:mysql://" + hostName + ":3306/" + databasePrefix
            + "?useInformationSchema=true";
    static final String password = "QWTgqaHHpbpSAtOsTa07@";

    // dynamic variables for the frontend
    public Connection sqlConnection;
    public String loggedInUser;
    private JPanel contentPanel;
    private JTable table;

    // Main method to start the application
    public void run() {
        connectToDatabase();
        showLoginWindow();
    }

    // Connect to the database
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConnection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Connected to database.");
        } catch (Exception e) {
            e.printStackTrace();
            attemptConnectionClose();
        }
    }

    // Show the login window
    private void showLoginWindow() {
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setLocationRelativeTo(null);

        // Create a panel for the login form
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // add username and password fields
        panel.add(new JLabel("Username:"));
        panel.add(Box.createVerticalStrut(5));
        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 25));
        panel.add(usernameField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Password:"));
        panel.add(Box.createVerticalStrut(5));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 25));
        panel.add(passwordField);

        // add login, register, and exit buttons
        panel.add(Box.createVerticalStrut(15));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);
        panel.add(buttonPanel);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // add action listeners for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (doesUserExist(username, password)) {
                loggedInUser = username;
                frame.dispose();
                showNavigation();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid username or password. Plese check your credentials or register a new account.");
            }
        });

        // add action listener for the register button
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (doesUserExist(username, password)) {
                JOptionPane.showMessageDialog(frame, "User already exists.");
            } else if (registerUser(username, password)) {
                loggedInUser = username;
                frame.dispose();
                showNavigation();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Registration failed. Please verify that an account with this username does not already exist.");
            }

        });

        // add action listener for the exit button
        exitButton.addActionListener(e -> {
            attemptConnectionClose();
            System.exit(0);
        });
    }

    // Show the main navigation panel
    private void showNavigation() {
        // create the main frame and panels
        JFrame frame = new JFrame("Bookstore Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel leftNavPanel = new JPanel();
        leftNavPanel.setLayout(new BoxLayout(leftNavPanel, BoxLayout.Y_AXIS));
        leftNavPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // create buttons to add to the left navigation panel
        JButton browseBoughtButton = new JButton("Browse Bought Books");
        JButton browseFavoritesButton = new JButton("Browse Favorite Books");
        JButton searchBooksButton = new JButton("Search Books");
        JButton browseByFavButton = new JButton("Browse Top Favorited Books");

        // add buttons to the left navigation panel
        leftNavPanel.add(browseBoughtButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(browseFavoritesButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(searchBooksButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(browseByFavButton);

        // create a logout button and add it to the top right corner
        JButton logoutButton = new JButton("Logout");
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);

        // create the content panel to display a welcome message and results when loaded
        contentPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Bookstore Management System!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        mainPanel.add(logoutPanel, BorderLayout.NORTH);
        mainPanel.add(leftNavPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

        // add action listeners for the logout button and set it to close the frame and
        // show the login window
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });

        // add action listeners for the navigation buttons
        browseBoughtButton.addActionListener(e -> reloadBoughtTable());
        browseFavoritesButton.addActionListener(e -> reloadFavoritesTable());
        searchBooksButton.addActionListener(e -> showSearchBookPanel());
        browseByFavButton.addActionListener(e -> showTopFavoritesPanel());
    }

    // Show the top favorites panel, which allows users to view the most favorited
    // books
    private void showTopFavoritesPanel() {
        // clear previous content
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        // create a panel for the top favorites
        JPanel topPanelFav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // add a label and a combo box to select the number of top favorites to show
        topPanelFav.add(new JLabel("Show top:"));
        String[] options = { "10", "20", "50", "100" };
        JComboBox<String> comboBox = new JComboBox<>(options);
        topPanelFav.add(comboBox);
        contentPanel.add(topPanelFav, BorderLayout.NORTH);

        // create a panel to hold the table of results
        JPanel tablePanel = new JPanel(new BorderLayout());
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // add an action listener to the combo box to reload the table when the
        // selection changes
        comboBox.addActionListener(ev -> reloadFavoriteTable(comboBox, tablePanel));

        // load the table with 10 top favorites by default
        comboBox.setSelectedIndex(0);

        // Load the initial table
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Register a new user in the database via SQL call to the database
    // Returns true if registration was successful, false otherwise
    private boolean registerUser(String username, String password) {
        String sql = "{CALL register_reader(?, ?)}";
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Check if a user exists in the database
    // Returns true if the user exists, false otherwise
    private boolean doesUserExist(String username, String password) {
        String sql = "{CALL find_reader(?, ?, ?)}";
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(3) == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Attempt to close the SQL connection if it exists
    private void attemptConnectionClose() {
        if (sqlConnection != null) {
            try {
                if (!sqlConnection.isClosed())
                    sqlConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Build a DefaultTableModel from a ResultSet
    // Converts the ResultSet from SQL queries into a format that can be used by
    // JTable
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int col = 1; col <= columnCount; col++)
            columnNames.add(metaData.getColumnName(col));

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int col = 1; col <= columnCount; col++)
                row.add(rs.getObject(col));
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // Reload the table of bought books for the logged-in user
    public void reloadBoughtTable() {
        String sql = "CALL FindBooksBoughtByUser(?)";
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            // Set the logged-in user as a parameter for the SQL call
            stmt.setString(1, loggedInUser);
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = buildTableModel(rs);

            // Set the last column as a button column for selling books
            model.addColumn("Action");
            for (int i = 0; i < model.getRowCount(); i++)
                model.setValueAt("Sell", i, model.getColumnCount() - 1);
            table = new JTable(model);
            TableColumn actionCol = table.getColumnModel().getColumn(model.getColumnCount() - 1);
            actionCol.setCellRenderer(new ButtonRenderer("Sell"));
            actionCol.setCellEditor(new SellButtonEditor(this, table));

            // Create a scroll pane for the table and add it to the content panel
            JScrollPane pane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(pane, BorderLayout.CENTER);

            // Refresh the content panel to show the new table
            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading bought books: " + ex.getMessage());
        }
    }

    // Reload the table of favorite books for the logged-in user
    public void reloadFavoritesTable() {
        String sql = "CALL FindUsersFavorites(?)"; // SQL procedure to get the favorite books of the logged-in user
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            // execute the SQL call with the logged-in user as a parameter and build the
            // table model
            stmt.setString(1, loggedInUser);
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = buildTableModel(rs);

            // Add an action column for buying books and set the button renderer and editor
            model.addColumn("Action");
            for (int i = 0; i < model.getRowCount(); i++)
                model.setValueAt("Buy", i, model.getColumnCount() - 1);
            table = new JTable(model);

            // Set the last column as a button column for buying books
            TableColumn actionCol = table.getColumnModel().getColumn(model.getColumnCount() - 1);
            actionCol.setCellRenderer(new ButtonRenderer("Buy"));
            actionCol.setCellEditor(new BuyButtonEditor(this, table));

            // Create a scroll pane for the table and add it to the content panel
            JScrollPane pane = new JScrollPane(table);

            // Clear the content panel and add the new table pane
            contentPanel.removeAll();
            contentPanel.add(pane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading favorites: " + ex.getMessage());
        }
    }

    // Reload the table of top favorited books based on the selected number from the
    // combo box
    public void reloadFavoriteTable(JComboBox<String> comboBox, JPanel tablePanel) {
        String sql = "CALL TopFavoritedBooks(?)";
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            // Set the number of top favorites to show based on the combo box selection
            stmt.setString(1, (String) comboBox.getSelectedItem());

            // Execute the SQL call and build the table model
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = buildTableModel(rs);

            // Add an action column for buying books and set the button renderer and editor
            model.addColumn("Action");
            for (int i = 0; i < model.getRowCount(); i++)
                model.setValueAt("Buy", i, model.getColumnCount() - 1);
            table = new JTable(model);
            TableColumn actionCol = table.getColumnModel().getColumn(model.getColumnCount() - 1);
            actionCol.setCellRenderer(new ButtonRenderer("Buy"));
            actionCol.setCellEditor(new BuyButtonEditor(this, table));

            // Create a scroll pane for the table and add it to the table panel
            JScrollPane pane = new JScrollPane(table);
            tablePanel.removeAll();

            // Add the new table pane to the table panel and refresh it
            tablePanel.add(pane, BorderLayout.CENTER);
            tablePanel.revalidate();
            tablePanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading top favorites: " + ex.getMessage());
        }
    }

    public void showSearchBookPanel() {
        // Clear previous content
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        // create top panel for search inputs
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));

        // set up grid bag constraints for the form panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // create title label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField titleField = new JTextField();
        formPanel.add(titleField, gbc);

        // create uthor label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField authorField = new JTextField();
        formPanel.add(authorField, gbc);

        // create category label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField categoryField = new JTextField();
        formPanel.add(categoryField, gbc);

        // create ISBN label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField isbnField = new JTextField();
        formPanel.add(isbnField, gbc);

        // add a search button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchButton = new JButton("Search");
        formPanel.add(searchButton, gbc);

        // add the form panel to the content panel
        contentPanel.add(formPanel, BorderLayout.NORTH);

        // create a panel for the results of the query
        JPanel resultPanel = new JPanel(new BorderLayout());
        contentPanel.add(resultPanel, BorderLayout.CENTER);

        // add addition listener for the search button to execute the search
        searchButton.addActionListener(ev -> {
            this.reloadSearchTable(titleField, authorField, categoryField, isbnField, resultPanel);
        });

        // add a listener to reload the search table when the Enter key is pressed in
        // any of the text fields
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    reloadSearchTable(titleField, authorField, categoryField, isbnField, resultPanel);
                }
            }
        };
        titleField.addKeyListener(enterKeyListener);
        authorField.addKeyListener(enterKeyListener);
        categoryField.addKeyListener(enterKeyListener);
        isbnField.addKeyListener(enterKeyListener);

        // refresh the content panel to show the search form and results
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Reload the search table based on the input fields
    public void reloadSearchTable(JTextField titleField,
            JTextField authorField,
            JTextField categoryField,
            JTextField isbnField,
            JPanel resultPanel) {
        final String sql = "{ CALL SearchBooksMultiFilter(?, ?, ?, ?) }";

        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, titleField.getText());
            stmt.setString(2, authorField.getText());
            stmt.setString(3, categoryField.getText());

            // handle ISBN input, allowing for blank inputs since it's a BIGINT in SQL
            String rawIsbn = isbnField.getText().trim();
            if (rawIsbn.isBlank()) {
                stmt.setNull(4, java.sql.Types.BIGINT);
            } else {
                try {
                    long isbn = Long.parseLong(rawIsbn);
                    stmt.setLong(4, isbn);
                } catch (NumberFormatException nfe) {
                    // Treat bad input as "no ISBN filter"
                    stmt.setNull(4, java.sql.Types.BIGINT);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                DefaultTableModel model = buildTableModel(rs);

                // add action columns for buying and favoriting books
                int buyCol = model.getColumnCount();
                model.addColumn("Buy");
                int favCol = model.getColumnCount();
                model.addColumn("Favorite");

                // populate action columns
                for (int i = 0; i < model.getRowCount(); i++) {
                    model.setValueAt("Buy/Sell", i, buyCol);
                    model.setValueAt("Favorite/Unfavorite", i, favCol);
                }

                // create the table with the model
                table = new JTable(model);

                // add renderers and editors for action columns
                TableColumn buyTableColumn = table.getColumnModel().getColumn(buyCol);
                TableColumn favTableColumn = table.getColumnModel().getColumn(favCol);

                buyTableColumn.setCellRenderer(new ButtonRenderer("Buy/Sell"));
                buyTableColumn.setCellEditor(new BuyButtonEditor(this, table));

                favTableColumn.setCellRenderer(new ButtonRenderer("Favorite/Unfavorite"));
                favTableColumn.setCellEditor(new FavoriteButtonEditor(this, table));

                // create a scroll pane for the table and add it to the result panel
                JScrollPane pane = new JScrollPane(table);
                resultPanel.removeAll();
                resultPanel.add(pane, BorderLayout.CENTER);

                // refresh the result panel to show the new table
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error searching books: " + ex.getMessage());
        }
    }

    // Check if a book can be favorited by the logged-in user
    public boolean canFavoriteBook(String isbn_13) {
        String sql = "{CALL CanFavoriteBook(?, ?, ?)}"; // ISBN, userId, OUT canFavorite
        try (CallableStatement stmt = this.sqlConnection.prepareCall(sql)) {
            stmt.setString(1, isbn_13);
            stmt.setString(2, this.loggedInUser);
            stmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
            stmt.execute();
            return stmt.getBoolean(3); // Retrieve the output parameter
        } catch (SQLException ex) {
            System.err.println("Error checking if book can be favorited:");
            ex.printStackTrace();
        }
        return false;
    }

    // Check if a book can be bought by the logged-in user
    // Returns true if the book can be bought, false otherwise
    public boolean canBuyBook(String isbn_13) {
        String sql = "{CALL CanSellBook(?, ?, ?)}";
        try (CallableStatement stmt = this.sqlConnection.prepareCall(sql)) {
            stmt.setString(1, isbn_13);
            stmt.setString(2, this.loggedInUser);
            stmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
            stmt.execute();
            return !stmt.getBoolean(3);
        } catch (SQLException ex) {
            System.err.println("Error checking if book can be bought:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking book purchase eligibility: " + ex.getMessage());
            return false;
        }
    }
}