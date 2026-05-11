package com.example.quanlysieuthiclient.VIEW;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;

public class HangThanhVienView extends JPanel {

    // Components Public để Controller truy cập
    public JComboBox<String> cboKhachHang;
    public JLabel lblTongChiTieuValue;
    public JButton btnCheck, btnThem, btnXoa;
    public DefaultTableModel modelXepHang;
    public JTable tblXepHang;

    public HangThanhVienView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));
        setBackground(new Color(245, 248, 250));

        JPanel pnlRules = new JPanel(null);
        pnlRules.setBounds(20, 20, 550, 200);
        pnlRules.setBackground(Color.WHITE);
        pnlRules.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), " Quy Định Hạng & Quyền Lợi ",
                TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204)));

        String[] colRules = {"Tên Hạng", "Mức Chi Tiêu", "Quyền Lợi"};

        // Rút gọn text một chút xíu để vừa vặn trên bảng nhưng vẫn đầy đủ ý nghĩa
        Object[][] dataRules = {
                {"Member (Đồng)", "> 500.000đ", "Nhận tin KM, Tích điểm đổi quà"},
                {"Bạc (Silver)", "> 1.000.000đ", "Free gói quà, Ưu tiên thanh toán"},
                {"Vàng (Gold)", "> 3.000.000đ", "Quà Lễ/Tết, Free ship (bán kính gần)"},
                {"Kim Cương (VIP)", "> 7.000.000đ", "Quầy riêng, Quà độc quyền, Giữ hạng"}
        };

        JTable tblRules = new JTable(new DefaultTableModel(dataRules, colRules));
        tblRules.setFont(new Font("Arial", Font.BOLD, 13));
        tblRules.setRowHeight(30); // Tăng độ cao mỗi dòng cho thoáng
        tblRules.setEnabled(false);

        // Chỉnh độ rộng cột: Ưu tiên cột Quyền Lợi siêu rộng
        tblRules.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblRules.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblRules.getColumnModel().getColumn(2).setPreferredWidth(300);

        JScrollPane scrollRules = new JScrollPane(tblRules);
        // Tăng chiều cao của ScrollPane
        scrollRules.setBounds(15, 30, 520, 150);
        pnlRules.add(scrollRules);
        add(pnlRules);

        // --- PHẦN 2: TRA CỨU NHANH (Bên phải) ---
        JPanel pnlLookup = new JPanel(null);
        // Tương tự, kéo dài khung này ra cho bằng khung bên trái (height = 200)
        pnlLookup.setBounds(590, 20, 580, 200);
        pnlLookup.setBackground(Color.WHITE);
        pnlLookup.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), " Tra Cứu Chi Tiêu ",
                TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204)));

        JLabel lblKH = new JLabel("Khách Hàng:");
        lblKH.setBounds(30, 50, 120, 30);
        lblKH.setFont(new Font("Arial", Font.BOLD, 14));
        pnlLookup.add(lblKH);

        cboKhachHang = new JComboBox<>();
        cboKhachHang.setBounds(150, 50, 300, 35);
        cboKhachHang.setFont(new Font("Arial", Font.BOLD, 14));
        pnlLookup.add(cboKhachHang);

        btnCheck = new JButton("Kiểm tra");
        btnCheck.setBounds(30, 100, 120, 40);
        btnCheck.setBackground(new Color(0, 102, 204));
        btnCheck.setForeground(Color.WHITE);
        btnCheck.setFont(new Font("Arial", Font.BOLD, 14));
        pnlLookup.add(btnCheck);

        lblTongChiTieuValue = new JLabel("0 VNĐ");
        lblTongChiTieuValue.setBounds(180, 100, 350, 40);
        lblTongChiTieuValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblTongChiTieuValue.setForeground(Color.RED);
        pnlLookup.add(lblTongChiTieuValue);
        add(pnlLookup);

        // --- NÚT CHỨC NĂNG ---
        btnThem = new JButton("Thêm Xếp Hạng");
        btnThem.setBounds(830, 790, 160, 45);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnThem);

        btnXoa = new JButton("Xóa Xếp Hạng");
        btnXoa.setBounds(1010, 790, 160, 45);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnXoa);

        // --- PHẦN 3: BẢNG DANH SÁCH XẾP HẠNG ---
        JLabel lblTitle = new JLabel("DANH SÁCH THÀNH VIÊN ĐÃ XẾP HẠNG", JLabel.CENTER);
        // Đẩy tọa độ y xuống 240 (tránh đè vào bảng quy định)
        lblTitle.setBounds(20, 240, 1150, 40);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle);

        String[] cols = {"Mã Khách Hàng", "Tên Khách Hàng", "Hạng Thành Viên"};
        modelXepHang = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblXepHang = new JTable(modelXepHang);
        tblXepHang.setRowHeight(35);
        tblXepHang.setFont(new Font("Arial", Font.BOLD, 14));

        JTableHeader header = tblXepHang.getTableHeader();
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 15));

        JScrollPane scrollTable = new JScrollPane(tblXepHang);
        // Đẩy tọa độ y xuống 290 và giảm height một chút để nút không bị che
        scrollTable.setBounds(20, 290, 1150, 480);
        add(scrollTable);
    }
}