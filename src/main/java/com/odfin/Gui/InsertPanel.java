package com.odfin.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.util.ArrayList;

import static com.odfin.Gui.MainGui.columns;
import static com.odfin.Gui.MainGui.values;
import static com.odfin.Main.*;

public class InsertPanel extends JPanel {
    public InsertPanel(ArrayList<String> columnArr, JFrame frame, String table, Container cp) {
        setLayout(null);
        JLabel label = new JLabel(String.format("Create a Dataset for the table %s", table));
        JTextField[] textFields = new JTextField[columnArr.size()];
        JLabel[] attributeLabel = new JLabel[textFields.length + 2];
        JButton createButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");


        // Setting up the JComponents
        Panels.setLabel(label, this, bFont, 20, 20);
        Panels.setComponentWithColor(createButton, this, Color.WHITE, 45, 500, 150, 30);
        Panels.setComponentWithColor(cancelButton, this, Color.WHITE, 225, 500, 150, 30);
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            attributeLabel[i] = new JLabel();
        }
        attributeLabel[textFields.length] = new JLabel("Value");
        attributeLabel[textFields.length + 1] = new JLabel("Attribute");
        Panels.setLabel(attributeLabel[textFields.length], this, font, 20, 60);
        Panels.setLabel(attributeLabel[textFields.length + 1], this, font, 160, 60);
        boolean first = false;
        for (int i = 0; i < textFields.length; i++) {
            if (!columnArr.get(i).equals("id") && i < 1) {
                Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + ((i + 1) * 40), 100, 20);
                Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160, 55 + ((i + 1) * 40));
                first = true;
                repaint();
                revalidate();
            } else if (!columnArr.get(i).equals("id")) {
                if (first) {
                    Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + ((i + 1) * 40), 100, 20);
                    Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160, 55 + ((i + 1) * 40));
                } else {
                    Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + (i * 40), 100, 20);
                    Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160, 55 + (i * 40));
                }
                repaint();
                revalidate();
            }
        }

        // Adding Listeners to JComponents
        cancelButton.addActionListener(e -> {
            frame.setContentPane(cp);
            frame.repaint();
            frame.revalidate();
        });
        createButton.addActionListener(e -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                Statement statement = connection.createStatement();
                for (int i = 1; i < textFields.length; i++) {
                    if (textFields[i].getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                stringBuilder.append("'" + textFields[1].getText() + "'");
                for (int i = 2; i < textFields.length; i++) {
                    if (isNumeric(textFields[i].getText()))
                        stringBuilder.append(", ").append(textFields[i].getText());
                    else
                        stringBuilder.append(", " + "'").append(textFields[i].getText()).append("'");
                }
                values = stringBuilder.toString();
                System.out.printf("INSERT INTO `%s`(%s) VALUES (%s);%n", table, columns, values);
                statement.execute(String.format("INSERT INTO `%s`(%s) VALUES (%s);", table, columns, values));
                JOptionPane.showMessageDialog(null, String.format("Inserted dataset to %s", table), "Dataset added", JOptionPane.INFORMATION_MESSAGE);
                showResult(frame, cp, table);
            } catch (SQLException ex) {
                System.out.printf("Error code: %s%n", ex.getSQLState());
                ex.printStackTrace();
                switch (ex.getSQLState()) {
                    case ("42000"): {
                        JOptionPane.showMessageDialog(null, "You do not have the privilege to create datasets on the table " + table, "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    default: {
                        System.err.printf("Error code: %s%n", ex.getSQLState());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Could not insert into table " + table, "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
            }
        });
    }

    void showResult(JFrame frame, Container cp, String table) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(String.format("SELECT * FROM `%s`;", table));
            ResultSet resultSet = statement.getResultSet();
            TableGui tableGui = new TableGui(640, 480, resultSet, frame);
            tableGui.setTitle("Result Table");
            tableGui.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    frame.setContentPane(cp);
                    repaint();
                    revalidate();
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });

        } catch (SQLSyntaxErrorException ex) {
            System.err.println("How did you get this error?");
        } catch (SQLException ex) {
            System.err.println("Now I am really confused");
        }
    }
}
