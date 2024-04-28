package com.oda.Gui;

import javax.swing.*;

import java.awt.*;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.oda.Main.*;

public class ServerSelectorGui extends JFrame {

    public ServerSelectorGui(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Select server");
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
        final JLabel label = new JLabel("Provide the Server details");
        final JTextField serverField = new JTextField("localhost");
        final JTextField portField = new JTextField("3306");
        final JTextField databaseField = new JTextField("database");

        // Setting up the JComponents
        Panels.setLabel(label, cp, font, 20, 20);
        Panels.setComponentWithColor(selectButton, cp, Color.WHITE, 20, 175, 75, 30);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 190, 175, 75, 30);
        Panels.setComponentDefaultBackground(serverField, cp, 20, 50, 100, 30);
        Panels.setComponentDefaultBackground(portField, cp, 165, 50, 100, 30);
        Panels.setComponentDefaultBackground(databaseField, cp, 20, 100, 100, 30);


        // Adding Listeners to the JComponents
        selectButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                address = serverField.getText();
                port = portField.getText();
                //database = databaseField.getText();
                url = String.format("jdbc:mysql://%s:%s", address, port);
                dispose();
                new DatabaseSelectorGui(260, 300);

            });
        });
        cancleButton.addActionListener(e -> {
            System.exit(0);
        });

    }


}
