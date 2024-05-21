package com.oda.Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import static com.oda.Main.*;

public class RevokePermissionGui extends JFrame {
    public RevokePermissionGui(int width, int height, JFrame frame, JList<String> users) {
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


        String revoke[] = users.getSelectedValue().split("@");
        String revokeUsername = revoke[0];
        String revokeAddress = revoke[1];

        // Declaring the JComponents
        final JLabel label = new JLabel("Revoke permission");
        final JLabel label2 = new JLabel(String.format("<html>from the user <b><i>%s</i></b></html>", revokeUsername));
        final JComboBox<String> comboBox = new JComboBox<>();
        final JButton grantButton = new JButton("Revoke");
        final JButton cancleButton = new JButton("Cancle");
        final JCheckBox adminCheckBox = new JCheckBox("<html><font color='red'>Revoke Admin privilege</font></html>");

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


        adminCheckBox.addActionListener(e -> {
            comboBox.setSelectedIndex(6);
            comboBox.setEnabled(!adminCheckBox.isSelected());
        });

        cancleButton.addActionListener(e -> dispose());

        grantButton.addActionListener(e -> {
            try {
                Statement statement = connection.createStatement();
                if (adminCheckBox.isSelected()) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to revoke all privileges from the user?", "Revoke all privileges", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 1)
                        statement.execute(String.format("REVOKE ALL PRIVILEGES ON *.* FROM '%s'@'%s';", revokeUsername, revokeAddress));
                        statement.execute("FLUSH PRIVILEGES;");
                        JOptionPane.showMessageDialog(null, String.format("Revoked Admin privileges from the user %s", revokeUsername), "Revoked permission", JOptionPane.INFORMATION_MESSAGE);
                        ImageIcon icon = new ImageIcon("master.jpg");
                        Image image = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                        JOptionPane.showMessageDialog(null, "WER HAT MIR MEINE MOD-RECHTE WEGGENOMMEN?!?!", "HALLO?!?!", JOptionPane.ERROR_MESSAGE, new ImageIcon(image));
                } else {
                    statement.execute(String.format("REVOKE %S ON %s.* FROM '%s'@'%s';%n", comboBox.getSelectedItem(), database, revokeUsername, revokeAddress));
                    statement.execute("FLUSH PRIVILEGES;");
                    JOptionPane.showMessageDialog(null, String.format("Revoked permission %s to the user %s", comboBox.getSelectedItem(), revokeUsername), "Revoked permission", JOptionPane.INFORMATION_MESSAGE);
                }

                if (comboBox.getSelectedIndex() != 6 || !adminCheckBox.isSelected())
                    statement.execute(String.format("UPDATE mysql.user SET %s_priv = 'N' WHERE user = '%s';", comboBox.getSelectedItem(), revokeUsername));
                else
                    statement.execute(String.format("UPDATE mysql.user SET Select_priv = 'N', Insert_priv = 'N', Update_priv = 'N', Delete_priv = 'N', Create_priv = 'N', Drop_priv = 'N' WHERE user = '%s';", revokeUsername));

                dispose();
            } catch (SQLException ex) {
                System.out.printf("Error Code: %s%n", ex.getSQLState());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, String.format("Could not revoke the permsission %s from the user %s", comboBox.getSelectedItem(), revokeUsername), "Error", JOptionPane.ERROR_MESSAGE);
            }

        });


    }
}
