import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class frontend {
    static final String databasePrefix ="bookstore";
    static final String netID ="dbadmin";
    static final String hostName ="34.71.180.162"; 
    static final String databaseURL ="jdbc:mysql://"+hostName+"/"+databasePrefix;
    static final String password="QWTgqaHHpbpSAtOsTa07@";

    public void run() {
        this.connectToDatabase();
        this.showLoginWindow();
    }
    private void connectToDatabase() {
        System.out.println("Connecting to the database...");
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("databaseURL:"+ databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");
         }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
        } finally {
if (connection != null) {
            try {
                connection.close();
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
        panel.add(loginButton);
        
        // Create a text area
        JTextArea textArea = new JTextArea("This is a text area");
        panel.add(textArea);
        
        // Add action listener to the search button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        // Make the frame visible
        frame.setVisible(true);
    }
}