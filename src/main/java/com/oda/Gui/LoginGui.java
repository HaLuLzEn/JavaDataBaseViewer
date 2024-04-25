package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.oda.Main.*;


public class LoginGui extends JFrame {
    public static String username = "username not found";

    public LoginGui(int width, int height) throws SQLException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("DB login");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        Container cp = getContentPane();
        setIconImage(imageIcon.getImage());
        cp.setLayout(null);


        // Declaration of JComponents
        final JButton loginButton = new JButton("Login");
        final JButton cancleButton = new JButton("Cancle");
        final JLabel greetLabel = new JLabel("Login");
        final JLabel statusLabel = new JLabel("Log in using your DB credentials");
        final JTextField usernameField = new JTextField();
        final JTextField passwordField = new JPasswordField();


        // Setting locations of JComponents
        cp.setBackground(Color.WHITE);
        Panels.setLabel(greetLabel, cp, font, 20, 20);
        Panels.setLabel(statusLabel, cp, sFont, 20, 40);
        Panels.setComponent(cancleButton, cp, 130, 200, 100, 30);
        Panels.setComponent(loginButton, cp, 15, 200, 100, 30);
        Panels.setComponent(usernameField, cp, 20, 80, 100, 30);
        Panels.setComponent(passwordField, cp, 20, 120, 100, 30);
        Panels.usernameFocusLost(usernameField);
        Panels.passwordFocusLost(passwordField);


        // Adding Listeners to JComponents
        cancleButton.addActionListener(e -> System.exit(0));

        this.requestFocus();
        loginButton.addActionListener(e -> {
            try {
                connection = DriverManager.getConnection(url, usernameField.getText(), passwordField.getText());
                Panels.setLabel(statusLabel, cp, sFont, "Login successful", 20, 40);
                statusLabel.setForeground(Color.GREEN);
                repaint();
                revalidate();
                JOptionPane.showMessageDialog(null, String.format("You are now logged in as the user %s", usernameField.getText()), "Login successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                username = usernameField.getText();
                System.out.printf("User now logged in as %s.%n", username);

                SwingUtilities.invokeLater(() -> {
                    new MainGui(800, 600);
                });
            } catch (SQLException ex) {
                Panels.setLabel(statusLabel, cp, sFont, "Username or password incorrect.", 20, 40);
                statusLabel.setForeground(Color.RED);
                repaint();
                revalidate();
            }
        });

        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().isEmpty() || usernameField.getText().equals("Username"))
                    Panels.textFocusGained(usernameField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty())
                    Panels.usernameFocusLost(usernameField);
            }
        });
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().isEmpty() || passwordField.getText().equals("Password"))
                    Panels.textFocusGained(passwordField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty())
                    Panels.passwordFocusLost(passwordField);
            }
        });

        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


    }
}
