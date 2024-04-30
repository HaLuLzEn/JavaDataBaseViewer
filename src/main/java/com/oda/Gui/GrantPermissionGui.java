package com.oda.Gui;

import javax.swing.*;
import java.awt.*;

import static com.oda.Main.font;
import static com.oda.Main.imageIcon;

public class GrantPermissionGui extends JFrame {
    public GrantPermissionGui(int width, int height, JFrame frame) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SQL Permissions");
        setIconImage(imageIcon);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        Container cp = getContentPane();
        cp.setLayout(null);


        // Declaring the JComponents
        final JLabel label = new JLabel("Grant permission");

        // Setting up JComponents
        Panels.setLabel(label, cp, font, 20, 20);
    }
}
