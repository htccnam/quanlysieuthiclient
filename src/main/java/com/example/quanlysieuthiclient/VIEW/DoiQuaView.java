package com.example.quanlysieuthiclient.VIEW;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DoiQuaView extends JPanel {

    public JComboBox<String> cboKhachHang;
    public JLabel lblDiemKhaDung;
    public JButton btnLichSu, btnXacNhan;

    // Panel chứa danh sách quà dạng Thẻ (Card)
    public JPanel pnlDanhSachQua;

    public DoiQuaView() {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 900));
        setBackground(new Color(245, 248, 250)); // Nền trắng tuyết/xám nhạt

        // --- 1. KHU VỰC THÔNG TIN KHÁCH HÀNG ---
        JPanel pnlKhachHang = new JPanel(null);
        pnlKhachHang.setBounds(20, 20, 1150, 120);
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 102)), " THÔNG TIN KHÁCH HÀNG ",
                TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), new Color(0, 51, 102)));

        JLabel lblKH = new JLabel("Chọn Khách Hàng:");
        lblKH.setBounds(30, 45, 150, 30);
        lblKH.setFont(new Font("Arial", Font.BOLD, 15));
        pnlKhachHang.add(lblKH);

        cboKhachHang = new JComboBox<>();
        cboKhachHang.setBounds(180, 40, 400, 40);
        cboKhachHang.setFont(new Font("Arial", Font.BOLD, 15));
        pnlKhachHang.add(cboKhachHang);

        JLabel lblDiem = new JLabel("Điểm khả dụng:");
        lblDiem.setBounds(650, 45, 120, 30);
        lblDiem.setFont(new Font("Arial", Font.BOLD, 15));
        pnlKhachHang.add(lblDiem);

        lblDiemKhaDung = new JLabel("0 Điểm");
        lblDiemKhaDung.setBounds(780, 40, 200, 40);
        lblDiemKhaDung.setFont(new Font("Arial", Font.BOLD, 22));
        lblDiemKhaDung.setForeground(new Color(204, 153, 0)); // Màu Vàng Gold sang trọng
        pnlKhachHang.add(lblDiemKhaDung);

        btnLichSu = new JButton("Lịch Sử Đổi Quà");
        btnLichSu.setBounds(950, 40, 160, 40);
        btnLichSu.setBackground(new Color(230, 240, 255));
        btnLichSu.setForeground(new Color(0, 51, 102));
        btnLichSu.setFont(new Font("Arial", Font.BOLD, 14));
        pnlKhachHang.add(btnLichSu);

        add(pnlKhachHang);

        // --- 2. KHU VỰC CHỌN QUÀ TẶNG ---
        JLabel lblTitleQua = new JLabel("DANH SÁCH QUÀ TẶNG ĐẶC QUYỀN");
        lblTitleQua.setBounds(20, 160, 400, 30);
        lblTitleQua.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitleQua.setForeground(new Color(0, 51, 102));
        add(lblTitleQua);

        // Nơi chứa các thẻ quà tặng (Sẽ được Controller fill dữ liệu động vào đây)
        pnlDanhSachQua = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        pnlDanhSachQua.setBackground(new Color(245, 248, 250));

        JScrollPane scrollQua = new JScrollPane(pnlDanhSachQua);
        scrollQua.setBounds(20, 200, 1150, 500);
        scrollQua.setBorder(BorderFactory.createEmptyBorder()); // Xóa viền cho đẹp
        add(scrollQua);

        // --- 3. NÚT XÁC NHẬN ---
        btnXacNhan = new JButton("XÁC NHẬN ĐỔI QUÀ");
        btnXacNhan.setBounds(450, 730, 300, 50);
        btnXacNhan.setBackground(new Color(0, 51, 102)); // Xanh Navy
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 18));
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnXacNhan);
    }
}