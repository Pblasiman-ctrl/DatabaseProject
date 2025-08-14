package com;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

// BuyButtonEditor class that extends ButtonEditor for buying books
public class BuyButtonEditor extends ButtonEditor {
    public BuyButtonEditor(frontend frontend, JTable table) {
        super(new JCheckBox(), table, frontend, "Buy");
    }

    // SQL query method to handle the book buying logic
    @Override
    protected void sqlQuery(frontend frontend, String isbn_13) {
        String sql = "{CALL BuyBook(?, ?)}";
        String userId = frontend.loggedInUser;
        if (!frontend.canBuyBook(isbn_13)) {
            JOptionPane.showMessageDialog(table, "You already own this book.");
            return;
        }
        Connection sqlConnection = frontend.sqlConnection;
        try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
            stmt.setString(1, isbn_13);
            stmt.setString(2, userId);
            stmt.execute();
            JOptionPane.showMessageDialog(table, "Book bought successfully!");
        } catch (SQLException ex) {
            System.err.println("SQL error during book purchase:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(table, "Error buying book: " + ex.getMessage());
        }
    }
}
