package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.DonHangApiClient;
import com.example.quanlysieuthiclient.APICLIENT.nhanvienApiClient;
// Giả định bạn đã có các ApiClient này tương tự nhanvienApiClient
// import com.example.quanlysieuthiclient.APICLIENT.khachhangApiClient;
// import com.example.quanlysieuthiclient.APICLIENT.sanphamApiClient;

import com.example.quanlysieuthiclient.DTO.ChiTietDon;
import com.example.quanlysieuthiclient.DTO.DonHang;
import com.example.quanlysieuthiclient.DTO.nhanvien;
import com.example.quanlysieuthiclient.VIEW.TaoDonView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TaoDonController {

    private final TaoDonView view;
    private final DonHangApiClient donHangApiClient;
    private final nhanvienApiClient nvApiClient;
    // private final khachhangApiClient khApiClient;
    // private final sanphamApiClient spApiClient;

    public TaoDonController(TaoDonView view) {
        this.view = view;
        this.donHangApiClient = DonHangApiClient.getInstance();
        this.nvApiClient = nhanvienApiClient.getInstance();

        initData();
        initEvents();
    }

    // 1. Load dữ liệu từ API đổ vào các ComboBox và Table Sản phẩm
    private void initData() {
        try {
            // Load Nhân viên
            List<nhanvien> listNV = nvApiClient.getAllNhanVien();
            view.getCboNV().removeAllItems();
            for (nhanvien nv : listNV) {
                view.getCboNV().addItem(nv.getManhanvien() + " - " + nv.getTennhanvien());
            }

            // Load Khách hàng (Tương tự nếu bạn có khachhangApiClient)
            // ...

            // Load Sản phẩm vào bảng bên trái (Sản phẩm có sẵn)
            // List<sanpham> listSP = spApiClient.getAllSanPham();
            // loadTableSanPham(listSP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        // Nút thêm sản phẩm vào đơn hàng (bảng bên phải)
        view.getBtnThem().addActionListener(e -> themSanPhamVaoGio());

        // Nút xóa sản phẩm khỏi đơn hàng
        view.getBtnXoa().addActionListener(e -> xoaSanPhamKhoiGio());

        // Nút Lưu đơn hàng (Gửi API)
        view.getBtnLuu().addActionListener(e -> luuDonHang());

        // Tìm kiếm sản phẩm nhanh
        view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // logic gọi api search sản phẩm và update tableSanPham
            }
        });
    }

    // 2. Logic xử lý giỏ hàng tại Client
    private void themSanPhamVaoGio() {
        int row = view.getTableSanPham().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm!");
            return;
        }

        String maSP = view.getTableSanPham().getValueAt(row, 0).toString();
        String tenSP = view.getTableSanPham().getValueAt(row, 1).toString();
        double gia = Double.parseDouble(view.getTableSanPham().getValueAt(row, 2).toString());
        int soLuong = (int) view.getSpinner().getValue();

        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0!");
            return;
        }

        double thanhTien = gia * soLuong;
        DefaultTableModel modelGioHang = (DefaultTableModel) view.getTable().getModel();
        modelGioHang.addRow(new Object[]{maSP, tenSP, gia, soLuong, thanhTien});

        tinhToanTongTien();
    }

    private void xoaSanPhamKhoiGio() {
        int row = view.getTable().getSelectedRow();
        if (row != -1) {
            ((DefaultTableModel) view.getTable().getModel()).removeRow(row);
            tinhToanTongTien();
        }
    }

    private void tinhToanTongTien() {
        double tong = 0;
        for (int i = 0; i < view.getTable().getRowCount(); i++) {
            tong += Double.parseDouble(view.getTable().getValueAt(i, 4).toString());
        }
        view.getLblTamTinh().setText(String.format("%,.0f đ", tong));
        view.getLblTongTien().setText(String.format("%,.0f đ", tong)); // Có thể trừ thêm KM nếu có logic
    }

    // 3. Gửi dữ liệu về API Server
    private void luuDonHang() {
        try {
            // Thu thập thông tin Đơn hàng (DTO)
            DonHang dh = new DonHang();
            dh.setMaDonHang(view.getTxtMaDon().getText());
            dh.setMaNV(view.getCboNV().getSelectedItem().toString().split(" - ")[0]);
            dh.setMaKH(view.getCboKH().getSelectedItem().toString());
            dh.setNgayGD(new java.sql.Date(view.getNgayGD().getDate().getTime()));
            dh.setPTban(view.getCboBanHang().getSelectedItem().toString());
            dh.setPTgiaodich(view.getCboThanhToan().getSelectedItem().toString());

            // Lấy text từ Label tổng tiền và parse lại số
            String tongTienStr = view.getLblTongTien().getText().replaceAll("[^0-9]", "");
            dh.setTongTien(Double.parseDouble(tongTienStr));

            // Thu thập danh sách Chi tiết (DTO List)
            List<ChiTietDon> dsChiTiet = new ArrayList<>();
            for (int i = 0; i < view.getTable().getRowCount(); i++) {
                ChiTietDon ct = new ChiTietDon(
                        dh.getMaDonHang(),
                        view.getTable().getValueAt(i, 0).toString(),
                        view.getTable().getValueAt(i, 1).toString(),
                        Integer.parseInt(view.getTable().getValueAt(i, 3).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 2).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 4).toString())
                );
                dsChiTiet.add(ct);
            }

            // Gọi ApiClient để POST lên Server
            donHangApiClient.saveDonHang(dh, dsChiTiet);

            JOptionPane.showMessageDialog(view, "Lưu đơn hàng thành công!");
            resetForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu đơn hàng: " + ex.getMessage());
        }
    }

    private void resetForm() {
        view.getTxtMaDon().setText("");
        ((DefaultTableModel) view.getTable().getModel()).setRowCount(0);
        tinhToanTongTien();
    }
}