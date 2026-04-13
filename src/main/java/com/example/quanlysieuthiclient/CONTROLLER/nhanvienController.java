/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.nhanvienApiClient;
import com.example.quanlysieuthiclient.DTO.nhanvien;
import com.example.quanlysieuthiclient.VIEW.nhanvienViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class nhanvienController {

    private final nhanvienViews views;
    private final nhanvienApiClient nvDAO;
//    private chucvuDAO cvDAO;
    private int selectedRow = -1;

    public nhanvienController(nhanvienViews v) {
        this.views = v;
        nvDAO = nhanvienApiClient.getInstance();
//        cvDAO = new chucvuDAO();
        views.addThemListener(new themNhanVienlistener());
        views.addSuaListener(new capNhapNhanVienListener());
        views.addXoaListener(new xoaNhanVienListetenr());
        views.addTimKiemListener(new timKiemListener());
        views.addResetListener(new reSetListener());

        views.addCellClicktable(new cellClickListener());
        load_table();
        load_chucvu();
        views.setVisible(true);
    }

    private void load_chucvu() {
//        try {
//            List<chucvu> list = cvDAO.getAllChucVu();
//            for (chucvu cv : list) {
//                views.chucvuBox.addItem(cv.getMachucvuString() + "-" + cv.getTenchucvuString());
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(views, "lỗi load chức vụ:" + e.getMessage());
//        }

    }

    private void load_table() {
        views.nhanvienDefaultTableModel.setRowCount(0);
        try {
            List<nhanvien> list = nvDAO.getAllNhanVien();
            for (nhanvien nv : list) {
                views.nhanvienDefaultTableModel.addRow(new Object[]{
                    nv.getManhanvien(),
                    nv.getTennhanvien(),
                    nv.getNgaysinh(),
                    nv.getGioitinh(),
                    nv.getSodienthoai(),
                    nv.getEmail(),
                    nv.getDiachi(),
                    nv.getMachucvu()
                });
            }
            views.nhanvienDefaultTableModel.fireTableDataChanged();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(views, "lỗi load bảng nhân viên:" + e.getMessage());
        }
    }

    private class themNhanVienlistener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String textMaNhanVienString = views.manhanvienField.getText().toString().trim();
            String textTenNhanVienString = views.tennhanvienField.getText().toString().trim();
            String ngaySinhDate = views.ngaysinhChooser.getDate().toString();
            String gioiTinhString = views.gioitinhComboBox.getSelectedItem().toString().trim();;
            String soDienThoaiString = views.sodienthoaiField.getText().toString().trim();
            String textemailString = views.emailField.getText().toString().trim();
            String textdiachiString = views.diachiField.getText().toString().trim();
            String machucvuString = views.chucvuBox.getSelectedItem().toString().trim();
            String tachchuoiString = machucvuString.split("-")[0].trim();

            if (!textemailString.matches(".+@gmail\\.com")) {
                JOptionPane.showMessageDialog(views, "email phải có đuôi @gmail.com");
                return;
            }
            if (!soDienThoaiString.startsWith("0") || soDienThoaiString.length() != 10) {
                JOptionPane.showMessageDialog(views, "số điện thoại phải bắt đầu bằng số 0 và có 10 số");
                return;
            }
            if (textMaNhanVienString.isEmpty()) {
                JOptionPane.showMessageDialog(views, "mã nhân viên không được để trống");
                return;
            }
            if (textTenNhanVienString.isEmpty()) {
                JOptionPane.showMessageDialog(views, "Tên nhân viên không được để trống");
                return;
            }
//            if (nvDAO.checkTrungMaNhanVien(textMaNhanVienString)) {
//                JOptionPane.showMessageDialog(views, "Mã nhân viên đã tồn tại");
//                return;
//            }

            nhanvien nv = new nhanvien(textMaNhanVienString, textTenNhanVienString, ngaySinhDate, gioiTinhString, soDienThoaiString, textemailString, textdiachiString, tachchuoiString);

            try {
                if (nvDAO.themNhanVien(nv)) {
                    JOptionPane.showMessageDialog(views, "thêm thành công");
                    load_table();
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(views, "lỗi thêm nhân viên:" + exception.getMessage());
            }

        }

    }

    private class capNhapNhanVienListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String textTenNhanVienString = views.tennhanvienField.getText().toString().trim();
            String ngaysinhDate = views.ngaysinhChooser.getDate().toString();
            String textGioiTinhString = views.gioitinhComboBox.getSelectedItem().toString().trim();
            String textSoDienThoaiString = views.sodienthoaiField.getText().toString().trim();
            String textEmailString = views.emailField.getText().toString().trim();
            String textDiaChiString = views.diachiField.getText().toString().trim();
            String textMaChucVuString = views.chucvuBox.getSelectedItem().toString().trim();
            String tachchuoiString = textMaChucVuString.split("-")[0].trim();

            if (textTenNhanVienString.isEmpty()) {
                JOptionPane.showMessageDialog(views, "Tên nhân viên không được để trống");
            }
            if (!textEmailString.matches(".+@gmail\\.com")) {
                JOptionPane.showMessageDialog(views, "email phải có đuôi @gmail.com");
                return;
            }
            if (!textSoDienThoaiString.startsWith("0") || textSoDienThoaiString.length() != 10) {
                JOptionPane.showMessageDialog(views, "số điện thoại phải bắt đầu bằng số 0 và có 10 số");
                return;
            }
            String textMaNhanVienString = views.manhanvienField.getText();

            nhanvien nv = new nhanvien(textMaNhanVienString, textTenNhanVienString, ngaysinhDate, textGioiTinhString, textSoDienThoaiString, textEmailString, textDiaChiString, tachchuoiString);
