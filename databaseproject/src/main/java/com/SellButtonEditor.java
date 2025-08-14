package com;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

// SellButtonEditor class that extends ButtonEditor for selling books
public class SellButtonEditor extends ButtonEditor {
    Connection sqlConnection;
    frontend frontend;
    String isbn_13;

    // Constructor that initializes the editor with a JCheckBox, JTable, frontend instance, and label
    public SellButtonEditor(frontend frontend, JTable table) {
        super(new JCheckBox(), table, frontend, "Sell");
        this.frontend = frontend;
        this.sqlConnection = frontend.sqlConnection;
    }

    // SQL query method to handle the book selling logic
    // It checks if the book can be sold and executes the SQL procedure to sell it
    // If the book cannot be sold, it shows an error message
    @Override
    protected void sqlQuery(frontend frontend, String isbn_13) {
        if(!canSellBook(isbn_13)) {
            JOptionPane.showMessageDialog(table, "You cannot sell this book.");
            return;
        }
        // Proceed with selling the book
        this.isbn_13 = isbn_13;
        String sql = "{CALL SellBook(?, ?)}";
        String userId = frontend.loggedInUser;

        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, isbn_13);
            stmt.setString(2, userId);
            stmt.execute();
            JOptionPane.showMessageDialog(table, "Book sold successfully!");
        } catch (SQLException ex) {
            System.err.println("SQL error during book sale:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(table, "Error selling book: " + ex.getMessage());
        }
        frontend.reloadBoughtTable();
    }

    // Method to check if the book can be sold
    private boolean canSellBook(String isbn_13) {
        String sql = "{CALL CanSellBook(?, ?, ?)}"; // ISBN, userId, OUT canSell
        try (CallableStatement stmt = frontend.sqlConnection.prepareCall(sql)) {
            stmt.setString(1, isbn_13);
            stmt.setString(2, frontend.loggedInUser);
            stmt.registerOutParameter(3, java.sql.Types.BOOLEAN); // index starts from 1

            stmt.execute(); // Use execute() when working with OUT parameters
            return stmt.getBoolean(3); // Retrieve the output parameter
        } catch (SQLException ex) {
            System.err.println("Error checking if book can be sold:");
            ex.printStackTrace();
        }
        return false;
    }
}
