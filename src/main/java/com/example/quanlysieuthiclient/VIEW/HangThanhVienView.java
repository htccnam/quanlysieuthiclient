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

        // --- PHẦN 1: QUY ĐỊNH HẠNG (Bên trái) ---
        JPanel pnlRules = new JPanel(null);
        pnlRules.setBounds(20, 20, 550, 180);
        pnlRules.setBackground(Color.WHITE);
        pnlRules.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), " Quy Định Hạng ",
                TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204)));

        String[] colRules = {"Tên Hạng", "Mức Chi Tiêu", "Quyền Lợi"};
        Object[][] dataRules = {
                {"Bạc (Silver)", "> 3 Triệu", "Giảm 2%"},
                {"Vàng (Gold)", "> 10 Triệu", "Giảm 5%"},
                {"Kim Cương", "> 30 Triệu", "Giảm 10%"}
        };
        JTable tblRules = new JTable(new DefaultTableModel(dataRules, colRules));
        tblRules.setFont(new Font("Arial", Font.BOLD, 13));
        tblRules.setRowHeight(25);
        tblRules.setEnabled(false);
        JScrollPane scrollRules = new JScrollPane(tblRules);
        scrollRules.setBounds(15, 30, 520, 130);
        pnlRules.add(scrollRules);
        add(pnlRules);

        // --- PHẦN 2: TRA CỨU NHANH (Bên phải) ---
        JPanel pnlLookup = new JPanel(null);
        pnlLookup.setBounds(590, 20, 580, 180);
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

        // --- PHẦN 3: BẢNG DANH SÁCH XẾP HẠNG ---
        JLabel lblTitle = new JLabel("DANH SÁCH THÀNH VIÊN ĐÃ XẾP HẠNG", JLabel.CENTER);
        lblTitle.setBounds(20, 220, 1150, 40);
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
        scrollTable.setBounds(20, 270, 1150, 500);
        add(scrollTable);

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
    }
}