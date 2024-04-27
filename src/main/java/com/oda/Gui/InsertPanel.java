package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.oda.Gui.MainGui.columns;
import static com.oda.Gui.MainGui.values;
import static com.oda.Main.*;

public class InsertPanel extends JPanel {
    public InsertPanel(ArrayList<String> columnArr, JFrame frame, String table, Container cp) {
        setLayout(null);
        JLabel label = new JLabel(String.format("Create a Dataset for the table %s", table));
        JTextField[] textFields = new JTextField[columnArr.size()];
        JLabel[] attributeLabel = new JLabel[textFields.length + 2];
        JButton createButton = new JButton("Confirm");
        JButton cancleButton = new JButton("Cancle");



        // Setting up the JComponents
        Panels.setLabel(label, this, bFont, 20, 20);
        Panels.setComponentWithColor(createButton, this, Color.WHITE, 45, 500, 150, 30);
        Panels.setComponentWithColor(cancleButton, this, Color.WHITE, 225, 500, 150, 30);
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
                Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160,55 + ((i + 1) * 40));
                first = true;
                repaint();
                revalidate();
            } else if (!columnArr.get(i).equals("id")) {
                if (first) {
                    Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + ((i + 1) * 40), 100, 20);
                    Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160,55 + ((i + 1) * 40));
                } else {
                    Panels.setComponentWithColor(textFields[i], this, Color.WHITE, 20, 60 + (i * 40), 100, 20);
                    Panels.setLabel(attributeLabel[i], this, font, columnArr.get(i), 160,55 + (i * 40));
                }

                repaint();
                revalidate();
            }
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
                showResult(frame, cp, table);
            } catch (SQLSyntaxErrorException  ex) {
                JOptionPane.showMessageDialog(null, "You do not have the privilege to create datasets on the table " + table, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Could not insert into table " + table, "Error", JOptionPane.ERROR_MESSAGE);
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
