package com;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;

public class frontend {
    static final String databasePrefix = "bookstore";
    static final String netID = "dbadmin";
    static final String hostName = "34.71.180.162";
    static final String databaseURL = "jdbc:mysql://" + hostName + "/" + databasePrefix;
    static final String password = "QWTgqaHHpbpSAtOsTa07@";
    static final String tableCreationSQLFile = "/workspaces/DatabaseProject/TableCreation.sql";

    Connection sqlConnection;
    String loggedInUser;
    JPanel contentPanel;
    JTable table;

    public void run() {
        this.connectToDatabase();
        // this.createTables();
        this.showLoginWindow();
    }

    /*
     * private void createTables() {
     * String sql;
     * try {
     * sql = new String(Files.readAllBytes(Paths.get(tableCreationSQLFile)));
     * } catch (IOException e) {
     * System.out.println("Error reading SQL file: " + tableCreationSQLFile);
     * e.printStackTrace();
     * return;
     * }
     * Statement statement;
     * try {
     * statement = this.sqlConnection.createStatement();
     * } catch (SQLException e) {
     * e.printStackTrace();
     * return;
     * }
     * // Split SQL statements by semicolon (basic splitting)
     * String[] commands = sql.split("(?<!\\\\);");
     * 
     * for (String command : commands) {
     * command = command.trim();
     * if (!command.isEmpty()) {
     * try {
     * statement.execute(command);
     * System.out.println("Executed SQL command: " + command);
     * } catch (SQLException e) {
     * e.printStackTrace();
     * }
     * }
     * }
     * }
     */
    private void connectToDatabase() {
        System.out.println("Connecting to the database...");
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("databaseURL:" + databaseURL);
            this.sqlConnection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.attemptConnectionClose();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
            this.attemptConnectionClose();
        }
    }

    private void showLoginWindow() {
        // Create the frame
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        // Create a panel
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create components
        JTextField usernameField = new JTextField("Enter username here");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        // Align components to center and set preferred sizes
        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));
        loginButton.setMaximumSize(new Dimension(200, 30));
        registerButton.setMaximumSize(new Dimension(200, 30));
        exitButton.setMaximumSize(new Dimension(200, 30));

        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the panel with spacing
        panel.add(Box.createVerticalStrut(20));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(exitButton);

        // Add panel to frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (frontend.this.doesUserExist(username, password)) {
                    System.out.println("User exists, proceeding to main UI.");
                    frontend.this.loggedInUser = username;
                    frame.dispose(); // Close the login window
                    frontend.this.showNavigation(); // Show the main UI
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid username or password. Please check your username or password or register.");
                }
            }

        });

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (frontend.this.doesUserExist(username, password)) {
                    JOptionPane.showMessageDialog(frame,
                            "User already exists. Please log in or choose a different username.");
                } else {
                    Boolean registered = frontend.this.registerUser(username, password);
                    if (!registered) {
                        JOptionPane.showMessageDialog(frame, "Registration failed. Please try again.");
                        return;
                    } else {
                        System.out.println("User registered successfully.");
                        frontend.this.loggedInUser = username;
                        frame.dispose(); // Close the login window
                        frontend.this.showNavigation(); // Show the main UI
                    }
                }
            }
        });

        // Add action listener to the exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frontend.this.attemptConnectionClose();
                System.exit(0);
            }
        });

        // Set the frame to close on exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frontend.this.attemptConnectionClose();
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private void showNavigation() {
        JFrame frame = new JFrame("Bookstore Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // create left navigation panel
        JPanel leftNavPanel = new JPanel();
        leftNavPanel.setLayout(new BoxLayout(leftNavPanel, BoxLayout.Y_AXIS));
        leftNavPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton browseBoughtButton = new JButton("Browse Bought Books");
        JButton browseFavoritesButton = new JButton("Browse Favorite Books");
        JButton searchBooksButton = new JButton("Search Books");
        JButton browseByFavGenresButton = new JButton("Browse Books by Favorites");

        // add buttons to the left navigation panel
        leftNavPanel.add(browseBoughtButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(browseFavoritesButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(searchBooksButton);
        leftNavPanel.add(Box.createVerticalStrut(10));
        leftNavPanel.add(browseByFavGenresButton);

        // top bar with logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton logoutButton = new JButton("Logout");
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        // main plane
        contentPanel = new JPanel();
        JLabel label = new JLabel("Welcome to the Bookstore Management System!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(label, BorderLayout.CENTER);

        // add components to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leftNavPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // event listeners for buttons
        logoutButton.addActionListener(e -> {
            frame.dispose();
            frontend.this.showLoginWindow();
        });

        browseBoughtButton.addActionListener(e -> {
            String sql = "{CALL FindBooksBoughtByUser(?)}";
            try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
                stmt.setString(1, this.loggedInUser);
                ResultSet rs = stmt.executeQuery();
                // Build the model from result set
                DefaultTableModel model = buildTableModel(rs);

                // Add Action column to the model
                model.addColumn("Action");
                for (int i = 0; i < model.getRowCount(); i++) {
                    model.setValueAt("Buy", i, model.getColumnCount() - 1);
                }

                // Create the JTable
                this.table = new JTable(model);

                // Set renderers/editors for the Action column (last column)
                TableColumn actionColumn = table.getColumnModel().getColumn(table.getColumnCount() - 1);
                actionColumn.setCellRenderer(new ButtonRenderer("Sell"));
                actionColumn.setCellEditor(new SellButtonEditor(this, table));

                // Display in scroll pane
                JScrollPane scrollPane = new JScrollPane(table);
                contentPanel.removeAll();
                contentPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (SQLException ex) {
                System.err.println("SQL error during browsing bought books:");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error retrieving bought books. " + ex.getMessage());
            }
            contentPanel.revalidate(); // Refresh layout
            contentPanel.repaint(); // Refresh display
        });

        browseFavoritesButton.addActionListener(e -> {
            String sql = "{CALL FindUsersFavorites(?)}";
            try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
                stmt.setString(1, this.loggedInUser);
                ResultSet rs = stmt.executeQuery();

                // Build the model from result set
                DefaultTableModel model = buildTableModel(rs);

                // Add Action column to the model
                model.addColumn("Action");
                for (int i = 0; i < model.getRowCount(); i++) {
                    model.setValueAt("Buy", i, model.getColumnCount() - 1);
                }

                // Create the JTable
                JTable table = new JTable(model);

                // Set renderers/editors for the Action column (last column)
                TableColumn actionColumn = table.getColumnModel().getColumn(table.getColumnCount() - 1);
                actionColumn.setCellRenderer(new ButtonRenderer("Buy"));
                actionColumn.setCellEditor(new BuyButtonEditor(this, table));

                // Display in scroll pane
                JScrollPane scrollPane = new JScrollPane(table);
                contentPanel.removeAll();
                contentPanel.add(scrollPane, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error retrieving favorites: " + ex.getMessage());
            }
            contentPanel.revalidate(); // Refresh layout
            contentPanel.repaint(); // Refresh display
        });

        searchBooksButton.addActionListener(e -> {
            frontend.this.showSearchBookPanel();
        });

        browseByFavGenresButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Feature not yet implemented: Browse Books by Favorites.");
        });

        // if we close the window, we want to close the connection
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frontend.this.attemptConnectionClose();
            }
        });

        // add panel to the frame and make it visible
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private boolean registerUser(String username, String password) {
        String sql = "{CALL register_reader(?, ?)}";

        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            System.out.println("User registered successfully.");
            return true;
        } catch (SQLException ex) {
            System.err.println("SQL error during registration:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error registering user. " + ex.getMessage());
            return false;
        }
    }

    private boolean doesUserExist(String username, String password) {
        String sql = "{CALL find_reader(?, ?, ?)}";

        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);

            stmt.execute();

            return stmt.getInt(3) == 1;
        } catch (SQLException ex) {
            System.err.println("SQL error during user check:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking user credentials. " + ex.getMessage());
            return false;
        }
    }

    private void showSearchBookPanel() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        // search panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField isbnField = new JTextField();
        JButton searchButton = new JButton("Search");

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel()); // Spacer
        formPanel.add(searchButton);

        contentPanel.add(formPanel, BorderLayout.NORTH);

        // show results panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        contentPanel.add(resultPanel, BorderLayout.CENTER);

        // add search button action listener
        searchButton.addActionListener(ev -> {
            resultPanel.removeAll();

            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            String isbn = isbnField.getText().trim();
            /*
             * // Call stored procedure
             * String sql = "{CALL search_books(?, ?, ?, ?)}";
             * 
             * try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
             * stmt.setString(1, title);
             * stmt.setString(2, author);
             * stmt.setString(3, category);
             * stmt.setString(4, isbn);
             * 
             * try (ResultSet rs = stmt.executeQuery()) {
             * JTable table = new JTable(buildTableModel(rs));
             * table.setAutoCreateRowSorter(true);
             * JScrollPane scrollPane = new JScrollPane(table);
             * resultPanel.add(scrollPane, BorderLayout.CENTER);
             * }
             * 
             * } catch (SQLException ex) {
             * JOptionPane.showMessageDialog(null, "Error searching books: " +
             * ex.getMessage());
             * ex.printStackTrace();
             * }
             */

            resultPanel.revalidate();
            resultPanel.repaint();
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void attemptConnectionClose() {
        if (this.sqlConnection != null) {
            try {
                if (!this.sqlConnection.isClosed()) {
                    this.sqlConnection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to close connection:");
                e.printStackTrace();
            }
        } else {
            System.err.println("Connection was never established.");
        }
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
            data.add(row);
        }
        return new DefaultTableModel(data, columnNames);
    }
    public void reloadBoughtTable() {
    String sql = "CALL FindBooksBoughtByUser(?)"; // or your SELECT query
    try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
        stmt.setString(1, loggedInUser);
        ResultSet rs = stmt.executeQuery();

        TableModel model = buildTableModel(rs);
        this.table.setModel(model);

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
    }
}
}


    