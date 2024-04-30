package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.util.HashSet;
import java.util.Objects;

import static com.oda.Main.*;

public class DatabaseSelectorGui extends JFrame {
    public DatabaseSelectorGui(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Select Databse");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        Container cp = getContentPane();
        setIconImage(imageIcon);
        cp.setLayout(null);


        // Declaring JComponents
        final JButton selectButton = new JButton("Select");
        final JButton cancleButton = new JButton("Cancle");
        final JComboBox<String> comboBox = new JComboBox<>();
        final HashSet<String> databases = new HashSet<>();
        final JLabel label = new JLabel("Select a database");

        // Setting up JComponents
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 130, 200, 100, 30);
        Panels.setComponentWithColor(selectButton, cp, Color.WHITE, 15, 200, 100, 30);
        Panels.setComponentWithColor(comboBox, cp, Color.WHITE, 45, 100, 150, 20);
        Panels.setLabel(label, cp, font, 20, 20);
        listDatabases(databases, comboBox);

        // Adding listeners to JComponents
        cancleButton.addActionListener(e -> {
            dispose();
            new LoginGui(260, 300);
        });
        selectButton.addActionListener(e -> {
            database = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
            try {
                Statement statement = connection.createStatement();
                statement.execute(String.format("USE %s", database));
                dispose();
                new MainGui(800, 600);
            } catch (SQLException ex) {
                System.err.printf("Error code: %s%n", ex.getSQLState());
            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_DOWN): {
                        comboBox.requestFocus();
                        break;
                    }
                    case (KeyEvent.VK_ENTER): {
                        selectButton.doClick();
                        break;
                    }
                    case (KeyEvent.VK_ESCAPE): {
                        cancleButton.doClick();
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        comboBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    getFrameFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    getFrameFocus();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    void listDatabases(HashSet<String> hashSet, JComboBox<String> comboBox) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();

            while (resultSet.next()) {
                hashSet.add(resultSet.getString(1));
            }

            for (String s : hashSet) {
                comboBox.addItem(s);
            }

            if (comboBox.getItemCount() <= 2) {
                JOptionPane.showMessageDialog(null, "You do not have the privelege to any database", "Warnig", JOptionPane.WARNING_MESSAGE);
                dispose();
                new LoginGui(260, 300);
            }
        } catch (SQLException ex) {
            System.out.printf("Error code: %s%n", ex.getSQLState());
            ex.printStackTrace();
        }
    }

    void getFrameFocus() {
        this.requestFocus();
    }
}
