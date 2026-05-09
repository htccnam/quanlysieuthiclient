/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.quanlysieuthiclient.CONTROLLER;


import com.example.quanlysieuthiclient.VIEW.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class manhinhchinhController {

    final manhinhchinh menu;
//    final DonHang dh = new DonHang();

    public manhinhchinhController(manhinhchinh view) {
        this.menu = view;
        menu.addClickQuanLyNhanVien(new clickNhanSuListener());
        menu.addClickQuanLyKhachHang(new clickKhachHangListener());
//        menu.addClickHangThanhVien(new clickHangThanhVienListener());
//        menu.addClickDoiQua(new clickDoiQuaListener());
        menu.addClickQuanLyChucVu(new clickChucVu());
        menu.addClickQuanLyKhuyenMai(new clickQuanLyKhuyenMai());
        menu.addClickPhanLoaiHang(new clickPhanLoaiHangListener());
//        menu.addClickTaoDonMoi(new clickTaoDonListener());
//        menu.addClickChiTiet(new clickChiTietListener());
        menu.addClickNhaCungCap(new clickNhaCungCapListener());
        menu.addClickDanhSachSanPham(new clickSanPhamListener());
//        menu.addClickDangXuat(new clickDangXuat());

        menu.setVisible(true);
    }

    private class clickNhanSuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nhanvienViews nhanvien = new nhanvienViews();
            new nhanvienController(nhanvien);
            menu.showpanel(nhanvien);
        }

    }
//    private class clickDoiQuaListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            // Chức năng này mở ra cửa sổ riêng (JFrame), không nhúng vào panel chính
//            new CONTROLLER.DoiQuaController();
//        }
//    }
    private class clickKhachHangListener implements ActionListener {

       @Override
        public void actionPerformed(ActionEvent e) {
            KhachHangView khachhang = new KhachHangView();
            new KhachHangController(khachhang);
            menu.showpanel(khachhang);
        }

   }
//
//    private class clickHangThanhVienListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//            HangThanhVienView htvView = new HangThanhVienView();
//
//            //Kích hoạt Controller : (Siêu quan trọng )
//            new HangThanhVienController(htvView);
//            menu.showpanel(htvView);
//        }
//    }

    private class clickChucVu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            chucvuView chucvu = new chucvuView();
            chucvuController controller = new chucvuController(chucvu);
            menu.showpanel(chucvu);
        }
    }

    private class clickQuanLyKhuyenMai implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            khuyenmaiView km = new khuyenmaiView();
            khuyenmaiController kmController = new khuyenmaiController(km);
            menu.showpanel(km);
        }

    }

private class clickPhanLoaiHangListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        loaihangView loaihang = new loaihangView();
        loaihangController controller = new loaihangController(loaihang); // Đã sửa biến chucvu thành loaihang ở đây
        menu.showpanel(loaihang);
    }
}
//
    private class clickChiTietListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ChiTietView ctView = new ChiTietView();
            new ChiTietDonHangController(ctView);
            menu.showpanel(ctView);
        }
    }

    private class clickTaoDonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            TaoDonView tdView = new TaoDonView();
            new TaoDonController(tdView);
            menu.showpanel(tdView);
        }
    }
//
private class clickNhaCungCapListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            nhacungcapView nccView = new nhacungcapView();
            new nhacungcapController(nccView);
            menu.showpanel(nccView);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(menu, "Lỗi: " + ex.getMessage());
        }
    }
}

    private class clickSanPhamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                sanphamView spView = new sanphamView();
                new sanphamController(spView);
                menu.showpanel(spView);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(menu, "Lỗi: " + ex.getMessage());
            }
        }
    }
//    private class clickDangXuat implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            int check = JOptionPane.showConfirmDialog(menu, "bạn có chắc chắn muốn đăng xuất");
//            if (check == JOptionPane.YES_OPTION) {
//                LoginView loginView = new LoginView();
//                loginController lgController = new loginController(loginView);
//                menu.dispose();
//                loginView.setVisible(true);
//            }
//
//        }
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            manhinhchinh manhinh = new manhinhchinh();
            new manhinhchinhController(manhinh);
            manhinh.setLocationRelativeTo(null);
            manhinh.setVisible(true);
        });

    }
}
