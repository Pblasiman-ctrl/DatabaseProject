import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.createTables();
        this.showLoginWindow();
        this.attemptConnectionClose();
    }

    private void createTables() {
        String sql;
        try {
            sql = new String(Files.readAllBytes(Paths.get(tableCreationSQLFile)));
        } catch (IOException e) {
            System.out.println("Error reading SQL file: " + tableCreationSQLFile);
            e.printStackTrace();
            return;
        }
            Statement statement = this.sqlConnection.createStatement()
            // Split SQL statements by semicolon (basic splitting)
            String[] commands = sql.split("(?<!\\\\);");

            for (String command : commands) {
                command = command.trim();
                if (!command.isEmpty()) {
                    statement.execute(command);
                }
            }
    }

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

        JTextField textField = new JTextField("Enter username here");
        textField.setBounds(50, 50, 200, 30); // x, y, width, height

        // Create a login button
        JButton loginButton = new JButton("Login");

        // Create a exit button
        JButton exitButton = new JButton("Exit");

        // add buttons
        panel.add(textField);
        panel.add(loginButton);
        panel.add(exitButton);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        // Make the frame visible
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
}