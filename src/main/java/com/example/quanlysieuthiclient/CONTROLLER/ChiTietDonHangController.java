package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.DonHangApiClient;
import com.example.quanlysieuthiclient.DTO.ChiTietDon;
import com.example.quanlysieuthiclient.DTO.DonHang;
import com.example.quanlysieuthiclient.VIEW.ChiTietView;
import com.example.quanlysieuthiclient.VIEW.TaoDonView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
            double tongDoanhThu = 0;

            for (DonHang dh : list) {
                view.getModel().addRow(new Object[]{
                        dh.getMadonhang(), dh.getNgaylap(), dh.getManhanvien(),
                        dh.getPhuongthucban(), dh.getThanhtoan(), dh.getTongtien()
                });
                tongDoanhThu += dh.getTongtien();
            }

            // Cập nhật các thẻ thống kê trên View
            view.getLblTongDon().setText(String.valueOf(list.size()));
            view.getLblTongDoanhThu().setText(String.format("%,.0f đ", tongDoanhThu));
            if (!list.isEmpty()) {
                view.getLblDoanhThuTB().setText(String.format("%,.0f đ/đơn", tongDoanhThu / list.size()));
            }else  {
                view.getLblDoanhThuTB().setText(String.format("0 đ/đơn"));
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
                int confirm = JOptionPane.showConfirmDialog(view, "Xóa đơn " + maDH + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        apiClient.xoaDonHang(maDH);
                        loadTable(); // Load lại dữ liệu
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Lỗi xóa: " + ex.getMessage());
                    }
                }
            }
        });

        view.getBtnXemChiTiet().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một đơn hàng để xem!");
                return;
            }

            // Lấy mã đơn hàng từ cột đầu tiên của dòng đang chọn
            String maDH = view.getTable().getValueAt(row, 0).toString();

            // Gọi hàm hiển thị Dialog
            hienThiChiTiet(maDH);
        });

        view.getBtnSua().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một đơn hàng để sửa!");
                return;
            }

            String maDH = view.getTable().getValueAt(row, 0).toString();

            try {
                // 1. Lấy thông tin đơn hàng đầy đủ từ danh sách đã load
                List<DonHang> dsDonHang = apiClient.getAllDonHang();
                DonHang dhSelected = dsDonHang.stream()
                        .filter(d -> d.getMadonhang().equals(maDH))
                        .findFirst().orElse(null);

                // 2. Lấy chi tiết đơn hàng từ API
                List<ChiTietDon> dsChiTiet = apiClient.getChiTietByMa(maDH);

                if (dhSelected != null) {
                    // 1. Tạo một cái khung cửa sổ mới
                    JFrame frame = new JFrame("Sửa đơn hàng");

                    // 2. Tạo cái panel giao diện của bạn
                    TaoDonView taoDonPanel = new TaoDonView();
                    TaoDonController taoDonController = new TaoDonController(taoDonPanel);

                    // 3. Đổ dữ liệu vào panel
                    taoDonController.hienThiDonHangDeSua(dhSelected, dsChiTiet);

                    // 4. Gắn cái panel vào khung cửa sổ
                    frame.add(taoDonPanel);
                    frame.pack();

                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu sửa: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void hienThiChiTiet(String maDH) {
        try {
            // Gọi API lấy danh sách chi tiết
            List<ChiTietDon> dsChiTiet = apiClient.getChiTietByMa(maDH);

            if (dsChiTiet == null || dsChiTiet.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Đơn hàng này không có chi tiết sản phẩm!");
                return;
            }

            // Tạo bảng để hiển thị trong Dialog
            String[] columns = {"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (ChiTietDon ct : dsChiTiet) {
                model.addRow(new Object[]{
                        ct.getMasanpham(),
                        ct.getTensanpham(),
                        ct.getSoluong(),
                        String.format("%,.0f", ct.getDongia()),
                        String.format("%,.0f", ct.getThanhtien())
                });
            }

            JTable tableChiTiet = new JTable(model);
            tableChiTiet.setRowHeight(30);
            JScrollPane scrollPane = new JScrollPane(tableChiTiet);
            scrollPane.setPreferredSize(new Dimension(600, 300));

            // Hiển thị Dialog
            JOptionPane.showMessageDialog(view, scrollPane, "Chi tiết đơn hàng: " + maDH, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lấy chi tiết: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}