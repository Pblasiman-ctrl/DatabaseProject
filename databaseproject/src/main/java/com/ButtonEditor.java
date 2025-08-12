package com;

import javax.swing.*;
import java.awt.*;

abstract class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        protected JTable table;
        private int row;
        abstract void sqlQuery(frontend frontend, String isbn_13);

        public ButtonEditor(JCheckBox checkBox, JTable table, frontend frontend, String label) {
            super(checkBox);
            this.table = table;
            this.label = label;

            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> {
                fireEditingStopped();
                String isbn_13 = table.getValueAt(row, 0).toString();
                this.sqlQuery(frontend, isbn_13);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }