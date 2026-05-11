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

public class nhacungcapView extends JPanel {
    // 6 trường dữ liệu
    public JTextField manccField, tennccField, loaihinhField;
    public JTextField emailField, sdtField, diachiField;

    public JButton themButton, suaButton, xoaButton, resetButton;
    public JTextField timkiemField;
    public JButton timkiemButton;

    public DefaultTableModel nccDefaultTableModel;
    public JTable nccJTable;

    public nhacungcapView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));

        Font labelFont = new Font("Arial", Font.BOLD, 20);
        Font fieldFont = new Font("Arial", Font.PLAIN, 20);

        // --- CỘT TRÁI ---
        JLabel manccJLabel = new JLabel("Mã NCC:");
        manccJLabel.setBounds(100, 50, 150, 35);
        manccJLabel.setFont(labelFont);
        add(manccJLabel);

        manccField = new JTextField();
        manccField.setBounds(250, 50, 250, 35);
        manccField.setFont(fieldFont);
        add(manccField);

        JLabel tennccJLabel = new JLabel("Tên NCC:");
        tennccJLabel.setBounds(100, 100, 150, 35);
        tennccJLabel.setFont(labelFont);
        add(tennccJLabel);

        tennccField = new JTextField();
        tennccField.setBounds(250, 100, 250, 35);
        tennccField.setFont(fieldFont);
        add(tennccField);

        JLabel loaihinhJLabel = new JLabel("Loại hình:");
        loaihinhJLabel.setBounds(100, 150, 150, 35);
        loaihinhJLabel.setFont(labelFont);
        add(loaihinhJLabel);

        loaihinhField = new JTextField();
        loaihinhField.setBounds(250, 150, 250, 35);
        loaihinhField.setFont(fieldFont);
        add(loaihinhField);

        // --- CỘT PHẢI ---
        JLabel emailJLabel = new JLabel("Email:");
        emailJLabel.setBounds(600, 50, 150, 35);
        emailJLabel.setFont(labelFont);
        add(emailJLabel);

        emailField = new JTextField();
        emailField.setBounds(750, 50, 250, 35);
        emailField.setFont(fieldFont);
        add(emailField);

        JLabel sdtJLabel = new JLabel("Số ĐT:");
        sdtJLabel.setBounds(600, 100, 150, 35);
        sdtJLabel.setFont(labelFont);
        add(sdtJLabel);

        sdtField = new JTextField();
        sdtField.setBounds(750, 100, 250, 35);
        sdtField.setFont(fieldFont);
        add(sdtField);

        JLabel diachiJLabel = new JLabel("Địa chỉ:");
        diachiJLabel.setBounds(600, 150, 150, 35);
        diachiJLabel.setFont(labelFont);
        add(diachiJLabel);

        diachiField = new JTextField();
        diachiField.setBounds(750, 150, 250, 35);
        diachiField.setFont(fieldFont);
        add(diachiField);

        // --- NÚT BẤM ---
        Font btnFont = new Font("Arial", Font.ITALIC, 20);

        themButton = new JButton("Thêm");
        themButton.setBounds(200, 220, 150, 40);
        themButton.setForeground(Color.white);
        themButton.setBackground(Color.GREEN);
        themButton.setFont(btnFont);
        add(themButton);

        suaButton = new JButton("Sửa");
        suaButton.setBounds(400, 220, 150, 40);
        suaButton.setFont(btnFont);
        suaButton.setForeground(Color.white);
        suaButton.setBackground(Color.blue);
        add(suaButton);

        xoaButton = new JButton("Xóa");
        xoaButton.setBounds(600, 220, 150, 40);
        xoaButton.setBackground(Color.red);
        xoaButton.setFont(btnFont);
        add(xoaButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(800, 220, 150, 40);
        resetButton.setBackground(Color.yellow);
        resetButton.setFont(btnFont);
        add(resetButton);

        // --- TÌM KIẾM ---
        timkiemField = new JTextField();
        timkiemField.setBounds(300, 280, 400, 40);
        timkiemField.setFont(fieldFont);
        add(timkiemField);

        timkiemButton = new JButton("Tìm kiếm");
        timkiemButton.setBounds(750, 280, 150, 40);
        timkiemButton.setBackground(Color.orange);
        timkiemButton.setFont(btnFont);
        add(timkiemButton);

        // --- BẢNG DỮ LIỆU ---
        String[] tieudeStrings = {"Mã NCC", "Tên NCC", "Loại hình", "Email", "SĐT", "Địa chỉ"};
        nccDefaultTableModel = new DefaultTableModel(tieudeStrings, 0);
        nccJTable = new JTable(nccDefaultTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nccJTable.setFont(new Font("Arial", Font.PLAIN, 16));
        nccJTable.setBackground(Color.white);
        nccJTable.setRowHeight(30);

        JTableHeader nccHeader = nccJTable.getTableHeader();
        nccHeader.setFont(new Font("Arial", Font.BOLD, 16));
        nccHeader.setForeground(Color.BLACK);
        nccHeader.setBackground(Color.GREEN);

        JScrollPane nccJScrollPane = new JScrollPane(nccJTable);
        // Bảng sẽ to hơn một chút để chứa đủ 6 cột
        nccJScrollPane.setBounds(50, 350, 1100, 500);
        add(nccJScrollPane);
    }

    public void addThemClickListener(ActionListener listener) { themButton.addActionListener(listener); }
    public void addSuaClickListener(ActionListener listener) { suaButton.addActionListener(listener); }
    public void addXoaClickListener(ActionListener listener) { xoaButton.addActionListener(listener); }
    public void addResetClickListener(ActionListener listener) { resetButton.addActionListener(listener); }
    public void addTimKiemClickListener(ActionListener listener) { timkiemButton.addActionListener(listener); }
    public void addClickTableListener(MouseListener listener) { nccJTable.addMouseListener(listener); }
}