package com.example.quanlysieuthiclient.VIEW;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class loaihangView extends JPanel {
    public JTextField maloaiField;
    public JTextField tenloaiField;
    public JButton themButton;
    public JButton suaButton;
    public JButton xoaButton;
    public JButton resetButton;

    public JTextField timkiemField;
    public JButton timkiemButton;

    public DefaultTableModel loaihangDefaultTableModel;
    public JTable loaihangJTable;

    public loaihangView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));

        JLabel maloaiJLabel = new JLabel("Mã loại hàng:");
        maloaiJLabel.setBounds(300, 50, 200, 40);
        maloaiJLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(maloaiJLabel);

        maloaiField = new JTextField();
        maloaiField.setBounds(600, 50, 300, 40);
        maloaiField.setFont(new Font("Arial", Font.BOLD, 24));
        add(maloaiField);

        JLabel tenloaiJLabel = new JLabel("Tên loại hàng:");
        tenloaiJLabel.setBounds(300, 100, 200, 40);
        tenloaiJLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(tenloaiJLabel);

        tenloaiField = new JTextField();
        tenloaiField.setBounds(600, 100, 300, 40);
        tenloaiField.setFont(new Font("Arial", Font.BOLD, 24));
        add(tenloaiField);

        themButton = new JButton("Thêm");
        themButton.setBounds(200, 150, 200, 40);
        themButton.setForeground(Color.white);
        themButton.setBackground(Color.GREEN);
        themButton.setFont(new Font("Arial", Font.ITALIC, 23));
        add(themButton);

        suaButton = new JButton("Sửa");
        suaButton.setBounds(450, 150, 200, 40);
        suaButton.setFont(new Font("Arial", Font.ITALIC, 23));
        suaButton.setForeground(Color.white);
        suaButton.setBackground(Color.blue);
        add(suaButton);

        xoaButton = new JButton("Xóa");
        xoaButton.setBounds(700, 150, 200, 40);
        xoaButton.setBackground(Color.red);
        xoaButton.setFont(new Font("Arial", Font.ITALIC, 23));
        add(xoaButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(950, 150, 200, 40);
        resetButton.setBackground(Color.yellow);
        resetButton.setFont(new Font("Arial", Font.ITALIC, 23));
        add(resetButton);

        timkiemField = new JTextField();
        timkiemField.setBounds(300, 200, 300, 40);
        timkiemField.setFont(new Font("Arial", Font.BOLD, 24));
        add(timkiemField);

        timkiemButton = new JButton("Tìm kiếm");
        timkiemButton.setBounds(700, 200, 200, 40);
        timkiemButton.setBackground(Color.orange);
        timkiemButton.setFont(new Font("Arial", Font.ITALIC, 23));
        add(timkiemButton);

        String[] tieudeStrings = {"Mã loại hàng", "Tên loại hàng"};
        loaihangDefaultTableModel = new DefaultTableModel(tieudeStrings, 0);
        loaihangJTable = new JTable(loaihangDefaultTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loaihangJTable.setFont(new Font("Arial", Font.BOLD, 18));
        loaihangJTable.setBackground(Color.pink);
        loaihangJTable.setRowHeight(30);

        JTableHeader loaihangHeader = loaihangJTable.getTableHeader();
        loaihangHeader.setFont(new Font("Arial", Font.BOLD, 20));
        loaihangHeader.setForeground(Color.BLACK);
        loaihangHeader.setBackground(Color.GREEN);

        JScrollPane loaihangJScrollPane = new JScrollPane(loaihangJTable);
        loaihangJScrollPane.setBounds(200, 250, 800, 600);
        add(loaihangJScrollPane);
    }

    public void addThemClickListener(ActionListener listener) { themButton.addActionListener(listener); }
    public void addSuaClickListener(ActionListener listener) { suaButton.addActionListener(listener); }
    public void addXoaClickListener(ActionListener listener) { xoaButton.addActionListener(listener); }
    public void addResetClickListener(ActionListener listener) { resetButton.addActionListener(listener); }
    public void addTimKiemClickListener(ActionListener listener) { timkiemButton.addActionListener(listener); }
    public void addClickTableListener(MouseListener listener) { loaihangJTable.addMouseListener(listener); }
}