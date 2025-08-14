package com;

import javax.swing.*;
import java.awt.*;

// abstract class that defines the structure for button editors in a JTable
abstract class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    protected boolean clicked;
    protected JTable table;
    private int row;

    abstract void sqlQuery(frontend frontend, String isbn_13);

    public ButtonEditor(JCheckBox checkBox, JTable table, frontend frontend, String label) {
        super(checkBox); // initialize the editor with a JCheckBox
        this.table = table;
        this.label = label;

        button = new JButton();
        button.setOpaque(true);

        // set the button's action when clicked
        button.addActionListener(e -> {
            // get the column index of the ISBN column
            int isbnCol = findIsbnColumn(table);
            if (isbnCol == -1) {
                JOptionPane.showMessageDialog(table, "ISBN column not found.");
                return;
            }
            // get the ISBN value from the clicked row
            String isbn_13 = table.getValueAt(row, isbnCol).toString();

            fireEditingStopped();

            // call the SQL query method with the frontend and ISBN
            this.sqlQuery(frontend, isbn_13);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // set the button's text and row index when the editor is activated
        this.row = row;
        button.setText(label);
        clicked = true;
        return button;
    }

    // add label to the button when editing is stopped
    @Override
    public Object getCellEditorValue() {
        return label;
    }

    // check if the button was clicked to stop editing
    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    // find the index of the ISBN column in the table
    private int findIsbnColumn(JTable table) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            String header = table.getColumnName(col);
            if ("isbn_13".equalsIgnoreCase(header.trim())) {
                return col;
            }
        }
        return -1;
    }
}