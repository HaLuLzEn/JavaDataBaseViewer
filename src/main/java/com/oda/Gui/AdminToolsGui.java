package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import static com.oda.Gui.LoginGui.username;
import static com.oda.Main.*;

public class AdminToolsGui extends JFrame {
    public AdminToolsGui(int width, int height, JFrame frame) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SQL Admin Tools");
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
        final JLabel label = new JLabel("Admin Tools");
        final JLabel usersLabel = new JLabel("Users");
        final JLabel permsLabel = new JLabel("Granted Permissions");
        final JList<String> users = new JList<>();
        final JList<String> perms = new JList<>();
        final HashSet<String> usersArr = new HashSet<>();
        final HashSet<String> permsArr = new HashSet<>();
        final JButton tableViewButton = new JButton("Table view");
        final JButton grantButton = new JButton("Grant permission");
        final JButton revokePermission = new JButton("Revoke permission");


        // Setting up the JComponents
        Panels.setLabel(label, cp, bFont, 20, 20);
        Panels.setLabel(usersLabel, cp, font, 20, 50);
        Panels.setLabel(permsLabel, cp, font, 355, 50);
        Panels.setComponentDefaultBackground(users, cp, 20, 80, 250, 300);
        Panels.setComponentDefaultBackground(perms, cp, 355, 80, 250, 300);
        Panels.setComponentWithColor(tableViewButton, cp, Color.WHITE, 450, 20, 150, 30);
        Panels.setComponentWithColor(grantButton, cp, Color.WHITE, 20, 400, 150, 30);
        Panels.setComponentWithColor(revokePermission, cp, Color.WHITE, 200, 400, 150, 30);
        addUsers(usersArr, users);
        repaint();
        revalidate();

        // Adding ActionListeners to the JComponents
        users.addListSelectionListener(e -> {
            addPerms(permsArr, perms);
        });
        tableViewButton.addActionListener(e -> {
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE Account_locked = 'N';");
                ResultSet resultSet = statement.getResultSet();
                TableGui tableGui = new TableGui(640, 480, resultSet, this);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Could not create a Table", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.printf("Error code: %s", ex.getSQLState());
            }
        });
        grantButton.addActionListener(e -> {
            new PermissionGui(640, 480, this);
        });

    }

    void addUsers(HashSet<String> arr, JList<String> jList) {
        try {
            arr.clear();
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE Account_locked = 'N';");
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                arr.add(resultSet.getString("user"));
            }

            jList.setListData(arr.toArray(new String[0]));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "There was an Error, finding the users", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    void addPerms(HashSet<String> arr, JList<String> jList) {
        try {
            arr.removeAll(arr);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE Account_locked = 'N';");
            ResultSet resultSet = statement.getResultSet();
            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    if (resultSet.getString(i).equals("Y"))
                        arr.add(metaData.getColumnName(i));
                    else if (resultSet.getString(i).equals("N")) {

                    }
                }

            }

            jList.setListData(arr.toArray(new String[0]));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an Error, getting the granted permissions", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
