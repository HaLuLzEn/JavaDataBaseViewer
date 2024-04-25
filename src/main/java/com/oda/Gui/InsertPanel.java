package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static com.oda.Main.bFont;

public class InsertPanel extends JPanel {
    public InsertPanel(ArrayList<String> columns, JFrame frame, String table) {
        setLayout(null);
        JLabel label = new JLabel(String.format("Create a Dataset for the table %s", table));
        JTextField[] textFields = new JTextField[columns.size()];
        JButton createButton = new JButton("Confirm");
        JButton cancleButton = new JButton("Cancle");

        Panels.setLabel(label, this, bFont,20,20);

        for (int i = 1; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            final int ev = i;
            textFields[i].addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    textFields[ev].setText("");
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textFields[ev].getText().isEmpty())
                        textFields[ev].setText(columns.get(ev));
                }
            });
        }

        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText(columns.get(i));
            Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + (i * 40), 100, 20);
            repaint();
            revalidate();
        }
    }
}
