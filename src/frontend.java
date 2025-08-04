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
    static final String netID ="pblasiman"; // Please enter your netId
    static final String hostName ="localhost"; 
    static final String databaseURL ="jdbc:mysql://"+hostName+"/"+databasePrefix;
    static final String password="3BNFq6xjZIgJmdhNarAC";

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
        }
        
        finally {
            try {
            connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
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
        
        // Create a layout for the panel
      //  panel.setLayout(new GridLayout(2, 1));
        
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