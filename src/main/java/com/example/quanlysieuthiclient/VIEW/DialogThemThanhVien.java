package com.example.quanlysieuthiclient.VIEW;

import javax.swing.*;
import java.awt.*;

public class DialogThemThanhVien extends JDialog {

    public JComboBox<String> cboHang;
    public JComboBox<String> cboKhachHang;
    public JButton btnCapNhat;

    private boolean isConfirmed = false;
    private String selectedMaKH, selectedTenKH, selectedHang;

    public DialogThemThanhVien(JFrame parent) {
        super(parent, "Xét Duyệt Hạng Thành Viên", true);
        setLayout(null);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);

        JLabel lbl1 = new JLabel("1. Chọn Hạng Thành Viên:");
        lbl1.setBounds(30, 30, 400, 30);
        lbl1.setFont(new Font("Arial", Font.BOLD, 14));
        add(lbl1);

        cboHang = new JComboBox<>(new String[]{"Bạc (Silver)", "Vàng (Gold)", "Kim Cương"});
        cboHang.setBounds(30, 65, 420, 40);
        cboHang.setFont(new Font("Arial", Font.BOLD, 14));
        add(cboHang);

        JLabel lbl2 = new JLabel("2. Chọn Khách Hàng Đủ Điều Kiện:");
        lbl2.setBounds(30, 125, 400, 30);
        lbl2.setFont(new Font("Arial", Font.BOLD, 14));
        add(lbl2);

        cboKhachHang = new JComboBox<>();
        cboKhachHang.setBounds(30, 160, 420, 40);
        cboKhachHang.setFont(new Font("Arial", Font.BOLD, 14));
        add(cboKhachHang);

        btnCapNhat = new JButton("XÁC NHẬN XẾP HẠNG");
        btnCapNhat.setBounds(130, 240, 240, 45);
        btnCapNhat.setBackground(new Color(0, 102, 204));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnCapNhat);

        // Sự kiện nút xác nhận
        btnCapNhat.addActionListener(e -> {
            if (cboKhachHang.getSelectedItem() == null) return;

            String raw = cboKhachHang.getSelectedItem().toString();
            if (raw.contains(" - ")) {
                selectedMaKH = raw.split(" - ")[0];
                selectedTenKH = raw.split(" - ")[1];
                selectedHang = cboHang.getSelectedItem().toString();
                isConfirmed = true;
                dispose();
            }
        });
    }

    public void showDialog() { setVisible(true); }
    public boolean isConfirmed() { return isConfirmed; }
    public String getMa() { return selectedMaKH; }
    public String getTen() { return selectedTenKH; }
    public String getHang() { return selectedHang; }
}