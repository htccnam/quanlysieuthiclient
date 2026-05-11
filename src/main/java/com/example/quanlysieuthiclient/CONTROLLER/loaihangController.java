package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.loaihangApiClient;
import com.example.quanlysieuthiclient.DTO.loaihang;
import com.example.quanlysieuthiclient.VIEW.loaihangView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class loaihangController {
    private loaihangView lhView;
    private loaihangApiClient lhApiClient;
    private int selectedrow = -1;

    public loaihangController(loaihangView lhView) {
        this.lhView = lhView;
        this.lhApiClient = loaihangApiClient.getInstance();

        lhView.addThemClickListener(new themloaihangListener());
        lhView.addSuaClickListener(new sualoaihangListener());
        lhView.addTimKiemClickListener(new timkiemloaihangListener());
        lhView.addClickTableListener(new clicktableListener());
        lhView.addXoaClickListener(new xoaloaihangListener());
        lhView.addResetClickListener(new resetListener());

        loadTable();
        lhView.xoaButton.setEnabled(false);
        lhView.suaButton.setEnabled(false);
    }

    private void loadTable() {
        try {
            lhView.loaihangDefaultTableModel.setRowCount(0);
            List<loaihang> list = lhApiClient.getAllLoaiHang();
            for (loaihang lh : list) {
                lhView.loaihangDefaultTableModel.addRow(new Object[]{
                        lh.getMaloai(),
                        lh.getTenloai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(lhView, "Lỗi tải bảng loại hàng: " + e.getMessage());
        }
    }

    private class themloaihangListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maloaiString = lhView.maloaiField.getText().trim();
            String tenloaiString = lhView.tenloaiField.getText().trim();

            if (maloaiString.isEmpty() || tenloaiString.isEmpty()) {
                JOptionPane.showMessageDialog(lhView, "Mã loại và tên loại không được để trống");
                return;
            }

            loaihang lh = new loaihang(maloaiString, tenloaiString);
            try {
                lhApiClient.themLoaiHang(lh);
                loadTable();
                lhView.maloaiField.setText("");
                lhView.tenloaiField.setText("");
                lhView.maloaiField.setEnabled(true);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(lhView, "Lỗi thêm loại hàng: " + exception.getMessage());
            }
        }
    }

    private class sualoaihangListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maloaiString = lhView.maloaiField.getText();
            String tenloaiString = lhView.tenloaiField.getText();

            int result = JOptionPane.showConfirmDialog(lhView, "Bạn có chắc chắn muốn sửa?");
            if (result == JOptionPane.YES_OPTION) {
                loaihang lh = new loaihang(maloaiString, tenloaiString);
                try {
                    lhApiClient.suaLoaiHang(lh);
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(lhView, "Lỗi sửa loại hàng: " + ex.getMessage());
                }
            }
        }
    }

    private class xoaloaihangListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maloaiString = lhView.maloaiField.getText();
            int result = JOptionPane.showConfirmDialog(lhView, "Bạn có chắc chắn muốn xóa?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    lhApiClient.xoaLoaiHang(maloaiString);
                    loadTable();

                    // Reset fields sau khi xóa
                    lhView.maloaiField.setText("");
                    lhView.tenloaiField.setText("");
                    lhView.maloaiField.setEnabled(true);
                    lhView.suaButton.setEnabled(false);
                    lhView.xoaButton.setEnabled(false);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(lhView, "Lỗi xóa loại hàng: " + ex.getMessage());
                }
            }
        }
    }

    private class timkiemloaihangListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            lhView.loaihangDefaultTableModel.setRowCount(0);
            try {
                List<loaihang> list = lhApiClient.timKiemLoaiHang(lhView.timkiemField.getText().trim());
                for (loaihang lh : list) {
                    lhView.loaihangDefaultTableModel.addRow(new Object[]{
                            lh.getMaloai(),
                            lh.getTenloai()
                    });
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(lhView, "Lỗi tìm kiếm: " + exception.getMessage());
            }
        }
    }

    private class clicktableListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            lhView.maloaiField.setEnabled(false);
            selectedrow = lhView.loaihangJTable.getSelectedRow();
            lhView.maloaiField.setText(lhView.loaihangDefaultTableModel.getValueAt(selectedrow, 0).toString().trim());
            lhView.tenloaiField.setText(lhView.loaihangDefaultTableModel.getValueAt(selectedrow, 1).toString().trim());
            lhView.suaButton.setEnabled(true);
            lhView.xoaButton.setEnabled(true);
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }

    private class resetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            lhView.maloaiField.setText("");
            lhView.tenloaiField.setText("");
            lhView.timkiemField.setText("");
            lhView.maloaiField.setEnabled(true);
            lhView.tenloaiField.setEnabled(true);
            lhView.themButton.setEnabled(true);
            lhView.xoaButton.setEnabled(false);
            lhView.suaButton.setEnabled(false);
            loadTable();
        }
    }
}