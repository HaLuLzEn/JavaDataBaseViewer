package com.oda.Gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;

import static com.oda.Main.*;

public class GrantPermissionGui extends JFrame {
    public GrantPermissionGui(int width, int height, JFrame frame, JList<String> users) {
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

        String grantUsername = users.getSelectedValue();

        // Declaring the JComponents
        final JLabel label = new JLabel("Grant permission");
        final JLabel label2 = new JLabel(String.format("<html>for the user <b><i>%s</i></b></html>", grantUsername));
        final JComboBox<String> comboBox = new JComboBox<>();
        final JButton grantButton = new JButton("Grant");
        final JButton cancleButton = new JButton("Cancle");
        final JCheckBox adminCheckBox = new JCheckBox("Grant Admin privilege");

        // Setting up JComponents
        Panels.setLabel(label, cp, font, 20, 20);
        Panels.setLabel(label2, cp, sFont, 25, 40);
        Panels.setComponentWithColor(comboBox, cp, Color.WHITE, 45, 100, 150, 20);
        comboBox.addItem("Select");
        comboBox.addItem("Insert");
        comboBox.addItem("Update");
        comboBox.addItem("Delete");
        comboBox.addItem("Create");
        comboBox.addItem("Drop");
        comboBox.addItem("All privileges");
        Panels.setComponentWithColor(grantButton, cp, Color.WHITE, 15, 200, 100, 30);
        Panels.setComponentWithColor(cancleButton, cp, Color.WHITE, 130, 200, 100, 30);
        Panels.setComponentDefaultBackground(adminCheckBox, cp, 15, 150, 200, 20);


        cancleButton.addActionListener(e -> dispose());

        grantButton.addActionListener(e -> {
            try {
                Statement statement = connection.createStatement();
                statement.execute(String.format("GRANT %S ON %s.* TO '%s'@'%s';%n", comboBox.getSelectedItem(), database, grantUsername, address));
                /*
                if (comboBox.getSelectedIndex() != 6)
                    statement.execute(String.format("UPDATE mysql.user SET %s_priv = 'Y' WHERE user = '%s';", comboBox.getSelectedItem(), grantUsername));
                else
                    statement.execute(String.format("UPDATE mysql.user SET Select_priv = 'Y', Insert_priv = 'Y', Update_priv = 'Y', Delete_priv = 'Y', Create_priv = 'Y', Drop_priv = 'Y' WHERE user = '%s';", grantUsername));
                */
                if (adminCheckBox.isSelected()) {
                    statement.execute(String.format("GRANT ALL PRIVILEGES ON *.* TO '%s'@'%s' WITH GRANT OPTION;", grantUsername, address));
                    //statement.execute(String.format("INSERT INTO admins(user) "));
                }
                JOptionPane.showMessageDialog(null, String.format("Granted permission %s to the user %s", comboBox.getSelectedItem(), grantUsername), "Granted permission", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                switch (ex.getSQLState()) {
                    case "42000": {
                        JOptionPane.showMessageDialog(null, "You do not have the permission to grant permissions", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    case "42S22": {
                        JOptionPane.showMessageDialog(null, "The user does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null, String.format("Could not grant the permsission %s to the user %s", comboBox.getSelectedItem(), grantUsername), "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        });


    }
}
