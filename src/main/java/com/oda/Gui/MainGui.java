package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import static com.oda.Gui.LoginGui.username;
import static com.oda.Main.*;

public class MainGui extends JFrame {
    public static String columns = "";
    public static String values = "";

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
        JLabel loggedInLabel = new JLabel(String.format("<html>Logged in as %s, using database %s</html>", username, database));
        final ArrayList<String> tableArr = new ArrayList<>();
        final ArrayList<String> columnArr = new ArrayList<>();
        listTables(tableArr);
        final JList<String> tableList = new JList<>(tableArr.toArray(new String[0]));
        final JList<String> columnList = new JList<>();
        final JLabel tableLabel = new JLabel("Tables");
        final JLabel columnLabel = new JLabel("Columns");
        final JButton tableButton = new JButton("Table view");
        final JButton insertButton = new JButton("Create Dataset");
        final JButton updateButton = new JButton("Modify Dataset");
        final JButton deleteButton = new JButton("Remove Dataset");


        if (username.equals("root")) {
            String hexCode = "#FF0000";
            loggedInLabel = new JLabel(String.format("<html>Logged in as <b><font color='%s'>%s</font></b>, using Database %s</html>", hexCode, username, database));
            JOptionPane.showMessageDialog(null, String.format("<html>You are logged in as <b><font color='%s'>%s</font><b>. Be responsible with your privileges</html>", hexCode, username), "Warning", JOptionPane.WARNING_MESSAGE);
            repaint();
            revalidate();
        }


        // Setting bounds of JComponents
        Panels.setLabel(loggedInLabel, cp, bFont, 20, 20);
        Panels.setLabel(tableLabel, cp, font, 20, 50);
        Panels.setLabel(columnLabel, cp, font, 410, 50);
        Panels.setComponentDefaultBackground(tableList, cp, 20, 80, 350, 400);
        Panels.setComponentDefaultBackground(columnList, cp, 410, 80, 350, 400);
        Panels.setComponentWithColor(tableButton, cp, Color.WHITE, 45, 500, 150, 30);
        Panels.setComponentWithColor(insertButton, cp, Color.WHITE, 225, 500, 150, 30);
        Panels.setComponentWithColor(updateButton, cp, Color.WHITE, 405, 500, 150, 30);
        Panels.setComponentWithColor(deleteButton, cp, Color.WHITE, 585, 500, 150, 30);


        // Adding ActionListeners to JComponents
        tableList.addListSelectionListener(e -> {
            columnArr.clear();
            listColumns(columnArr, tableList.getSelectedValue());
            columnList.setListData(columnArr.toArray(new String[0]));
        });
        tableList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    tableButton.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        tableButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s;", tableList.getSelectedValue()));
                    new TableGui(640, 480, resultSet, this);
                } catch (SQLException | IndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(null, "This table does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
        insertButton.addActionListener(e -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("`").append(columnArr.get(1)).append("`");
                for (int i = 2; i < columnArr.size(); i++) {
                    stringBuilder.append(", ").append("`").append(columnArr.get(i)).append("`");
                }
                columns = stringBuilder.toString();

                SwingUtilities.invokeLater(() -> {
                    setContentPane(new InsertPanel(columnArr, this, tableList.getSelectedValue(), cp));
                    repaint();
                    revalidate();
                });
            } catch (IndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "This table does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            }


        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                JList<String> selectedList = tableList;
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_UP): {
                        selectedList.requestFocus();
                        if (selectedList.getSelectedIndex() > 0)
                            selectedList.setSelectedIndex(tableList.getSelectedIndex() - 1);
                        break;
                    }
                    case (KeyEvent.VK_DOWN): {
                        selectedList.requestFocus();
                        if (selectedList.getSelectedIndex() < tableArr.size())
                            selectedList.setSelectedIndex(selectedList.getSelectedIndex() + 1);
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        tableList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    columnList.requestFocus();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        columnList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    tableList.requestFocus();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

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
            System.out.printf(Arrays.toString(ex.getStackTrace()));
            JOptionPane.showMessageDialog(null, "Error loading columns", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}