package com.odfin.Gui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import static com.odfin.Main.*;

public class AdminToolsGui extends JFrame {
    final JList<String> users = new JList<>();
    final JList<String> perms = new JList<>();
    final HashSet<String> usersArr = new HashSet<>();
    final HashSet<String> permsArr = new HashSet<>();

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
        final JScrollPane usersPane = new JScrollPane(users);
        final JScrollPane permsPane = new JScrollPane(perms);
        final JButton tableViewButton = new JButton("Table view");
        final JButton grantButton = new JButton("Grant permission");
        final JButton revokeButton = new JButton("Revoke permission");
        final JButton createUserButton = new JButton("Create user");
        final JButton removeUserButton = new JButton("Remove user");


        // Setting up the JComponents
        Panels.setLabel(label, cp, bFont, 20, 20);
        Panels.setLabel(usersLabel, cp, font, 20, 50);
        Panels.setLabel(permsLabel, cp, font, 355, 50);
        Panels.setComponentDefaultBackground(usersPane, cp, 20, 80, 250, 300);
        Panels.setComponentDefaultBackground(permsPane, cp, 355, 80, 250, 300);
        Panels.setComponentWithColor(tableViewButton, cp, Color.WHITE, 450, 20, 150, 30);
        Panels.setComponentWithColor(grantButton, cp, Color.WHITE, 20, 400, 125, 30);
        Panels.setComponentWithColor(revokeButton, cp, Color.WHITE, 145, 400, 125, 30);
        Panels.setComponentWithColor(createUserButton, cp, Color.WHITE, 355, 400, 125, 30);
        Panels.setComponentWithColor(removeUserButton, cp, Color.WHITE, 480, 400, 125, 30);
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
                statement.executeQuery("SELECT user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE user not like 'sys' OR 'mysql' or 'mariadb' OR 'root';");
                ResultSet resultSet = statement.getResultSet();
                TableGui tableGui = new TableGui(640, 480, resultSet, this);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Could not create a Table", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.printf("Error code: %s", ex.getSQLState());
            }
        });
        grantButton.addActionListener(e -> new GrantPermissionGui(260, 300, this, users));
        revokeButton.addActionListener(e -> new RevokePermissionGui(260, 300, this, users));
        createUserButton.addActionListener(e -> {
            setContentPane(new CreateUserPanel(this, cp));
            repaint();
            revalidate();
        });
        removeUserButton.addActionListener(e -> {
            try {
                String user[] = users.getSelectedValue().split("@");
                String username = user[0];
                String host = user[1];
                Statement statement = connection.createStatement();
                if (JOptionPane.showConfirmDialog(null, String.format("Are you sure, you want to delete the user %s?", username), "Warning", JOptionPane.WARNING_MESSAGE) == 0) {
                    statement.execute(String.format("DROP USER '%s'@'%s';", username, host));
                    addUsers(usersArr, users);
                    JOptionPane.showMessageDialog(null, String.format("Successfully deleted the user %s", username), "Deleted user", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getSQLState());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Could not delete the user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    void addUsers(HashSet<String> arr, JList<String> jList) {
        try {
            arr.clear();
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT host,user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE user NOT LIKE '%mysql%' or '%mariadb%' OR '%root%';");
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                arr.add(String.format("%s@%s", resultSet.getString(2), resultSet.getString(1)));
            }

            jList.setListData(arr.toArray(new String[0]));

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an Error, finding the users", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    void addPerms(HashSet<String> arr, JList<String> jList) {
        try {
            arr.removeAll(arr);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT user,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,Drop_priv FROM mysql.user WHERE user not like 'root' OR 'mysql';");
            ResultSet resultSet = statement.getResultSet();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    if (resultSet.getString(i).equals("Y"))
                        arr.add(metaData.getColumnName(i));
                }

            }
            jList.setListData(arr.toArray(new String[0]));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an Error, getting the granted permissions", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void switchBack() {
        addUsers(usersArr, users);
    }
}
