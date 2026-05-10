package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.khuyenmaiApiClient;
import com.example.quanlysieuthiclient.DTO.khuyenmai;
import com.example.quanlysieuthiclient.VIEW.khuyenmaiView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class khuyenmaiController {

    private final khuyenmaiView kmView;
    private final khuyenmaiApiClient kmApiClient;
    private int selectedRow = -1;

    public khuyenmaiController(khuyenmaiView view) {
        this.kmView = view;
        this.kmApiClient = khuyenmaiApiClient.getInstance();

        kmView.addThemActionListener(new them());
        kmView.addSuaActionListener(new sua());
        kmView.addXoaActionListener(new xoa());
        kmView.addResetActionListener(new reset());
        kmView.addTimKiemActionListener(new timkiem());
        kmView.addcellClickListener(new cellClickListener());

        load_table();
        kmView.setVisible(true);
    }

    private void load_table() {
        kmView.khuyenmaiDefaultTableModel.setRowCount(0);
        try {
            List<khuyenmai> list = kmApiClient.getAllKhuyenMai();
            for (khuyenmai km : list) {
                kmView.khuyenmaiDefaultTableModel.addRow(new Object[]{
                        km.getMakhuyenmai(),
                        km.getTenkhuyenmai(),
                        km.getMota(),
                        km.getSotiengiam(),
                        km.getNgaytao()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(kmView, "Lỗi load table: " + e.getMessage());
        }
    }

    class them implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ma = kmView.makhuyenmaiField.getText().trim();
            String ten = kmView.tenkhuyenmaiField.getText().trim();
            String mota = kmView.motaField.getText().trim();
            String sotiengiamText = kmView.sotiemgiamField.getText().trim();
            java.util.Date ngaytaoDate = kmView.ngaytaoChooser.getDate();

            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(kmView, "Mã khuyến mãi không được trống");
                return;
            }
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(kmView, "Tên khuyến mãi không được trống");
                return;
            }
            int sotiengiam = 0;
            try {
                sotiengiam = Integer.parseInt(sotiengiamText);
                if (sotiengiam <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(kmView, "Số tiền giảm phải là số nguyên dương");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngaytaoStr = sdf.format(ngaytaoDate);

            khuyenmai km = new khuyenmai(ma, ten, mota, sotiengiam, ngaytaoStr);
            try {
                kmApiClient.themKhuyenMai(km);
                JOptionPane.showMessageDialog(kmView, "Thêm thành công");
                load_table();
                resetFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(kmView, "Lỗi thêm: " + ex.getMessage());
            }
        }
    }

    class sua implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ma = kmView.makhuyenmaiField.getText().trim();
            String ten = kmView.tenkhuyenmaiField.getText().trim();
            String mota = kmView.motaField.getText().trim();
            String sotiengiamText = kmView.sotiemgiamField.getText().trim();
            java.util.Date ngaytaoDate = kmView.ngaytaoChooser.getDate();

            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(kmView, "Tên khuyến mãi không được trống");
                return;
            }
            int sotiengiam = 0;
            try {
                sotiengiam = Integer.parseInt(sotiengiamText);
                if (sotiengiam <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(kmView, "Số tiền giảm phải là số nguyên dương");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngaytaoStr = sdf.format(ngaytaoDate);

            khuyenmai km = new khuyenmai(ma, ten, mota, sotiengiam, ngaytaoStr);
            try {
                kmApiClient.suaKhuyenMai(km);
                JOptionPane.showMessageDialog(kmView, "Sửa thành công");
                load_table();
                resetFields();
                kmView.makhuyenmaiField.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(kmView, "Lỗi sửa: " + ex.getMessage());
            }
        }
    }

    class xoa implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(kmView, "Bạn có chắc chắn xóa?");
            if (confirm == JOptionPane.YES_OPTION) {
                String ma = kmView.makhuyenmaiField.getText().trim();
                try {
                    kmApiClient.xoaKhuyenMai(ma);
                    JOptionPane.showMessageDialog(kmView, "Xóa thành công");
                    load_table();
                    resetFields();
                    kmView.makhuyenmaiField.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(kmView, "Lỗi xóa: " + ex.getMessage());
                }
            }
        }
    }

    class reset implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetFields();
            load_table();
            kmView.makhuyenmaiField.setEnabled(true);
        }
    }

    private void resetFields() {
        kmView.makhuyenmaiField.setText("");
        kmView.tenkhuyenmaiField.setText("");
        kmView.motaField.setText("");
        kmView.sotiemgiamField.setText("");
        kmView.timkiemField.setText("");
        Calendar cal = Calendar.getInstance();
        kmView.ngaytaoChooser.setDate(cal.getTime());
    }

    class timkiem implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyword = kmView.timkiemField.getText().trim();
            kmView.khuyenmaiDefaultTableModel.setRowCount(0);
            try {
                List<khuyenmai> list = kmApiClient.timKiemKhuyenMai(keyword);
                for (khuyenmai km : list) {
                    kmView.khuyenmaiDefaultTableModel.addRow(new Object[]{
                            km.getMakhuyenmai(),
                            km.getTenkhuyenmai(),
                            km.getMota(),
                            km.getSotiengiam(),
                            km.getNgaytao()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(kmView, "Lỗi tìm kiếm: " + ex.getMessage());
            }
        }
    }

    // Click vào table để load lên form
    class cellClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedRow = kmView.khuyenmaiJTable.getSelectedRow();
            kmView.makhuyenmaiField.setText(kmView.khuyenmaiJTable.getValueAt(selectedRow, 0).toString());
            kmView.tenkhuyenmaiField.setText(kmView.khuyenmaiJTable.getValueAt(selectedRow, 1).toString());
            kmView.motaField.setText(kmView.khuyenmaiJTable.getValueAt(selectedRow, 2).toString());
            kmView.sotiemgiamField.setText(kmView.khuyenmaiJTable.getValueAt(selectedRow, 3).toString());
            String ngaytaoStr = kmView.khuyenmaiJTable.getValueAt(selectedRow, 4).toString();
            try {
                java.sql.Date sqlDate = java.sql.Date.valueOf(ngaytaoStr);
                kmView.ngaytaoChooser.setDate(sqlDate);
            } catch (IllegalArgumentException ex) {
                // Nếu server trả về datetime có T, cắt chuỗi
                if (ngaytaoStr.contains("T")) {
                    ngaytaoStr = ngaytaoStr.split("T")[0];
                    kmView.ngaytaoChooser.setDate(java.sql.Date.valueOf(ngaytaoStr));
                }
            }
            kmView.makhuyenmaiField.setEnabled(false);
        }
        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }
}