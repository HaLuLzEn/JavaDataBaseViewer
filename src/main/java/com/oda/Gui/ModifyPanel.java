package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.oda.Gui.MainGui.columns;
import static com.oda.Gui.MainGui.values;
import static com.oda.Main.*;
import static com.oda.Main.font;

public class ModifyPanel extends JPanel {
    public ModifyPanel(int id, String table, ArrayList<String> columnArr, JFrame frame, Container cp) {
        setLayout(null);

        // Decleration of JComponents
        JLabel label = new JLabel(String.format("Modify a Dataset (id = %d) for the table %s", id, table));
        JTextField[] textFields = new JTextField[columnArr.size()];
        JLabel[] attributeLabel = new JLabel[textFields.length + 2];
        JButton createButton = new JButton("Confirm");
        JButton cancleButton = new JButton("Cancle");

        //Setting up the JComponents
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


        SwingUtilities.invokeLater(() -> {
            try {

                Statement statement = connection.createStatement();
                statement.execute(String.format("SELECT * FROM %s WHERE `id`=%d", table, id));
                ResultSet resultSet = statement.getResultSet();

                TableGui tableGui = new TableGui(640, 480, resultSet, frame);

                cancleButton.addActionListener(e -> {
                    frame.setContentPane(cp);
                    frame.repaint();
                    frame.revalidate();
                    tableGui.dispose();
                });

                createButton.addActionListener(e -> {
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("'" + textFields[1].getText() + "'");
                        for (int i = 2; i < textFields.length; i++) {
                            if (isNumeric(textFields[i].getText()))
                                stringBuilder.append(", " + textFields[i].getText());
                            else
                                stringBuilder.append(", " + "'" + textFields[i].getText() + "'");
                        }
                        values = stringBuilder.toString();
                        for (int i = 0; i < columnArr.size(); i++) {
                            if (isNumeric(textFields[i].getText()) && !textFields[i].getText().isEmpty())
                                statement.execute(String.format("UPDATE `%s` SET `%s` = %d WHERE id = %d;", table, columnArr.get(i), Integer.parseInt(textFields[i].getText()), id));
                            else if (!textFields[i].getText().isEmpty())
                                statement.execute(String.format("UPDATE `%s` SET `%s` = '%s' WHERE id = %d;", table, columnArr.get(i), textFields[i].getText(), id));
                        }


                        JOptionPane.showMessageDialog(null, String.format("Inserted dataset to %s", table), "Dataset added", JOptionPane.INFORMATION_MESSAGE);
                        frame.setContentPane(cp);
                        frame.repaint();
                        frame.revalidate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Could not insert into table " + table, "Error", JOptionPane.ERROR_MESSAGE);
                        frame.setContentPane(cp);
                        frame.repaint();
                        frame.revalidate();
                    }
                });

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Could not get selected id", "Error", JOptionPane.ERROR_MESSAGE);
                frame.setContentPane(cp);
                repaint();
                revalidate();
            }
        });



    }
}
