package com.example.quanlysieuthiclient.CONTROLLER;

import com.example.quanlysieuthiclient.APICLIENT.*;
import com.example.quanlysieuthiclient.DTO.*;
import com.example.quanlysieuthiclient.VIEW.TaoDonView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller điều khiển màn hình Tạo / Sửa Đơn Hàng (Bán hàng tại quầy).
 * Được thiết kế rõ ràng, dễ hiểu cho người mới học lập trình Swing và API.
 */
public class TaoDonController {

    private final TaoDonView view;
    private final DonHangApiClient donHangApiClient;
    private final nhanvienApiClient nvApiClient;

    // Biến trạng thái: true nếu đang ở chế độ sửa đơn cũ, false nếu tạo đơn mới
    private boolean isEditMode = false;

    // Callback thông báo khi lưu/cập nhật đơn hàng thành công (dùng để đóng form sửa và reload bảng)
    private Runnable onSaveSuccessCallback;

    // Lưu danh sách sản phẩm và khuyến mãi lấy từ API để tiện tra cứu khi tính toán
    private List<sanpham> listSP = new ArrayList<>();
    private List<khuyenmai> listKM = new ArrayList<>();

    // Biến lưu tổng tiền thực tế (tránh lỗi parse chuỗi giao diện bằng regex)
    private double currentTongTien = 0.0;

    // Bộ lọc hỗ trợ tìm kiếm sản phẩm trên bảng
    private TableRowSorter<DefaultTableModel> productSorter;

    public TaoDonController(TaoDonView view) {
        this.view = view;
        this.donHangApiClient = DonHangApiClient.getInstance();
        this.nvApiClient = nhanvienApiClient.getInstance();

        initData();
        initEvents();
    }

    /**
     * Thiết lập callback khi lưu đơn thành công
     */
    public void setOnSaveSuccessCallback(Runnable onSaveSuccessCallback) {
        this.onSaveSuccessCallback = onSaveSuccessCallback;
    }

    /**
     * Tải dữ liệu ban đầu từ API lên các ComboBox và Bảng sản phẩm
     */
    private void initData() {
        try {
            // 1. Tải danh sách Nhân viên vào ComboBox
            List<nhanvien> listNV = nvApiClient.getAllNhanVien();
            view.getCboNV().removeAllItems();
            for (nhanvien nv : listNV) {
                view.getCboNV().addItem(nv.getManhanvien() + " - " + nv.getTennhanvien());
            }

            // 2. Tải danh sách Khách hàng vào ComboBox
            List<KhachHang> listKH = KhachHangApiClient.getInstance().getAllKhachHang();
            view.getCboKH().removeAllItems();
            for (KhachHang kh : listKH) {
                view.getCboKH().addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }

            // 3. Tải danh sách Sản phẩm vào Bảng bên trái
            this.listSP = sanphamApiClient.getInstance().getAllSanPham();
            view.loadDataTable(this.listSP);

            // Cấu hình bộ lọc tìm kiếm cho bảng sản phẩm
            DefaultTableModel modelSP = (DefaultTableModel) view.getTableSanPham().getModel();
            productSorter = new TableRowSorter<>(modelSP);
            view.getTableSanPham().setRowSorter(productSorter);

            // 4. Tải danh sách Khuyến mãi động từ database vào ComboBox
            this.listKM = khuyenmaiApiClient.getInstance().getAllKhuyenMai();
            view.getCboMaKM().removeAllItems();
            view.getCboMaKM().addItem(""); // Tùy chọn không áp dụng mã
            for (khuyenmai km : this.listKM) {
                view.getCboMaKM().addItem(km.getMakhuyenmai());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu ban đầu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gắn sự kiện lắng nghe (Listener) cho các nút bấm và ô nhập liệu
     */
    private void initEvents() {
        // Nút Thêm sản phẩm vào giỏ hàng
        view.getBtnThem().addActionListener(e -> themSanPhamVaoGio());

        // Nút Xóa sản phẩm khỏi giỏ hàng
        view.getBtnXoa().addActionListener(e -> xoaSanPhamKhoiGio());

        // Nút Lưu đơn hàng
        view.getBtnLuu().addActionListener(e -> luuDonHang());

        // Khi chọn mã khuyến mãi khác -> tự động tính lại tổng tiền
        view.getCboMaKM().addActionListener(e -> tinhToanTongTien());

        // Ô tìm kiếm sản phẩm theo thời gian thực (gõ tới đâu lọc tới đó)
        view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                timKiemSanPham();
            }
        });
    }

