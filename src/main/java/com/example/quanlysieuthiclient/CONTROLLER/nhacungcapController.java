package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.nhacungcapApiClient;
import com.example.quanlysieuthiclient.DTO.nhacungcap;
import com.example.quanlysieuthiclient.VIEW.nhacungcapView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class nhacungcapController {
    private nhacungcapView nccView;
    private nhacungcapApiClient nccApiClient;
    private int selectedrow = -1;

    public nhacungcapController(nhacungcapView nccView) {
        this.nccView = nccView;
        this.nccApiClient = nhacungcapApiClient.getInstance();

        nccView.addThemClickListener(new themNhaCungCapListener());
        nccView.addSuaClickListener(new suaNhaCungCapListener());
        nccView.addTimKiemClickListener(new timkiemNhaCungCapListener());
        nccView.addClickTableListener(new clicktableListener());
        nccView.addXoaClickListener(new xoaNhaCungCapListener());
        nccView.addResetClickListener(new resetListener());

        loadTable();
        nccView.xoaButton.setEnabled(false);
        nccView.suaButton.setEnabled(false);
    }

    private void loadTable() {
        try {
            nccView.nccDefaultTableModel.setRowCount(0);
            List<nhacungcap> list = nccApiClient.getAllNhaCungCap();
            for (nhacungcap ncc : list) {
                nccView.nccDefaultTableModel.addRow(new Object[]{
                        ncc.getManhacungcap(),
                        ncc.getTennhacungcap(),
                        ncc.getLoaihinh(),
                        ncc.getEmail(),
                        ncc.getSodienthoai(),
                        ncc.getDiachi()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(nccView, "Lỗi tải bảng nhà cung cấp: " + e.getMessage());
        }
    }

    // --- LẤY DỮ LIỆU TỪ VIEW ---
    private nhacungcap getModelFromView() {
        return new nhacungcap(
                nccView.manccField.getText().trim(),
                nccView.tennccField.getText().trim(),
                nccView.loaihinhField.getText().trim(),
                nccView.emailField.getText().trim(),
                nccView.sdtField.getText().trim(),
                nccView.diachiField.getText().trim()
        );
    }

    private class themNhaCungCapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nhacungcap ncc = getModelFromView();

            // Validate cơ bản
            if (ncc.getManhacungcap().isEmpty() || ncc.getTennhacungcap().isEmpty()) {
                JOptionPane.showMessageDialog(nccView, "Mã và Tên Nhà cung cấp không được để trống!");
                return;
            }

            try {
                nccApiClient.themNhaCungCap(ncc);
                loadTable();
                resetForm();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(nccView, "Lỗi thêm NCC: " + exception.getMessage());
            }
        }
    }

    private class suaNhaCungCapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(nccView, "Bạn có chắc chắn muốn sửa?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    nhacungcap ncc = getModelFromView();
                    nccApiClient.suaNhaCungCap(ncc);
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(nccView, "Lỗi sửa NCC: " + ex.getMessage());
                }
            }
        }
    }

    private class xoaNhaCungCapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mancc = nccView.manccField.getText();
            int result = JOptionPane.showConfirmDialog(nccView, "Bạn có chắc chắn muốn xóa?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    nccApiClient.xoaNhaCungCap(mancc);
                    loadTable();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(nccView, "Lỗi xóa NCC: " + ex.getMessage());
                }
            }
        }
    }

    private class timkiemNhaCungCapListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nccView.nccDefaultTableModel.setRowCount(0);
            try {
                String keyword = nccView.timkiemField.getText().trim();
                List<nhacungcap> list = nccApiClient.timKiemNhaCungCap(keyword);
                for (nhacungcap ncc : list) {
                    nccView.nccDefaultTableModel.addRow(new Object[]{
                            ncc.getManhacungcap(), ncc.getTennhacungcap(),
                            ncc.getLoaihinh(), ncc.getEmail(),
                            ncc.getSodienthoai(), ncc.getDiachi()
                    });
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(nccView, "Lỗi tìm kiếm: " + exception.getMessage());
            }
        }
    }

    private class clicktableListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            nccView.manccField.setEnabled(false); // Khóa sửa mã
            selectedrow = nccView.nccJTable.getSelectedRow();

            // Đổ 6 cột dữ liệu ngược lên 6 ô TextField
            nccView.manccField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 0).toString());
            nccView.tennccField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 1).toString());
            nccView.loaihinhField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 2).toString());
            nccView.emailField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 3).toString());
            nccView.sdtField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 4).toString());
            nccView.diachiField.setText(nccView.nccDefaultTableModel.getValueAt(selectedrow, 5).toString());

            nccView.suaButton.setEnabled(true);
            nccView.xoaButton.setEnabled(true);
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }

    private class resetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetForm();
            loadTable();
        }
    }

    // Hàm phụ trợ để dọn dẹp các trường nhập liệu
    private void resetForm() {
        nccView.manccField.setText("");
        nccView.tennccField.setText("");
        nccView.loaihinhField.setText("");
        nccView.emailField.setText("");
        nccView.sdtField.setText("");
        nccView.diachiField.setText("");
        nccView.timkiemField.setText("");

        nccView.manccField.setEnabled(true);
        nccView.themButton.setEnabled(true);
        nccView.xoaButton.setEnabled(false);
        nccView.suaButton.setEnabled(false);
    }
}