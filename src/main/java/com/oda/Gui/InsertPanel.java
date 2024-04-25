package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.oda.Gui.MainGui.columns;
import static com.oda.Gui.MainGui.values;
import static com.oda.Main.bFont;
import static com.oda.Main.connection;

public class InsertPanel extends JPanel {
    public InsertPanel(ArrayList<String> columnArr, JFrame frame, String table, Container cp) {
        setLayout(null);
        JLabel label = new JLabel(String.format("Create a Dataset for the table %s", table));
        JTextField[] textFields = new JTextField[columnArr.size()];
        JButton createButton = new JButton("Confirm");
        JButton cancleButton = new JButton("Cancle");


        // Setting up the JComponents
        Panels.setLabel(label, this, bFont, 20, 20);
        Panels.setComponentWithColor(createButton, this, Color.WHITE, 40, 500, 150, 30);
        Panels.setComponentWithColor(cancleButton, this, Color.WHITE, 220, 500, 150, 30);
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
        }
        for (int i = 1; i < textFields.length; i++) {
            textFields[i].setText(columnArr.get(i));
            Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + ((i - 1) * 40), 100, 20);
            repaint();
            revalidate();
        }

        // Adding Listeners to JComponents
        cancleButton.addActionListener(e -> {
            frame.setContentPane(cp);
            frame.repaint();
            frame.revalidate();
        });
        createButton.addActionListener(e -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                Statement statement = connection.createStatement();
                stringBuilder.append("'" + textFields[1].getText() + "'");
                for (int i = 2; i < textFields.length; i++) {
                    if (isNumeric(textFields[i].getText()))
                        stringBuilder.append(", " + textFields[i].getText());
                    else
                        stringBuilder.append(", " + "'" + textFields[i].getText() + "'");
                }
                values = stringBuilder.toString();
                System.out.println(String.format("INSERT INTO `%s`(%s) VALUES (%s);", table, columns, values));
                statement.execute(String.format("INSERT INTO `%s`(%s) VALUES (%s);", table, columns, values));
                JOptionPane.showMessageDialog(null, String.format("Inserted dataset to %s", table), "Dataset added", JOptionPane.INFORMATION_MESSAGE);
                frame.setContentPane(cp);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Could not insert into table " + table, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isNumeric(String str) {
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
