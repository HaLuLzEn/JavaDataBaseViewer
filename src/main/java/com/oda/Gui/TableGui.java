package com.oda.Gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import static com.oda.Main.imageIcon;

public class TableGui extends JFrame {
    JTable table;

    public TableGui(int width, int height, ResultSet resultSet, JFrame frame) throws SQLException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SQL Table");
        setIconImage(imageIcon.getImage());
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(width, height);
        int x = (int) (d.getWidth() - width) / 2;
        int y = (int) (d.getHeight() - height) / 2;
        setLocation(x, y);
        setVisible(true);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();

        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            tableModel.addColumn(metaData.getColumnName(i));
        }

        while (resultSet.next()) {
            Vector<String> row = new Vector<>(metaData.getColumnCount());
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            tableModel.addRow(row);
        }

        table = new JTable(tableModel);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        cp.add(scrollPane, BorderLayout.CENTER);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    frame.requestFocus();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

}