//            try {
//                if (nvDAO.suaNhanVien(nv)) {
//                    JOptionPane.showMessageDialog(views, "sửa thành công");
//                    load_table();
//                }
//            } catch (Exception exception) {
//                JOptionPane.showMessageDialog(views, "lỗi sửa nhân viên:" + exception.getMessage());
//            }

        }
    }

    private class xoaNhanVienListetenr implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(views, "bạn có chắc chắn ");
            if (result == JOptionPane.YES_OPTION) {
                String maNhanVienString = views.manhanvienField.getText();

//                try {
//                    if (nvDAO.checkXoaNhanVien(maNhanVienString)) {
//                        JOptionPane.showMessageDialog(views, "Mã nhân viên đã được tạo đơn không thể xóa");
//                        return;
//                    }
//                    if (nvDAO.xoaNhanVien(maNhanVienString)) {
//                        JOptionPane.showMessageDialog(views, "xóa thành công");
//                        load_table();
//                    }
//                } catch (Exception exception) {
//                    JOptionPane.showMessageDialog(views, "lỗi xóa nhân viên:" + exception.getMessage());
//                }
            }
        }

    }

    private class reSetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            views.manhanvienField.setEnabled(true);

            views.manhanvienField.setText("");
            views.tennhanvienField.setText("");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -18);
            views.ngaysinhChooser.setDate(calendar.getTime());
            views.gioitinhComboBox.setSelectedIndex(0);
            views.sodienthoaiField.setText("");
            views.emailField.setText("");
            views.diachiField.setText("");
            views.chucvuBox.setSelectedIndex(0);
            views.timkiemField.setText("");

            load_table();
        }

    }

    private class timKiemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String timkiemString = views.timkiemField.getText().toString().trim();
            views.nhanvienDefaultTableModel.setRowCount(0);
//            try {
//                List<nhanvien> list = nvDAO.timKiemNhanVien(timkiemString);
//                for (nhanvien nv : list) {
//                    views.nhanvienDefaultTableModel.addRow(new Object[]{
//                        nv.getManhanvienString(),
//                        nv.getTennhanvienString(),
//                        nv.getNgaysinhDate(),
//                        nv.getGioitinhString(),
//                        nv.getSodienthoaiString(),
//                        nv.getEmailString(),
//                        nv.getDiachiString(),
//                        nv.getMachucvuString()
//                    });
//                }
//            } catch (Exception exception) {
//                JOptionPane.showMessageDialog(views, "lỗi tìm kiếm:" + exception.getMessage());
//            }
        }
    }

    private class cellClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            selectedRow = views.nhanvienJTable.getSelectedRow();
            views.manhanvienField.setText(views.nhanvienDefaultTableModel.getValueAt(selectedRow, 0).toString());
            views.tennhanvienField.setText(views.nhanvienDefaultTableModel.getValueAt(selectedRow, 1).toString());
            String ngaySinh=views.nhanvienDefaultTableModel.getValueAt(selectedRow,2).toString();
            String ngaySinhPart=ngaySinh.split("T")[0];
//            Lấy "2005-02-09"
            views.ngaysinhChooser.setDate(Date.valueOf(ngaySinhPart));
            views.gioitinhComboBox.setSelectedItem(views.nhanvienDefaultTableModel.getValueAt(selectedRow, 3));
            views.sodienthoaiField.setText(views.nhanvienDefaultTableModel.getValueAt(selectedRow, 4).toString());
            views.emailField.setText(views.nhanvienJTable.getValueAt(selectedRow, 5).toString());
            views.diachiField.setText(views.nhanvienDefaultTableModel.getValueAt(selectedRow, 6).toString());
            String machucvuString = views.nhanvienJTable.getValueAt(selectedRow, 7).toString();

            for (int i = 0; i < views.chucvuBox.getItemCount(); i++) {
                String item = views.chucvuBox.getItemAt(i);
                if (item.startsWith(machucvuString + "-")) {
                    views.chucvuBox.setSelectedIndex(i);
                    break;
                }
            }
            views.manhanvienField.setEnabled(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }
}
