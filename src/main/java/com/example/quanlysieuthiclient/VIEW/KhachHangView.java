package com.example.quanlysieuthiclient.VIEW;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import com.toedter.calendar.JDateChooser;

public class KhachHangView extends JPanel {
    // Các trường nhập liệu (Public để Controller truy cập trực tiếp)
    public JTextField maKHField, hoTenField, sdtField, emailField, diachiField, timKiemField;
    public JDateChooser ngaySinhDate;
    public JComboBox<String> gioiTinhBox;
    public JButton themButton, suaButton, xoaButton, resetButton, timKiemButton;
    public JTable khachHangTable;
    public DefaultTableModel khachHangModel;

    public KhachHangView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));
        setBackground(new Color(245, 248, 250));

        // --- KHUNG THÔNG TIN KHÁCH HÀNG ---
        JPanel pnlInput = new JPanel(null);
        pnlInput.setBounds(20, 20, 1150, 200);
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), " Thông Tin Khách Hàng ",
                TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204)));

        // Cột 1
        addLabel(pnlInput, "Mã Khách Hàng:", 30, 40);
        maKHField = createTextField(180, 40); pnlInput.add(maKHField);

        addLabel(pnlInput, "Họ và Tên:", 30, 80);
        hoTenField = createTextField(180, 80); pnlInput.add(hoTenField);

        addLabel(pnlInput, "Số Điện Thoại:", 30, 120);
        sdtField = createTextField(180, 120); pnlInput.add(sdtField);

        addLabel(pnlInput, "Giới Tính:", 30, 160);
        gioiTinhBox = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        gioiTinhBox.setBounds(180, 160, 350, 30);
        gioiTinhBox.setFont(new Font("Arial", Font.BOLD, 14));
        pnlInput.add(gioiTinhBox);

        // Cột 2
        addLabel(pnlInput, "Email:", 600, 40);
        emailField = createTextField(800, 40); pnlInput.add(emailField);

        addLabel(pnlInput, "Ngày Sinh:", 600, 80);
        ngaySinhDate = new JDateChooser();
        ngaySinhDate.setDateFormatString("yyyy-MM-dd");
        ngaySinhDate.setBounds(800, 80, 350, 30);
        ngaySinhDate.setFont(new Font("Arial", Font.BOLD, 14));
        pnlInput.add(ngaySinhDate);

        addLabel(pnlInput, "Địa Chỉ:", 600, 120);
        diachiField = createTextField(800, 120);
        pnlInput.add(diachiField);

        add(pnlInput);

        // --- CÁC NÚT CHỨC NĂNG ---
        themButton = createStyledButton("Thêm", 350, 240, new Color(0, 102, 204)); add(themButton);
        suaButton = createStyledButton("Sửa", 500, 240, new Color(255, 193, 7)); add(suaButton);
        xoaButton = createStyledButton("Xóa", 650, 240, new Color(220, 53, 69)); add(xoaButton);
        resetButton = createStyledButton("Làm mới", 800, 240, new Color(108, 117, 125)); add(resetButton);

        // --- TÌM KIẾM ---
        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setBounds(850, 300, 100, 30);
        lblTim.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTim);

        timKiemField = new JTextField();
        timKiemField.setBounds(930, 300, 150, 30);
        add(timKiemField);

        timKiemButton = new JButton("Tìm");
        timKiemButton.setBounds(1090, 300, 80, 30);
        timKiemButton.setBackground(new Color(0, 102, 204));
        timKiemButton.setForeground(Color.WHITE);
        add(timKiemButton);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"Mã KH", "Họ Tên", "Giới Tính", "SĐT", "Ngày Sinh", "Địa Chỉ", "Email", "Điểm"};
        khachHangModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        khachHangTable = new JTable(khachHangModel);
        khachHangTable.setRowHeight(30);
        khachHangTable.setFont(new Font("Arial", Font.BOLD, 14));

        JTableHeader header = khachHangTable.getTableHeader();
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane scroll = new JScrollPane(khachHangTable);
        scroll.setBounds(20, 350, 1150, 500);
        add(scroll);
    }

    // --- HELPER METHODS ---
    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 30);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        p.add(lbl);
    }

    private JTextField createTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 350, 30);
        txt.setFont(new Font("Arial", Font.BOLD, 14));
        return txt;
    }

    private JButton createStyledButton(String text, int x, int y, Color bg) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 120, 40);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    // --- REGISTER LISTENERS (Theo mẫu chucvuView) ---
    public void addThemClickListener(ActionListener log) { themButton.addActionListener(log); }
    public void addSuaClickListener(ActionListener log) { suaButton.addActionListener(log); }
    public void addXoaClickListener(ActionListener log) { xoaButton.addActionListener(log); }
    public void addResetClickListener(ActionListener log) { resetButton.addActionListener(log); }
    public void addTimKiemClickListener(ActionListener log) { timKiemButton.addActionListener(log); }
    public void addTableClickListener(MouseListener log) { khachHangTable.addMouseListener(log); }
}