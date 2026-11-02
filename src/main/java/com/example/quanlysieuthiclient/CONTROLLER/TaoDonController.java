package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.*;

import com.example.quanlysieuthiclient.DTO.*;
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
    private boolean isEditMode = false;

    public TaoDonController(TaoDonView view) {
        this.view = view;
        this.donHangApiClient = DonHangApiClient.getInstance();
        this.nvApiClient = nhanvienApiClient.getInstance();

        initData();
        initEvents();
    }

    private void initData() {
        try {
            List<nhanvien> listNV = nvApiClient.getAllNhanVien();
            view.getCboNV().removeAllItems();
            for (nhanvien nv : listNV) {
                view.getCboNV().addItem(nv.getManhanvien() + " - " + nv.getTennhanvien());
            }

            List<KhachHang> listKH = KhachHangApiClient.getInstance().getAllKhachHang();
            view.getCboKH().removeAllItems();
            for (KhachHang kh : listKH) {
                view.getCboKH().addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }

           List<sanpham> listSP = sanphamApiClient.getInstance().getAllSanPham();
            view.loadDataTable(listSP);

            List<khuyenmai> listKM = khuyenmaiApiClient.getInstance().getAllKhuyenMai();
            view.getCboMaKM().removeAllItems();
            view.getCboMaKM().addItem("");
            for(khuyenmai km : listKM){
                view.getCboMaKM().addItem(km.getMakhuyenmai());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        view.getBtnThem().addActionListener(e -> themSanPhamVaoGio());

        view.getBtnXoa().addActionListener(e -> xoaSanPhamKhoiGio());

        view.getBtnLuu().addActionListener(e -> luuDonHang());

        view.getCboMaKM().addActionListener(e -> tinhToanTongTien());

        view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

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
        if (view.getTable() == null) return;
        double tamTinh = 0;

        for (int i = 0; i < view.getTable().getRowCount(); i++) {
            tamTinh += Double.parseDouble(view.getTable().getValueAt(i, 4).toString());
        }

        String selectedKM = (String) view.getCboMaKM().getSelectedItem();
        double phanTramGiam = 0;

        if (selectedKM != null) {
            String maKM = selectedKM.toString();
            if (maKM.equals("KM01")) phanTramGiam = 0.1;      // 10%
            else if (maKM.equals("KM02")) phanTramGiam = 0.2; // 20%
            else if (maKM.equals("KM03")) phanTramGiam = 0.3; // 30%
        }

        double tienGiam = tamTinh * phanTramGiam;
        double tongCuoi = tamTinh - tienGiam;

        view.getLblTamTinh().setText(String.format("Tạm tính: %,.0f đ", tamTinh));
        view.getLblKM().setText(String.format("Giảm giá (%d%%): %,.0f đ", (int)(phanTramGiam * 100), tienGiam));
        view.getLblTongTien().setText(String.format("Tổng tiền: %,.0f đ", tongCuoi));
    }

    public void hienThiDonHangDeSua(DonHang dh, List<ChiTietDon> dsChiTiet) {
        isEditMode = true;

        view.getTxtMaDon().setText(dh.getMadonhang());
        view.getTxtMaDon().setEditable(false);
        view.getNgayGD().setDate(dh.getNgaylap());
        view.getCboBanHang().setSelectedItem(dh.getPhuongthucban());
        view.getCboThanhToan().setSelectedItem(dh.getThanhtoan());

        for (int i = 0; i < view.getCboNV().getItemCount(); i++) {
            if (view.getCboNV().getItemAt(i).startsWith(dh.getManhanvien())) {
                view.getCboNV().setSelectedIndex(i);
                break;
            }
        }

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

    private void luuDonHang() {
        try {
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

            String tongTienStr = view.getLblTongTien().getText().replaceAll("[^0-9]", "");
            dh.setTongtien(Double.parseDouble(tongTienStr));

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
                donHangApiClient.updateDonHang(dh.getMadonhang(), dh, dsChiTiet);
                JOptionPane.showMessageDialog(view, "Cập nhật đơn hàng thành công!");
            } else {
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