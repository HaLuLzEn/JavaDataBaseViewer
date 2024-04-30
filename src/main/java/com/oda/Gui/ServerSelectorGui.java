package com.oda.Gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        final JLabel addressLabel = new JLabel(url);

        // Setting up the JComponents
        Panels.setLabel(label, cp, font, 20, 20);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 130, 200, 100, 30);
        Panels.setComponentWithColor(selectButton, cp, Color.WHITE, 15, 200, 100, 30);
        Panels.setComponentDefaultBackground(serverField, cp, 20, 80, 100, 30);
        Panels.setComponentDefaultBackground(portField, cp, 20, 120, 100, 30);
        Panels.setLabel(addressLabel, cp, font, 20, 160);
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
        serverField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel(addressLabel, serverField, portField, cp);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel(addressLabel, serverField, portField, cp);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel(addressLabel, serverField, portField, cp);
            }
        });
        serverField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (serverField.getText().isEmpty() || serverField.getText().equals("Server Address"))
                    Panels.textFocusGained(serverField);
                updateLabel(addressLabel, serverField, portField, cp);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (serverField.getText().isEmpty()) Panels.serverFocusLost(serverField);
                updateLabel(addressLabel, serverField, portField, cp);
            }
        });
        portField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (portField.getText().isEmpty() || portField.getText().equals("Port"))
                    Panels.textFocusGained(portField);
                updateLabel(addressLabel, serverField, portField, cp);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (portField.getText().isEmpty()) Panels.portFocusLost(portField);
                updateLabel(addressLabel, serverField, portField, cp);
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
                        getFrameFocus();
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
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    cancleButton.doClick();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if ((!serverField.getText().isEmpty() && !serverField.getText().equals("Server Address")) && (!portField.getText().isEmpty() && !portField.getText().equals("Port")))
                        selectButton.doClick();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    void getFrameFocus() {
        this.requestFocus();
    }

    void updateLabel(JLabel label, JTextField serverField, JTextField portField, Container cp) {
        address = serverField.getText();
        port = portField.getText();
        label.setText(url);
        cp.repaint();
        cp.revalidate();
    }

}
