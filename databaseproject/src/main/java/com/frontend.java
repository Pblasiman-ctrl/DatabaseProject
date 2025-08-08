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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class frontend {
    static final String databasePrefix = "bookstore";
    static final String netID = "dbadmin";
    static final String hostName = "34.71.180.162";
    static final String databaseURL = "jdbc:mysql://" + hostName + "/" + databasePrefix;
    static final String password = "QWTgqaHHpbpSAtOsTa07@";
    static final String tableCreationSQLFile = "/workspaces/DatabaseProject/TableCreation.sql";

    Connection sqlConnection;

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
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        // Create a panel
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create components
        JTextField usernameField = new JTextField("Enter username here");
        JTextField passwordField = new JTextField("Enter username here");
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
            }
        });

        // add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                if (doesUserExist(username, password)) {
                    System.out.println("User exists, proceeding to main UI.");
                    frame.dispose(); // Close the login window
                    showUI(); // Show the main UI
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
                String password = passwordField.getText();

                if (doesUserExist(username, password)) {
                    JOptionPane.showMessageDialog(frame,
                            "User already exists. Please log in or choose a different username.");
                } else {
                    Boolean registered = registerUser(username, password);
                    if (!registered) {
                        JOptionPane.showMessageDialog(frame, "Registration failed. Please try again.");
                        return;
                    } else {
                        System.out.println("User registered successfully.");
                        frame.dispose(); // Close the login window
                        showUI(); // Show the main UI
                    }
                }
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
        });

        // Add action listener to the exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptConnectionClose();
                System.exit(0);
            }
        });

        // Set the frame to close on exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                attemptConnectionClose();
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private void showUI() {
        JFrame frame = new JFrame("Bookstore Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Center welcome label
        JLabel label = new JLabel("Welcome to the Bookstore Management System!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.CENTER);

        // Top panel for logout button, aligned to the right
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> {
            attemptConnectionClose();
            frame.dispose();
            showLoginWindow();
        });

        // Set the frame to close on exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                attemptConnectionClose();
            }
        });

        topPanel.add(logoutButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add main panel to frame
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void attemptConnectionClose() {
        if (this.sqlConnection != null) {
            try {
                this.sqlConnection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection:");
                e.printStackTrace();
            }
        } else {
            System.err.println("Connection was never established.");
        }
    }

    boolean doesUserExist(String username, String password) {
        String sql = "{CALL find_reader(?, ?, ?)}";

        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);

            stmt.execute();

            if (stmt.getInt(3) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.err.println("SQL error during user check:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking user credentials. " + ex.getMessage());
            return false;
        }
    }
}