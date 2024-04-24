package com.oda;

import com.oda.Gui.LoginGui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {
    public static Font font = new Font("Arial", Font.PLAIN, 16);
    public static Font bFont = new Font("Arial", Font.PLAIN, 20);
    public static Font sFont = new Font("Arial", Font.PLAIN, 12);
    public static Connection connection = null;
    public static String url = "jdbc:mysql://localhost:3306/webshop";

    public static void main(String[] args) {

        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            SwingUtilities.invokeLater(() -> {
                try {
                    new LoginGui(260, 300);
                } catch (SQLException e) {
                    System.err.println("Connecten could not be established.");
                }
            });

            // Connection successful
            System.out.println("Connected to the database!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

