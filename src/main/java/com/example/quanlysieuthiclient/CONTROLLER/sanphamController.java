package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.sanphamApiClient;
import com.example.quanlysieuthiclient.DTO.sanpham;
import com.example.quanlysieuthiclient.VIEW.sanphamView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class sanphamController {
    private sanphamView spView;
    private sanphamApiClient spApiClient;
    private int selectedrow = -1;

    public sanphamController(sanphamView spView) {
        this.spView = spView;
        this.spApiClient = sanphamApiClient.getInstance();

        spView.addThemClickListener(new themSanPhamListener());
        spView.addSuaClickListener(new suaSanPhamListener());
        spView.addTimKiemClickListener(new timkiemSanPhamListener());
        spView.addClickTableListener(new clicktableListener());
        spView.addXoaClickListener(new xoaSanPhamListener());
        spView.addResetClickListener(new resetListener());

        loadTable();
        spView.xoaButton.setEnabled(false);
        spView.suaButton.setEnabled(false);
    }

    private void loadTable() {
        try {
            spView.spDefaultTableModel.setRowCount(0);
            List<sanpham> list = spApiClient.getAllSanPham();
            for (sanpham sp : list) {
                // Không hiển thị "xuất xứ" lên bảng cho đỡ chật, nhưng vẫn quản lý
                spView.spDefaultTableModel.addRow(new Object[]{
                        sp.getMasanpham(), sp.getTensanpham(), sp.getMaloai(), sp.getManhacungcap(),
                        sp.getSoluong(), sp.getNgaysanxuat(), sp.getHansudung(),
                        sp.getTinhtrang(), sp.getGianhap(), sp.getGiaban(), sp.getDonvitinh()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(spView, "Lỗi tải bảng sản phẩm: " + e.getMessage());
        }
    }

    // LẤY VÀ ÉP KIỂU DỮ LIỆU TỪ VIEW (Cẩn thận các trường số và ngày tháng)
    private sanpham getModelFromView() throws Exception {
        String masp = spView.maspField.getText().trim();
        String tensp = spView.tenspField.getText().trim();
        if (masp.isEmpty() || tensp.isEmpty()) {
            throw new Exception("Mã và Tên SP không được để trống!");
        }

        Integer soluong = spView.soluongField.getText().isEmpty() ? 0 : Integer.parseInt(spView.soluongField.getText().trim());
        Double gianhap = spView.gianhapField.getText().isEmpty() ? 0.0 : Double.parseDouble(spView.gianhapField.getText().trim());
        Double giaban = spView.giabanField.getText().isEmpty() ? 0.0 : Double.parseDouble(spView.giabanField.getText().trim());

        Date ngaysx = null, hansd = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Bắt buộc nhập ngày hợp lệ (không cho phép nhập 32-01-2024)
        try {
            if (!spView.ngaysxField.getText().trim().isEmpty()) {
                java.util.Date parsedDate = sdf.parse(spView.ngaysxField.getText().trim());
                ngaysx = new Date(parsedDate.getTime());
            }
            if (!spView.hansdField.getText().trim().isEmpty()) {
                java.util.Date parsedDate = sdf.parse(spView.hansdField.getText().trim());
                hansd = new Date(parsedDate.getTime());
            }
        } catch (ParseException ex) {
            throw new Exception("Ngày tháng phải nhập đúng định dạng dd-MM-yyyy (Ví dụ: 15-10-2024)");
        }

        return new sanpham(
                masp, tensp, spView.maloaiField.getText().trim(), spView.manccField.getText().trim(),
                spView.xuatxuField.getText().trim(), soluong, ngaysx, hansd,
                spView.tinhtrangField.getText().trim(), gianhap, giaban, spView.donvitinhField.getText().trim()
        );
    }

    private class themSanPhamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                sanpham sp = getModelFromView();
                spApiClient.themSanPham(sp);
                loadTable();
                resetForm();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(spView, "Lỗi thêm SP: " + exception.getMessage());
            }
        }
    }

    private class suaSanPhamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(spView, "Bạn có chắc chắn muốn sửa?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    sanpham sp = getModelFromView();
                    spApiClient.suaSanPham(sp);
                    loadTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(spView, "Lỗi sửa SP: " + ex.getMessage());
                }
            }
        }
    }

    private class xoaSanPhamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String masp = spView.maspField.getText();
            int result = JOptionPane.showConfirmDialog(spView, "Bạn có chắc chắn muốn xóa?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    spApiClient.xoaSanPham(masp);
                    loadTable();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(spView, "Lỗi xóa SP: " + ex.getMessage());
                }
            }
        }
    }

    private class timkiemSanPhamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            spView.spDefaultTableModel.setRowCount(0);
            try {
                String keyword = spView.timkiemField.getText().trim();
                List<sanpham> list = spApiClient.timKiemSanPham(keyword);
                for (sanpham sp : list) {
                    spView.spDefaultTableModel.addRow(new Object[]{
                            sp.getMasanpham(), sp.getTensanpham(), sp.getMaloai(), sp.getManhacungcap(),
                            sp.getSoluong(), sp.getNgaysanxuat(), sp.getHansudung(),
                            sp.getTinhtrang(), sp.getGianhap(), sp.getGiaban(), sp.getDonvitinh()
                    });
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(spView, "Lỗi tìm kiếm: " + exception.getMessage());
            }
        }
    }

    private class clicktableListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            spView.maspField.setEnabled(false); // Khóa sửa mã
            selectedrow = spView.spJTable.getSelectedRow();

            // Lấy lại danh sách đầy đủ để lấy được biến 'xuatxu' không bị hiển thị trên bảng
            try {
                List<sanpham> list = spApiClient.getAllSanPham();
                sanpham sp = list.get(selectedrow); // Dòng được click sẽ tương ứng với index trong list
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                spView.maspField.setText(sp.getMasanpham());
                spView.tenspField.setText(sp.getTensanpham() != null ? sp.getTensanpham() : "");
                spView.maloaiField.setText(sp.getMaloai() != null ? sp.getMaloai() : "");
                spView.manccField.setText(sp.getManhacungcap() != null ? sp.getManhacungcap() : "");
                spView.xuatxuField.setText(sp.getXuatxu() != null ? sp.getXuatxu() : "");
                spView.soluongField.setText(sp.getSoluong() != null ? String.valueOf(sp.getSoluong()) : "0");
                spView.ngaysxField.setText(sp.getNgaysanxuat() != null ? sdf.format(sp.getNgaysanxuat()) : "");
                spView.hansdField.setText(sp.getHansudung() != null ? sdf.format(sp.getHansudung()) : "");
                spView.tinhtrangField.setText(sp.getTinhtrang() != null ? sp.getTinhtrang() : "");
                spView.gianhapField.setText(sp.getGianhap() != null ? String.valueOf(sp.getGianhap()) : "0.0");
                spView.giabanField.setText(sp.getGiaban() != null ? String.valueOf(sp.getGiaban()) : "0.0");
                spView.donvitinhField.setText(sp.getDonvitinh() != null ? sp.getDonvitinh() : "");

                spView.suaButton.setEnabled(true);
                spView.xoaButton.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(spView, "Lỗi tải chi tiết: " + ex.getMessage());
            }
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

    private void resetForm() {
        spView.maspField.setText("");
        spView.tenspField.setText("");
        spView.maloaiField.setText("");
        spView.manccField.setText("");
        spView.xuatxuField.setText("");
        spView.soluongField.setText("");
        spView.ngaysxField.setText("");
        spView.hansdField.setText("");
        spView.tinhtrangField.setText("");
        spView.gianhapField.setText("");
        spView.giabanField.setText("");
        spView.donvitinhField.setText("");
        spView.timkiemField.setText("");

        spView.maspField.setEnabled(true);
        spView.themButton.setEnabled(true);
        spView.xoaButton.setEnabled(false);
        spView.suaButton.setEnabled(false);
    }
}