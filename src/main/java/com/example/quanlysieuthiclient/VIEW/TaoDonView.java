package com.example.quanlysieuthiclient.VIEW;

//import DTO.SanPham;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TaoDonView extends JPanel {

    private JDateChooser ngayGD;
    private JTextField txtMaDon, txtTimKiem, txtHang;
    private JComboBox<String> cboBanHang, cboThanhToan, cboNV, cboMaKM, cboKH;
    private JTable table, tableSanPham;
    private JLabel lblTongTien, lblTamTinh, lblKM;
    private JSpinner spinner;
    private JButton btnLuu, btnThem, btnXoa;

    public TaoDonView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 249));

        add(panelThongTinChung());
        add(Box.createVerticalStrut(15));
        add(panelChiTiet());
        add(Box.createVerticalStrut(15));
        add(panelTongTien());
    }

    //THÔNG TIN CHUNG
    private JPanel panelThongTinChung() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Thông tin chung (Đơn hàng)"));
        p.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtMaDon = new JTextField();
        txtHang = new JTextField();
        txtHang.setEditable(false);
        ngayGD = new JDateChooser();
        cboNV = new JComboBox<>(new String[]{});
        cboKH = new JComboBox<>(new String[]{});
        cboMaKM = new JComboBox<>(new String[]{});
        cboBanHang = new JComboBox<>(new String[]{"Online", "Offline"});
        cboThanhToan = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản"});

        //Hàng 0
        g.gridx = 0; g.gridy = 0;
        p.add(new JLabel("Mã đơn hàng"), g);

        g.gridx = 1;
        p.add(txtMaDon, g);

        g.gridx = 2;
        p.add(new JLabel("Ngày giao dịch"), g);

        g.gridx = 3;
        p.add(ngayGD, g);

        g.gridx = 4;
        p.add(new JLabel("Nhân viên"), g);

        g.gridx = 5;
        p.add(cboNV, g);

        g.gridx = 6;
        p.add(new JLabel("Khách hàng"), g);

        g.gridx = 7;
        p.add(cboKH, g);

        //Hàng 1
        g.gridx = 0; g.gridy = 1;
        p.add(new JLabel("Phương thức bán"), g);

        g.gridx = 1;
        p.add(cboBanHang, g);

        g.gridx = 2;
        p.add(new JLabel("Thanh toán"), g);

        g.gridx = 3;
        p.add(cboThanhToan, g);

        g.gridx = 4;
        p.add(new JLabel("Mã khuyến mại"), g);

        g.gridx = 5;
        p.add(cboMaKM, g);

        g.gridx = 6;
        p.add(new JLabel("Hạng thành viên"), g);

        g.gridx = 7;
        p.add(txtHang, g);

        return p;
    }

    // SẢN PHẨM
    private JPanel panelChiTiet() {

        JPanel p = new JPanel(new GridLayout(1, 2, 15, 0));
        p.setBackground(Color.WHITE);

        // Bảng trái
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        leftPanel.setBackground(Color.WHITE);

        //Panel trên
        JPanel topSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSearch.setBackground(Color.WHITE);
        txtTimKiem = new JTextField(15);
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));// (value, min, max, stepsize)
        spinner.setPreferredSize(new Dimension(50, 25));
        btnThem = new JButton("+ Thêm");
        btnThem.setBackground(new Color(59, 130, 246));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBorderPainted(false);
        btnThem.setFocusPainted(false);
        btnThem.setFont(new Font("Arial", Font.BOLD, 13));
        topSearch.add(new JLabel("Tìm:"));
        topSearch.add(txtTimKiem);
        topSearch.add(new JLabel("Số lượng:"));
        topSearch.add(spinner);
        topSearch.add(btnThem);

        // Table
        String[] colsSP = {"MÃ SẢN PHẨM", "TÊN SẢN PHẨM", "ĐƠN GIÁ", "SỐ LƯỢNG"};
        DefaultTableModel modelSP;
        modelSP = new DefaultTableModel(colsSP, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(modelSP);
        tableSanPham.setRowHeight(30);
        JScrollPane scrollSP = new JScrollPane(tableSanPham);

        leftPanel.add(topSearch, BorderLayout.NORTH);
        leftPanel.add(scrollSP, BorderLayout.CENTER);


        //Bảng phải
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết đơn hàng"));
        rightPanel.setBackground(Color.WHITE);

        JPanel topControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topControl.setBackground(Color.WHITE);

        btnXoa = new JButton("- Xóa");
        btnXoa.setBackground(new Color(239, 68, 68));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBorderPainted(false);
        btnXoa.setFocusPainted(false);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 13));
        topControl.add(btnXoa);

        //Bảng sản phẩm đã chọn
        String[] cols = {"MÃ SẢN PHẨM", "TÊN SẢN PHẨM", "ĐƠN GIÁ", "SỐ LƯỢNG", "THÀNH TIỀN"};
        DefaultTableModel model;
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(32);
        JScrollPane scroll = new JScrollPane(table);

        rightPanel.add(topControl, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);

        p.add(leftPanel);
        p.add(rightPanel);

        return p;
    }

    //TỔNG TIỀN
    private JPanel panelTongTien() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 247, 249));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(245, 247, 249));

        lblTongTien = new JLabel("Tổng tiền: 0 đ");
        lblTamTinh = new JLabel("Tạm tính: 0 đ");
        lblKM = new JLabel("Giảm giá: 0 đ");
        btnLuu = new JButton("Lưu đơn hàng");

        btnLuu.setBackground(new Color(59, 130, 246));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBorderPainted(false);
        btnLuu.setFocusPainted(false);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 15));

        lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD, 16f));
        lblTamTinh.setFont(lblTongTien.getFont().deriveFont(Font.BOLD, 16f));
        lblKM.setFont(lblTongTien.getFont().deriveFont(Font.BOLD, 16f));
        lblTongTien.setForeground(new Color(22, 163, 74));

        left.add(lblTamTinh);
        left.add(Box.createVerticalStrut(10));
        left.add(lblKM);
        left.add(Box.createVerticalStrut(10));
        left.add(new JSeparator(JSeparator.HORIZONTAL));
        left.add(Box.createVerticalStrut(10));
        left.add(lblTongTien);
        left.add(Box.createVerticalStrut(10));
        left.add(btnLuu);

        p.add(left, BorderLayout.EAST);
        return p;
    }

    public void setNVToComboBox(ArrayList<String> list) {
        cboNV.removeAllItems();
        for (String ten : list) {
            cboNV.addItem(ten);
        }
    }

    public void setKHToComboBox(ArrayList<String> list) {
        cboKH.removeAllItems();
        for (String ten : list) {
            cboKH.addItem(ten);
        }
    }

    public void setKMToComboBox(ArrayList<String> list) {
        cboMaKM.removeAllItems();
        for (String ten : list) {
            cboMaKM.addItem(ten);
        }
    }

    /* public void loadDataTable(ArrayList<SanPham> list){
        DefaultTableModel model = (DefaultTableModel) tableSanPham.getModel();

        model.setRowCount(0);
        for (SanPham sp : list) {
            Object[] row = {
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getGiaBan(),
                    sp.getSoLuong()
            };
            model.addRow(row);
        }
    } */

    public JTextField getTxtHang() {
        return txtHang;
    }

    public JButton getBtnLuu() {
        return btnLuu;
    }

    public JTextField getTxtMaDon() {
        return txtMaDon;
    }

    public JDateChooser getNgayGD() {
        return ngayGD;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JComboBox<String> getCboBanHang() {
        return cboBanHang;
    }

    public JComboBox<String> getCboThanhToan() {
        return cboThanhToan;
    }

    public JComboBox<String> getCboNV() {
        return cboNV;
    }

    public JComboBox<String> getCboMaKM() {
        return cboMaKM;
    }

    public JComboBox<String> getCboKH() {
        return cboKH;
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getLblTongTien() {
        return lblTongTien;
    }

    public JLabel getLblTamTinh() {
        return lblTamTinh;
    }

    public JLabel getLblKM() {
        return lblKM;
    }

    public JSpinner getSpinner() {
        return spinner;
    }

    public JTable getTableSanPham() {
        return tableSanPham;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public DefaultTableModel getModelTablePhai() {
        return (DefaultTableModel) table.getModel();
    }

    public DefaultTableModel getModelTableTrai() {
        return (DefaultTableModel) tableSanPham.getModel();
    }
}
