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

public class sanphamView extends JPanel {
    // 12 trường dữ liệu
    public JTextField maspField, tenspField, maloaiField;
    public JTextField manccField, xuatxuField, soluongField;
    public JTextField ngaysxField, hansdField, tinhtrangField;
    public JTextField gianhapField, giabanField, donvitinhField;

    public JButton themButton, suaButton, xoaButton, resetButton;
    public JTextField timkiemField;
    public JButton timkiemButton;

    public DefaultTableModel spDefaultTableModel;
    public JTable spJTable;

    public sanphamView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        // --- HÀNG 1 ---
        addLabelAndField("Mã SP:", 30, 20, labelFont, fieldFont, maspField = new JTextField());
        addLabelAndField("Tên SP:", 400, 20, labelFont, fieldFont, tenspField = new JTextField());
        addLabelAndField("Mã Loại:", 780, 20, labelFont, fieldFont, maloaiField = new JTextField());

        // --- HÀNG 2 ---
        addLabelAndField("Mã NCC:", 30, 70, labelFont, fieldFont, manccField = new JTextField());
        addLabelAndField("Xuất xứ:", 400, 70, labelFont, fieldFont, xuatxuField = new JTextField());
        addLabelAndField("Số lượng:", 780, 70, labelFont, fieldFont, soluongField = new JTextField());

        // --- HÀNG 3 ---
        addLabelAndField("Ngày SX:", 30, 120, labelFont, fieldFont, ngaysxField = new JTextField());
        addLabelAndField("Hạn SD:", 400, 120, labelFont, fieldFont, hansdField = new JTextField());
        addLabelAndField("Tình trạng:", 780, 120, labelFont, fieldFont, tinhtrangField = new JTextField());

        // --- HÀNG 4 ---
        addLabelAndField("Giá nhập:", 30, 170, labelFont, fieldFont, gianhapField = new JTextField());
        addLabelAndField("Giá bán:", 400, 170, labelFont, fieldFont, giabanField = new JTextField());
        addLabelAndField("ĐVT:", 780, 170, labelFont, fieldFont, donvitinhField = new JTextField());

        // --- NÚT BẤM ---
        Font btnFont = new Font("Arial", Font.ITALIC, 20);
        int btnY = 230;

        themButton = new JButton("Thêm");
        themButton.setBounds(250, btnY, 150, 40);
        themButton.setForeground(Color.white);
        themButton.setBackground(Color.GREEN);
        themButton.setFont(btnFont);
        add(themButton);

        suaButton = new JButton("Sửa");
        suaButton.setBounds(450, btnY, 150, 40);
        suaButton.setFont(btnFont);
        suaButton.setForeground(Color.white);
        suaButton.setBackground(Color.blue);
        add(suaButton);

        xoaButton = new JButton("Xóa");
        xoaButton.setBounds(650, btnY, 150, 40);
        xoaButton.setBackground(Color.red);
        xoaButton.setFont(btnFont);
        add(xoaButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(850, btnY, 150, 40);
        resetButton.setBackground(Color.yellow);
        resetButton.setFont(btnFont);
        add(resetButton);

        // --- TÌM KIẾM ---
        timkiemField = new JTextField();
        timkiemField.setBounds(350, 290, 350, 40);
        timkiemField.setFont(fieldFont);
        add(timkiemField);

        timkiemButton = new JButton("Tìm kiếm");
        timkiemButton.setBounds(720, 290, 150, 40);
        timkiemButton.setBackground(Color.orange);
        timkiemButton.setFont(btnFont);
        add(timkiemButton);

        // --- BẢNG DỮ LIỆU ---
        String[] tieudeStrings = {"Mã SP", "Tên SP", "Loại", "NCC", "SL", "Ngày SX", "Hạn SD", "Tình trạng", "Giá nhập", "Giá bán", "ĐVT"};
        spDefaultTableModel = new DefaultTableModel(tieudeStrings, 0);
        spJTable = new JTable(spDefaultTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        spJTable.setFont(new Font("Arial", Font.PLAIN, 14));
        spJTable.setBackground(Color.white);
        spJTable.setRowHeight(25);

        JTableHeader spHeader = spJTable.getTableHeader();
        spHeader.setFont(new Font("Arial", Font.BOLD, 14));
        spHeader.setForeground(Color.BLACK);
        spHeader.setBackground(Color.GREEN);

        JScrollPane spJScrollPane = new JScrollPane(spJTable);
        spJScrollPane.setBounds(20, 350, 1160, 500); // Kéo giãn bảng hết mức
        add(spJScrollPane);
    }

    // Hàm phụ trợ giúp vẽ giao diện gọn hơn
    private void addLabelAndField(String text, int x, int y, Font labelFont, Font fieldFont, JTextField field) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 100, 30);
        label.setFont(labelFont);
        add(label);

        field.setBounds(x + 100, y, 220, 30);
        field.setFont(fieldFont);
        add(field);
    }

    public void addThemClickListener(ActionListener listener) { themButton.addActionListener(listener); }
    public void addSuaClickListener(ActionListener listener) { suaButton.addActionListener(listener); }
    public void addXoaClickListener(ActionListener listener) { xoaButton.addActionListener(listener); }
    public void addResetClickListener(ActionListener listener) { resetButton.addActionListener(listener); }
    public void addTimKiemClickListener(ActionListener listener) { timkiemButton.addActionListener(listener); }
    public void addClickTableListener(MouseListener listener) { spJTable.addMouseListener(listener); }
}