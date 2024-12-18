package com.odfin.Gui;

import javax.swing.*;
import java.awt.*;

public abstract class Panels {

    public static void setLabel(JLabel label, Container cp, Font font, String text, int x, int y) {
        label.setBackground(cp.getBackground());
        label.setFont(font);
        label.setText(text);
        label.setBounds(x, y, (label.getText().toCharArray().length * label.getFont().getSize()) + 10, (label.getFont().getSize()) + 10);
        cp.add(label);
    }

    public static void setLabel(JLabel label, Container cp, Font font, int x, int y) {
        label.setBackground(cp.getBackground());
        label.setFont(font);
        label.setBounds(x, y, (label.getText().toCharArray().length * label.getFont().getSize()) + 10, (label.getFont().getSize()) + 10);
        label.setLocation(x, y);
        cp.add(label);
    }

    public static void setComponent(JComponent c, Container cp, int x, int y, int width, int height) {
        c.setBackground(cp.getBackground());
        c.setBounds(x, y, width, height);
        cp.add(c);
    }

    public static void setComponent(JComponent c, Container cp, String text, int x, int y, int width, int height) {
        c.setBackground(cp.getBackground());
        c.setBounds(x, y, width, height);
        cp.add(c);
    }

    public static void setComponentWithColor(JComponent c, Container cp, Color color, int x, int y, int width, int height) {
        c.setBackground(color);
        c.setBounds(x, y, width, height);
        cp.add(c);
    }


    public static void setComponentDefaultBackground(JComponent c, Container cp, int x, int y, int width, int height) {
        c.setBounds(x, y, width, height);
        cp.add(c);
    }


    public static void textFocusGained(JTextField tf) {
        tf.setText("");
    }

    public static void passwordFocusLost(JTextField tf) {
        tf.setText("Password");
    }

    public static void usernameFocusLost(JTextField tf) {
        tf.setText("Username");
    }

    public static void serverFocusLost(JTextField tf) {
        tf.setText("Server Address");
    }

    public static void portFocusLost(JTextField tf) {
        tf.setText("Port");
    }


}
