package com.oda.Gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashSet;

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

        // Setting up JComponents
        Panels.setComponentWithColor(selectButton, cp, Color.WHITE, 20, 200, 75, 30);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 150, 200, 75, 30);
        Panels.setComponentWithColor(comboBox, cp, Color.WHITE, 45, 100, 150, 20);
        listDatabases(databases, comboBox);

        // Adding listeners to JComponents
        cancleButton.addActionListener(e -> {
            dispose();
            new ServerSelectorGui(300, 260);
        });

    }

    void listDatabases(HashSet<String> hashSet, JComboBox<String> comboBox) {
        try {
            Statement statement = connection.createStatement();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();

            while (resultSet.next()) {
                hashSet.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
                System.out.println("test");
            }

            for (String s: hashSet) {
                comboBox.addItem(s);
            }
        } catch (SQLException ex) {

        }
    }
}
