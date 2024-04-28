package com.oda;

import com.oda.Gui.ServerSelectorGui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static Font font = new Font("Arial", Font.PLAIN, 16);
    public static Font bFont = new Font("Arial", Font.PLAIN, 20);
    public static Font sFont = new Font("Arial", Font.PLAIN, 12);
    public static Connection connection = null;
    public static String database;
    public static String port;
    public static String address;
    public static String url = String.format("jdbc:mysql://%s:%s/%s", address, port, database);
    public static Image imageIcon;

    public static void main(String[] args) {
        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            imageIcon = ImageIO.read(new File("icon.png"));

            // Connect to the database
            SwingUtilities.invokeLater(() -> {
                new ServerSelectorGui(300, 260);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Image not found");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isNumeric(String str) {
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

