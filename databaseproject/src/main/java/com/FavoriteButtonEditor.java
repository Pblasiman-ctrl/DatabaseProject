package com;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

// FavoriteButtonEditor class that extends ButtonEditor for managing favorite books
public class FavoriteButtonEditor extends ButtonEditor {
    Connection sqlConnection;
    frontend frontend;
    String isbn_13;

    // Constructor that initializes the editor with a JCheckBox, JTable, frontend instance, and label
    public FavoriteButtonEditor(frontend frontend, JTable table) {
        super(new JCheckBox(), table, frontend, "Favorite");
        this.frontend = frontend;
        this.sqlConnection = frontend.sqlConnection;
    }

    // SQL query method to handle the favorite/unfavorite logic
    // If the book is already favorited, it will unfavorite it; otherwise, it will favorite it
    @Override
    protected void sqlQuery(frontend frontend, String isbn_13) {
        if (!frontend.canFavoriteBook(isbn_13)) {
             this.isbn_13 = isbn_13;
            String sql = "{CALL UnFavoriteBook(?, ?)}";
            String userId = frontend.loggedInUser;
            try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
                stmt.setString(1, isbn_13);
                stmt.setString(2, userId);
                stmt.execute();
                JOptionPane.showMessageDialog(table, "Book unfavorited successfully!");
            } catch (SQLException ex) {
                System.err.println("SQL error during book unfavoriting:");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(table, "Error unfavoriting book: " + ex.getMessage());
            }
        } else {
            // proceed with favoriting the book
            this.isbn_13 = isbn_13;
            String sql = "{CALL FavoriteBook(?, ?)}";
            String userId = frontend.loggedInUser;
            try (CallableStatement stmt = sqlConnection.prepareCall(sql)) {
                stmt.setString(1, isbn_13);
                stmt.setString(2, userId);
                stmt.execute();
                JOptionPane.showMessageDialog(table, "Book favorited successfully!");
            } catch (SQLException ex) {
                System.err.println("SQL error during book favoriting:");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(table, "Error favoriting book: " + ex.getMessage());
            }
        }
    }
}
