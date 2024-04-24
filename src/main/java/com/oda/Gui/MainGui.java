package com.oda.Gui;

import javax.swing.*;
import java.awt.*;

import static com.oda.Gui.LoginGui.username;
import static com.oda.Main.bFont;

public class MainGui extends JFrame {
    public MainGui(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        JPanel cp = new JPanel();
        setContentPane(cp);
        cp.setLayout(null);

        // Decleration of JComponents
        JLabel loggedInLabel = new JLabel(String.format("<html>Logged in as %s</html>", username));

        if (username.equals("root")) {
            String hexCode = "#FF0000";
            loggedInLabel = new JLabel(String.format("<html>Logged in as <font color='%s'>%s</font></html>", hexCode, username));
        }


        // Setting bounds of JComponents
        Panels.setLabel(loggedInLabel, cp, bFont, 20, 20);

    }
}
