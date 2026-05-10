package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.DonHangApiClient;
import com.example.quanlysieuthiclient.APICLIENT.KhachHangApiClient;
import com.example.quanlysieuthiclient.APICLIENT.nhanvienApiClient;
import com.example.quanlysieuthiclient.APICLIENT.KhachHangApiClient;
import com.example.quanlysieuthiclient.APICLIENT.sanphamApiClient;

import com.example.quanlysieuthiclient.DTO.ChiTietDon;
import com.example.quanlysieuthiclient.DTO.DonHang;
import com.example.quanlysieuthiclient.DTO.KhachHang;
import com.example.quanlysieuthiclient.DTO.nhanvien;
import com.example.quanlysieuthiclient.VIEW.TaoDonView;
import com.example.quanlysieuthiclient.DTO.sanpham;

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
    private boolean isEditMode = false;

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

            // Load Khách hàng
            List<KhachHang> listKH = KhachHangApiClient.getInstance().getAllKhachHang();
            view.getCboKH().removeAllItems();
            for (KhachHang kh : listKH) {
                view.getCboKH().addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }

            // Load Sản phẩm vào bảng bên trái (Sản phẩm có sẵn)
           List<sanpham> listSP = sanphamApiClient.getInstance().getAllSanPham();
            view.loadDataTable(listSP);

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

    public void hienThiDonHangDeSua(DonHang dh, List<ChiTietDon> dsChiTiet) {
        isEditMode = true;

        // 1. Đổ thông tin đơn hàng
        view.getTxtMaDon().setText(dh.getMadonhang());
        view.getTxtMaDon().setEditable(false); // Không cho sửa mã đơn hàng
        view.getNgayGD().setDate(dh.getNgaylap());
        view.getCboBanHang().setSelectedItem(dh.getPhuongthucban());
        view.getCboThanhToan().setSelectedItem(dh.getThanhtoan());

        // Đổ nhân viên (Cần xử lý chuỗi vì combo của bạn là "Ma - Ten")
        for (int i = 0; i < view.getCboNV().getItemCount(); i++) {
            if (view.getCboNV().getItemAt(i).startsWith(dh.getManhanvien())) {
                view.getCboNV().setSelectedIndex(i);
                break;
            }
        }

        // 2. Đổ danh sách sản phẩm vào bảng (bên phải)
        DefaultTableModel modelGioHang = (DefaultTableModel) view.getTable().getModel();
        modelGioHang.setRowCount(0);
        for (ChiTietDon ct : dsChiTiet) {
            modelGioHang.addRow(new Object[]{
                    ct.getMasanpham(),
                    ct.getTensanpham(),
                    ct.getDongia(),
                    ct.getSoluong(),
                    ct.getThanhtien()
            });
        }

        tinhToanTongTien();
    }

    // 3. Gửi dữ liệu về API Server
    private void luuDonHang() {
        try {
            // Thu thập thông tin Đơn hàng (DTO)
            DonHang dh = new DonHang();
            dh.setMadonhang(view.getTxtMaDon().getText());
            dh.setManhanvien(view.getCboNV().getSelectedItem().toString().split(" - ")[0]);
            String khSelected = view.getCboKH().getSelectedItem().toString();
            dh.setMakhachhang(khSelected.contains(" - ") ? khSelected.split(" - ")[0] : khSelected);
            if (view.getNgayGD().getDate() != null) {
                dh.setNgaylap(view.getNgayGD().getDate());
            } else {
                dh.setNgaylap(new java.util.Date());
            }
            dh.setPhuongthucban(view.getCboBanHang().getSelectedItem().toString());
            dh.setThanhtoan(view.getCboThanhToan().getSelectedItem().toString());

            // Lấy text từ Label tổng tiền và parse lại số
            String tongTienStr = view.getLblTongTien().getText().replaceAll("[^0-9]", "");
            dh.setTongtien(Double.parseDouble(tongTienStr));

            // Thu thập danh sách Chi tiết
            List<ChiTietDon> dsChiTiet = new ArrayList<>();
            for (int i = 0; i < view.getTable().getRowCount(); i++) {
                ChiTietDon ct = new ChiTietDon(
                        dh.getMadonhang(),
                        view.getTable().getValueAt(i, 0).toString(),
                        view.getTable().getValueAt(i, 1).toString(),
                        Integer.parseInt(view.getTable().getValueAt(i, 3).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 2).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 4).toString())
                );
                dsChiTiet.add(ct);
            }

            if (isEditMode) {
                // Gửi yêu cầu PUT để cập nhật
                donHangApiClient.updateDonHang(dh.getMadonhang(), dh, dsChiTiet);
                JOptionPane.showMessageDialog(view, "Cập nhật đơn hàng thành công!");
            } else {
                // Gửi yêu cầu POST để tạo mới
                donHangApiClient.saveDonHang(dh, dsChiTiet);
                JOptionPane.showMessageDialog(view, "Lưu đơn hàng mới thành công!");
            }

            resetForm();
            isEditMode = false;
            view.getTxtMaDon().setEditable(true);

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