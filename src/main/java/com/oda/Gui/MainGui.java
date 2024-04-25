package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.oda.Gui.LoginGui.username;
import static com.oda.Main.*;

public class MainGui extends JFrame {
    public MainGui(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Database Management");
        setIconImage(imageIcon.getImage());
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        Container cp = getContentPane();
        cp.setLayout(null);

        // Decleration of JComponents
        JLabel loggedInLabel = new JLabel(String.format("<html>Logged in as %s</html>", username));
        ArrayList<String> tableArr = new ArrayList<>();
        ArrayList<String> columnArr = new ArrayList<>();
        listTables(tableArr);
        JList<String> tableList = new JList<>(tableArr.toArray(new String[0]));
        JList<String> columnList = new JList<>();
        tableList.addListSelectionListener(e -> {
            listColumns(columnArr, tableList.getSelectedValue());
            columnList.setListData(columnArr.toArray(new String[0]));
            columnArr.clear();
        });

        if (username.equals("root")) {
            String hexCode = "#FF0000";
            loggedInLabel = new JLabel(String.format("<html>Logged in as <b><font color='%s'>%s</font></b></html>", hexCode, username));
        }


        // Setting bounds of JComponents
        Panels.setLabel(loggedInLabel, cp, bFont, 20, 20);
        Panels.setComponentDefaultBackground(tableList, cp, 20, 80, 350, 400);
        Panels.setComponentDefaultBackground(columnList, cp, 410, 80, 350, 400);
    }

    void listTables(ArrayList<String> list) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'webshop';");

            while (resultSet.next()) {
                list.add(resultSet.getString("table_name"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Tables could not be loaded", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void listColumns(ArrayList<String> list, String tableName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SHOW COLUMNS FROM `%s`", tableName));

            while (resultSet.next()) {
                list.add(resultSet.getString("Field"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading columns", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
