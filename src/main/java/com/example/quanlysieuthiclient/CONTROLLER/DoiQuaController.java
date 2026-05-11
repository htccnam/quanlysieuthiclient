package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.DoiQuaApiClient;
import com.example.quanlysieuthiclient.APICLIENT.KhachHangApiClient;
import com.example.quanlysieuthiclient.DTO.KhachHang;
import com.example.quanlysieuthiclient.DTO.LichSuDoiQua;
import com.example.quanlysieuthiclient.DTO.QuaTang;
import com.example.quanlysieuthiclient.VIEW.DialogLichSuDoiQua;
import com.example.quanlysieuthiclient.VIEW.DoiQuaView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DoiQuaController {
    private DoiQuaView view;
    private DoiQuaApiClient doiQuaApi;
    private KhachHangApiClient khachHangApi;

    private List<KhachHang> danhSachKH;
    private String maQuaDangChon = null; // Lưu vết món quà đang được click

    public DoiQuaController(DoiQuaView view) {
        this.view = view;
        this.doiQuaApi = DoiQuaApiClient.getInstance();
        this.khachHangApi = KhachHangApiClient.getInstance();

        loadKhachHang();
        loadDanhSachQua();

        // Đăng ký sự kiện
        this.view.cboKhachHang.addActionListener(e -> capNhatDiemKhaDung());
        this.view.btnXacNhan.addActionListener(e -> xuLyDoiQua());
        this.view.btnLichSu.addActionListener(e -> xemLichSu());
    }

    // 1. Tải danh sách khách hàng vào ComboBox
    private void loadKhachHang() {
        try {
            danhSachKH = khachHangApi.getAllKhachHang();
            view.cboKhachHang.removeAllItems();
            view.cboKhachHang.addItem("-- Chọn khách hàng --");
            for (KhachHang kh : danhSachKH) {
                view.cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải khách hàng: " + e.getMessage());
        }
    }

    // 2. Cập nhật điểm khi chọn ComboBox
    private void capNhatDiemKhaDung() {
        if (view.cboKhachHang.getSelectedIndex() <= 0) {
            view.lblDiemKhaDung.setText("0 Điểm");
            return;
        }
        String selected = view.cboKhachHang.getSelectedItem().toString();
        String maKH = selected.split(" - ")[0];

        // Tìm khách hàng trong List để lấy điểm
        for (KhachHang kh : danhSachKH) {
            if (kh.getMaKH().equals(maKH)) {
                view.lblDiemKhaDung.setText(kh.getDiemtichluy() + " Điểm");
                break;
            }
        }
    }

    // 3. Tải kho quà từ API và vẽ động các thẻ (Cards) lên giao diện
    private void loadDanhSachQua() {
        try {
            List<QuaTang> listQua = doiQuaApi.getKhoQua();
            view.pnlDanhSachQua.removeAll();

            for (QuaTang qua : listQua) {
                JPanel card = taoTheQuaTang(qua);
                view.pnlDanhSachQua.add(card);
            }
            view.pnlDanhSachQua.revalidate();
            view.pnlDanhSachQua.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải kho quà: " + e.getMessage());
        }
    }

    // Hàm hỗ trợ vẽ 1 thẻ quà tặng
    private JPanel taoTheQuaTang(QuaTang qua) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(220, 150));
        card.setBackground(Color.WHITE);

        // Viền mặc định
        Border defaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        // Viền khi được chọn (Màu Vàng Gold)
        Border selectedBorder = BorderFactory.createLineBorder(new Color(204, 153, 0), 3);
        card.setBorder(defaultBorder);

        JLabel lblTen = new JLabel(qua.getTenQua(), JLabel.CENTER);
        lblTen.setFont(new Font("Arial", Font.BOLD, 15));
        lblTen.setForeground(new Color(0, 51, 102));
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDiem = new JLabel(qua.getDiemYeuCau() + " điểm", JLabel.CENTER);
        lblDiem.setFont(new Font("Arial", Font.BOLD, 16));
        lblDiem.setForeground(new Color(204, 153, 0));
        lblDiem.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(20));
        card.add(lblTen);
        card.add(Box.createVerticalStrut(15));
        card.add(lblDiem);

        // Hiệu ứng Click chọn thẻ
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                maQuaDangChon = qua.getMaQua(); // Lưu lại mã quà
                // Reset viền tất cả các thẻ khác
                for (Component comp : view.pnlDanhSachQua.getComponents()) {
                    ((JPanel) comp).setBorder(defaultBorder);
                }
                // Đổi màu viền thẻ đang chọn
                card.setBorder(selectedBorder);
            }
        });

        return card;
    }

    // 4. Xử lý logic Đổi Quà gọi API
    private void xuLyDoiQua() {
        if (view.cboKhachHang.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng!");
            return;
        }
        if (maQuaDangChon == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng click chọn một món quà!");
            return;
        }

        String maKH = view.cboKhachHang.getSelectedItem().toString().split(" - ")[0];

        try {
            // Gọi API thực hiện đổi quà
            String thongBao = doiQuaApi.doiQua(maKH, maQuaDangChon);
            JOptionPane.showMessageDialog(view, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật lại số điểm trên màn hình ngay lập tức
            loadKhachHang(); // Load lại list KH từ API để lấy điểm mới
            for (int i = 0; i < view.cboKhachHang.getItemCount(); i++) {
                if (view.cboKhachHang.getItemAt(i).startsWith(maKH)) {
                    view.cboKhachHang.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception ex) {
            // Hiển thị lỗi nếu thiếu điểm
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 5. Xem lịch sử
    private void xemLichSu() {
        if (view.cboKhachHang.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng để xem lịch sử!");
            return;
        }

        String selected = view.cboKhachHang.getSelectedItem().toString();
        String maKH = selected.split(" - ")[0];
        String tenKH = selected.split(" - ")[1];

        try {
            List<LichSuDoiQua> listLichSu = doiQuaApi.getLichSu(maKH);
            if (listLichSu.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Khách hàng này chưa từng đổi quà.");
                return;
            }

            DialogLichSuDoiQua dialog = new DialogLichSuDoiQua(
                    (JFrame) SwingUtilities.getWindowAncestor(view), listLichSu, tenKH);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải lịch sử: " + e.getMessage());
        }
    }
}