package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.HangThanhVienApiClient;
import com.example.quanlysieuthiclient.DTO.HangThanhVien;
import com.example.quanlysieuthiclient.VIEW.DialogThemThanhVien;
import com.example.quanlysieuthiclient.VIEW.HangThanhVienView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HangThanhVienController {
    private HangThanhVienView view;
    private HangThanhVienApiClient apiClient;

    public HangThanhVienController(HangThanhVienView view) {
        this.view = view;
        this.apiClient = HangThanhVienApiClient.getInstance();

        this.view.btnThem.addActionListener(new ThemListener());
        this.view.btnXoa.addActionListener(new XoaListener());

        loadTable();
        this.view.setVisible(true);
    }

    private void loadTable() {
        try {
            view.modelXepHang.setRowCount(0);
            List<HangThanhVien> list = apiClient.getAll();
            for (HangThanhVien h : list) {
                view.modelXepHang.addRow(new Object[]{ h.getMaKH(), h.getTenKH(), h.getTenHang() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    class ThemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mở Dialog y hệt cấu trúc cũ của bạn
            DialogThemThanhVien dialog = new DialogThemThanhVien((JFrame) SwingUtilities.getWindowAncestor(view));
            dialog.showDialog();

            if (dialog.isConfirmed()) {
                try {
                    HangThanhVien htv = new HangThanhVien(dialog.getMa(), dialog.getTen(), dialog.getHang());
                    apiClient.themHang(htv);
                    JOptionPane.showMessageDialog(view, "Xếp hạng thành công!");
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
                }
            }
        }
    }

    class XoaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.tblXepHang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn thành viên trong bảng để xóa hạng!");
                return;
            }

            String maKH = view.modelXepHang.getValueAt(row, 0).toString();
            if (JOptionPane.showConfirmDialog(view, "Hủy hạng của khách hàng " + maKH + "?") == JOptionPane.YES_OPTION) {
                try {
                    apiClient.xoaHang(maKH);
                    JOptionPane.showMessageDialog(view, "Xóa hạng thành công!");
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi xóa: " + ex.getMessage());
                }
            }
        }
    }
}