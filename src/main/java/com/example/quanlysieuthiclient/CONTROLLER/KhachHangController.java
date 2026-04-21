package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.KhachHangApiClient;
import com.example.quanlysieuthiclient.DTO.KhachHang;
import com.example.quanlysieuthiclient.VIEW.KhachHangView;
import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class KhachHangController {
    private KhachHangView view;
    private KhachHangApiClient apiClient;

    public KhachHangController(KhachHangView view) {
        this.view = view;
        this.apiClient = KhachHangApiClient.getInstance();

        // Gắn sự kiện (Giống mẫu chucvuController)
        this.view.addThemClickListener(new ThemListener());
        this.view.addSuaClickListener(new SuaListener());
        this.view.addXoaClickListener(new XoaListener());
        this.view.addTimKiemClickListener(new TimKiemListener());
        this.view.addResetClickListener(new ResetListener());
        this.view.addTableClickListener(new TableClick());

        loadTable();
        this.view.xoaButton.setEnabled(false);
        this.view.suaButton.setEnabled(false);
    }

    private void loadTable() {
        try {
            view.khachHangModel.setRowCount(0);
            List<KhachHang> list = apiClient.getAllKhachHang();
            for (KhachHang kh : list) {
                view.khachHangModel.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getGioiTinh(),
                        kh.getSdt(),
                        kh.getNgaySinh(),
                        kh.getDiachi(),
                        kh.getEmail(),
                        kh.getDiemtichluy()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải bảng: " + e.getMessage());
        }
    }

    // Xử lý sự kiện click bảng để điền form (MouseListener)
    private class TableClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = view.khachHangTable.getSelectedRow();
            if (row >= 0) {
                view.maKHField.setText(view.khachHangModel.getValueAt(row, 0) .toString());;
                view.hoTenField.setText(view.khachHangModel.getValueAt(row, 1).toString());;
                view.gioiTinhBox.setSelectedItem(view.khachHangModel.getValueAt(row, 2).toString());
                view.sdtField.setText(view.khachHangModel.getValueAt(row, 3).toString());
                view.ngaySinhField.setText(view.khachHangModel.getValueAt(row, 4) != null ? view.khachHangModel.getValueAt(row, 4).toString() : "");
                view.diachiField.setText(view.khachHangModel.getValueAt(row, 5) != null ? view.khachHangModel.getValueAt(row, 5).toString() : "");
                view.emailField.setText(view.khachHangModel.getValueAt(row, 6) != null ? view.khachHangModel.getValueAt(row, 6).toString() : "");

                view.maKHField.setEnabled(false);
                view.suaButton.setEnabled(true);
                view.xoaButton.setEnabled(true);
            }
        }
    }

    private class ThemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                KhachHang kh = new KhachHang(
                        view.maKHField.getText().trim(),
                        view.hoTenField.getText().trim(),
                        view.sdtField.getText().trim(),
                        view.gioiTinhBox.getSelectedItem().toString(),
                        view.emailField.getText().trim(),
                        LocalDate.parse(view.ngaySinhField.getText().trim()),
                        view.diachiField.getText().trim(),
                        0

                );
                apiClient.themKhachHang(kh);
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
                loadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            }
        }
    }

    // Các class ResetListener, XoaListener... tương tự như chucvuController của bạn
    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.maKHField.setText("");
            view.hoTenField.setText("");
            view.sdtField.setText("");
            view.emailField.setText("");
            view.ngaySinhField.setText("");
            view.diachiField.setText("");
            view.maKHField.setEnabled(true);
            view.suaButton.setEnabled(false);
            view.xoaButton.setEnabled(false);
            loadTable();
        }
    }

    private class TimKiemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String kw = view.timKiemField.getText().trim();
                List<KhachHang> list = apiClient.timKiemKhachHang(kw);
                view.khachHangModel.setRowCount(0);
                for (KhachHang kh : list) {
                    view.khachHangModel.addRow(new Object[]{ kh.getMaKH(), kh.getHoTen(), kh.getSdt(), kh.getGioiTinh(), kh.getEmail(), kh.getNgaySinh(), kh.getDiachi(), kh.getDiemtichluy() });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi tìm kiếm: " + ex.getMessage());
            }
        }
    }

    private class XoaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = view.maKHField.getText();
            if(JOptionPane.showConfirmDialog(view, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
                try {
                    apiClient.xoaKhachHang(id);
                    JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadTable();
                    view.resetButton.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(view, "Lỗi xóa: " + ex.getMessage()); }
            }
        }
    }

    private class SuaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                KhachHang kh = new KhachHang(
                        view.maKHField.getText().trim(),
                        view.hoTenField.getText().trim(),
                        view.sdtField.getText().trim(),
                        view.gioiTinhBox.getSelectedItem().toString(),
                        view.emailField.getText().trim(),
                        LocalDate.parse(view.ngaySinhField.getText().trim()),
                        view.diachiField.getText().trim(),
                        0
                );
                apiClient.suaKhachHang(kh);
                JOptionPane.showMessageDialog(view, "Sửa thành công!");
                loadTable();
            } catch (Exception ex) { JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage()); }
        }
    }
}