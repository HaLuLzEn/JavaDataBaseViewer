package com.oda.Gui;

import javax.swing.*;
import java.awt.*;

import static com.oda.Main.imageIcon;

public class PermissionGui extends JFrame {
    public PermissionGui(int width, int height, JFrame frame) {
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

    }
}
