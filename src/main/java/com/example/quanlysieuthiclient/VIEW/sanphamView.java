package com.example.quanlysieuthiclient.VIEW;

import com.toedter.calendar.JDateChooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class sanphamView extends JPanel {
    // Các ô nhập chữ
    public JTextField maspField, tenspField, xuatxuField, soluongField;
    public JTextField gianhapField, giabanField;

    // Các ô thả xuống (ComboBox)
    public JComboBox<String> maloaiComboBox, manccComboBox;
    public JComboBox<String> tinhtrangComboBox, dvtComboBox;

    // BỘ CHỌN NGÀY LỊCH THÔNG MINH
    public JDateChooser ngaysxChooser, hansdChooser;

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

        JLabel loaiLbl = new JLabel("Mã Loại:"); loaiLbl.setBounds(780, 20, 100, 30); loaiLbl.setFont(labelFont); add(loaiLbl);
        maloaiComboBox = new JComboBox<>(); maloaiComboBox.setBounds(880, 20, 220, 30); maloaiComboBox.setFont(fieldFont); add(maloaiComboBox);

        // --- HÀNG 2 ---
        JLabel nccLbl = new JLabel("Mã NCC:"); nccLbl.setBounds(30, 70, 100, 30); nccLbl.setFont(labelFont); add(nccLbl);
        manccComboBox = new JComboBox<>(); manccComboBox.setBounds(130, 70, 220, 30); manccComboBox.setFont(fieldFont); add(manccComboBox);

        addLabelAndField("Xuất xứ:", 400, 70, labelFont, fieldFont, xuatxuField = new JTextField());
        addLabelAndField("Số lượng:", 780, 70, labelFont, fieldFont, soluongField = new JTextField());

        // --- HÀNG 3 (DÙNG DATE CHOOSER) ---
        JLabel ngaysxLbl = new JLabel("Ngày SX:"); ngaysxLbl.setBounds(30, 120, 100, 30); ngaysxLbl.setFont(labelFont); add(ngaysxLbl);
        ngaysxChooser = new JDateChooser(); ngaysxChooser.setBounds(130, 120, 220, 30); ngaysxChooser.setDateFormatString("dd-MM-yyyy"); ngaysxChooser.setFont(fieldFont); add(ngaysxChooser);

        JLabel hansdLbl = new JLabel("Hạn SD:"); hansdLbl.setBounds(400, 120, 100, 30); hansdLbl.setFont(labelFont); add(hansdLbl);
        hansdChooser = new JDateChooser(); hansdChooser.setBounds(500, 120, 220, 30); hansdChooser.setDateFormatString("dd-MM-yyyy"); hansdChooser.setFont(fieldFont); add(hansdChooser);

        JLabel ttLbl = new JLabel("Tình trạng:"); ttLbl.setBounds(780, 120, 100, 30); ttLbl.setFont(labelFont); add(ttLbl);
        String[] ttData = {"Tốt", "Sắp hết hạn", "Hư hỏng", "Ngừng kinh doanh"};
        tinhtrangComboBox = new JComboBox<>(ttData); tinhtrangComboBox.setBounds(880, 120, 220, 30); tinhtrangComboBox.setFont(fieldFont); add(tinhtrangComboBox);

        // --- HÀNG 4 ---
        addLabelAndField("Giá nhập:", 30, 170, labelFont, fieldFont, gianhapField = new JTextField());
        addLabelAndField("Giá bán:", 400, 170, labelFont, fieldFont, giabanField = new JTextField());

        JLabel dvtLbl = new JLabel("ĐVT:"); dvtLbl.setBounds(780, 170, 100, 30); dvtLbl.setFont(labelFont); add(dvtLbl);
        String[] dvtData = {"Hộp", "Thùng", "Gói", "Túi", "Chai", "Lon", "Khay", "Kg"};
        dvtComboBox = new JComboBox<>(dvtData); dvtComboBox.setBounds(880, 170, 220, 30); dvtComboBox.setFont(fieldFont); add(dvtComboBox);

        // --- NÚT BẤM ---
        Font btnFont = new Font("Arial", Font.ITALIC, 20);
        int btnY = 230;

        themButton = new JButton("Thêm"); themButton.setBounds(250, btnY, 150, 40); themButton.setForeground(Color.white); themButton.setBackground(Color.GREEN); themButton.setFont(btnFont); add(themButton);
        suaButton = new JButton("Sửa"); suaButton.setBounds(450, btnY, 150, 40); suaButton.setFont(btnFont); suaButton.setForeground(Color.white); suaButton.setBackground(Color.blue); add(suaButton);
        xoaButton = new JButton("Xóa"); xoaButton.setBounds(650, btnY, 150, 40); xoaButton.setBackground(Color.red); xoaButton.setFont(btnFont); add(xoaButton);
        resetButton = new JButton("Reset"); resetButton.setBounds(850, btnY, 150, 40); resetButton.setBackground(Color.yellow); resetButton.setFont(btnFont); add(resetButton);

        // --- TÌM KIẾM ---
        timkiemField = new JTextField(); timkiemField.setBounds(350, 290, 350, 40); timkiemField.setFont(fieldFont); add(timkiemField);
        timkiemButton = new JButton("Tìm kiếm"); timkiemButton.setBounds(720, 290, 150, 40); timkiemButton.setBackground(Color.orange); timkiemButton.setFont(btnFont); add(timkiemButton);

        // --- BẢNG DỮ LIỆU ---
        String[] tieudeStrings = {"Mã SP", "Tên SP", "Loại", "NCC", "SL", "Ngày SX", "Hạn SD", "Tình trạng", "Giá nhập", "Giá bán", "ĐVT"};
        spDefaultTableModel = new DefaultTableModel(tieudeStrings, 0);
        spJTable = new JTable(spDefaultTableModel) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        spJTable.setFont(new Font("Arial", Font.PLAIN, 14)); spJTable.setBackground(Color.white); spJTable.setRowHeight(25);
        JTableHeader spHeader = spJTable.getTableHeader(); spHeader.setFont(new Font("Arial", Font.BOLD, 14)); spHeader.setForeground(Color.BLACK); spHeader.setBackground(Color.GREEN);

        JScrollPane spJScrollPane = new JScrollPane(spJTable);
        spJScrollPane.setBounds(20, 350, 1160, 500);
        add(spJScrollPane);
    }

    private void addLabelAndField(String text, int x, int y, Font labelFont, Font fieldFont, JTextField field) {
        JLabel label = new JLabel(text); label.setBounds(x, y, 100, 30); label.setFont(labelFont); add(label);
        field.setBounds(x + 100, y, 220, 30); field.setFont(fieldFont); add(field);
    }

    public void addThemClickListener(ActionListener listener) { themButton.addActionListener(listener); }
    public void addSuaClickListener(ActionListener listener) { suaButton.addActionListener(listener); }
    public void addXoaClickListener(ActionListener listener) { xoaButton.addActionListener(listener); }
    public void addResetClickListener(ActionListener listener) { resetButton.addActionListener(listener); }
    public void addTimKiemClickListener(ActionListener listener) { timkiemButton.addActionListener(listener); }
    public void addClickTableListener(MouseListener listener) { spJTable.addMouseListener(listener); }
}