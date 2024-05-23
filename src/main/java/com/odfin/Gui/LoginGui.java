package com.odfin.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.odfin.Main.*;


public class LoginGui extends JFrame {
    public static String username = "username not found";

    public LoginGui(int width, int height) {
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
        setIconImage(imageIcon);
        cp.setLayout(null);


        // Declaration of JComponents
        final JButton loginButton = new JButton("Login");
        final JButton cancleButton = new JButton("Cancle");
        final JLabel greetLabel = new JLabel("Login");
        final JLabel statusLabel = new JLabel("Log in using your DB credentials");
        final JTextField usernameField = new JTextField();
        final JTextField passwordField = new JPasswordField();


        // Setting locations of JComponents
        Panels.setLabel(greetLabel, cp, font, 20, 20);
        Panels.setLabel(statusLabel, cp, sFont, 20, 40);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 130, 200, 100, 30);
        Panels.setComponentWithColor(loginButton, cp, Color.WHITE, 15, 200, 100, 30);
        Panels.setComponentDefaultBackground(usernameField, cp, 20, 80, 100, 30);
        Panels.setComponentDefaultBackground(passwordField, cp, 20, 120, 100, 30);
        Panels.usernameFocusLost(usernameField);
        Panels.passwordFocusLost(passwordField);


        // Adding Listeners to JComponents
        cancleButton.addActionListener(e -> {
            dispose();
            new ServerSelectorGui(260, 300, true);
        });

        this.requestFocus();
        loginButton.addActionListener(e -> {
            try {
                loginButton.setEnabled(false);
                Panels.setLabel(statusLabel, cp, sFont, "Connecting...", 20, 40);
                connection = DriverManager.getConnection(url, usernameField.getText(), passwordField.getText());
                System.out.println("Connected to the database!");
                Panels.setLabel(statusLabel, cp, sFont, "Login successful", 20, 40);
                statusLabel.setForeground(Color.GREEN);
                repaint();
                revalidate();
                if (usernameField.getText().equals("root"))
                    JOptionPane.showMessageDialog(null, String.format("<html>You are now logged in as the user <b><font color ='%s'>%s</font></b></html>", "#FF0000", usernameField.getText()));
                else
                    JOptionPane.showMessageDialog(null, String.format("You are now logged in as the user %s", usernameField.getText()), "Login successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                username = usernameField.getText();
                System.out.printf("User now logged in as %s.%n", username);

                SwingUtilities.invokeLater(() -> {
                    new DatabaseSelectorGui(260, 300);
                });
            } catch (SQLException ex) {
                loginButton.setEnabled(true);
                System.err.printf("Error code: %s%n", ex.getSQLState());
                switch (ex.getSQLState()) {
                    case ("28000"): {
                        Panels.setLabel(statusLabel, cp, sFont, "Username or password incorrect", 20, 40);
                        statusLabel.setForeground(Color.RED);
                        repaint();
                        revalidate();
                        break;
                    }
                    case ("42000"): {
                        Panels.setLabel(statusLabel, cp, sFont, "Access denied", 20, 40);
                        statusLabel.setForeground(Color.RED);
                        repaint();
                        revalidate();
                        break;
                    }
                    case ("08S01"):
                    case ("08001"): {
                        Panels.setLabel(statusLabel, cp, sFont, "Could not connect to the Address", 20, 40);
                        statusLabel.setForeground(Color.RED);
                        repaint();
                        revalidate();
                        break;
                    }
                    default: {
                        Panels.setLabel(statusLabel, cp, sFont, "An unknown error occurred", 20, 40);
                        statusLabel.setForeground(Color.RED);
                        repaint();
                        revalidate();
                    }

                    System.out.println(ex.getSQLState());
                }

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
                if (usernameField.getText().isEmpty()) Panels.usernameFocusLost(usernameField);
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
                if (passwordField.getText().isEmpty()) Panels.passwordFocusLost(passwordField);
            }
        });
        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_ENTER):
                        if (passwordField.getText().isEmpty() || passwordField.getText().equals("Password"))
                            passwordField.requestFocus();
                        else
                            loginButton.doClick();
                        break;
                    case (KeyEvent.VK_DOWN):
                        passwordField.requestFocus();
                        break;
                    case (KeyEvent.VK_UP):
                        getFrameFocus();
                        break;
                    case (KeyEvent.VK_ESCAPE):
                        cancleButton.doClick();
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
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_ENTER):
                        loginButton.doClick();
                        break;
                    case (KeyEvent.VK_DOWN):
                        loginButton.requestFocus();
                        break;
                    case (KeyEvent.VK_UP):
                        usernameField.requestFocus();
                        break;
                    case (KeyEvent.VK_ESCAPE):
                        cancleButton.doClick();
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
                    if (usernameField.getText().isEmpty() || usernameField.getText().equals("Username"))
                        usernameField.requestFocus();
                    else
                        passwordField.requestFocus();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if ((!usernameField.getText().isEmpty() && !usernameField.getText().equals("Username")) && (!passwordField.getText().isEmpty() && !passwordField.getText().equals("Password")))
                        loginButton.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    cancleButton.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    void getFrameFocus() {
        this.requestFocus();
    }
}
