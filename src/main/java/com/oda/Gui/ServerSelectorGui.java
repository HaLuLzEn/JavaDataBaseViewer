package com.oda.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.oda.Main.*;

public class ServerSelectorGui extends JFrame {
    public ServerSelectorGui(int width, int height, boolean switchBack) {
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
        final JTextField serverField = new JTextField("Server Address");
        final JTextField portField = new JTextField("Port");

        // Setting up the JComponents
        Panels.setLabel(label, cp, font, 20, 20);
        Panels.setComponentWithColor(selectButton, cp, Color.WHITE, 20, 175, 75, 30);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 190, 175, 75, 30);
        Panels.setComponentDefaultBackground(serverField, cp, 20, 75, 100, 30);
        Panels.setComponentDefaultBackground(portField, cp, 165, 75, 100, 30);
        if (switchBack) {
            serverField.setText(address);
            portField.setText(port);
        }
        this.requestFocus();


        // Adding Listeners to the JComponents
        selectButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                address = serverField.getText();
                port = portField.getText();
                url = String.format("jdbc:mysql://%s:%s", address, port);
                dispose();
                new LoginGui(260, 300);

            });
        });
        cancleButton.addActionListener(e -> {
            System.exit(0);
        });
        serverField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (serverField.getText().isEmpty() || serverField.getText().equals("Server Address"))
                    Panels.textFocusGained(serverField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (serverField.getText().isEmpty()) Panels.serverFocusLost(serverField);
            }
        });
        portField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (portField.getText().isEmpty() || portField.getText().equals("Port"))
                    Panels.textFocusGained(portField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (portField.getText().isEmpty()) Panels.portFocusLost(portField);
            }
        });
        serverField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_ENTER):
                        if (portField.getText().isEmpty() || portField.getText().equals("Port"))
                            portField.requestFocus();
                        else
                            selectButton.doClick();
                        break;
                    case (KeyEvent.VK_DOWN):
                        portField.requestFocus();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        portField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_ENTER):
                        selectButton.doClick();
                        break;
                    case (KeyEvent.VK_DOWN):
                        selectButton.requestFocus();
                        break;
                    case (KeyEvent.VK_UP):
                        serverField.requestFocus();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (serverField.getText().isEmpty() || serverField.getText().equals("Server Address"))
                        serverField.requestFocus();
                    else
                        portField.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

}
