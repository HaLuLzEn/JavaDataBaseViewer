package com.odfin.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.sql.Statement;

import static com.odfin.Main.*;

public class CreateUserPanel extends JPanel {
    public CreateUserPanel(AdminToolsGui frame, Container cp) {
        // Declaration of Components
        setLayout(null);
        final JLabel label = new JLabel(String.format("Create a User (Default address: %s)", address));
        final JTextField usernameField = new JTextField();
        final JTextField addressField = new JTextField();
        final JPasswordField authenticationField = new JPasswordField();
        final JLabel usernameLabel = new JLabel("Username");
        final JLabel addressLabel = new JLabel("Address");
        final JLabel authenticationLabel = new JLabel("Authentication String");
        final JButton createButton = new JButton("Confirm");
        final JButton cancelButton = new JButton("Cancel");

        //Setting up JComponents
        Panels.setLabel(label, this, font, 20, 20);
        Panels.setComponentDefaultBackground(usernameField, this, 25, 100, 100, 20);
        Panels.setComponentDefaultBackground(addressField, this, 25, 160, 100, 20);
        addressField.setText("Leave empty for default");
        Panels.setComponentDefaultBackground(authenticationField, this, 25, 220, 100, 20);
        Panels.setLabel(usernameLabel, this, font, 140, 100);
        Panels.setLabel(addressLabel, this, font, 140, 160);
        Panels.setLabel(authenticationLabel, this, font, 140, 220);
        Panels.setComponentWithColor(createButton, this, Color.WHITE, 20, 400, 150, 30);
        Panels.setComponentWithColor(cancelButton, this, Color.WHITE, 450, 400, 150, 30);

        // Adding Listeners to JComponents
        addressField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (addressField.getText().equals("Leave empty for default"))
                    addressField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        cancelButton.addActionListener(e -> {
            frame.setContentPane(cp);
            frame.repaint();
            frame.revalidate();
        });
        createButton.addActionListener(e -> {
            try {
                Statement statement = connection.createStatement();
                if (addressField.getText().equals("Leave empty for default") || addressField.getText().isEmpty())
                    addressField.setText(address);
                statement.execute(String.format("CREATE USER '%s'@'%s' IDENTIFIED BY '%s'", usernameField.getText(), addressField.getText(), authenticationField.getText()));
                JOptionPane.showMessageDialog(null, String.format("Successfully created the user %s", usernameField.getText(), "Success", JOptionPane.INFORMATION_MESSAGE));
                frame.switchBack();
                frame.setContentPane(cp);
            } catch (SQLException ex) {
                switch (ex.getSQLState()) {
                    case ("HY000"): {
                        JOptionPane.showMessageDialog(null, "This user already exists", "Error" ,JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    default: {
                        JOptionPane.showMessageDialog(null, "Could not create the user", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
                System.out.println(ex.getSQLState());
                ex.printStackTrace();
                frame.setContentPane(cp);
                frame.repaint();
                frame.revalidate();
            }
        });

    }
}
