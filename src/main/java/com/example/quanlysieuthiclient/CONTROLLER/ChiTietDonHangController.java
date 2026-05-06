package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.DonHangApiClient;
import com.example.quanlysieuthiclient.DTO.DonHang;
import com.example.quanlysieuthiclient.VIEW.ChiTietView;
import javax.swing.*;
import java.util.List;

public class ChiTietDonHangController {
    private ChiTietView view;
    private DonHangApiClient apiClient;

    public ChiTietDonHangController(ChiTietView view) {
        this.view = view;
        this.apiClient = DonHangApiClient.getInstance();

        loadTable();
        initEvents();
    }

    private void loadTable() {
        try {
            List<DonHang> list = apiClient.getAllDonHang();
            view.getModel().setRowCount(0);
            for (DonHang dh : list) {
                view.getModel().addRow(new Object[]{
                        dh.getMaDonHang(), dh.getNgayGD(), dh.getMaNV(),
                        dh.getPTban(), dh.getPTgiaodich(), dh.getTongTien()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        view.getBtnXoa().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row != -1) {
                String maDH = view.getTable().getValueAt(row, 0).toString();
                try {
                    apiClient.xoaDonHang(maDH);
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi khi xóa!");
                }
            }
        });
    }
}