    /**
     * Lọc danh sách sản phẩm theo từ khóa nhập vào ô tìm kiếm
     */
    private void timKiemSanPham() {
        if (productSorter == null) return;
        String keyword = view.getTxtTimKiem().getText().trim();
        if (keyword.isEmpty()) {
            productSorter.setRowFilter(null); // Hiển thị toàn bộ nếu ô tìm kiếm rỗng
        } else {
            // Lọc không phân biệt chữ hoa/thường ("(?i)")
            productSorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }

    /**
     * Xử lý thêm sản phẩm vào giỏ hàng:
     * - Kiểm tra dòng được chọn
     * - Kiểm tra số lượng hợp lệ
     * - Kiểm tra tồn kho
     * - Gộp số lượng nếu sản phẩm đã có sẵn trong giỏ
     */
    private void themSanPhamVaoGio() {
        int selectedViewRow = view.getTableSanPham().getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một sản phẩm từ bảng bên trái!");
            return;
        }

        // Chuyển đổi chỉ số dòng hiển thị sang chỉ số thực tế trong Model (khi có lọc tìm kiếm)
        int modelRow = view.getTableSanPham().convertRowIndexToModel(selectedViewRow);
        DefaultTableModel modelSP = (DefaultTableModel) view.getTableSanPham().getModel();

        String maSP = modelSP.getValueAt(modelRow, 0).toString();
        String tenSP = modelSP.getValueAt(modelRow, 1).toString();
        double gia = Double.parseDouble(modelSP.getValueAt(modelRow, 2).toString());
        int tonKho = Integer.parseInt(modelSP.getValueAt(modelRow, 3).toString());

        int soLuongThem = (int) view.getSpinner().getValue();
        if (soLuongThem <= 0) {
            JOptionPane.showMessageDialog(view, "Số lượng thêm vào giỏ phải lớn hơn 0!");
            return;
        }

        DefaultTableModel modelGioHang = (DefaultTableModel) view.getTable().getModel();

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        int dongDaCo = -1;
        int soLuongHienCoTrongGio = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            if (modelGioHang.getValueAt(i, 0).toString().equals(maSP)) {
                dongDaCo = i;
                soLuongHienCoTrongGio = Integer.parseInt(modelGioHang.getValueAt(i, 3).toString());
                break;
            }
        }

        // Kiểm tra tổng số lượng mua so với tồn kho thực tế
        int tongSoLuongMua = soLuongHienCoTrongGio + soLuongThem;
        if (tongSoLuongMua > tonKho) {
            JOptionPane.showMessageDialog(view,
                    "Không đủ hàng trong kho!\n" +
                    "- Tồn kho hiện có: " + tonKho + "\n" +
                    "- Đang có trong giỏ: " + soLuongHienCoTrongGio + "\n" +
                    "- Số lượng muốn thêm: " + soLuongThem);
            return;
        }

        // Nếu đã có trong giỏ: cộng dồn số lượng và cập nhật thành tiền (không tạo dòng mới)
        if (dongDaCo != -1) {
            double thanhTienMoi = tongSoLuongMua * gia;
            modelGioHang.setValueAt(tongSoLuongMua, dongDaCo, 3);
            modelGioHang.setValueAt(thanhTienMoi, dongDaCo, 4);
        } else {
            // Nếu chưa có: thêm một dòng mới vào giỏ hàng
            double thanhTien = soLuongThem * gia;
            modelGioHang.addRow(new Object[]{maSP, tenSP, gia, soLuongThem, thanhTien});
        }

