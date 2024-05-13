package com.oda;

import com.oda.Gui.ServerSelectorGui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.oda.Gui.LoginGui.username;

public class Main {
    public static Font font = new Font("Arial", Font.PLAIN, 16);
    public static Font bFont = new Font("Arial", Font.PLAIN, 20);
    public static Font sFont = new Font("Arial", Font.PLAIN, 12);
    public static Connection connection = null;
    public static String database = "";
    public static String port = "Port";
    public static String address = "IP";
    public static String url = String.format("jdbc:mysql://%s:%s/%s", address, port, database);
    public static Image imageIcon;
    public static boolean isAdmin = false;

    public static void main(String[] args) {
        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            imageIcon = ImageIO.read(new File("icon.png"));

            // Connect to the database
            SwingUtilities.invokeLater(() -> {
                new ServerSelectorGui(260, 300, false);
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

    public static boolean checkAdmin(String user) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT user from mysql.user;");
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getSQLState());
            return false;
        }
    }
}

