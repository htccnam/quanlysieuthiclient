package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.loaihangApiClient;
import com.example.quanlysieuthiclient.APICLIENT.nhacungcapApiClient;
import com.example.quanlysieuthiclient.APICLIENT.sanphamApiClient;
import com.example.quanlysieuthiclient.DTO.loaihang;
import com.example.quanlysieuthiclient.DTO.nhacungcap;
import com.example.quanlysieuthiclient.DTO.sanpham;
import com.example.quanlysieuthiclient.VIEW.sanphamView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
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

        loadComboBoxData();
        loadTable();

        spView.xoaButton.setEnabled(false);
        spView.suaButton.setEnabled(false);
    }

    private void loadComboBoxData() {
        try {
            List<loaihang> dsLoai = loaihangApiClient.getInstance().getAllLoaiHang();
            spView.maloaiComboBox.removeAllItems();
            for (loaihang lh : dsLoai) {
                spView.maloaiComboBox.addItem(lh.getMaloai());
            }

            List<nhacungcap> dsNcc = nhacungcapApiClient.getInstance().getAllNhaCungCap();
            spView.manccComboBox.removeAllItems();
            for (nhacungcap ncc : dsNcc) {
                spView.manccComboBox.addItem(ncc.getManhacungcap());
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải ComboBox: " + e.getMessage());
        }
    }

    private void loadTable() {
        try {
            spView.spDefaultTableModel.setRowCount(0);
            List<sanpham> list = spApiClient.getAllSanPham();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (sanpham sp : list) {
                String strNgaySx = (sp.getNgaysanxuat() != null) ? sdf.format(sp.getNgaysanxuat()) : "";
                String strHanSd = (sp.getHansudung() != null) ? sdf.format(sp.getHansudung()) : "";

                spView.spDefaultTableModel.addRow(new Object[]{
                        sp.getMasanpham(), sp.getTensanpham(), sp.getMaloai(), sp.getManhacungcap(),
                        sp.getSoluong(), strNgaySx, strHanSd,
                        sp.getTinhtrang(), sp.getGianhap(), sp.getGiaban(), sp.getDonvitinh()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(spView, "Lỗi tải bảng sản phẩm: " + e.getMessage());
        }
    }

    private sanpham getModelFromView() throws Exception {
        String masp = spView.maspField.getText().trim();
        String tensp = spView.tenspField.getText().trim();
        if (masp.isEmpty() || tensp.isEmpty()) {
            throw new Exception("Mã và Tên SP không được để trống!");
        }

        Integer soluong = spView.soluongField.getText().isEmpty() ? 0 : Integer.parseInt(spView.soluongField.getText().trim());
        Double gianhap = spView.gianhapField.getText().isEmpty() ? 0.0 : Double.parseDouble(spView.gianhapField.getText().trim());
        Double giaban = spView.giabanField.getText().isEmpty() ? 0.0 : Double.parseDouble(spView.giabanField.getText().trim());

        // Lấy ngày chuẩn từ Lịch (JDateChooser)
        java.util.Date nx = spView.ngaysxChooser.getDate();
        java.util.Date hs = spView.hansdChooser.getDate();

        Date ngaysx = (nx != null) ? new Date(nx.getTime()) : null;
        Date hansd = (hs != null) ? new Date(hs.getTime()) : null;

        String maloai = spView.maloaiComboBox.getSelectedItem() != null ? spView.maloaiComboBox.getSelectedItem().toString() : "";
        String mancc = spView.manccComboBox.getSelectedItem() != null ? spView.manccComboBox.getSelectedItem().toString() : "";
        String tinhtrang = spView.tinhtrangComboBox.getSelectedItem() != null ? spView.tinhtrangComboBox.getSelectedItem().toString() : "";
        String dvt = spView.dvtComboBox.getSelectedItem() != null ? spView.dvtComboBox.getSelectedItem().toString() : "";

        return new sanpham(
                masp, tensp, maloai, mancc, spView.xuatxuField.getText().trim(),
                soluong, ngaysx, hansd, tinhtrang, gianhap, giaban, dvt
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                for (sanpham sp : list) {
                    String strNgaySx = (sp.getNgaysanxuat() != null) ? sdf.format(sp.getNgaysanxuat()) : "";
                    String strHanSd = (sp.getHansudung() != null) ? sdf.format(sp.getHansudung()) : "";

                    spView.spDefaultTableModel.addRow(new Object[]{
                            sp.getMasanpham(), sp.getTensanpham(), sp.getMaloai(), sp.getManhacungcap(),
                            sp.getSoluong(), strNgaySx, strHanSd,
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
            spView.maspField.setEnabled(false);
            selectedrow = spView.spJTable.getSelectedRow();

            try {
                List<sanpham> list = spApiClient.getAllSanPham();
                sanpham sp = list.get(selectedrow);

                spView.maspField.setText(sp.getMasanpham());
                spView.tenspField.setText(sp.getTensanpham() != null ? sp.getTensanpham() : "");
                spView.xuatxuField.setText(sp.getXuatxu() != null ? sp.getXuatxu() : "");
                spView.soluongField.setText(sp.getSoluong() != null ? String.valueOf(sp.getSoluong()) : "0");
                spView.gianhapField.setText(sp.getGianhap() != null ? String.valueOf(sp.getGianhap()) : "0.0");
                spView.giabanField.setText(sp.getGiaban() != null ? String.valueOf(sp.getGiaban()) : "0.0");

                // Đổ dữ liệu vào Lịch JDateChooser
                spView.ngaysxChooser.setDate(sp.getNgaysanxuat());
                spView.hansdChooser.setDate(sp.getHansudung());

                // Set cho ComboBox
                if (sp.getMaloai() != null) spView.maloaiComboBox.setSelectedItem(sp.getMaloai());
                if (sp.getManhacungcap() != null) spView.manccComboBox.setSelectedItem(sp.getManhacungcap());
                if (sp.getTinhtrang() != null) spView.tinhtrangComboBox.setSelectedItem(sp.getTinhtrang());
                if (sp.getDonvitinh() != null) spView.dvtComboBox.setSelectedItem(sp.getDonvitinh());

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
        spView.xuatxuField.setText("");
        spView.soluongField.setText("");
        spView.gianhapField.setText("");
        spView.giabanField.setText("");
        spView.timkiemField.setText("");

        // Reset ngày tháng
        spView.ngaysxChooser.setDate(null);
        spView.hansdChooser.setDate(null);

        if(spView.maloaiComboBox.getItemCount() > 0) spView.maloaiComboBox.setSelectedIndex(0);
        if(spView.manccComboBox.getItemCount() > 0) spView.manccComboBox.setSelectedIndex(0);
        spView.tinhtrangComboBox.setSelectedIndex(0);
        spView.dvtComboBox.setSelectedIndex(0);

        spView.maspField.setEnabled(true);
        spView.themButton.setEnabled(true);
        spView.xoaButton.setEnabled(false);
        spView.suaButton.setEnabled(false);
    }
}