        // Tính lại tổng tiền sau khi thêm
        tinhToanTongTien();
    }

    /**
     * Xóa sản phẩm đang chọn khỏi giỏ hàng
     */
    private void xoaSanPhamKhoiGio() {
        int row = view.getTable().getSelectedRow();
        if (row != -1) {
            ((DefaultTableModel) view.getTable().getModel()).removeRow(row);
            tinhToanTongTien();
        } else {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng sản phẩm cần xóa trong giỏ hàng!");
        }
    }

    /**
     * Tính toán tạm tính, tiền giảm giá từ khuyến mãi động và tổng tiền cuối cùng
     */
    private void tinhToanTongTien() {
        if (view.getTable() == null) return;

        // 1. Tính tổng tạm tính từ các món hàng trong giỏ
        double tamTinh = 0;
        for (int i = 0; i < view.getTable().getRowCount(); i++) {
            tamTinh += Double.parseDouble(view.getTable().getValueAt(i, 4).toString());
        }

        // 2. Tra cứu tiền giảm giá động từ danh sách khuyến mãi lấy từ database
        String selectedKM = (String) view.getCboMaKM().getSelectedItem();
        double tienGiam = 0;

        if (selectedKM != null && !selectedKM.trim().isEmpty()) {
            for (khuyenmai km : this.listKM) {
                if (km.getMakhuyenmai().equals(selectedKM)) {
                    // Đọc số tiền giảm thực tế đã thiết lập trong cơ sở dữ liệu
                    tienGiam = km.getSotiengiam();
                    break;
                }
            }
        }

        // Không cho phép tiền giảm lớn hơn tiền hàng
        if (tienGiam > tamTinh) {
            tienGiam = tamTinh;
        }

        // 3. Tính tổng thanh toán cuối cùng
        this.currentTongTien = tamTinh - tienGiam;

        // 4. Hiển thị thông tin lên giao diện
        view.getLblTamTinh().setText(String.format("Tạm tính: %,.0f đ", tamTinh));
        view.getLblKM().setText(String.format("Giảm giá: %,.0f đ", tienGiam));
        view.getLblTongTien().setText(String.format("Tổng tiền: %,.0f đ", this.currentTongTien));
    }

    /**
     * Nạp dữ liệu một đơn hàng cũ lên form để tiến hành chỉnh sửa
     */
    public void hienThiDonHangDeSua(DonHang dh, List<ChiTietDon> dsChiTiet) {
        isEditMode = true;

        view.getTxtMaDon().setText(dh.getMadonhang());
        view.getTxtMaDon().setEditable(false); // Khóa mã đơn hàng không cho đổi
        view.getNgayGD().setDate(dh.getNgaylap());
        view.getCboBanHang().setSelectedItem(dh.getPhuongthucban());
        view.getCboThanhToan().setSelectedItem(dh.getThanhtoan());

        // 1. Chọn đúng Nhân viên tương ứng trên ComboBox
        if (dh.getManhanvien() != null) {
            for (int i = 0; i < view.getCboNV().getItemCount(); i++) {
                if (view.getCboNV().getItemAt(i).startsWith(dh.getManhanvien())) {
                    view.getCboNV().setSelectedIndex(i);
                    break;
                }
            }
        }

        // 2. Chọn đúng Khách hàng tương ứng trên ComboBox (tránh lỗi bị đổi khách hàng)
        if (dh.getMakhachhang() != null) {
            for (int i = 0; i < view.getCboKH().getItemCount(); i++) {
                if (view.getCboKH().getItemAt(i).startsWith(dh.getMakhachhang())) {
                    view.getCboKH().setSelectedIndex(i);
                    break;
                }
            }
        }

        // 3. Chọn đúng Mã khuyến mãi tương ứng trên ComboBox
        if (dh.getMakhuyenmai() != null && !dh.getMakhuyenmai().trim().isEmpty()) {
            view.getCboMaKM().setSelectedItem(dh.getMakhuyenmai().trim());
        } else {
            view.getCboMaKM().setSelectedIndex(0);
        }

        // 4. Nạp chi tiết các mặt hàng vào bảng giỏ hàng
        DefaultTableModel modelGioHang = (DefaultTableModel) view.getTable().getModel();
        modelGioHang.setRowCount(0);
        if (dsChiTiet != null) {
            for (ChiTietDon ct : dsChiTiet) {
                modelGioHang.addRow(new Object[]{
                        ct.getMasanpham(),
                        ct.getTensanpham(),
                        ct.getDongia(),
                        ct.getSoluong(),
                        ct.getThanhtien()
                });
            }
        }

        tinhToanTongTien();
    }

    /**
     * Lưu đơn hàng mới hoặc Cập nhật đơn hàng cũ lên hệ thống qua API
     */
    private void luuDonHang() {
        try {
            // 1. Kiểm tra mã đơn hàng
            String maDon = view.getTxtMaDon().getText().trim();
            if (maDon.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập mã đơn hàng!");
                view.getTxtMaDon().requestFocus();
                return;
            }

            // 2. Chặn lỗi lưu đơn hàng rỗng (giỏ hàng không có sản phẩm)
            if (view.getTable().getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống! Vui lòng thêm ít nhất 1 sản phẩm trước khi lưu.");
                return;
            }

            // 3. Kiểm tra lựa chọn nhân viên và khách hàng
            if (view.getCboNV().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên phụ trách!");
                return;
            }
            if (view.getCboKH().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng!");
                return;
            }

            // 4. Tạo đối tượng đơn hàng DTO
            DonHang dh = new DonHang();
            dh.setMadonhang(maDon);
            dh.setManhanvien(view.getCboNV().getSelectedItem().toString().split(" - ")[0]);

            String khSelected = view.getCboKH().getSelectedItem().toString();
            dh.setMakhachhang(khSelected.contains(" - ") ? khSelected.split(" - ")[0] : khSelected);

            if (view.getNgayGD().getDate() != null) {
                dh.setNgaylap(view.getNgayGD().getDate());
            } else {
                dh.setNgaylap(new java.util.Date());
            }

            dh.setPhuongthucban(view.getCboBanHang().getSelectedItem().toString());
            dh.setThanhtoan(view.getCboThanhToan().getSelectedItem().toString());

            // Lưu mã khuyến mãi nếu có
            String selectedKM = (String) view.getCboMaKM().getSelectedItem();
            if (selectedKM != null && !selectedKM.trim().isEmpty()) {
                dh.setMakhuyenmai(selectedKM.trim());
            } else {
                dh.setMakhuyenmai(null);
            }

            // Lưu trực tiếp biến tổng tiền thực tế (chính xác 100%, không dùng regex)
            dh.setTongtien(this.currentTongTien);

            // 5. Thu thập danh sách chi tiết đơn hàng từ giỏ
            List<ChiTietDon> dsChiTiet = new ArrayList<>();
            for (int i = 0; i < view.getTable().getRowCount(); i++) {
                ChiTietDon ct = new ChiTietDon(
                        dh.getMadonhang(),
                        view.getTable().getValueAt(i, 0).toString(),
                        view.getTable().getValueAt(i, 1).toString(),
                        Integer.parseInt(view.getTable().getValueAt(i, 3).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 2).toString()),
                        Double.parseDouble(view.getTable().getValueAt(i, 4).toString())
                );
                dsChiTiet.add(ct);
            }

            // 6. Gửi request lên REST API
            if (isEditMode) {
                donHangApiClient.updateDonHang(dh.getMadonhang(), dh, dsChiTiet);
                JOptionPane.showMessageDialog(view, "Cập nhật đơn hàng thành công!");

                // Gọi callback để đóng form sửa và làm mới bảng danh sách bên ngoài
                if (onSaveSuccessCallback != null) {
                    onSaveSuccessCallback.run();
                }
            } else {
                donHangApiClient.saveDonHang(dh, dsChiTiet);
                JOptionPane.showMessageDialog(view, "Lưu đơn hàng mới thành công!");
                resetForm();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu đơn hàng: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Xóa trắng các trường nhập liệu trên form và làm rỗng giỏ hàng
     */
    private void resetForm() {
        view.getTxtMaDon().setText("");
        ((DefaultTableModel) view.getTable().getModel()).setRowCount(0);
        view.getCboMaKM().setSelectedIndex(0);
        isEditMode = false;
        view.getTxtMaDon().setEditable(true);
        tinhToanTongTien();
    }
}
