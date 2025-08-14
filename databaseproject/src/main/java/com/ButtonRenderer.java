package com;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// ButtonRenderer class that renders a button in a JTable cell
class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String caption) {
            setOpaque(true);
            setText(caption);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }